package ru.skipor.RssReader.UserInterface;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import ru.skipor.RssReader.R;
import ru.skipor.RssReader.RSSFeedReader.RSSFeedReader;
import ru.skipor.RssReader.RSSFeedReader.RSSFeedReaderException;
import ru.skipor.RssReader.RSSFeedReader.RSSItem;
import ru.skipor.RssReader.RSSFeedReader.SAXRSSReader;

/**
 * Created by Vladimir Skipor on 10/24/13.
 * Email: vladimirskipor@gmail.com
 */
public class RSSAdapter extends BaseAdapter {
    private static final String TAG = "RSSAdapter";
    public final Context context;
    private ArrayList<RSSItem> items;
    private RSSFeedReader feedReader;

    public RSSAdapter(Context context, String feedUrl) {
        feedReader = new SAXRSSReader();

        this.context = context;
        items = new ArrayList<RSSItem>();

        Log.d(TAG, "RSSAdapter created");
        showContent(feedUrl);

    }

    public void setItems(Collection<RSSItem> items) {
        this.items = new ArrayList<RSSItem>(items);
        notifyDataSetChanged();

    }

    public void add(RSSItem item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void showContent(String feedURL) {
        ShowContentTask showContentTask = new ShowContentTask();
        showContentTask.execute(feedURL);

    }

    private class ShowContentTask extends AsyncTask<String, Void, ArrayList<RSSItem>> {

        @Override
        protected ArrayList<RSSItem> doInBackground(String... params) {
            String feedURL = params[0];
            try {
                return new ArrayList<RSSItem>(feedReader.parse(feedURL).getItemList());
            } catch (RSSFeedReaderException e) {
                Log.e(TAG, "load and parse error", e);
            }
            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<RSSItem> taskItems) {
            setItems(taskItems);

        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public RSSItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rss_item_layout, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.item_title);
        textView.setText(items.get(position).getTitle());

        return convertView;
    }
}
