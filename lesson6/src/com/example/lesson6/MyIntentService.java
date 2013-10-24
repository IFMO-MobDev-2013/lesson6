package com.example.lesson6;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: slavian
 * Date: 24.10.13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 * this file is responsible for downloading and parsing(call to SAX parser)
 */
public class MyIntentService extends IntentService {


    public static final String ACTION_MyIntentService = "RESPONSE";

    public static final String KEY_DATA = "data";



    public ArrayList<FeedItem> feed = null;
    //AlarmManager am = null;


    public MyIntentService() {
        super("myname");
     }

    public void onCreate() {
        super.onCreate();

      //  am = (AlarmManager) getSystemService(ALARM_SERVICE);


    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        Intent repeateIntent = new Intent();
//        PendingIntent p = PendingIntent.getBroadcast(this, 0, repeateIntent, 0);
//
//        am.set(AlarmManager.RTC, System.currentTimeMillis() + 4000, p);


        String request = intent.getStringExtra(MyActivity.QUERY);

        Intent intentResponse = new Intent();
        intentResponse.setAction(ACTION_MyIntentService);
        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);

        URLConnection urlConnection = null;
        BufferedReader br = null;
        String xml = null;

        try {
            // load data

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(request);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);


        } catch (Exception e) {
            e.printStackTrace();


            sendBroadcast(intentResponse);
            return;
        }




        intentResponse.putExtra(KEY_DATA, xml);

        sendBroadcast(intentResponse);
    }
}
