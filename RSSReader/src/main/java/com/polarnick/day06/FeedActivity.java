package com.polarnick.day06;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.polarnick.rss.Feed;
import com.polarnick.rss.FeedEntry;

/**
 * Date: 16.09.13
 *
 * @author Nickolay Polyarniy aka PolarNick
 */
public class FeedActivity extends ListActivity {

    private String currentFeedURL = "http://feeds.newsru.com/com/www/news/big";
    private Feed currentFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entries_list_view);
        ImageButton refreshButton = (ImageButton) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFeed();
            }
        });
        loadFeed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        currentFeedURL = item.getTitle().toString();
        loadFeed();
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(FeedActivity.this, EntryWebActivity.class);
        intent.putExtra(EntryWebActivity.ENTRY_KEY, currentFeed.getEntries().get(position));
        startActivity(intent);
    }

    private void loadFeed() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Feed...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final Activity activity = this;

        new FeedDownloadTask() {
            @Override
            public void onSuccess(Feed feed) {
                currentFeed = feed;
                TextView feedTitle = (TextView) findViewById(R.id.feedTitle);
                feedTitle.setText(feed.getTitle());
                TextView feedDescription = (TextView) findViewById(R.id.feedDescription);
                feedDescription.setText(feed.getDescription());
                feed.sortEntriesByDatePublished();
                ArrayAdapter<FeedEntry> adapter = new FeedEntriesAdapter(activity, feed.getEntries());
                setListAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Exception exception) {
                progressDialog.dismiss();
                new AlertDialog.Builder(activity)
                        .setMessage(exception.getMessage() + "\nApplication will be closed!")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activity.finish();
                                    }
                                }).create().show();
            }
        }.execute(currentFeedURL);
    }
}
