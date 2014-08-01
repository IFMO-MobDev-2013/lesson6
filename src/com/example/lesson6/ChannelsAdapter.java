package com.example.lesson6;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Vector;

public class ChannelsAdapter extends ArrayAdapter<Channel>
{
    private final Context context;
    public Vector<Channel> channels;
    private ChannelsActivity program;

    public ChannelsAdapter(Context context, Vector<Channel> channels, ChannelsActivity program)
    {
        super(context, R.layout.entry, channels);
        this.context = context;
        this.channels = channels;
        this.program = program;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View entryView = inflater.inflate(R.layout.channel, parent, false);

        TextView entryTitleText = (TextView) entryView.findViewById(R.id.channelTitle);

        final Channel entry = channels.get(index);

        entryTitleText.setText(entry.url);


        entryView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(program, ArticlesActivity.class);
                intent.putExtra("URL", entry.url);
                program.startActivity(intent);
            }
        });

        return entryView;
    }
}
