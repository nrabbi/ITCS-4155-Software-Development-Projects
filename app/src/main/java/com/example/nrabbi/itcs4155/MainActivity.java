package com.example.nrabbi.itcs4155;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

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

        findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()){
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    EditText search = (EditText) findViewById(R.id.searchBar);
                    String location = search.getText().toString();
                    String connection = "http://api.eventful.com/json/events/search?&location=" + location + "&app_key=rHhzpXwdTd7mncVB";
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
                ArrayList<String> lst = new ArrayList<>();

                for (int i=0; i<result.size();i++){
                    lst.add(result.get(i).title);
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, lst);

                ll.setAdapter(adapter);
                ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        TextView txt = (TextView) arg1;
                        System.out.println(txt.getText().toString());
                    }
                });
            } else {
                progressDialog.dismiss();
                ListView listView = (ListView)findViewById(R.id.listView);
                listView.setAdapter(null);
                Toast.makeText(MainActivity.this, "Error! Invalid Location", Toast.LENGTH_SHORT).show();
                Log.d("Output", "empty result");
            }
        }
    }

}
