package com.ifmomd.igushkin.rss_reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sergey on 10/24/13.
 */
public class FeedsActivity extends Activity implements AdapterView.OnItemClickListener {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeds_activity_layout);
        lstFeeds = (ListView)findViewById(R.id.lstFeeds);
        lstFeeds.setOnItemClickListener(this);
        lstFeeds.setAdapter(new ArrayAdapter<RSSFeed>(this,android.R.layout.simple_list_item_1,feeds));
    }

    ListView lstFeeds;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuAddFeed) {
            final EditText inputName = new EditText(this);
            final EditText inputUrl = new EditText(this);
            inputUrl.setText("http://");
            inputUrl.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.addView(inputName);
            linearLayout.addView(inputUrl);
            inputName.setHint(getString(R.string.feedName));
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dlgInput_title)
                    .setView(linearLayout)
                    .setPositiveButton(getString(R.string.dlgChangeFeed_Ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            feeds.add(new RSSFeed(inputName.getText().toString(), inputUrl.getText().toString()));
                            ((ArrayAdapter)lstFeeds.getAdapter()).notifyDataSetChanged();
                        }
                    }).setNegativeButton(getString(R.string.dlgChangeFeed_Cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    ArrayList<RSSFeed> feeds = new ArrayList<RSSFeed>();

    {
        feeds.add(new RSSFeed("Хабрахабр", "http://habrahabr.ru/rss"));
        feeds.add(new RSSFeed("Bash", "http://bash.im/rss"));
        feeds.add(new RSSFeed("Lenta.ru", "http://lenta.ru/rss"));
        feeds.add(new RSSFeed("StackOverflow #Android", "http://stackoverflow.com/feeds/tag/android"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent result = new Intent();
        result.putExtra("resultUrl", feeds.get(position).url);
        setResult(RESULT_OK,result);
        finish();
    }
}

class RSSFeed implements Serializable {
    String name;
    String url;

    RSSFeed(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String toString() {
        return name;
    }
}