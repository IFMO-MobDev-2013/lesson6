package com.example.RSS_reader2;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class SomeArticle extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.some_article);

        String description = getIntent().getStringExtra("Description");
        String title = getIntent().getStringExtra("Title");
        String pubdate = getIntent().getStringExtra("PubDate");

        WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings settings = myWebView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        myWebView.loadDataWithBaseURL(null, "<html><body>" + title + "<br /><br /><br />" + description + "<br /><br /><br />" + pubdate + "</body></html>","text/html","UTF-8","about:blank");
   }
}
