package ru.zulyaev.ifmo.lesson6;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Никита
 */
public class ArticleActivity extends Activity {
    public static final String HTML_INDEX = "HTML";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String html = getIntent().getStringExtra(HTML_INDEX);
        WebView view = new WebView(this);
        setContentView(view);
        String data = "Error";
        try {
            data = URLEncoder.encode(html, "utf-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("WTF", e);
        }
        view.loadData(data, "text/html; charset=utf-8", null);
    }
}