package com.example.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class EntryActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        Intent intent = getIntent();
        String link = intent.getStringExtra("URL");
        String description = intent.getStringExtra("DESCRIPTION");
        WebView wv = (WebView)findViewById(R.id.webView);
        wv.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null);
    }
}