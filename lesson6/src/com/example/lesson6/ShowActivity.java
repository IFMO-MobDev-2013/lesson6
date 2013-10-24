package com.example.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;

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

//    class LoadOneItemTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void...params) {
//                webView.loadUrl(intent.getStringExtra("link"));
//
//            //String content = extras.getString(MyActivity.ID_EXTRA2);
//            //content = "<b>" + title + "</b>" + "<br>" + "<br>" + content;
//            //webView.loadData(content, "text/html; charset=UTF-8", null);
//                return null;
//        }
//    }

    // TODO: merge mywebview and one_item layouts!!

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

//
//        LoadOneItemTask load = new LoadOneItemTask();
//        load.execute();

        //FeedItem fi =
//
//        TextView title = (TextView)findViewById(R.id.title);
//        TextView date = (TextView)findViewById(R.id.date);
//        TextView link = (TextView)findViewById(R.id.link);
//        TextView description = (TextView)findViewById(R.id.description);
//        CheckBox rank = (CheckBox)findViewById(R.id.checkBox);
//
//        description.setMovementMethod(new ScrollingMovementMethod());
//
//        title.setText(intent.getStringExtra("title"));
//        date.setText(intent.getStringExtra("date"));
//        link.setText(intent.getStringExtra("link"));
//        description.setText(intent.getStringExtra("description"));
//
////        if(intent.getStringExtra("rank").equals("like"))

    }
}
