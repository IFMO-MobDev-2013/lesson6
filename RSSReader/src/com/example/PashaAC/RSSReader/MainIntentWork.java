package com.example.PashaAC.RSSReader;

import android.app.IntentService;
import android.content.Intent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainIntentWork extends IntentService {
    public static final String ACTION_MyIntentService = "ru.pavelasadchii.intentservice.RESPONSE";
    public static final String EXTRA_KEY_OUT = "EXTRA_OUT";

    public MainIntentWork() {
        super("myname");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String URLAdress = intent.getStringExtra("task");
        String answer = "";
        try {
            URL url = new URL(URLAdress);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(15000);
            if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception("Some problem with HttpConnecion");
            }
            InputStream inputStream = httpConnection.getInputStream();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = buffReader.readLine()) != null)
                answer = answer + line + "\n";

        } catch (Exception e) {
            answer = e.getMessage();
        }
        Intent intentResponse = new Intent();
        intentResponse.setAction(ACTION_MyIntentService);
        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
        intentResponse.putExtra(EXTRA_KEY_OUT, answer);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

