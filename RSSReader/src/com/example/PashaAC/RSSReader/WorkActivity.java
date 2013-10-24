package com.example.PashaAC.RSSReader;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class WorkActivity extends Activity {
    ListView listView;
    TextView message;
    ArrayList<Articles> rssArticles = new ArrayList<Articles>();
    ArrayAdapter<Articles> arrayAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss);

        message = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.renew);
        arrayAdapter = new ArrayAdapter<Articles>(this,	R.layout.my_style, rssArticles);
        listView.setAdapter(arrayAdapter);
        rssArticles.clear();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rssArticles.clear();
                new MainWork(getIntent().getStringExtra("key").toString()).execute();
            }
        });

        new MainWork(getIntent().getStringExtra("key").toString()).execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long number) {
                Intent intent = new Intent(WorkActivity.this, ThisArticle.class);
                intent.putExtra("Description", rssArticles.get(index).getDescription());
                intent.putExtra("Link", rssArticles.get(index).getLink());
                intent.putExtra("Title", rssArticles.get(index).getTitle());
                intent.putExtra("PubDate", rssArticles.get(index).getPubDate());
                startActivity(intent);
            }
        });
    }

    class MainWork extends AsyncTask<Void, Void, String >  {
        String URLAdress;
        public MainWork(String URLAdress) {
            this.URLAdress = URLAdress;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            message.setText("Loading...");
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                DefaultHandler handler = new DefaultHandler() {
                    boolean isTitle = false;
                    boolean isDescription = false;
                    boolean isLink = false;
                    boolean isPubDate = false;

                    String title = "";
                    String description = "";
                    String link = "";
                    String pubdate = "";

                    @Override
                    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
                        super.startElement(uri, localName, name, attributes);
                        if (name.equalsIgnoreCase("TITLE")) {
                            title = "";
                            isTitle = true;
                        }
                        if (name.equalsIgnoreCase("DESCRIPTION")) {
                            description = "";
                            isDescription = true;
                        }
                        if (name.equalsIgnoreCase("LINK")) {
                            link = "";
                            isLink = true;
                        }
                        if (name.equalsIgnoreCase("PUBDATE")) {
                            pubdate = "";
                            isPubDate = true;
                        }
                    }

                    @Override
                    public void endElement(String uri, String localName,
                                           String name) throws SAXException {
                        if (name.equalsIgnoreCase("TITLE")) {
                            isTitle = false;
                        }
                        if (name.equalsIgnoreCase("DESCRIPTION")) {
                            isDescription = false;
                        }
                        if (name.equalsIgnoreCase("LINK")) {
                            isLink = false;
                        }
                        if (name.equalsIgnoreCase("PUBDATE")) {
                            isPubDate = false;
                        }
                        if (name.equalsIgnoreCase("ITEM")) {
                            Articles temporaryVariable = new Articles(title, description, link, pubdate);
                            rssArticles.add(temporaryVariable);
                        }
                    }
                    @Override
                    public void characters(char chars[], int start, int length) throws SAXException {
                        if (isTitle == true) {
                            title += new String(chars, start, length);
                        }
                        if (isDescription == true) {
                            description += new String(chars, start, length);
                        }
                        if (isLink == true) {
                            link += new String(chars, start, length);
                        }
                        if (isPubDate == true) {
                            pubdate += new String(chars, start, length);
                        }
                    }
                };
                URL url = new URL(URLAdress);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setConnectTimeout(15000);
                if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new Exception("Some problem with HttpConnecion");
                }

                InputStream inputStream = httpConnection.getInputStream();
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(inputStream));
                String tmp = buffReader.readLine().toString();
                String encoding = tmp.substring(tmp.indexOf("encoding") + 10, tmp.indexOf("?>") - 1);

                url = new URL(URLAdress);
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setConnectTimeout(15000);
                inputStream = httpConnection.getInputStream();

                Reader reader = new InputStreamReader(inputStream, encoding);
                InputSource is = new InputSource(reader);
                is.setEncoding(encoding);
                saxParser.parse(is, handler);
                return "Several articles for you ^^ :";
            } catch(Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            message.setText(result);
            listView.setAdapter(arrayAdapter);
        }
    }
}
