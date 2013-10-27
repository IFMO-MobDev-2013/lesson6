package com.example.lesson6;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class MyIntentServ extends IntentService{
    private static final int REFRESH = 10 * 60 * 1000; //10 minutes
    private static ArrayList<RSSItem> rssItems = new ArrayList<RSSItem>();

    public MyIntentServ() {
        this("MyIntentServ");
    }

    public MyIntentServ(String name) {
        super(name);
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent){
        String task = intent.getStringExtra("task");
        if ("fetch".equals(task)){
            String link = intent.getStringExtra("link");
            try {
                URL url = new URL(link);
                rssItems = new ArrayList<RSSItem>();
                String var6;
                URLConnection connection;
                connection = url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                String encode = "utf-8";
                Scanner scanner = new Scanner(connection.getInputStream(),encode);
                var6 = scanner.nextLine();
                if (var6.split("encoding=").length>1){
                    encode = "";
                    for (int i=1;var6.split("encoding=")[1].charAt(i) != '"';i++)
                        encode += var6.split("encoding=")[1].charAt(i);
                }
                var6="";
                connection = url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                scanner = new Scanner(connection.getInputStream(),encode);
                while (scanner.hasNext()) {
                    var6 += scanner.nextLine();
                }
                MySAXApp mySAXApp = new MySAXApp();
                mySAXApp.parseSAX(var6);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setAlarm(intent);
        }
    }

    public void setAlarm(Intent intent){
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + REFRESH, pendingIntent);
    }

    public class MySAXApp extends DefaultHandler {
        private String currentElement = null;
        private String title;
        private String link;
        private String descrip;
        private String date;
        private boolean itemOpen;

        public void parseSAX(String rss) {
            try {
                System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");
                XMLReader xr = XMLReaderFactory.createXMLReader();
                MySAXApp handler = new MySAXApp();
                xr.setContentHandler(handler);
                xr.setErrorHandler(handler);
                File tmp = File.createTempFile("123", null);
                FileWriter fw = new FileWriter(tmp);
                BufferedWriter bf = new BufferedWriter(fw);
                bf.write(rss);
                bf.close();
                FileReader r = new FileReader(tmp);
                xr.parse(new InputSource(r));
                tmp.deleteOnExit();
            } catch(Exception e){
                System.out.println(e);
            }
        }

        public void startElement(String uri, String local_name, String raw_name, Attributes amap){
            currentElement = local_name;
            if ("item".equals(local_name) || "entry".equals(local_name)){
                itemOpen = true;
                title="";
                link="";
                date="";
                descrip="";
            }
        }

        public void endElement(String uri, String local_name, String raw_name){
            if ("item".equals(local_name) || "entry".equals(local_name)){
                itemOpen = false;
                rssItems.add(new RSSItem(title, descrip, date, link));
            }
        }

        public void characters(char[] ch, int start, int length){
            String value = new String(ch,start,length);
            if (!Character.isISOControl(value.charAt(0))) {
                if (itemOpen){
                    if ("title".equals(currentElement)) {
                        title += value;
                    } else if ("summary".equals(currentElement)) {
                        descrip += value;
                    } else if ("description".equals(currentElement)) {
                        descrip += value;
                    } else if ("published".equals(currentElement)) {
                        date += value;
                    } else if ("pubDate".equals(currentElement)) {
                        date += value;
                    } else if ("link".equals(currentElement)) {
                        link += value;
                    } else if ("id".equals(currentElement)) {
                        link += value;
                    }
                }
            }
        }
    }
}
