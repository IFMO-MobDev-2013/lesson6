package com.example.RSS_HW5;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class ChannelActivity extends Activity {
    String link;
    String name;
    MyBroadcastReceiver mbr;
    IntentFilter intentFilter;
    ArrayAdapter<String> adapter;
    ArrayList<String> summaries;
    ArrayList<String> titles;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel);
        name = getIntent().getExtras().getString("name");
        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(name);
        link = getIntent().getExtras().getString("link");
        if (!hasInternetConnection()) {
            error("Ошибка: Невозможно выполнить сетевой запрос. Проверьте наличие Интернет соединения");
        } else {
            Intent newIntent = new Intent(this, MyIntentService.class);
            newIntent.putExtra("link", link);
            startService(newIntent);
            mbr = new MyBroadcastReceiver();
            intentFilter = new IntentFilter(MyIntentService.key);
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
            registerReceiver(mbr, intentFilter);
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mbr);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mbr, intentFilter);
    }


    public void error(String s) {
        Toast myToast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.CENTER, 0, 0);
        myToast.show();
    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        if (netInfo == null) {
            return false;
        }
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) {
                    return true;
                }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()) {
                    return true;
                }
        }
        return false;
    }

    private void getResult() {
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChannelActivity.this, ArticleActivity.class);
                intent.putExtra("title", titles.get(i));
                intent.putExtra("description", summaries.get(i));
                startActivity(intent);
            }
        });

    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean result = true;
            intent.getBooleanExtra("result", result);
            if (result) {
                summaries = (ArrayList<String>) intent.getSerializableExtra("summaries");
                titles = (ArrayList<String>) intent.getSerializableExtra("titles");
                getResult();
            } else {
                error("Ошибка загрузки данных");
            }
        }
    }
}
