package com.mobdev.rss;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

public class RSS extends Activity {
    Button b;
    public HashMap<String, String> channels = new HashMap<String, String>();
    public ArrayList<String> names = new ArrayList<String>();
    Point p = new Point(50, 50);
    ListView lv;
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p != null) {
                    showPopup(RSS.this, p);
                }
            }
        });
        putSomeRSS();
        lv = (ListView) findViewById(R.id.listView2);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String link = channels.get((String) parent.getItemAtPosition(position));
                Intent in = new Intent(getApplicationContext(), SecondPage.class);
                in.putExtra(SecondPage.URLL, link);
                startActivity(in);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder ad = new AlertDialog.Builder(RSS.this);
                ad.setTitle("Please confirm removal");
                ad.setMessage("Are you sure want to delete this channel?");
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        String temp = (String) adapterView.getItemAtPosition(i);
                        channels.remove(temp);
                        names.remove(temp);
                        lv.setAdapter(adapter);
                    }
                });
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });
                ad.setCancelable(true);
                ad.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                ad.show();
                return false;
            }
        });
    }


    private void putSomeRSS() {
        channels.put("Google", "https://news.google.ru/news/feeds?output=rss");
        channels.put("Yandex", "http://news.yandex.ru/world.rss");
        channels.put("Lenta", "http://lenta.ru/rss");
        channels.put("Bash", "http://bash.im/rss");
        channels.put("Habrahabr", "http://habrahabr.ru/rss");
        names.add("Google");
        names.add("Yandex");
        names.add("Lenta");
        names.add("Bash");
        names.add("Habrahabr");
    }

    private void showPopup(final Activity context, Point p) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int popupWidth = metrics.widthPixels;
        int popupHeight = metrics.heightPixels / 2;

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        final EditText ed1 = (EditText) layout.findViewById(R.id.editTextName);
        final EditText ed2 = (EditText) layout.findViewById(R.id.editTextLink);

        popup.showAtLocation(layout, Gravity.CENTER, p.x, p.y);

        Button close = (Button) layout.findViewById(R.id.buttoncancel);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        Button ok = (Button) layout.findViewById(R.id.buttonok);
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               names.add(ed1.getEditableText().toString());
               channels.put(ed1.getEditableText().toString(), ed2.getEditableText().toString());
               lv.setAdapter(adapter);
               popup.dismiss();
            }
        });
    }


}
