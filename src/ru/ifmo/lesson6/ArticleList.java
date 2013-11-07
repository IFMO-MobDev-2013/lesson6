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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
                Log.w(TAG, "Parser failed: " + ex.getMessage());
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
    public static final String RSS_TYPE = "rss";
    public static final String ATOM_TYPE = "atom";

    static String articleTag = "entry";
    String url;
    static String ulrTag = "id";
    String title;
    static String titleTag = "title";
    Date date;
    static String dateTag = "published";
    static SimpleDateFormat dateFormat = new SimpleDateFormat();
    String description;
    static String descriptionTag = "summary";
    static String[] otherDescriptionsTag = new String[]{"etc", "content"};

    public static void setType(String s){
        if (s == null){
            Log.w("Article class", "Null pointer here");
        }
        if (ATOM_TYPE.equals(s)){
            articleTag = "entry";
            ulrTag = "id";
            titleTag = "title";
            dateTag = "published";
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            descriptionTag = "summary";

        } else if (RSS_TYPE.equals(s)){
            articleTag = "item";
            ulrTag = "link";
            titleTag = "title";
            dateTag = "pubDate";
            dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss ZZZZ");
            descriptionTag = "description";
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
    private static RootElement prepare(String type, ArrayList<Article> a) throws Exception{
        final Article currentArticle = new Article();
        final ArrayList<Article> messages = a;
        RootElement root;
        if (type.equals(Article.ATOM_TYPE)){
            root = new RootElement("http://www.w3.org/2005/Atom", "feed");
            android.sax.Element channel = root;
            Article.setType(Article.ATOM_TYPE);

            android.sax.Element item = channel.getChild("http://www.w3.org/2005/Atom", Article.articleTag);
            item.setEndElementListener(new EndElementListener(){
                public void end() {
                    messages.add(currentArticle.makeCopy());
                }
            });
            item.getChild("http://www.w3.org/2005/Atom", Article.titleTag).setEndTextElementListener(new EndTextElementListener(){
                public void end(String body) {
                    currentArticle.title = body;
                }
            });
            item.getChild("http://www.w3.org/2005/Atom", Article.ulrTag).setEndTextElementListener(new EndTextElementListener(){
                public void end(String body) {
                    currentArticle.url = body;
                }
            });
            item.getChild("http://www.w3.org/2005/Atom", Article.descriptionTag).setEndTextElementListener(new EndTextElementListener(){
                public void end(String body) {
                    currentArticle.description = body;
                }
            });
            item.getChild("http://www.w3.org/2005/Atom", Article.dateTag).setEndTextElementListener(new EndTextElementListener(){
                public void end(String body) {
                    try {
                        currentArticle.date = Article.dateFormat.parse(body);
                    } catch (ParseException ex){
                        Log.w("Parser", "Date parsing error");
                    }
                }
            });
            for (int j = 0; j < Article.otherDescriptionsTag.length; j++){
                item.getChild("http://www.w3.org/2005/Atom", Article.otherDescriptionsTag[j]).setEndTextElementListener(new EndTextElementListener(){
                    public void end(String body) {
                        if (body != "" && body != null){
                            currentArticle.description = body;
                        }
                    }
                });
            }

        } else if (type.equals(Article.RSS_TYPE)){
            root = new RootElement("rss");
            android.sax.Element channel = root.getChild("channel");
            Article.setType(Article.RSS_TYPE);

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
                    try {
                        currentArticle.date = Article.dateFormat.parse(body);
                    } catch (ParseException ex){
                        Log.w("Parser", "Date parsing error");
                    }
                }
            });
            for (int j = 0; j < Article.otherDescriptionsTag.length; j++){
                item.getChild(Article.otherDescriptionsTag[j]).setEndTextElementListener(new EndTextElementListener(){
                    public void end(String body) {
                        if (body != "" && body != null){
                            currentArticle.description = body;
                        }
                    }
                });
            }

        } else {
            throw new Exception("Wrong RSS type case");
        }

        return root;
    }
    public static ArrayList<Article> parse(String URLAdress) throws Exception {
        final ArrayList<Article> messages = new ArrayList<Article>();

        InputStream in = null;
        InputStreamReader inr = null;
        try {
            URL feedUrl = new URL(URLAdress);
            URLConnection conn = feedUrl.openConnection();
            in = conn.getInputStream();
            String headerPart = "";
            final String enc_key = "charset=";
            for (int j = 0; ; j++){
               headerPart = conn.getHeaderField(j).toLowerCase();
                if (headerPart == null || headerPart.indexOf(enc_key) != -1) break;
            }
            String encoding;
            String feedType;

            if (headerPart != null){
                encoding = headerPart.substring(headerPart.indexOf(enc_key) + enc_key.length());
                if (encoding.indexOf(';') != -1){
                    encoding = encoding.substring(0, encoding.indexOf(';'));
                }
                if (headerPart.indexOf(Article.ATOM_TYPE) != -1){
                    feedType = Article.ATOM_TYPE;
                } else if (headerPart.indexOf(Article.RSS_TYPE) != -1 || headerPart.indexOf("xml") != -1) {
                    feedType = Article.RSS_TYPE;
                } else {
                    throw new Exception("RSS type is out of consideration or isn't defined");
                }

            } else {
                throw new Exception("Failed to get encoding / RSS type");
            }

            RootElement root = prepare(feedType, messages);

            inr = new InputStreamReader(in, encoding);
            Xml.parse(inr, root.getContentHandler());
        } finally
         {
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