package ru.ifmo.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ru.ifmo.lesson6.R;

import java.util.ArrayList;

public class WebActivity extends Activity {
    WebView myWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        Intent intent = getIntent();
        String text = intent.getStringExtra("description");

        myWebView = (WebView) findViewById(R.id.webView);

        myWebView.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);

    }

}

