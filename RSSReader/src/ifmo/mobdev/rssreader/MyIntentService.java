package ifmo.mobdev.rssreader;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

public class MyIntentService extends IntentService {
    public static final String ACTION_MyIntentService = "ifmo.mobdev.rssreader.intentservice.RESPONSE";

    public MyIntentService() {
        super("myname");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("url");

        String xml = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            Log.d("DownloadXMLTask", e.getLocalizedMessage());
        } catch (ClientProtocolException e) {
            Log.d("DownloadXMLTask", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("DownloadXMLTask", e.getLocalizedMessage());
        } catch (RuntimeException e) {
            Log.d("DownloadXMLTask", e.getLocalizedMessage());
        }

        Intent intentResponse = new Intent();
        intentResponse.setAction(ACTION_MyIntentService);
        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
        intentResponse.putExtra("xml", xml);
        sendBroadcast(intentResponse);
    }
}