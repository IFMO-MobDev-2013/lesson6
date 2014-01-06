package com.example.rssreader2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created with IntelliJ IDEA.
 * User: elena
 * Date: 24.10.13
 * Time: 13:38
 * To change this template use File | Settings | File Templates.
 */
public class WebActivity extends Activity {
    WebView wv;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        WebView wv = (WebView) findViewById(R.id.webview);
        String text = getIntent().getStringExtra("text");
        wv.getSettings().setJavaScriptEnabled(true);

        wv.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
    }

    public void onBackPressed() {
        Intent i = new Intent(WebActivity.this, ItemActivity.class);
        i.putExtra("titles", getIntent().getStringArrayListExtra("titles"));
        i.putExtra("descriptions", getIntent().getStringArrayListExtra("descriptions"));
        startActivity(i);
        finish();
    }
}