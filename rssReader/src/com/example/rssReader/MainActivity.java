package com.example.rssReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button bash = (Button) findViewById(R.id.bashFeedButton);
        Button lenta = (Button) findViewById(R.id.lentaFeedButton);


        bash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                intent.putExtra("link", "bash.im/rss");
                startActivity(intent);
            }
        });

        lenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                intent.putExtra("link", "lenta.ru/rss");
            }
        });
    }
}
