package com.example.Lession6;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MyActivity extends Activity {
	ListView listView;
	private ArrayList<Node> nodes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		listView = (ListView) findViewById(R.id.listView);
		onClickItem("http://lenta.ru/rss");
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View view, int index, long arg3) {
				Intent intent = new Intent(MyActivity.this, WebActivity.class);
				intent.putExtra("link", nodes.get(index).getDescription());
				startActivity(intent);
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.lenta:
				return onClickItem("http://lenta.ru/rss");
			case R.id.bash:
				return onClickItem("http://bash.im/rss/");
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	public boolean onClickItem(String link) {
		RSSAsyncTask rssTape = new RSSAsyncTask();
		rssTape.execute(link);
		Intent intentRSS = new Intent(this, RSSIntentService.class);
		startService(intentRSS.putExtra("link", link).putExtra("task", "load"));
		RSSBroadcastReceiver rssBroadcastReceiver = new RSSBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter("com.example.RSSReader.RESPONSE");
		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(rssBroadcastReceiver, intentFilter);
		Intent intent = new Intent(this, RSSIntentService.class);
		intent.putExtra("task", "refresh");
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager.cancel(pendingIntent);
		int halfhour = 1800000;
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + halfhour, halfhour, pendingIntent);
		return true;
	}

	private class RSSAsyncTask extends AsyncTask<String, Void, ArrayList<Node>> {
		@Override
		protected ArrayList<Node> doInBackground(String... strings) {
			ArrayList<Node> result = new ArrayList<Node>();
			InputStream inputStream = null;
			InputSource is;
			String decode;
			SAXParserFactory factory;
			SAXParser parser;
			RSSDefaultHandler rssDefaultHandler;

			try {
				inputStream = new URL(strings[0])
						.openConnection()
						.getInputStream();
				decode = (strings[0].indexOf("lenta") != -1) ? "UTF-8" : "windows-1251";
				is = new InputSource(new InputStreamReader(inputStream, decode));
				is.setEncoding(decode);
				factory = SAXParserFactory.newInstance();
				factory.setNamespaceAware(false);
				parser = factory.newSAXParser();
				rssDefaultHandler = new RSSDefaultHandler();
				parser.parse(is, rssDefaultHandler);
				result = rssDefaultHandler.getNodeArrayList();
			} catch (Exception e) {

				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(ArrayList<Node> result) {
			nodes = result;
			ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map;
			for (int i = 0; i < nodes.size(); i++) {
				map = new HashMap<String, String>();
				map.put("title", nodes.get(i).getTitle());
				map.put("date", nodes.get(i).getDate().toString());
				map.put("description", nodes.get(i).getDescription());
				items.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(MyActivity.this, items, R.layout.my_simple_item,
					new String[]{"title", "date"},
					new int[]{R.id.headerTextView, R.id.timeTextView});
			listView.setAdapter(adapter);
		}
	}



	public class RSSBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			nodes = getIntent().getExtras().getParcelableArrayList("nodes");
			ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map;
			for (int i = 0; i < nodes.size(); i++) {
				map = new HashMap<String, String>();
				map.put("title", nodes.get(i).getTitle());
				map.put("date", nodes.get(i).getDate().toString());
				map.put("description", nodes.get(i).getDescription());
				items.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(MyActivity.this, items, R.layout.my_simple_item,
					new String[]{"title", "date"},
					new int[]{R.id.headerTextView, R.id.timeTextView});

			listView.setAdapter(adapter);
		}
	}
}
