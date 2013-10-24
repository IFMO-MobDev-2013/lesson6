package com.example.lesson6;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: satori
 * Date: 10/24/13
 * Time: 8:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeedActivity extends ListActivity {

    public static ArrayAdapter<String> arrayAdapter;
    private int index;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        index = intent.getIntExtra("Index", -1);
        if (index == -1) {
            Toast.makeText(this, "index is -1, errore", 2000).show();
        }
        else {
            arrayAdapter = new ArrayAdapter<>(this, R.layout.channelentry, UpdateArticles.articles.get(index));
            setListAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();

        }
    }
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra("Description", UpdateArticles.descriptions.get(index).get(position));
        startActivity(intent);
    }
}