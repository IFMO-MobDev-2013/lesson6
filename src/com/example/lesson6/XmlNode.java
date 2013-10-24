package com.example.lesson6;


import java.util.HashMap;
import java.util.Vector;

public class XmlNode
{
    private String name = "";
    private String value = "";
    public Vector<XmlNode> children;
    private HashMap<String, String> attributes;

    public XmlNode(String name)
    {
        this.name = name;
        children = new Vector<XmlNode>();
        attributes = new HashMap<String, String>();
    }

    public void setAttribute(String name, String value)
    {
        attributes.put(name, value);
    }

    public String getAttribute(String name)
    {
        if (!attributes.containsKey(name))
        {
            return "";
        }
        return attributes.get(name);
    }
    public String getName()
    {
        return name;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        XmlNode current = this;
            sb.append("<"+current.getName()+">");
            if (current.children.size() == 0)
            {
                sb.append(current.getValue());
            }
            else
            {
                for (int i = 0; i < current.children.size(); ++i)
                {
                    sb.append(current.children.get(i).toString());
                }
            }
            sb.append("</"+current.getName()+">");

        return sb.toString();
    }
}
