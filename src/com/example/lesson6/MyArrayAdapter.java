package com.example.lesson6;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<RSSItem> {
    private final Context context;
    private final ArrayList<RSSItem> values;

    public MyArrayAdapter(Context context, ArrayList<RSSItem> values) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(values.get(position).toString());
        if (values.get(position).getOpen()){
            textView.setTextColor(Color.GRAY);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }
        return rowView;
    }
}
