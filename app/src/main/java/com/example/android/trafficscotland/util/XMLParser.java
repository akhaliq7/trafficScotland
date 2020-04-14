package com.example.android.trafficscotland.util;

import android.util.Log;

import com.example.android.trafficscotland.model.RSSItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class XMLParser {
    RSSItem item;
    private ArrayList<RSSItem> items;

    public ArrayList<RSSItem> getItems() {
        return items;
    }

    public void pullParser(String reader)
    {
        items = new ArrayList<RSSItem>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(reader));
            int eventType = xpp.getEventType();
            helper help = new helper();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    System.out.println("Start Document");
                }
                else if(eventType == XmlPullParser.START_TAG){
                    if(xpp.getName().equals("item")) {
                        item = new RSSItem();
                        eventType = xpp.nextTag();
                        if(xpp.getName().equals("title")) {
                            eventType = xpp.next();
                            item.setTitle(xpp.getText());
                            eventType = xpp.nextTag();
                            eventType = xpp.nextTag();
                        }
                        if(xpp.getName().equals("description")){
                            eventType = xpp.next();
                            item.setDescription(help.getDescription(xpp.getText()));
                            String[] dates = help.getDates(xpp.getText());

                            if(dates != null) {
                                item.setStartDate(help.convertLongDateToShort(dates[0]));
                                item.setStartDate(help.convertLongDateToShort(dates[1]));
                            }
                            eventType = xpp.nextTag();
                            eventType = xpp.nextTag();
                        }
                        if(xpp.getName().equals("link")) {
                            eventType = xpp.next();
                            item.setLink(xpp.getText());
                            eventType = xpp.nextTag();
                            eventType = xpp.nextTag();
                        }
                        if(xpp.getName().equals("point")) {
                            eventType = xpp.next();
                            item.setGeorss(xpp.getText());
                            eventType = xpp.nextTag();
                            eventType = xpp.nextTag();
                        }
                        eventType = xpp.nextTag();
                        eventType = xpp.nextTag();
                        eventType = xpp.nextTag();
                        eventType = xpp.nextTag();

                        if(xpp.getName().equalsIgnoreCase("pubDate")) {
                            eventType = xpp.next();
                            item.setPubDate(help.convertLongDateToShort(xpp.getText()));

                            eventType = xpp.nextTag();
                            eventType = xpp.nextTag();
                        }
                        items.add(item);
                    }
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            Log.e("MyTag", "Parsing error" + e.toString());
        } catch (IOException ae1) {
            Log.e("MyTag", "IO error during parsing");
        }
        Log.e("XMLparser","End of document");
    }

}
