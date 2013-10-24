package ru.ifmo.rain.loboda.rss;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class AnnotationsActivity extends Activity {
    EventReceiver receiver;

    private class EventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            List<RSSRecord> records = (List<RSSRecord>) intent.getSerializableExtra("Result");
            if (records == null) {
                Toast.makeText(getApplicationContext(), "Something went wrong", 1).show();
            }
            showResult(records);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new EventReceiver();
        IntentFilter filter = new IntentFilter("ru.ifmo.rain.loboda.ACTION.sendResult");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
        setContentView(R.layout.annotations);
        Bundle bundle = getIntent().getExtras();
        String feed = bundle.getString("Url");
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("task", "SITE");
        intent.putExtra("site", feed);
        startService(intent);
    }

    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void showResult(List<RSSRecord> records) {
        ListView listView = (ListView) findViewById(R.id.listView);
        if (records != null) {
            MyAdapter adapter = new MyAdapter(this, records);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(adapterView.getContext(), Details.class);
                    RSSRecord record = (RSSRecord) adapterView.getAdapter().getItem(i);
                    intent.putExtra("Annotation", record.getAnnotation());
                    intent.putExtra("Description", record.getDescription());
                    startActivity(intent);
                }
            });
        } else {
            this.finish();
        }
    }
}

