package com.example.android.trafficscotland.controller;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.android.trafficscotland.R;
import com.example.android.trafficscotland.model.RSSItem;
import com.example.android.trafficscotland.util.FeedDownload;
import com.example.android.trafficscotland.util.XMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlannedRoadworksFragment extends Fragment {
    private FeedDownload io;
    private XMLParser parse;
    private ListView itemsListView;
    private String URL_STRING = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private ArrayList<RSSItem> itemsView;
    public PlannedRoadworksFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planned_roadworks, container, false);
        // 2. create objects
        io = new FeedDownload();
        parse = new XMLParser();
        // 3. get references to the widgets
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Traffic Scotland - Planned Roadworks");
        itemsListView = (ListView) view.findViewById(R.id.itemsListView);
        // 4. set the listeners
        new DownloadFeed().execute(URL_STRING);
        // 5. return the View for the layout
        return view;
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
            PlannedRoadworksFragment.this.updateDisplay();
        }
    }
    public void updateDisplay()
    {
        if (itemsView == null) {
            Context context = getActivity().getApplicationContext();
            CharSequence text ="Unable to get RSS feed";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (RSSItem it : itemsView) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("pubDate", ( it.getPubDateFormatted()));
            map.put("title", ( "Title: " + it.getTitle()));
            map.put("startEndDate", ( "Start Date: " + it.getStartDate() + " - End Date: " + it.getEndDate()));
            map.put("description", ( "Description: " + it.getDescription()));
            map.put("link", ( "Link: " + it.getLink()));
            map.put("georss", ( "Coordinates: " + it.getGeorss()));
            data.add(map);
        }
        // create the resource, from, and to variables
        int resource = R.layout.listview_roadworks;
        String[] from = {"pubDate", "title", "startEndDate", "description", "link", "georss"};
        int[] to = {R.id.pubDateTextView, R.id.titleTextView, R.id.startEndDateTextView,
            R.id.descriptionTextView, R.id.linkTextView, R.id.geoTextView};

        // create and set the adapter
        if(getActivity() != null) {
            SimpleAdapter adapter =
                    new SimpleAdapter(getActivity(), data, resource, from, to);
            itemsListView.setAdapter(adapter);
        }
    }
}
