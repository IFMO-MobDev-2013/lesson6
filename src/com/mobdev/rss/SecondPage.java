package com.mobdev.rss;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SecondPage extends ListActivity {
    public static final String URLL = "URLL";
    public static ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
    public static String url;
    static ListAdapter adapter = null;
    static ListView lv;
    MyBroadcastReceiver br;

    static final String KEY_TITLE = "title";
    static final String KEY_LINK = "link";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_PUBDATE = "pubDate";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        url = extras.getString(URLL);
        adapter = new SimpleAdapter(this, menuItems,
                R.layout.rssitem,
                new String[] { KEY_TITLE, KEY_PUBDATE, KEY_DESCRIPTION, KEY_LINK }, new int[] {
                R.id.firstLine, R.id.date, R.id.secondLine, R.id.link});
        lv = getListView();
        setListAdapter(adapter);
        Intent myIntent = new Intent(this, MyIntentService.class);
        startService(myIntent.putExtra("link", url));

        br = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(MyIntentService.ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(br, intentFilter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String link = ((TextView) view.findViewById(R.id.link)).getText().toString();
                Intent in = new Intent(getApplicationContext(), ShowWeb.class);
                in.putExtra(URLL, link);
                startActivity(in);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent
                    .getStringExtra(MyIntentService.ACTION);
            if ("Complete".equals(result)) {
                 lv.setAdapter(adapter);
            } else {
                if ("Loading".equals(result)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.wait) , Toast.LENGTH_LONG).show();
                } else {
                    if ("Error".equals(result)) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error) , Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        }
    }

}
