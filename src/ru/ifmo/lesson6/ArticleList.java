package ru.ifmo.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: asus
 * Date: 23.10.13
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
public class ArticleList extends Activity {
    ArrayList<String> articleNames;
    ArrayAdapter<String> adapter;
    ArrayList<Article> articles;

    private static final String TAG = "ArticleList";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_list);

        ListView listView = (ListView) findViewById(R.id.articleList);
        articleNames = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, articleNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(ArticleList.this, WebActivity.class);
                intent.putExtra("description", articles.get(position).description);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        new RSSDownloader().execute(intent.getStringExtra("XmlUrl"));

    }

    class RSSDownloader extends AsyncTask<String, Void, ArrayList<Article>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Article> doInBackground(String... s) {
            try {
                return SaxParser.parse(s[0]);
            } catch (Exception ex){
                Log.w(TAG, "Parser failed");
            }
            return new ArrayList<Article>();
        }

        @Override
        protected void onPostExecute(ArrayList<Article> articles) {
            articleNames.clear();
            for (int i = 0; i < articles.size(); i++){
                articleNames.add(articles.get(i).title);
            }
            adapter.notifyDataSetChanged();
            ArticleList.this.articles = articles;
        }
    }

}

class Article {
    static int type = 0;
    static String articleTag = "entry";
    String url;
    static String ulrTag = "id";
    String title;
    static String titleTag = "title";
    String date;
    static String dateTag = "published";
    String description;
    static String descriptionTag = "description";
    static String[] otherDescriptionsTag = new String[]{"etc", "content"};

    public static void setType(int a){
        if (a == 0){
            type = 0;
            articleTag = "entry";
            ulrTag = "id";
            titleTag = "title";
            dateTag = "published";
            descriptionTag = "summary";
        } else if (a == 1){
            type = 1;
            articleTag = "item";
            ulrTag = "link";
            titleTag = "title";
            dateTag = "pubDate";
            descriptionTag = "description";
        }
    }
    public static void changeType(){
        if (type == 0){
            setType(1);
        } else {
            setType(0);
        }
    }
    public Article makeCopy(){
        Article a = new Article();
        a.url = url;
        a.title = title;
        a.date = date;
        a.description = description;
        return a;
    }

}


class SaxParser {

    public static ArrayList<Article> parse(String URLAdress) throws Exception {
        final Article currentArticle = new Article();
        RootElement root = new RootElement("rss");
        final ArrayList<Article> messages = new ArrayList<Article>();
        android.sax.Element channel = root.getChild("channel");
        for (int type = 0; type < 2; type++){
            android.sax.Element item = channel.getChild(Article.articleTag);
            item.setEndElementListener(new EndElementListener(){
                public void end() {
                    messages.add(currentArticle.makeCopy());
                }
            });
            item.getChild(Article.titleTag).setEndTextElementListener(new EndTextElementListener(){
                public void end(String body) {
                    currentArticle.title = body;
                }
            });
            item.getChild(Article.ulrTag).setEndTextElementListener(new EndTextElementListener(){
                public void end(String body) {
                    currentArticle.url = body;
                }
            });
            item.getChild(Article.descriptionTag).setEndTextElementListener(new EndTextElementListener(){
                public void end(String body) {
                    currentArticle.description = body;
                }
            });
            item.getChild(Article.dateTag).setEndTextElementListener(new EndTextElementListener(){
                public void end(String body) {
                    currentArticle.date = body;
                }
            });
            Article.changeType();
            for (int j = 0; j < Article.otherDescriptionsTag.length; j++){
                item.getChild(Article.otherDescriptionsTag[j]).setEndTextElementListener(new EndTextElementListener(){
                    public void end(String body) {
                        if (body != "" && body != null){
                            currentArticle.description = body;
                        }
                    }
                });
            }
        }


        InputStream in = null;
        InputStreamReader inr = null;
        try {
            URL feedUrl = new URL(URLAdress);
            URLConnection conn = feedUrl.openConnection();
            in = conn.getInputStream();
            String headerPart = "charset=UTF-8";
            for (int j = 0; headerPart != null; j++){
               headerPart = conn.getHeaderField(j);
               if (headerPart.indexOf("charset=") != -1) break;
            }
            String key = "charset=";
            String encoding;
            if (headerPart != null){
                encoding = headerPart.substring(headerPart.indexOf(key) + key.length());
                if (encoding.indexOf(';') != -1){
                    encoding = encoding.substring(0, encoding.indexOf(';'));
                }
            } else {
                encoding = "UTF-8";
            }

            inr = new InputStreamReader(in, encoding);
            Xml.parse(inr, root.getContentHandler());
        } finally {
            try{
                if (in != null) in.close();
            } catch (Throwable ex){
            }
            try{
                if (inr != null) inr.close();
            } catch (Throwable ex){
            }
        }

        return messages;
    }
}