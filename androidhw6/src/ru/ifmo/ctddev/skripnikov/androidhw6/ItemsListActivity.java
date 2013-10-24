package ru.ifmo.ctddev.skripnikov.androidhw6;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ItemsListActivity extends Activity {
    private ProgressDialog dialog;
    private ListView listView;
    private String url;
    private String encoding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        url = getIntent().getStringExtra("url");
        encoding = getIntent().getStringExtra("encoding");
        listView = (ListView) findViewById(R.id.listView);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching feed ...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        startFeedReader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                startFeedReader();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startFeedReader() {
        if (internetConnectionIsAvailable()) {
            dialog.show();
            new UIFeedReader().execute(url, encoding);
        } else {
            Toast.makeText(this, "No internet connection available", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean internetConnectionIsAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class UIFeedReader extends FeedReader {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (feed != null) {
                FeedAdapter adapter = new FeedAdapter(getBaseContext(), feed.toArray(new FeedItem[feed.size()]));
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                        Intent intent = new Intent(getBaseContext(), WebActivity.class);
                        intent.putExtra("description", feed.get(position).description);
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(getBaseContext(), "Feed fetching failed", Toast.LENGTH_LONG).show();
                finish();
            }
            dialog.dismiss();
        }
    }
}
