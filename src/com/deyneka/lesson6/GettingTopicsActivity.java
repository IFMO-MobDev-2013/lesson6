package com.deyneka.lesson6;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class GettingTopicsActivity extends Activity {
    ArrayList<String> texts,refs,titles;
    ListView listView;
    ArrayAdapter<String> adapter;
    MyBroadcastReceiver receiver;
    IntentFilter newIntentFilter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String channel = null;
        listView = (ListView) findViewById(R.id.lvMain);
        Intent intent = getIntent();
        channel = intent.getStringExtra("channel");
        Intent newIntent = new Intent(this, MyIntentService.class);
        newIntent.putExtra("channel", channel);
        startParsingData(newIntent);
    }

    public void startParsingData(Intent newIntent)
    {
        startService(newIntent);
        receiver = new MyBroadcastReceiver();
        this.newIntentFilter = new IntentFilter(MyIntentService.key);
        this.newIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, this.newIntentFilter);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            if ("good".equals(result)) {
                texts = (ArrayList<String>) intent.getSerializableExtra("summaries");
                refs = (ArrayList<String>) intent.getSerializableExtra("links");
                titles = (ArrayList<String>) intent.getSerializableExtra("titles");
                makeAdapter();
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = makeIntent();
                        intent.putExtra("summary", texts.get(position));
                        intent.putExtra("link", refs.get(position));
                        intent.putExtra("title", titles.get(position));
                        startActivity(intent);
                    }
                });
            }
        }
    }



    public void makeAdapter() {
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, titles);
    }

    public Intent makeIntent() {
        Intent intent = new Intent(this, WebShowing.class);
        return intent;
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver(receiver, newIntentFilter);
        super.onResume();
    }
}
