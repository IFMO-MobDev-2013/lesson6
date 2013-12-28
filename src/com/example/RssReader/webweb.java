package com.example.RssReader;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;



public class webweb extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webmain);
        WebView web = (WebView) findViewById(R.id.webView);
        String sum = getIntent().getStringExtra("summary");
        web.setBackgroundColor(Color.LTGRAY);
        web.loadDataWithBaseURL(null, sum, "text/html", "utf-8", null);
    }


}
