package com.example.RSS_reader2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class ArticleDataBase {
   public static SQLiteDatabase sqLiteDatabase;
    private Context context;
    private DatabaseHelper databaseHelper;

    public static final String DATABASE_NAME = "rss_articles.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_ARTICLE_NAME = "my_articles";
    public static final String KEY_LINK_ARTICLE = "link";
    public static final String KEY_PUBDATE_ARTICLE = "time";
    public static final String KEY_LINKNAME_ARTICLE = "linkname";
    public static final String KEY_ID_ARTICLE = "_id";
    public static final String KEY_TITLE_ARTICLE = "title";
    public static final String KEY_DESCRIPTION_ARTICLE = "description";

    public static final String TABLE_FEED_NAME = "my_feeds";
    public static final String KEY_ID_FEED = "_id";
    public static final String KEY_FEED = "feed";
    public static final String KEY_FEED_NAME = "feed_name";

    private static final String DATABASE_ARTICLE_CREATE = "CREATE TABLE " + TABLE_ARTICLE_NAME + " (" + KEY_ID_ARTICLE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE_ARTICLE + " TEXT NOT NULL, " + KEY_DESCRIPTION_ARTICLE + " TEXT NOT NULL, " + KEY_LINKNAME_ARTICLE + " TEXT NOT NULL, " + KEY_LINK_ARTICLE + " TEXT NOT NULL, "
            + KEY_PUBDATE_ARTICLE + " TEXT NOT NULL);";

    private static final String DATABASE_FEED_CREATE = "CREATE TABLE " + TABLE_FEED_NAME + " (" + KEY_ID_FEED + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_FEED_NAME + " TEXT NOT NULL, " + KEY_FEED + " TEXT NOT NULL);";


    public ArticleDataBase(Context context) {
        this.context = context;
    }

    public ArticleDataBase open() throws SQLiteException {
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public long insertArticle(String title, String description, String linkname, String link, String pubdate) throws SQLiteException {
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE_ARTICLE, title);
        values.put(KEY_DESCRIPTION_ARTICLE, description);
        values.put(KEY_PUBDATE_ARTICLE, pubdate);
        values.put(KEY_LINKNAME_ARTICLE, linkname);
        values.put(KEY_LINK_ARTICLE, link);
        return sqLiteDatabase.insert(TABLE_ARTICLE_NAME, null, values);
    }

    public long insertFeed(String name, String feed) throws SQLiteException {
        ContentValues values = new ContentValues();
        values.put(KEY_FEED_NAME, name);
        values.put(KEY_FEED, feed);
        return sqLiteDatabase.insert(TABLE_FEED_NAME, null, values);
    }

    public ArrayList<HashMap<String, String>> getFeeds() {
        ArrayList<HashMap<String, String>> feeds = new ArrayList<HashMap<String, String>>();
        Cursor cursor = sqLiteDatabase.query(ArticleDataBase.TABLE_FEED_NAME, new String[] {
                ArticleDataBase.KEY_ID_FEED, ArticleDataBase.KEY_FEED_NAME, ArticleDataBase.KEY_FEED},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            String feedName = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_FEED_NAME));
            String feed = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_FEED));
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(MainActivity.KEY_URL_NAME, feedName);
            map.put(MainActivity.KEY_URL, feed);
            feeds.add(map);
        }
        return feeds;
    }

    public ArrayList<Article> getArticles(String mainLinkName) {
        ArrayList<Article> articles = new ArrayList<Article>();
        Cursor cursor = sqLiteDatabase.query(ArticleDataBase.TABLE_ARTICLE_NAME, new String[] {
                ArticleDataBase.KEY_ID_ARTICLE, ArticleDataBase.KEY_TITLE_ARTICLE, ArticleDataBase.KEY_DESCRIPTION_ARTICLE, ArticleDataBase.KEY_LINKNAME_ARTICLE, ArticleDataBase.KEY_LINK_ARTICLE, ArticleDataBase.KEY_PUBDATE_ARTICLE },
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            String linkName = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_LINKNAME_ARTICLE));
            if (linkName.equals(mainLinkName)) {
                String title = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_TITLE_ARTICLE));
                String description = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_DESCRIPTION_ARTICLE));
                String link = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_LINK_ARTICLE));
                String pubdate = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_PUBDATE_ARTICLE));
                Article tmp = new Article(title, description, link, pubdate);
                articles.add(tmp);
            }
        }
        if (articles.size() != 0)
            return articles;
        else
            return null;
    }

    public int getCountArticles() {
        Cursor cursor = sqLiteDatabase.query(ArticleDataBase.TABLE_ARTICLE_NAME, new String[] {
                ArticleDataBase.KEY_ID_ARTICLE, ArticleDataBase.KEY_TITLE_ARTICLE, ArticleDataBase.KEY_DESCRIPTION_ARTICLE, ArticleDataBase.KEY_LINKNAME_ARTICLE, ArticleDataBase.KEY_LINK_ARTICLE, ArticleDataBase.KEY_PUBDATE_ARTICLE },
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        return cursor.getCount();
    }

    public void updateFeed(String mainName, String mainFeed) {
        Cursor cursor = sqLiteDatabase.query(ArticleDataBase.TABLE_FEED_NAME, new String[] {
                ArticleDataBase.KEY_ID_FEED, ArticleDataBase.KEY_FEED_NAME, ArticleDataBase.KEY_FEED},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            String feed = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_FEED));
            if (feed.equals(mainFeed)) {
                ContentValues values = new ContentValues();
                values.put(KEY_FEED_NAME, mainName);
                sqLiteDatabase.update(TABLE_FEED_NAME, values, KEY_ID_FEED + "=" + cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_ID_FEED)), null);
                return;
            }
        }
    }

    public void deleteAllArticlesWithSuchLinkName(String mainLinkName) {
        Cursor cursor = sqLiteDatabase.query(ArticleDataBase.TABLE_ARTICLE_NAME, new String[] {
                ArticleDataBase.KEY_ID_ARTICLE, ArticleDataBase.KEY_TITLE_ARTICLE, ArticleDataBase.KEY_DESCRIPTION_ARTICLE, ArticleDataBase.KEY_LINKNAME_ARTICLE, ArticleDataBase.KEY_LINK_ARTICLE, ArticleDataBase.KEY_PUBDATE_ARTICLE },
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(ArticleDataBase.KEY_ID_ARTICLE));
            String linkName = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_LINKNAME_ARTICLE));
            if (mainLinkName.equals(linkName)) deleteArticle(id);
        }
    }

    public void deleteSuchURLFEeed(String mainFeed) {
        Cursor cursor = sqLiteDatabase.query(ArticleDataBase.TABLE_FEED_NAME, new String[] {
                ArticleDataBase.KEY_ID_FEED, ArticleDataBase.KEY_FEED_NAME, ArticleDataBase.KEY_FEED},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            String feed = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_FEED));
            if (feed.equals(mainFeed)) {
                deleteFeed(cursor.getInt(cursor.getColumnIndex(ArticleDataBase.KEY_ID_FEED)));
            }
        }
    }

    public boolean deleteFeed(int feedId) throws SQLiteException {
        return sqLiteDatabase.delete(TABLE_FEED_NAME, KEY_ID_FEED + "=" +  (new Integer(feedId)).toString(), null) > 0;
    }

    public boolean deleteArticle(int articleId) throws SQLiteException {
        return sqLiteDatabase.delete(TABLE_ARTICLE_NAME, KEY_ID_ARTICLE + "=" +  (new Integer(articleId)).toString(), null) > 0;
    }

    public void deleteALLArticles() throws SQLiteException {
        Cursor cursor = sqLiteDatabase.query(ArticleDataBase.TABLE_ARTICLE_NAME, new String[] {
                ArticleDataBase.KEY_ID_ARTICLE, ArticleDataBase.KEY_TITLE_ARTICLE, ArticleDataBase.KEY_DESCRIPTION_ARTICLE, ArticleDataBase.KEY_LINKNAME_ARTICLE, ArticleDataBase.KEY_LINK_ARTICLE, ArticleDataBase.KEY_PUBDATE_ARTICLE },
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            deleteArticle(cursor.getInt(cursor.getColumnIndex(ArticleDataBase.KEY_ID_ARTICLE)));
        }
    }

    public boolean isSuchArticles(String mainLink) {
        Cursor cursor = sqLiteDatabase.query(ArticleDataBase.TABLE_ARTICLE_NAME, new String[] {
                ArticleDataBase.KEY_ID_ARTICLE, ArticleDataBase.KEY_TITLE_ARTICLE, ArticleDataBase.KEY_DESCRIPTION_ARTICLE, ArticleDataBase.KEY_LINKNAME_ARTICLE, ArticleDataBase.KEY_LINK_ARTICLE, ArticleDataBase.KEY_PUBDATE_ARTICLE },
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            String linkName = cursor.getString(cursor.getColumnIndex(ArticleDataBase.KEY_LINKNAME_ARTICLE));
            if (mainLink.equals(linkName)) return true;
        }
        return false;
    }

    public void deleteALLFeeds() throws SQLiteException {
        Cursor cursor = sqLiteDatabase.query(ArticleDataBase.TABLE_FEED_NAME, new String[] {
                ArticleDataBase.KEY_ID_FEED, ArticleDataBase.KEY_FEED_NAME, ArticleDataBase.KEY_FEED},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            deleteFeed(cursor.getInt(cursor.getColumnIndex(ArticleDataBase.KEY_ID_FEED)));
        }
    }

    public boolean isEmptyFeedTable() {
        Cursor cursor = sqLiteDatabase.query(ArticleDataBase.TABLE_FEED_NAME, new String[] {
                ArticleDataBase.KEY_ID_FEED, ArticleDataBase.KEY_FEED_NAME, ArticleDataBase.KEY_FEED},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        return (cursor.moveToNext() ? false : true);
    }



    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("TABLE ARTICLE CREATE", DATABASE_ARTICLE_CREATE);
            Log.d("TABLE FEED CREATE", DATABASE_FEED_CREATE);
            db.execSQL(DATABASE_ARTICLE_CREATE);
            db.execSQL(DATABASE_FEED_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_ARTICLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_FEED_NAME);
            onCreate(db);
        }
    }
}
