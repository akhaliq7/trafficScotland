package com.example.android.trafficscotland.util;

import com.example.android.trafficscotland.model.RSSFeed;
import com.example.android.trafficscotland.model.RSSItem;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RSSFeedHandler extends DefaultHandler
{
    private RSSFeed feed;
    private RSSItem item;

    private boolean feedTitleHasBeenRead = false;
    private boolean feedPubDateHasBeenRead = false;

    private boolean isTitle = false;
    private boolean isDescription = false;
    private boolean isLink = false;
    private boolean isPubDate = false;    // pub date for feed
    public RSSFeed getFeed() {
        return feed;
    }

    @Override
    public void startDocument() throws SAXException {
        feed = new RSSFeed();
        item = new RSSItem();
    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        if (qName.equals("item")) {
            item = new RSSItem();
            return;
        }
        else if (qName.equals("title")) {
            isTitle = true;
            return;
        }
        else if (qName.equals("description")) {
            isDescription = true;
            return;
        }
        else if (qName.equals("link")) {
            isLink = true;
            return;
        }
        else if (qName.equals("pubDate")) {            // pub date for feed
            isPubDate = true;
            return;
        }

    }

    @Override
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException
    {
        if (qName.equals("item")) {
            feed.addItem(item);
            return;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
        String s = new String(ch, start, length);
        if (isTitle) {
            if (feedTitleHasBeenRead == false) {
                feed.setTitle(s);
                feedTitleHasBeenRead = true;
            }
            else {
                if (s.startsWith("<")) {
                    item.setTitle("No title available.");
                }

                else if (s.length() > 60) {  // s is description, not title
                    // set s as description
                    item.setDescription(s);

                    // truncate s and set as title
                    int endIndex = s.indexOf(" ", 50);
                    if (endIndex == -1) {
                        endIndex = 60;
                    }
                    String title = s.substring(0, endIndex);
                    title += "...";
                    item.setTitle(title);
                }
                else {
                    item.setTitle(s);
                    item.setDescription(s);  // only necessary for very short descriptions
                }
            }
            isTitle = false;
        }
        else if (isLink) {
            item.setLink(s);
            isLink = false;
        }
        else if (isDescription) {
            item.setDescription(s);
            isDescription = false;
        }
        else if (isPubDate) {
            if (feedPubDateHasBeenRead == false) {
                feed.setPubDate(s);
                feedPubDateHasBeenRead = true;
            }
            else {
                item.setPubDate(s);
            }
            isPubDate = false;
        }

    }
}
