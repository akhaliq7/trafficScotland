package com.example.android.trafficscotland.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.android.trafficscotland.R;
import com.example.android.trafficscotland.model.RSSItem;
import com.example.android.trafficscotland.util.FileIO;
import com.example.android.trafficscotland.util.XMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RoadworksActivity extends Activity implements AdapterView.OnItemClickListener {

    private FileIO io;
    private XMLParser parse;
    private TextView titleTextView;
    private ListView itemsListView;
    private ArrayList<RSSItem> itemsView;
    private RSSItem view;
    private final String URL_STRING = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadworks_activity);

        io = new FileIO();
        parse = new XMLParser();

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        itemsListView = (ListView) findViewById(R.id.itemsListView);

        itemsListView.setOnItemClickListener(this);

        new DownloadFeed().execute(URL_STRING);
    }


    class DownloadFeed extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String in = null;
            try {
                in = io.downloadFile(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return in;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("News reader", "Feed downloaded");
            new ReadFeed().execute(result);
        }
    }
        // 1. params, 2. progress 3. results
    class ReadFeed extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String s = params[0];
            parse.pullParser(s);
            itemsView = parse.getItems();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("Roadworks reader", "Feed read");
            // update the display for the activity
            RoadworksActivity.this.updateDisplay();
        }
    }

    public void updateDisplay()
    {
        if (itemsView == null) {
            titleTextView.setText("Unable to get RSS feed");
            return;
        }
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (RSSItem it : itemsView) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("title", it.getTitle());
            data.add(map);
        }
        // create the resource, from, and to variables
        int resource = R.layout.listview_roadworks;
        String[] from = {"title"};
        int[] to = {R.id.titleTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
//        RSSItem item = itemsView.getItem(position);

        Intent intent = new Intent(this, RoadworkActivity.class);
//        intent.putExtra("pubdate", item.getPubDate());
//        intent.putExtra("title", item.getTitle());
//        intent.putExtra("description", item.getDescription());
//        intent.putExtra("link", item.getLink());

        this.startActivity(intent);
    }

} // End of MainActivity
