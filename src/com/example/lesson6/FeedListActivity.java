package com.example.lesson6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Genyaz
 * Date: 23.10.13
 * Time: 18:19
 * To change this template use File | Settings | File Templates.
 */
public class FeedListActivity extends Activity {
    private ListView feedList;
    private FeedAdapter feedAdapter;
    private Context context;

    public class FeedDownloader extends AsyncTask<String, Void, Vector<Feed>> {
        @Override
        protected Vector<Feed> doInBackground(String... params) {
            try {
                return RSSDownloader.downloadFromURL(params[0], params[1]);
            } catch (Exception e) {
                Vector<Feed> v = new Vector<Feed>();
                v.add(new Feed(e.toString(), "", "utf-8"));
                return v;
            }
        }

        @Override
        protected void onPostExecute(Vector<Feed> result) {
            for (int i = 0; i < result.size(); i++) {
                feedAdapter.addFeed(result.get(i));
            }
            feedAdapter.notifyDataSetChanged();
        }
    }



    public class FeedAdapter extends BaseAdapter {

        private class FeedView {
            Feed feed;
            TextView view;

            public FeedView(Feed feed) {
                this.feed = feed;
                this.view = new TextView(context);
                this.view.setTextSize(20);
                this.view.setText(feed.title);
            }
        }

        private Vector<FeedView> feedViews;

        public FeedAdapter() {
            feedViews = new Vector<FeedView>();
        }

        public void addFeed(Feed element) {
            feedViews.add(new FeedView(element));
        }

        public String getEncoding(int position) {
            return feedViews.get(position).feed.encoding;
        }

        public String getDescription(int position) {
            return feedViews.get(position).feed.description;
        }

        @Override
        public int getCount() {
            return feedViews.size();
        }

        @Override
        public Object getItem(int position) {
            return feedViews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return feedViews.get(position).view;
        }
    }

    public void onFeedClick(int position) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("description", feedAdapter.getDescription(position));
        intent.putExtra("encoding", feedAdapter.getEncoding(position));
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        context = this;
        feedAdapter = new FeedAdapter();
        feedList = (ListView) findViewById(R.id.feedList);
        feedList.setAdapter(feedAdapter);
        feedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onFeedClick(position);
            }
        });
        Intent intent = getIntent();
        new FeedDownloader().execute(intent.getStringExtra("channel"), intent.getStringExtra("encoding"));
    }
}