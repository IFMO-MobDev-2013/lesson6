package com.deyneka.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebShowing extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forweb);
        WebView webView = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        String text = intent.getStringExtra("summary");
        String link = intent.getStringExtra("link");
        text += "<a href=\"" + link + "\">Читать полностью</a>";
        webView.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
    }
}
