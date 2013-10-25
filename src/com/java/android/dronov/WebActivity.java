package com.java.android.dronov;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created with IntelliJ IDEA.
 * User: dronov
 * Date: 17.10.13
 * Time: 21:14
 * To change this template use File | Settings | File Templates.
 */
public class WebActivity extends Activity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.web_view);

        webView = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        String summary = intent.getStringExtra("Content");
        if (summary != null) {
            webView.loadData(summary, "text/html; charset=UTF-8", null);
        } else webView.loadUrl(link);
    }
}
