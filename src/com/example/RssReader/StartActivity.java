package com.example.RssReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;



public class StartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    private void start(String stream, String str){
        Intent intent = new Intent(this, MyActivity.class);
        intent.putExtra("link", stream);
        intent.putExtra("str", str);
        startActivity(intent);
    }
    public void bash(View v){
        start("http://bash.im/rss","bash");
    }
    public void stack(View v){
        start("http://stackoverflow.com/feeds", "stack");
    }
    public void lenta(View v){
        start("http://lenta.ru/rss", "lenta");
    }


}
