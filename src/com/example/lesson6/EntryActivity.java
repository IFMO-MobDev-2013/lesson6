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
        WebView wv =  new WebView(this);
        Intent intent = getIntent();
        String link = intent.getStringExtra("URL");
        wv.loadUrl(link);
        setContentView(wv);
    }
}