package com.example.lesson6;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Vector;

class MyAdapter extends ArrayAdapter<Entry>
{
    private final Context context;
    public Vector<Entry> entries;
    private Program program;

    public MyAdapter(Context context, Vector<Entry> entries, Program program)
    {
        super(context, R.layout.entry, entries);
        this.context = context;
        this.entries = entries;
        this.program = program;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View entryView = inflater.inflate(R.layout.entry, parent, false);

        TextView entryTitleText = (TextView) entryView.findViewById(R.id.entryTitle);

        final Entry entry = entries.get(index);

        entryTitleText.setText(entry.title);


        entryView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(program, EntryActivity.class);
                intent.putExtra("URL", entry.link);
                program.startActivity(intent);
            }
        });

        return entryView;
    }

}
