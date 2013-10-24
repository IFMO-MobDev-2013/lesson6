package com.example.RSSReader;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

public class RSSActivity extends Activity {

    private ListView listView;
    private String link;
    private ArrayList<RSSNode> nodes;
    private RSSBroadcastReceiver rssBroadcastReceiver;
    private RSSTape rssTape;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss);

        link = getIntent().getStringExtra("link");

        listView = (ListView) findViewById(R.id.listView);

        ActivRSSTape activRssTape = new ActivRSSTape();
        activRssTape.execute();



        Intent intentRSS = new Intent(this, RSSTape.class);
        startService(intentRSS.putExtra("link", link).putExtra("task", "load"));

        rssBroadcastReceiver = new RSSBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter("com.example.RSSReader.RESPONSE");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(rssBroadcastReceiver, intentFilter);

        Intent intent = new Intent(this, RSSTape.class);
        intent.putExtra("task", "refresh");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30 * 60 * 1000, 30 * 60 * 1000, pendingIntent);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int index, long arg3) {

                Intent intent = new Intent(RSSActivity.this, ArticleActivity.class);
                intent.putExtra("text", nodes.get(index).getDescription());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(rssBroadcastReceiver);
    }

    public class RSSBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            nodes = getIntent().getExtras().getParcelableArrayList("nodes");

            ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for (int i = 0; i < nodes.size(); i++) {

                map = new HashMap<String, String>();
                map.put("title", nodes.get(i).getTitle());
                map.put("date", nodes.get(i).getDate().toString());
                map.put("description", nodes.get(i).getDescription());
                items.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(RSSActivity.this, items, R.layout.listview,
                    new String[]{"title", "date"},
                    new int[] {R.id.title, R.id.date});

            listView.setAdapter(adapter);
        }
    }

    private class ActivRSSTape extends AsyncTask < Void, Void, ArrayList<RSSNode> > {

        @Override
        protected ArrayList<RSSNode> doInBackground(Void... params) {
            return getTape();
        }

        @Override
        protected void onPostExecute(ArrayList<RSSNode> result) {
            nodes = result;

            ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;


            for (int i = 0; i < nodes.size(); i++) {
                map = new HashMap<String, String>();
                map.put("title", nodes.get(i).getTitle());
                map.put("date", nodes.get(i).getDate().toString());
                map.put("description", nodes.get(i).getDescription());
                items.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(RSSActivity.this, items, R.layout.listview,
                    new String[]{"title", "date"},
                    new int[] {R.id.title, R.id.date});

            listView.setAdapter(adapter);
        }

        private ArrayList<RSSNode> getTape() {
            String request;

            ArrayList<RSSNode> result = new ArrayList<RSSNode>();

            request = link;

            InputStream inputStream = null;

            URL url;
            HttpURLConnection connect;

            try {
                url = new URL(request);
                connect = (HttpURLConnection) url.openConnection();



                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

                inputStream = connect.getInputStream();
                Reader reader;
                InputSource is;

                if ("http://lenta.ru/rss".equals(link)) {
                    reader = new InputStreamReader(inputStream, "UTF-8");
                    is = new InputSource(reader);
                    is.setEncoding("UTF-8");
                }
                else {
                    reader = new InputStreamReader(inputStream, "windows-1251");
                    is = new InputSource(reader);
                    is.setEncoding("windows-1251");
                }

                SAXParserFactory factory = SAXParserFactory.newInstance();


                factory.setNamespaceAware(false);
                SAXParser parser;

                parser = factory.newSAXParser();

                RSSParser rssParser = new RSSParser();

                parser.parse(is, rssParser);

                result = rssParser.getResult();

            }
            catch (Exception e) {

                e.printStackTrace();
            }
            finally {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }

            return result;
        }
    }

    private class RSSParser extends DefaultHandler {

        private ArrayList<RSSNode> result;
        private boolean isItem;
        private boolean isDate;
        private boolean isTitle;
        private boolean isDescription;
        private RSSNode rssNode;
        String ans;

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            result = new ArrayList<RSSNode>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            ans = "";
            if (qName.equals("item")) {
                isItem = true;
                rssNode = new RSSNode();
            }

            if (qName.equals("title")) {
                isTitle = true;
            }
            if (qName.equals("pubDate")) {
                isDate = true;
            }
            if (qName.equals("description")) {
                isDescription = true;
            }
            super.startElement(uri, localName, qName, attributes);

        }

        @Override
        public void characters(char[] c, int start, int length) throws SAXException {
            super.characters(c, start,  length);
            String s = new String(c, start, length);

            if (isItem) {
                ans += s;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("item")) {
                isItem = false;
                result.add(rssNode);
            }

            if (isItem) {
                if (qName.equals("title")) {
                    rssNode.setTitle(ans);
                    ans = "";
                    isTitle = false;
                }
                if (qName.equals("pubDate")) {
                    rssNode.setDate(ans);
                    ans = "";
                    isDate = false;
                }
                if (qName.equals("description")) {
                    rssNode.setDescription(ans);
                    ans = "";
                    isDescription = false;
                }
            }

            super.endElement(uri,localName, qName);
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        public ArrayList<RSSNode> getResult() {
            return this.result;
        }
    }
}
