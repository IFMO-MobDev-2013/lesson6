package com.example.lesson6;

import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Vector;

public class MyService extends IntentService {
    public MyService(){
        super("My RSS service");
    }

    public void onCreate(){
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<String> list = intent.getStringArrayListExtra("URLS");
        // here we can update list of out channels URLS
    }
}
