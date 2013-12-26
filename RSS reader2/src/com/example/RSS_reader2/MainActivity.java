package com.example.RSS_reader2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
    public static final String KEY_FOR_WORKACTIVITY = "key_for_workactivity";
    String someString;
    TextView textView;
    ListView listView;
    Intent intent;

    public static final String KEY_URL_NAME = "name";
    public static final String KEY_URL = "url";
    public static ArticleDataBase articlesDataBase;
    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> sites;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);
        //deleteDatabase(ArticleDataBase.DATABASE_NAME);
        articlesDataBase = new ArticleDataBase(this);
        articlesDataBase.open();
        loadFeeds();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long number) {
                while (Updater.goodArticle > 1) ;
                if (articlesDataBase.isSuchArticles(sites.get(index).get(MainActivity.KEY_URL).toString()) == true ||
                        isNetworkAvailable()) {
                    if (sites.get(index).get(MainActivity.KEY_URL).toString().indexOf("rss") == -1 &&
                            sites.get(index).get(MainActivity.KEY_URL).toString().indexOf("feed") == -1) {
                        Toast toast = Toast.makeText(MainActivity.this, "Wrong URL!", 3000);
                        toast.show();
                        return;
                    }
                    try {
                        URL url = new URL(sites.get(index).get(MainActivity.KEY_URL).toString());
                        url.openConnection();
                        intent = new Intent(MainActivity.this, ArticleActivity.class);
                        intent.putExtra(KEY_FOR_WORKACTIVITY, sites.get(index).get(MainActivity.KEY_URL));
                        someString = sites.get(index).get(MainActivity.KEY_URL);
                        startActivity(intent);
                    } catch(Exception e) {
                        Toast toast = Toast.makeText(MainActivity.this, "Wrong URL!", 3000);
                        toast.show();
                    }
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.this, "Error: no internet connection!", 3000);
                    toast.show();
                }
            }
        });

        Button send = (Button) findViewById(R.id.button);
        send.setOnClickListener(new View.OnClickListener() {
            EditText inputText = (EditText) findViewById(R.id.editText);
            @Override
            public void onClick(View v) {
                while (Updater.goodArticle > 1) ;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
                if (articlesDataBase.isSuchArticles(inputText.getText().toString()) == true ||
                        isNetworkAvailable()) {
                    if (inputText.getText().toString().indexOf("rss") == -1 &&
                            inputText.getText().toString().indexOf("feed") == -1) {
                        Toast toast = Toast.makeText(MainActivity.this, "Wrong URL!", 3000);
                        toast.show();
                        return;
                    }
                    try {
                        URL url = new URL(inputText.getText().toString());
                        url.openConnection();
                        intent = new Intent(MainActivity.this, ArticleActivity.class);
                        intent.putExtra(KEY_FOR_WORKACTIVITY, inputText.getText().toString());
                        someString = inputText.getText().toString();
                        startActivity(intent);
                    } catch(Exception e) {
                        Toast toast = Toast.makeText(MainActivity.this, "Wrong URL!", 3000);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "Error: no internet connection!", 3000);
                    toast.show();
                }
            }
        });

        EditText editText = (EditText) findViewById(R.id.editText);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        intent = new Intent(MainActivity.this, Updater.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +  10 * 60 * 60 * 1000, 10 * 60 * 60 * 1000, pendingIntent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static final int IDM_RENAME = 101;
    public static final int IDM_DELETE = 102;
    public static final int IDM_ADD_NEXT = 103;
    public static final int IDM_ADD_PREV = 104;
    int pozition = -1;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        pozition = aMenuInfo.position;
        menu.add(Menu.NONE, IDM_RENAME, Menu.NONE, "Rename");
        menu.add(Menu.NONE, IDM_DELETE, Menu.NONE, "Delete");
        menu.add(Menu.NONE, IDM_ADD_NEXT, Menu.NONE, "Add after this site");
        menu.add(Menu.NONE, IDM_ADD_PREV, Menu.NONE, "Add before this site");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        EditText editText;
        Button button;
        InputMethodManager imm;
        switch (item.getItemId())
        {
            case IDM_RENAME:
                editText = (EditText) findViewById(R.id.editText);
                editText.setHint("Enter your name of feeds");
                editText.setText(sites.get(pozition).get(MainActivity.KEY_URL_NAME));
                button = (Button) findViewById(R.id.button);
                button.setText("Rename");
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, 0);
                button.setOnClickListener(new View.OnClickListener() {
                    EditText editText = (EditText) findViewById(R.id.editText);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    Button button = (Button) findViewById(R.id.button);
                    @Override
                    public void onClick(View v) {
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        articlesDataBase.updateFeed(editText.getText().toString(), sites.get(pozition).get(MainActivity.KEY_URL));
                        loadFeeds();
                        button.setText("GO");
                        editText.setText("");
                        editText.setHint("Enter your URL adress");
                    }
                });
                break;
            case IDM_DELETE:
                articlesDataBase.deleteAllArticlesWithSuchLinkName(sites.get(pozition).get(MainActivity.KEY_URL));
                articlesDataBase.deleteSuchURLFEeed(sites.get(pozition).get(MainActivity.KEY_URL));
                loadFeeds();
                break;
            case IDM_ADD_NEXT:
                editText = (EditText) findViewById(R.id.editText);
                button = (Button) findViewById(R.id.button);
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                editText.setText("");
                editText.setHint("enter your URL NAME and URL ADRESS separated by a newline:");
                button.setText("Add");
                imm.showSoftInput(editText, 0);
                button.setOnClickListener(new View.OnClickListener() {
                    EditText editText = (EditText) findViewById(R.id.editText);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    Button button = (Button) findViewById(R.id.button);
                    @Override
                    public void onClick(View v) {
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        String request = editText.getText().toString();
                        String name;
                        map = new HashMap<String, String>();
                        if (request.indexOf("\n") != -1) {
                            name = request.substring(0, request.indexOf("\n"));
                            request = request.replaceAll("\n", "");
                            request = request.substring(request.indexOf("\n"), request.length());
                        }
                        else {
                            name = request;
                            request = "";
                        }
                        map.put(MainActivity.KEY_URL_NAME, name);
                        map.put(MainActivity.KEY_URL, request);
                        articlesDataBase.deleteALLFeeds();
                        for (int i = 0; i < pozition + 1; ++i)
                            articlesDataBase.insertFeed(sites.get(i).get(MainActivity.KEY_URL_NAME), sites.get(i).get(MainActivity.KEY_URL));
                        articlesDataBase.insertFeed(map.get(MainActivity.KEY_URL_NAME), map.get(MainActivity.KEY_URL));
                        for (int i = pozition + 1; i < sites.size(); ++i)
                            articlesDataBase.insertFeed(sites.get(i).get(MainActivity.KEY_URL_NAME), sites.get(i).get(MainActivity.KEY_URL));
                        loadFeeds();
                        editText.setText("");
                        editText.setHint("Enter your URL adress");
                        button.setText("GO");
                    }
                });
                break;
            case IDM_ADD_PREV:
                editText = (EditText) findViewById(R.id.editText);
                button = (Button) findViewById(R.id.button);
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                editText.setText("");
                editText.setHint("enter your URL NAME and URL ADRESS separated by a newline:");
                button.setText("Add");
                imm.showSoftInput(editText, 0);
                button.setOnClickListener(new View.OnClickListener() {
                    EditText editText = (EditText) findViewById(R.id.editText);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    Button button = (Button) findViewById(R.id.button);
                    @Override
                    public void onClick(View v) {
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        String request = editText.getText().toString();
                        String name = request.substring(0, request.indexOf("\n"));
                        map = new HashMap<String, String>();
                        if (request.indexOf("\n") != -1) {
                            name = request.substring(0, request.indexOf("\n"));
                            request = request.replaceAll("\n", "");
                            request = request.substring(request.indexOf("\n"), request.length());
                        }
                        else {
                            name = request;
                            request = "";
                        }
                        map.put(MainActivity.KEY_URL_NAME, name);
                        map.put(MainActivity.KEY_URL, request);
                        articlesDataBase.deleteALLFeeds();
                        for (int i = 0; i < pozition; ++i)
                            articlesDataBase.insertFeed(sites.get(i).get(MainActivity.KEY_URL_NAME), sites.get(i).get(MainActivity.KEY_URL));
                        articlesDataBase.insertFeed(map.get(MainActivity.KEY_URL_NAME), map.get(MainActivity.KEY_URL));
                        for (int i = pozition; i < sites.size(); ++i)
                            articlesDataBase.insertFeed(sites.get(i).get(MainActivity.KEY_URL_NAME), sites.get(i).get(MainActivity.KEY_URL));
                        loadFeeds();
                        editText.setText("");
                        editText.setHint("Enter your URL adress");
                        button.setText("GO");
                    }
                });
                break;
            default:
                return super.onContextItemSelected(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, sites, R.layout.row, new String[] {MainActivity.KEY_URL_NAME, MainActivity.KEY_URL}, new int[] {R.id.Colname, R.id.Colurl});
        listView.setAdapter(adapter);
        return true;
    }

    private void loadFeeds() {
        sites = new ArrayList<HashMap<String, String>>();
        if (articlesDataBase.isEmptyFeedTable()) {
            articlesDataBase.insertFeed("Bash.im - Цитатник Рунета", "http://bash.im/rss");
            articlesDataBase.insertFeed("Петербург-Пятый канал", "http://5-tv.ru/news/rss/");
            articlesDataBase.insertFeed("Яндекс.Новости: Игры", "http://news.yandex.ru/games.rss");
            articlesDataBase.insertFeed("4PDA", "http://4pda.ru/feed/");
            articlesDataBase.insertFeed("Яндекс.Новости: Спорт", "http://news.yandex.ru/sport.rss");
            articlesDataBase.insertFeed("Яндекс.Новости: Кино", "http://news.yandex.ru/movies.rss");
            articlesDataBase.insertFeed("Яндекс.Новости: Hi-Tech", "http://news.yandex.ru/computers.rss");
            articlesDataBase.insertFeed("Яндекс.Новости: Главные новости", "http://news.yandex.ru/index.rss");
            articlesDataBase.insertFeed("Хабрахабр / Захабренные / Тематические / Посты", "http://habrahabr.ru/rss/hubs/");
            articlesDataBase.insertFeed("Яндекс.Новости: Интернет", "http://news.yandex.ru/internet.rss");
            articlesDataBase.insertFeed("Яндекс.Новости: Software", "http://news.yandex.ru/software.rss");
            articlesDataBase.insertFeed("Яндекс.Новости: Экономика", "http://news.yandex.ru/business.rss");
            articlesDataBase.insertFeed("Lenta.ru : Новости", "http://lenta.ru/rss");
        }

        sites = articlesDataBase.getFeeds();
        SimpleAdapter adapter = new SimpleAdapter(this, sites, R.layout.row2, new String[] {MainActivity.KEY_URL_NAME, MainActivity.KEY_URL}, new int[] {R.id.Colname, R.id.Colurl});
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }
}
