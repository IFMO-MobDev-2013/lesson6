package com.example.RSS;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SecondPage extends ListActivity {
    public static final String URLL = "";
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
                    Toast.makeText(getApplicationContext(), "Loading, pls wait", Toast.LENGTH_LONG).show();
                } else {
                    if ("Error".equals(result)) {
                        Toast.makeText(getApplicationContext(), "Error, please try again", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        }
    }

}
