package ru.ifmo.rain.loboda.rss;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyService extends IntentService
{
    protected static String feed;
    protected List<RSSRecord> records;
    PendingIntent pendingIntent;

    public MyService(){
        super("TaramPapPapPam");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String task = bundle.getString("task");
        if(task != null){
            if(feed != null){
                if(task.equals("UPDATE")){
                    RSSFetcher fetcher = new RSSFetcher();
                    fetcher.execute(feed);
                }
            }
            if(task.equals("SITE")){
                // By contract, make it impossible to be null
                feed = bundle.getString("site");
                RSSFetcher fetcher = new RSSFetcher();
                fetcher.execute(feed);
            }
        }
    }

    private void setResult(List<RSSRecord> records){
        this.records = records;
        Intent intent = new Intent();
        intent.setAction("ru.ifmo.rain.loboda.ACTION.sendResult");
        intent.putExtra("Result", (Serializable)records);
        sendBroadcast(intent);
    }

    private class RSSFetcher extends AsyncTask<String, Void, List<RSSRecord>> {
        private String cause;

        public String getCause() {
            return cause;
        }

        @Override
        protected List<RSSRecord> doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                inputStream = (new URL(strings[0])).openStream();
                String charset;
                byte[] buffer = new byte[2000];
                int count = 0;
                for (int i = 0; i < 2000; ++i) {
                    int ch = inputStream.read();
                    if (ch == -1) {
                        break;
                    }
                    buffer[count] = (byte) ch;
                    ++count;
                }
                byte[] newBuffer = new byte[count];
                System.arraycopy(buffer, 0, newBuffer, 0, count);
                String s = new String(newBuffer);
                Pattern pattern = Pattern.compile("encoding=\"(.*?)\"");
                Matcher matcher = pattern.matcher(s);
                boolean existsEncoding = matcher.find();
                if (existsEncoding) {
                    charset = matcher.group(1);
                } else {
                    charset = null;
                }
                RSSParser parser = new RSSParser(new SequenceInputStream(new ByteArrayInputStream(newBuffer), inputStream), charset);
                List<RSSRecord> records = parser.parse();
                return records;
            } catch (MalformedURLException e) {
                cause = "Невреный URL адрес";
                return null;
            } catch (SAXException e) {
                cause = "RSS канал отсутствует, пуст или содержит ошибки";
                return null;
            } catch (UnsupportedEncodingException e) {
                cause = "RSS канал отсутствует, пуст или содержит ошибки";
                return null;
            } catch (IOException e) {
                cause = "Ошибка чтения канала";
                return null;
            } catch (ParserConfigurationException e) {
                cause = "Ошибка чтения канала";
                return null;
            } catch (Exception e) {
                cause = "Ошибка чтения канала";
                return null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(List<RSSRecord> records) {
            setResult(records);
            return;
        }
    }
}
