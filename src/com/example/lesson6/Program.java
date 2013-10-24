package com.example.lesson6;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Vector;

public class Program extends Activity
{
    public MyAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Vector<Entry> e = new Vector<Entry>();
        adapter = new MyAdapter(this, e, this);
        ListView list_view = (ListView) findViewById(R.id.listView);
        list_view.setAdapter(adapter);
        loadArticles(getIntent().getStringExtra("URL"));
    }

    public void loadArticles(String url)
    {
        adapter.entries.clear();
        String link = String.valueOf(url);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(ProgressBar.VISIBLE);
            }
        });
        new XmlLoader(link, this).start();
    }

    public void onException(final Exception e)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(ProgressBar.INVISIBLE);
                Console.print("Exception: "+e.getMessage());
            }
        });
    }

    public void onRssLoaded(Document target)
    {
        Element root = target.getDocumentElement();
        Console.print("Root "+root.getTagName());
        if (target.getElementsByTagName("channel").getLength() > 0)
        {
            Console.print("Channel");
            NodeList nodes = root.getElementsByTagName("item");
            for (int i = 0; i < nodes.getLength(); ++i)
            {
                Entry entry = parseItem(nodes.item(i));
                Console.print(nodes.item(i).getNodeName());
                Console.print("Item ("+entry.link+"): "+entry.title);
                adapter.entries.add(entry);
            }
        }
        else if (target.getElementsByTagName("feed").getLength() > 0)
        {
            Console.print("Feed");
            NodeList nodes = root.getElementsByTagName("entry");
            for (int i = 0; i < nodes.getLength(); ++i)
            {
                Entry entry = parseEntry(nodes.item(i));
                Console.print(nodes.item(i).getNodeName());
                Console.print("Entry ("+entry.link+"): "+entry.title);
                adapter.entries.add(entry);
            }
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public Entry parseEntry(Node item)
    {
        Entry temp = new Entry();
        NodeList list = item.getChildNodes();
        for (int i = 0; i < list.getLength(); ++i)
        {
            Node c = list.item(i);
            if (c.getNodeName().equals(""))
            {
                continue;
            }
            if (c.getNodeName().equals("title"))
            {
                temp.title = c.getFirstChild().getNodeValue();
            }
            else if (c.getNodeName().equals("link"))
            {
                temp.link = ((Element)c).getAttribute("href");
            }
        }
        return temp;
    }

    public Entry parseItem(Node item)
    {
        Entry temp = new Entry();
        NodeList list = item.getChildNodes();
        for (int i = 0; i < list.getLength(); ++i)
        {
            Node c = list.item(i);
            if (c.getNodeName().equals(""))
            {
                continue;
            }
            if (c.getNodeName().equals("title"))
            {
                temp.title = c.getFirstChild().getNodeValue();
            }
            else if (c.getNodeName().equals("link"))
            {
                temp.link = c.getFirstChild().getNodeValue();
            }
        }
        return temp;
    }

    /*public void onRssLoaded(XmlDocument target)
    {
        Console.print("Rss loaded");
        //Vector<Entry> e = new Vector<Entry>();
        if (target.findByName("feed").size() > 0)
        {
            Vector<XmlNode> nodes = target.findByName("entry");
            for (int i = 0; i < nodes.size(); ++i)
            {
                Entry entry = parseEntry(nodes.get(i));
                Console.print("Entry ("+entry.link+"): "+entry.title);
                adapter.entries.add(entry);
                //e.add(entry);
            }
        }
        else if (target.findByName("channel").size() > 0)
        {
            Console.print("Channel");
            Vector<XmlNode> nodes = target.findByName("item");
            for (int i = 0; i < nodes.size(); ++i)
            {
                Entry entry = parseItem(nodes.get(i));
                Console.print("Item ("+entry.link+"): "+entry.title);
                adapter.entries.add(entry);
                //e.add(entry);
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }*/

    public void onClick(View v)
    {
        /*adapter.entries.clear();
        TextView tv = (TextView)findViewById(R.id.linkText);
        String link = String.valueOf(tv.getText());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(ProgressBar.VISIBLE);
            }
        });
        new XmlLoader(link, this).start();*/
    }

    public Entry parseItem(XmlNode item)
    {
        Entry temp = new Entry();
        for (int i = 0; i < item.children.size(); ++i)
        {
            XmlNode c = item.children.get(i);
            if (c.getName().equals("title"))
            {
                temp.title = c.getValue();
            }
            else if (c.getName().equals("link"))
            {
                temp.link = c.getValue();
            }
        }
        return temp;
    }

    public Entry parseEntry(XmlNode entry)
    {
        Entry temp = new Entry();
        for (int i = 0; i < entry.children.size(); ++i)
        {
            XmlNode c = entry.children.get(i);
            if (c.getName().equals("title"))
            {
                temp.title = c.getValue();
            }
            else if (c.getName().equals("link"))
            {
                temp.link = c.getAttribute("href");
            }
        }
        return temp;
    }
}
