package com.example.android.trafficscotland.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.example.android.trafficscotland.R;
import com.example.android.trafficscotland.model.RSSItem;
import com.example.android.trafficscotland.util.FileIO;
import com.example.android.trafficscotland.util.XMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RoadworksFragment extends Fragment {

    private FileIO io; // object that will download the data
    private XMLParser parse; // object that will extract the data
    private ListView itemsListView; // the single widget in the roadworks fragment xml
    private ArrayList<RSSItem> itemsView; // Data structure to hold the dat
    private String URL_STRING = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    public RoadworksFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_roadworks, container, false);
        // create objects
        io = new FileIO();
        parse = new XMLParser();
        // get references to the widgets
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Traffic Scotland - Roadworks");
        itemsListView = (ListView) view.findViewById(R.id.itemsListView);
        // set the listeners
        new DownloadFeed().execute(URL_STRING);
        // return the View for the layout
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
            RoadworksFragment.this.updateDisplay();
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
            map.put("title", it.getTitle());
            data.add(map);
        }
        // create the resource, from, and to variables
        int resource = R.layout.listview_roadworks;
        String[] from = {"title"};
        int[] to = {R.id.titleTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(getActivity(), data, resource, from, to);
        itemsListView.setAdapter(adapter);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("CONFIGTAG", "Orientation changed");
        //super.onConfigurationChanged(newConfig);
        Configuration c = getResources().getConfiguration();

        if (c.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //	setContentView(R.layout.main);
            Toast toast = Toast.makeText(getActivity(), "Portrait Mode", Toast.LENGTH_SHORT);
            toast.show();
        } else if (c.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //setContentView(R.layout.main);
            Toast toast = Toast.makeText(getActivity(), "Landscape Mode", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

} // End of MainActivity
