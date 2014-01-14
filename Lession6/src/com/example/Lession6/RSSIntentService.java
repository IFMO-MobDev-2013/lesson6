package com.example.Lession6;

import android.app.IntentService;
import android.content.Intent;
import org.xml.sax.InputSource;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class RSSIntentService extends IntentService {
	private ArrayList<Node> nodes;
	private static String link;
	public RSSIntentService() {
		super("RSS#6");
	}
	public void onCreate() {
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String link;
		String task = intent.getStringExtra("task");
		InputStream inputStream = null;
		InputSource is;
		String decode;
		SAXParserFactory factory;
		SAXParser parser;
		RSSDefaultHandler rssDefaultHandler = null;

		if ("load".equals(task)) {
			RSSIntentService.link = intent.getStringExtra("link");
			link = RSSIntentService.link;
		} else {
			link = RSSIntentService.link;
		}
		if (RSSIntentService.link == null) {
			return;
		}

		try {
			inputStream = new URL(link)
					.openConnection()
					.getInputStream();
			decode = (link.indexOf("lenta") != -1) ? "UTF-8" : "windows-1251";
			is = new InputSource(new InputStreamReader(inputStream, decode));
			is.setEncoding(decode);
			factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(false);
			parser = factory.newSAXParser();
			parser.parse(is, rssDefaultHandler);
			nodes = rssDefaultHandler.getResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ignored) {
			}
		}
		Intent intentResponse = new Intent("com.example.RSSReader.RESPONSE");
		intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
		intentResponse.putParcelableArrayListExtra("nodes", nodes);
	}
}
