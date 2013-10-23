package ru.zulyaev.ifmo.lesson6;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import ru.zulyaev.ifmo.lesson6.feed.Entry;
import ru.zulyaev.ifmo.lesson6.feed.Feed;
import ru.zulyaev.ifmo.lesson6.feed.FeedParser;
import ru.zulyaev.ifmo.lesson6.feed.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Никита
 */
public class FeedActivity extends Activity {
    private ArrayListAdapter<FeedItem> adapter = new ArrayListAdapter<FeedItem>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feed);

        ListView listView = (ListView) findViewById(R.id.feed);
        listView.setAdapter(adapter);

        String url = getIntent().getStringExtra(MainActivity.URL_INDEX);
        new RssTask().execute(url);
    }

    class RssTask extends AsyncTask<String, Void, Feed> {
        private int error;

        @Override
        protected Feed doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(params[0]);
                HttpResponse response = client.execute(get);
                return new FeedParser().parse(EntityUtils.toString(response.getEntity(), "UTF-8"));
            } catch (ParseException e) {
                error = R.string.error_parse;
            } catch (IOException e) {
                error = R.string.error_net;
            } catch (Exception e) {
                error = R.string.error_unknown;
                Log.wtf("WTF", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Feed feed) {
            if (feed == null) {
                Toast.makeText(FeedActivity.this, error, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                List<? extends Entry> entries = feed.getEntries();
                List<FeedItem> list = new ArrayList<FeedItem>(entries.size());
                for (Entry entry : entries) {
                    list.add(new FeedItem(entry, getLayoutInflater()));
                }
                adapter.addAll(list);
            }
        }
    }
}