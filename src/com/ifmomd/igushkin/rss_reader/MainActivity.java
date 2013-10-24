package com.ifmomd.igushkin.rss_reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.*;
import android.widget.*;
import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    ListView           lstItems;
    ArrayList<RSSItem> rssItems;
    ArrayList<RSSChannel> rssChannels = new ArrayList<RSSChannel>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("feed"))
            feedURL = savedInstanceState.getString("feed");
        setContentView(R.layout.feed_activity_layout);
        lstItems = (ListView) findViewById(R.id.lstItems);
        rssItems = new ArrayList<RSSItem>();
        lstItems.setItemsCanFocus(false);
        lstItems.setOnItemClickListener(this);
        lstItems.setAdapter(new RSSListAdapter(rssItems));
        startFetchingFeed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("feed", feedURL);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        feedURL = savedInstanceState.getString("feed");
    }


    private void startFetchingFeed() {
        showProgress();
        RSSFetcher f = new RSSFetcher(rssItems, rssChannels);
        f.execute(feedURL);
    }

    String feedURL = "http://habrahabr.ru/rss";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fetch_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            feedURL = data.getStringExtra("resultUrl");
            startFetchingFeed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuChangeSource) {
            Intent i = new Intent(this, FeedsActivity.class);
            startActivityForResult(i, 0);
        }
        if (item.getItemId() == R.id.mnuRefresh) {
            startFetchingFeed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RSSItem r = (RSSItem) parent.getAdapter().getItem(position);
        Intent i = new Intent(this, BrowseActivity.class);
        i.putExtra("data", r.description);
        i.putExtra("use UTF-8", r.link.contains("bash")); //horrible peace of shit
        startActivity(i);
    }

    private void showProgress() {
        findViewById(R.id.pbProgress).setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        findViewById(R.id.pbProgress).setVisibility(View.INVISIBLE);
    }

    private void onFetchFailed() {
        Toast.makeText(this, R.string.toaFetchFailed, Toast.LENGTH_SHORT).show();
    }

    class RSSFetcher extends RSSGetter {
        List<RSSItem> workingList;
        List<RSSChannel> channels;

        RSSFetcher(List<RSSItem> workingList, List<RSSChannel> channels) {
            this.workingList = workingList;
            this.channels = channels;
        }

        @Override
        protected void onPostExecute(List<RSSItem> rssItems) {
            super.onPostExecute(null);
            hideProgress();
            workingList.clear();
            if (items == null || items.size() == 0) {
                onFetchFailed();
            } else {
                workingList.addAll(items);
                channels.clear();
                channels.addAll(channels);
            }
            ((RSSListAdapter) lstItems.getAdapter()).notifyDataSetChanged();
        }
    }

    class RSSListAdapter extends BaseAdapter {

        RSSListAdapter(ArrayList<RSSItem> items) {
            if (items == null)
                this.items = new ArrayList<RSSItem>();
            else
                this.items = items;
        }

        List<RSSItem> items;

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if (convertView != null) v = convertView;
            else {
                v = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, null);
                //((TextView)v.findViewById(android.R.id.text2)).setMovementMethod(LinkMovementMethod.getInstance());
                ((TextView) v.findViewById(android.R.id.text2)).setMaxLines(3);
            }
            ((TextView) v.findViewById(android.R.id.text1)).setText(((RSSItem) getItem(position)).title);
            if (((RSSItem) ((RSSItem) getItem(position))).description != null)
                ((TextView) v.findViewById(android.R.id.text2)).setText(Html.fromHtml(((RSSItem) getItem(position)).description).toString());
            else
                ((TextView) v.findViewById(android.R.id.text2)).setText("");
            ((TextView) v.findViewById(android.R.id.text1)).setFocusable(false);
            ((TextView) v.findViewById(android.R.id.text2)).setFocusable(false);
            return v;
        }
    }
}