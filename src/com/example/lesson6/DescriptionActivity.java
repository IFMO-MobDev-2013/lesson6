package com.example.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created with IntelliJ IDEA.
 * User: satori
 * Date: 10/24/13
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class DescriptionActivity extends Activity {
    private WebView webView;
    String description;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        webView = (WebView)findViewById(R.id.descriptionView);
        Intent intent = getIntent();
        description = intent.getStringExtra("Description");
        //webView.loadData(description, "text/html", "UTF-8");
        webView.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null);

    }
}