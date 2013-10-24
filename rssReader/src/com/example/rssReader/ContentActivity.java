package com.example.rssReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslan
 * Date: 24.10.13
 * Time: 11:36
 */
public class ContentActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        WebView webView = (WebView)findViewById(R.id.webView);
        Intent intent = getIntent();
        String text = intent.getStringExtra("link");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(text, "text/html; charset=utf-8", null);
    }
}