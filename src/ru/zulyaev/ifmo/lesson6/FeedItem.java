package ru.zulyaev.ifmo.lesson6;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import ru.zulyaev.ifmo.lesson6.feed.Entry;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Никита
 */
class FeedItem extends ArrayListAdapter.Item {
    private static final View.OnClickListener EXPANDER = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((FeedItem)v.getTag()).toggle();
        }
    };

    private final Entry entry;
    private final String data;
    private final LayoutInflater inflater;
    private ViewHolder holder;
    private boolean expanded;

    FeedItem(Entry entry, LayoutInflater inflater) {
        this.entry = entry;
        this.inflater = inflater;

        String data = "Error";
        try {
            data = URLEncoder.encode(entry.getDescription(), "utf-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("WTF", e);
        }
        this.data = data;
    }

    @Override
    protected View composeView(View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.feed_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.description = (WebView) view.findViewById(R.id.description);
            holder.title.setOnClickListener(EXPANDER);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setTag(this);
        holder.title.setText(entry.getTitle());
        holder.description.setVisibility(expanded ? View.VISIBLE : View.GONE);
        holder.description.loadData(data, "text/html; charset=utf-8", null);
        return view;
    }

    public void toggle() {
        expanded = !expanded;
        holder.description.setVisibility(expanded ? View.VISIBLE : View.GONE);
    }

    static class ViewHolder {
        TextView title;
        WebView description;
    }
}
