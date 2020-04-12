package com.example.android.trafficscotland.util;

import android.content.Context;
import android.util.Log;

import com.example.android.trafficscotland.model.RSSFeed;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class FileIO
{
    // Traffic Scotland URLs
    //private String urlSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    //private String urlSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    //private String urlSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private final String URL_STRING = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private final String FILENAME = "rss_feed.xml";
    private Context context = null;

    public FileIO (Context context) { this.context = context;}

    public void downloadFile() {
        try{
            // get the URL
            URL url = new URL(URL_STRING);

            // get the input stream
            InputStream in = url.openStream();

            // get the output stream
            FileOutputStream out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

            // read input and write output
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);

            while (bytesRead != -1)
            {
                out.write(buffer, 0, bytesRead);
                bytesRead = in.read(buffer);
            }

            out.close();
            in.close();
        } catch (IOException e) {
            Log.e("Roadworks reader", e.toString());
        }
    }
    public RSSFeed readFile()
    {
        try {
            // REPLACE WITH PULLPARSER
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            RSSFeedHandler theRssHandler = new RSSFeedHandler();
            xmlreader.setContentHandler(theRssHandler);

            // read the file from internal storage
            FileInputStream in = context.openFileInput(FILENAME);

            // parse the data
            InputSource is = new InputSource(in);
            xmlreader.parse(is);

            // set the feed in the activity
            RSSFeed feed = theRssHandler.getFeed();
            return feed;
        } catch (Exception e) {
            Log.e("roadworks reader", e.toString());
            return null;
        }
    }
}
