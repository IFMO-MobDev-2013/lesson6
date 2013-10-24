package ru.ifmo.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ru.ifmo.lesson6.R;

import java.util.ArrayList;

public class WebActivity extends Activity {
    WebView myWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String description = intent.getStringExtra("description");

        myWebView = (WebView) findViewById(R.id.webView);

        //new Browser().execute(url);
        myWebView.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class Browser extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... s) {
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.loadUrl(s[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void a) {

        }
    }



}

