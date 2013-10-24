package ru.ifmo.rain.loboda.rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends ArrayAdapter<RSSRecord> {
    private Context context;
    List<RSSRecord> records;

    public MyAdapter(Context context, List<RSSRecord> records) {
        super(context, R.layout.item);
        this.context = context;
        this.records = records;
    }

    @Override
    public RSSRecord getItem(int position) {
        return records.get(position);
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        RSSRecord record = records.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item, null);
        }
        TextView author = (TextView) view.findViewById(R.id.author);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView content = (TextView) view.findViewById(R.id.content);
        if (record.getAuthor() == null) {
            author.setVisibility(View.GONE);
        } else {
            author.setText(record.getAuthor());
        }
        if (record.getAnnotation() == null) {
            content.setVisibility(View.GONE);
        } else {
            content.setText(record.getAnnotation());
        }
        if (record.getDate() == null) {
            date.setVisibility(View.GONE);
        } else {
            date.setText(record.getDate());
        }
        return view;
    }
}
