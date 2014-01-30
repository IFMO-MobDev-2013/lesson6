package com.deyneka.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooser);
        ListView lvChooser = (ListView) findViewById(R.id.lvChooser);
        String[] names = {"Lenta","Bash"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);
        lvChooser.setAdapter(adapter);
        lvChooser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), GettingTopicsActivity.class);
                if (position == 0)
                    intent.putExtra("channel", "http://lenta.ru/rss");
                else
                    intent.putExtra("channel", "http://bash.im/rss");
                startActivity(intent);
            }
        });
    }
}