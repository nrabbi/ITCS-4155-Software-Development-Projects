// Nazmul Rabbi
// ITCS 4155 : Event Finder
// listAdapter.java
// Group 12
// 3/20/18

package com.example.nrabbi.itcs4155;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

class listAdapter extends ArrayAdapter<String>{
    public listAdapter(@NonNull Context context, ArrayList<String> resource) {
        super(context, R.layout.custom_list ,resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater customInflater = LayoutInflater.from(getContext());
        View customView = customInflater.inflate(R.layout.custom_list, parent, false);

        String singleEvent = getItem(position);
        TextView title = (TextView) customView.findViewById(R.id.eventTitle);
        title.setText(singleEvent);

        return customView;
    }
}
