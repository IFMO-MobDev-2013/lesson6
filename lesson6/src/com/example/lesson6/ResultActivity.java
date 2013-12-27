package com.example.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;


public class ResultActivity extends Activity {
    private WebView webView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        webView = (WebView) findViewById(R.id.feedView);
        Intent intent = getIntent();
        String description = intent.getStringExtra("description");
        webView.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null);
    }
}