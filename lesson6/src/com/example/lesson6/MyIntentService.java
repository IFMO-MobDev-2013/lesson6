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
    //public static final String XML= "xml";
    public static final String KEY_OUT = "outCode";
    public static final String KEY_DATA = "data";

    public static final String KEY_SUCC = "success";
    public static final String KEY_LOAD_FAIL = "loading failure";
  //  public static final String KEY_PARSE_FAIL = "parsing failure";

//    public static String KEY_LINK = "link";
//    public static String KEY_TITLE = "title";
//    public static String KEY_DATE = "pubDate";
//    public static String KEY_DESC = "description";
//    public static String KEY_ITEM = "item";

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

//            URL url = new URL(request);
//            urlConnection = url.openConnection();
//
//            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//            String line;
//            StringBuffer stb = new StringBuffer();
//            while ((line = br.readLine()) != null) {
//                stb.append(line);
//            }
//            xml = stb.toString();

        } catch (Exception e) {   // TODO: check what the exceptions i should catch
            e.printStackTrace();



            //intentResponse.putExtra(KEY_OUT, KEY_LOAD_FAIL);
            sendBroadcast(intentResponse);
            return;
        }

//        try {
//            SAXXMLParser sxp = new SAXXMLParser();
//            feed = sxp.parse(xml);
//
//        } catch (Exception e) {   // TODO: check what the exceptions i should catch
//            e.printStackTrace();
//
//            intentResponse.putExtra(KEY_OUT, KEY_PARSE_FAIL);
//            sendBroadcast(intentResponse);
//            return;
//        }

     //   intentResponse.putExtra(KEY_OUT, KEY_SUCC);

//        if(feed != null)
//           intentResponse.putExtra(KEY_OUT, KEY_SUCC);
//        else
//            intentResponse.putExtra(KEY_OUT, KEY_PARSE_FAIL);



        intentResponse.putExtra(KEY_DATA, xml);

        sendBroadcast(intentResponse);
    }
}
