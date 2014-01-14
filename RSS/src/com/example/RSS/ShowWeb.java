package com.example.RSS;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ShowWeb extends Activity {
    public static final String URLL = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        WebView wb = (WebView) findViewById(R.id.webView);
        WebSettings settings = wb.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        Bundle extras = getIntent().getExtras();
        String url = extras.getString(URLL);
        wb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        wb.loadUrl(url);
    }
}
