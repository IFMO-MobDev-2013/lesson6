package com.example.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    public static final String QUERY = "query";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView listView = (ListView)findViewById(R.id.feeds);

        String[] f = getResources().getStringArray(R.array.default_feeds);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, f);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s =  ((TextView) view).getText().toString();
                Intent intent = new Intent(MyActivity.this, OneFeedActivity.class);
                intent.putExtra(QUERY, s);
                startActivity(intent);
            }
        });
    }
}
