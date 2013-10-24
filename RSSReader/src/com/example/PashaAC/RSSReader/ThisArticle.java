package com.example.PashaAC.RSSReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ThisArticle extends Activity {
    ArrayList<String> rss = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);
        TextView title = (TextView) findViewById(R.id.textView);
        TextView date = (TextView) findViewById(R.id.textView1);

        title.setText(getIntent().getStringExtra("Title").toString());
        date.setText(getIntent().getStringExtra("PubDate").toString());

        rss.add(getIntent().getStringExtra("Description").toString());
        //rss.add(getIntent().getStringExtra("Link").toString());
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter <String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.my_style, rss);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long number) {
                WebView mWebView = (WebView) findViewById(R.id.webView);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl(getIntent().getStringExtra("Link"));
            }
        });
    }
}