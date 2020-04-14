package com.example.android.trafficscotland.util;

import android.util.Log;

import com.example.android.trafficscotland.model.RSSItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class FileIO
{
    // Traffic Scotland URLs
    //private String urlSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    //private String urlSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    //private String urlSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";

    ArrayList<RSSItem> roadworks = new ArrayList<>();
    public FileIO () {}

    public String downloadFile(String urlString) throws IOException{
        // variables
        URL url = new URL(urlString);
        BufferedReader buffer = null;
        InputStream in = null;
        String result = "";
        int response = -1;
        URLConnection conn = url.openConnection();
        try{
            // get the URL
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.setConnectTimeout(5000);
            httpConn.connect();

            response = httpConn.getResponseCode();

            if(response == HttpURLConnection.HTTP_OK) {
                Log.e("XML TAG", "Connection Found");
                // Read data
                in = httpConn.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);

                // pass data to a string
                String x = "";
                while((x = br.readLine()) != null) {
                    result = result + x;
                }
            } else {
                Log.e("XML TAG", "Connection not found!");
            }

        } catch (IOException e) {
            Log.e("Sorry can not connect!", e.toString());
        }
        return result;
    }

}
