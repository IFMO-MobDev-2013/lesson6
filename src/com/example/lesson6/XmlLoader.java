package com.example.lesson6;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.net.URLConnection;

public class XmlLoader extends Thread {
    private String path = "";
    Program program;
    public XmlLoader(String url, Program program)
    {
        this.path = url;
        this.program = program;
    }
    @Override
    public void run() {
        super.run();
        try {
            URL url = new URL(path);

            URLConnection connection = null;
            connection = url.openConnection();
            //String line;
            //StringBuilder builder = new StringBuilder();
            //BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            /*while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String response = builder.toString(); */
            //Console.print(response);
            //XmlDocument xd = new XmlDocument();
            //xd.loadXml(response);
            DocumentBuilder db =DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xd = db.parse(connection.getInputStream());
            program.onRssLoaded(xd);
        }
        catch (Exception e)
        {
            program.onException(e);
        }
    }
}
