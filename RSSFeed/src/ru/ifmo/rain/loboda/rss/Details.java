package ru.ifmo.rain.loboda.rss;


import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Details extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        Bundle bundle = getIntent().getExtras();

        WebView webView = (WebView) findViewById(R.id.webView);
        String data = "";
        if (bundle.getString("Annotation") != null) {
            data = "<h3>" + bundle.getString("Annotation") + "</h3>";
        }
        if (bundle.getString("Description") != null) {
            data = data + bundle.getString("Description");
        }
        webView.loadData(data, "text/html;charset=UTF-8", null);
    }
}

