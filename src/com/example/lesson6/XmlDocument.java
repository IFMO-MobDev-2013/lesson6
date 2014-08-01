package com.example.lesson6;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

public class XmlDocument
{
    public XmlNode documentElement = null;
    private HashMap<String, Vector<XmlNode>> table;

    public XmlDocument()
    {
        table = new HashMap<String, Vector<XmlNode>>();
    }

    public void loadXml(String xml)
    {
        try
        {
            parse(xml);
        }
        catch (Exception e)
        {
            Console.print("ERROR! "+e.getMessage());
            e.printStackTrace();
        }
    }

    public Vector<XmlNode> findByName(String name)
    {
        if (!table.containsKey(name))
        {
            table.put(name, new Vector<XmlNode>());
        }
        return table.get(name);
    }

    private void parse(String xml) throws Exception {
        XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
        xpp.setInput(new StringReader(xml));
        xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        Stack<XmlNode> stack = new Stack<XmlNode>();
        int type = xpp.getEventType();
        while (type != XmlPullParser.END_DOCUMENT)
        {
            switch (type)
            {
                case XmlPullParser.START_TAG:
                {
                    //Console.print("<"+xpp.getName()+">");
                    XmlNode node = new XmlNode(xpp.getName());
                    if (stack.empty())
                    {
                        documentElement = node;
                    }
                    else
                    {
                        stack.peek().children.add(node);
                    }
                    stack.push(node);
                    if (!table.containsKey(xpp.getName()))
                    {
                        table.put(xpp.getName(), new Vector<XmlNode>());
                    }
                    table.get(xpp.getName()).add(node);
                    for (int j = 0; j < xpp.getAttributeCount(); ++j)
                    {
                       // Console.print(" "+xpp.getAttributeName(j)+" "+xpp.getAttributeValue(j));
                        node.setAttribute(xpp.getAttributeName(j), xpp.getAttributeValue(j));
                    }
                } break;
                case XmlPullParser.TEXT:
                {
                    if (stack.empty())
                    {
                        throw new Exception("Parse Error: EMPTY STACK");
                    }
                    stack.peek().setValue(xpp.getText());
                    //Console.print("Value: "+xpp.getText());
                } break;
                case XmlPullParser.END_TAG:
                {
                    //Console.print("</"+xpp.getName()+">");
                    if (stack.empty())
                    {
                        throw new Exception("Parse Error: EMPTY STACK");
                    }
                    if (!stack.peek().getName().equals(xpp.getName()))
                    {
                        throw new Exception("Parse Error");
                    }
                    stack.pop();
                } break;
            }
            type = xpp.next();
        }
    }
}
