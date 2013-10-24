package ru.zulyaev.ifmo.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.google.gson.Gson;
import ru.zulyaev.ifmo.lesson6.util.Utils;

import java.util.ArrayList;

/**
 * @author Никита
 */
public class MainActivity extends Activity {
    public static final String URL_INDEX = "url";

    private static final Gson GSON = new Gson();
    private static final String FEEDS_PREF = "FEEDS";

    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showFeed(feeds.get(position));
        }
    };
    private final View.OnCreateContextMenuListener onCreateContextMenuListener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(feeds.get(info.position));
            menu.add(Menu.NONE, Menu.NONE, Menu.NONE, R.string.remove);
        }
    };

    private ArrayList<String> feeds;
    private SharedPreferences preferences;
    private EditText input;
    private ArrayAdapter<String> adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getPreferences(MODE_PRIVATE);
        String defaultUrls = GSON.toJson(getResources().getStringArray(R.array.default_urls));
        feeds = Utils.asArrayList(GSON.<String[]>fromJson(preferences.getString(FEEDS_PREF, defaultUrls), String[].class));
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, feeds);

        setContentView(R.layout.main);

        ListView listView = (ListView) findViewById(R.id.feed_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setOnCreateContextMenuListener(onCreateContextMenuListener);

        input = (EditText) findViewById(R.id.input);

        refreshList();
    }

    private void showFeed(String url) {
        Intent intent = new Intent(this, FeedActivity.class);
        intent.putExtra(URL_INDEX, url);
        startActivity(intent);
    }

    @SuppressWarnings("unused")
    public void addFeed(View view) {
        String url = input.getText().toString();
        input.setText("");

        feeds.add(0, url);
        refreshList();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        feeds.remove(info.position);
        refreshList();
        return true;
    }

    private void refreshList() {
        preferences.edit().putString(FEEDS_PREF, GSON.toJson(feeds.toArray())).commit();
        adapter.notifyDataSetChanged();

        Intent intent = new Intent(this, UpdaterService.class);
        intent.putExtra(UpdaterService.REQUEST_INDEX, feeds);
        startService(intent);
    }
}