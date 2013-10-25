package com.java.android.dronov;

import android.util.Log;
import com.java.android.dronov.RSS.Entry;
import com.java.android.dronov.RSS.Feed;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: dronov
 * Date: 23.10.13
 * Time: 20:11
 * To change this template use File | Settings | File Templates.
 */

public class RSSHandler extends DefaultHandler {

    public static final String ATOM = "http://www.w3.org/2005/Atom";
    StringBuilder builder;
    Feed feed;
    Entry entry;
    private Stack<String> stack;
    private Stack<State> tagsStack;
    private Format currentFormat;

    @Override
    public void startDocument() throws SAXException {
        stack = new Stack<String>();
//        Log.d("start", "good");
        tagsStack = new Stack<State>();
        builder = null;
        feed = null;
        entry = null;
        currentFormat = Format.UNDEFINED_FORMAT;
    }

    @Override
    public void endDocument() throws SAXException {
        stack = null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Log.d("startElement", localName);
        stack.add(localName);
        if (currentFormat == Format.UNDEFINED_FORMAT) {
            if ((!uri.isEmpty() && uri.equals(ATOM)) && localName.equals("feed")) {
                currentFormat = Format.ATOM_FORMAT;
                feed = new Feed();
            } else if (localName.equals("rss")) {
                currentFormat = Format.RSS_FORMAT;
            }
            tagsStack.add(State.RSS);
        } else if (currentFormat == Format.RSS_FORMAT) {
            if (tagsStack.peek() == State.RSS) { // new feed
                if (localName.equals("channel")) {
                    feed = new Feed();
                    tagsStack.add(State.FEED);
                }
            } else if (tagsStack.peek() == State.FEED) { //in feed
                if (localName.equals("item")) {
                    entry = new Entry();  // new entry
                    tagsStack.add(State.ENTRY);
                }
            } else if (tagsStack.peek() == State.ENTRY) {
                if (localName.equals("description")) {
                    tagsStack.add(State.String);
                    builder = new StringBuilder();

                }
            }
        } else if (currentFormat == Format.ATOM_FORMAT) {
            if (tagsStack.peek() == State.RSS) { // new feed
                if (localName.equals("entry")) {
                    entry = new Entry();  // new entry
                    tagsStack.add(State.ENTRY);
                }
            } else if (tagsStack.peek() == State.ENTRY) {
                if (localName.equals("summary")) {
                    tagsStack.add(State.String);
                    builder = new StringBuilder();

                }
            } else if (tagsStack.peek() == State.FEED) { //in feed
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Log.d("endElement", localName);
        if (localName.equals("feed"))
            Log.d("MY god", tagsStack.size() + " ");
        if (tagsStack.isEmpty())
            return;
        if (!localName.equals(stack.pop()))
            return;

        if (currentFormat == Format.RSS_FORMAT) {
            if (tagsStack.peek() == State.RSS) {
                if (localName.equals("rss")) {
                    tagsStack.pop();
                }
            } else if (tagsStack.peek() == State.FEED && localName.equals("channel")) {
                tagsStack.pop();
            } else if (tagsStack.peek() == State.ENTRY && localName.equals("item")) {
                tagsStack.pop();
                feed.add(entry);
                entry = null;

            } else if (tagsStack.peek() == State.String && localName.equals("description")) {
                entry.setDescription(builder.toString());
                builder = null;
                tagsStack.pop();
            }
        } else if (currentFormat == Format.ATOM_FORMAT) {
            if (tagsStack.peek() == State.RSS) {
                if (localName.equals("feed")) {
                    tagsStack.pop();
                }
            } else if (tagsStack.peek() == State.ENTRY && localName.equals("entry")) {
                tagsStack.pop();
                feed.add(entry);
                entry = null;

            } else if (tagsStack.peek() == State.String && localName.equals("summary")) {
                entry.setDescription(builder.toString());
                builder = null;
                tagsStack.pop();
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        Log.d("char", start + " GOOD ");
        if (tagsStack.isEmpty())
            return;
        if (currentFormat == Format.RSS_FORMAT) {
            if (tagsStack.peek() == State.FEED) {
                String string = new String(ch, start, length);
                if (stack.peek().equals("title"))
                    feed.setTitle(string);
                else if (stack.peek().equals("description")) {
                    feed.setDescription(string);
                }
            } else if (tagsStack.peek() == State.ENTRY) {
                String string = new String(ch, start, length);
                if (stack.peek().equals("title")) {
                    entry.setTitle(string);
                } else if (stack.peek().equals("link")) {
                    entry.setLink(string);
                } else if (stack.peek().equals("pubDate")) {
                    entry.setPublishedDate(string);
                }
            } else if (tagsStack.peek() == State.String) {
                if (stack.peek().equals("description")) {
                    builder.append(ch, start, length);
                }
            }
        } else {
            if (tagsStack.peek() == State.RSS) {
                String string = new String(ch, start, length);
                if (stack.peek().equals("title"))
                    feed.setTitle(string);
                else if (stack.peek().equals("summary")) {
                    feed.setDescription(string);
                }
            } else if (tagsStack.peek() == State.ENTRY) {
                String string = new String(ch, start, length);
                if (stack.peek().equals("title")) {
                    entry.setTitle(string);
                } else if (stack.peek().equals("link")) {
                    entry.setLink(string);
                } else if (stack.peek().equals("published")) {
                    entry.setPublishedDate(string);
                }
            } else if (tagsStack.peek() == State.String) {
                if (stack.peek().equals("summary")) {
                    builder.append(ch, start, length);
                }
            }
        }
    }

    public Feed getFeed() {
        return feed;
    }

    private static enum Format {
        UNDEFINED_FORMAT, ATOM_FORMAT, RSS_FORMAT
    }

    private static enum State {
        RSS, FEED, ENTRY, String
    }
}
