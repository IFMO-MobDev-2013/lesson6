package com.example.lesson6;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.webkit.WebView;


/**
 * Created with IntelliJ IDEA.
 * User: slavian
 * Date: 15.10.13
 * Time: 19:30
 * To change this template use File | Settings | File Templates.
 */
public class ShowActivity extends Activity {
    Intent intent;
    WebView webView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mywebview);
        intent = getIntent();

        String title = intent.getStringExtra(OneFeedActivity.KEY_TITLE);
        String description = intent.getStringExtra(OneFeedActivity.KEY_DESC);

        webView = (WebView)findViewById(R.id.webView);

        String content = null;
        content = "<b>" + title + "</b>" + "<br>" + "<br>" + description;
        webView.loadData(content, "text/html; charset=UTF-8", null);



    }
}
