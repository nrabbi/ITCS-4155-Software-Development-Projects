// Nazmul Rabbi
// ITCS 4155 : Event Finder
// MainActivity.java
// Group 12
// 3/20/18

package com.example.nrabbi.itcs4155;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Event Finder");

        final Spinner categoryList = (Spinner) findViewById(R.id.categorySelect);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.category_list));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryList.setAdapter(categoryAdapter);

        findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()){
                    Log.d("Selected Item: " , categoryList.getSelectedItem().toString());
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    EditText search = (EditText) findViewById(R.id.searchBar);
                    String location = search.getText().toString();
                    String connection = "";

                    if (categoryList.getSelectedItem().toString().equals("All")) {
                        connection = "http://api.eventful.com/json/events/search?&location=" + location + "&app_key=rHhzpXwdTd7mncVB";
                    }
                    else if (categoryList.getSelectedItem().toString().equals("Family") && !search.getText().toString().isEmpty()){
                        connection = "http://api.eventful.com/json/events/search?category=family_fun_kids&location=" + location + "&app_key=rHhzpXwdTd7mncVB";
                    }
                    else if (categoryList.getSelectedItem().toString().equals("Festivals") && !search.getText().toString().isEmpty()){
                        connection = "http://api.eventful.com/json/events/search?category=festivals_parades&location=" + location + "&app_key=rHhzpXwdTd7mncVB";
                    }
                    else if (categoryList.getSelectedItem().toString().equals("Movies") && !search.getText().toString().isEmpty()){
                        connection = "http://api.eventful.com/json/events/search?category=movies_film&location=" + location + "&app_key=rHhzpXwdTd7mncVB";
                    }
                    else if (!search.getText().toString().isEmpty()){
                        connection = "http://api.eventful.com/json/events/search?category=" + categoryList.getSelectedItem().toString().toLowerCase() + "&location=" + location + "&app_key=rHhzpXwdTd7mncVB";
                    }

                    Log.d("Output URL", connection);

                    new GetDataAsync().execute(connection);
                }
                else{
                    ListView listView = (ListView)findViewById(R.id.listView);
                    listView.setAdapter(null);
                    Toast.makeText(MainActivity.this, "Error! Not Connected to the internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<Event>> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Searching Events...");
            progressDialog.show();
        }

        @Override
        protected ArrayList<Event> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Event> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONObject root2 = root.getJSONObject("events");
                    JSONArray root3 = root2.getJSONArray("event");

                    String title;
                    for (int i=0;i<root3.length();i++) {
                        JSONObject personJson = root3.getJSONObject(i);
                        Event event = new Event();
                        event.title = personJson.getString("title");
                        event.city = personJson.getString("city_name");
                        event.state = personJson.getString("region_abbr");
                        event.country = personJson.getString("country_abbr");
                        event.zip = personJson.getString("postal_code");
                        event.url = personJson.getString("url");
                        event.address = personJson.getString("venue_address");
                        event.description = personJson.getString("description");
                        event.startTime = personJson.getString("start_time");
                        event.endTIme = personJson.getString("stop_time");
                        result.add(event);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Event> result) {
            if (result.size() > 0) {
                progressDialog.dismiss();
                Log.d("Output", result.toString());
                Toast.makeText(MainActivity.this,  "Location: " + result.get(0).city.toString() + " " + result.get(0).state.toString() + " " + result.get(0).country.toString() + " " + result.get(0).zip.toString(), Toast.LENGTH_SHORT).show();
                ListView ll = (ListView)findViewById(R.id.listView);
                final ArrayList<String> lst = new ArrayList<>();
                final ArrayList<String> desc = new ArrayList<>();
                final ArrayList<String> srt = new ArrayList<>();
                final ArrayList<String> ed = new ArrayList<>();
                final ArrayList<String> urlArray = new ArrayList<>();
                final ArrayList<String> addressArray = new ArrayList<>();

                for (int i=0; i<result.size();i++){
                    lst.add(result.get(i).title);
                    desc.add(result.get(i).description);
                    srt.add(result.get(i).startTime);
                    ed.add(result.get(i).endTIme);
                    urlArray.add(result.get(i).url);
                    addressArray.add(result.get(i).address);
                    Log.d("Address:", addressArray.get(i));
                }

                final ArrayAdapter<String> adapter = new listAdapter(MainActivity.this, lst);
                ll.setAdapter(adapter);

                ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getApplicationContext(), webView.class);
                        intent.putExtra("title", lst.get(i));
                        intent.putExtra("url", urlArray.get(i));
                        startActivity(intent);
                    }
                });

                ll.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra("title", lst.get(position));
                        intent.putExtra("description", desc.get(position));
                        intent.putExtra("eventLocation", addressArray.get(position));

                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
                            intent.putExtra("beginTime", sdf.parse(srt.get(position)).getTime());
                            Boolean endDateCheck;

                            if ("null".equals(ed.get(position)))
                                endDateCheck = false;
                            else
                                endDateCheck = true;

                            if (endDateCheck)
                                intent.putExtra("endTime", sdf.parse(ed.get(position)).getTime());

                        } catch (Exception e) {
                            System.out.println("Error " + e.getMessage());
                            return false;
                        }

                        startActivity(intent);
                        return true;
                    }
                });

            } else {
                progressDialog.dismiss();
                ListView listView = (ListView)findViewById(R.id.listView);
                listView.setAdapter(null);
                Toast.makeText(MainActivity.this, "Error! invalid location or not found", Toast.LENGTH_SHORT).show();
                Log.d("Output", "empty result");
            }
        }
    }

}
