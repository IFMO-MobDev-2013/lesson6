package ru.ifmo.rain.loboda.rss;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class Details extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        Bundle bundle = getIntent().getExtras();

        WebView webView = (WebView)findViewById(R.id.webView);
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

