package ru.ifmo.rssreader;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ShowTitlesActivity extends Activity {

    AlarmManager am;
    PendingIntent pi;
    private List<Article> articleList;
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        setContentView(R.layout.titleslayout);
        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("task", "new");
        intent.putExtra("url", url);
        startService(intent);
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(MyIntentService.key);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent newIntent = new Intent(this, MyIntentService.class);
        newIntent.putExtra("task", "upd");
        pi = PendingIntent.getService(this, 0, newIntent, 0);
        am.setRepeating(AlarmManager.RTC, 0, 30000, pi);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
        am.cancel(pi);
    }

    private void errorMessage(String message) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(message);
    }

    private Context getContext() {
        return this;
    }

    private void showTitles(List<Article> articles) {
        if (articles == null) {
            errorMessage("An error occurred while reading RSS.");
            return;
        }
        articleList = articles;
        if (articles.size() == 0) {
            errorMessage("RSS is empty!");
            return;
        }
        String[] titles = new String[articles.size()];
        for (int i = 0; i < articles.size(); i++) {
            titles[i] = articles.get(i).getTitle();
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                String article = articleList.get(position).getArticle();
                Intent intent = new Intent(getContext(), ShowArticleActivity.class);
                intent.putExtra("article", article);
                startActivity(intent);
            }

        });
        listView.setAdapter(adapter);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            showTitles((List<Article>) intent.getSerializableExtra("articles"));
        }
    }
}
