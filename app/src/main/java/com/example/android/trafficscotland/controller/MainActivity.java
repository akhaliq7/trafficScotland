/*
 * Author: Amar Khaliq
 * Matric Number: S1828979
 * Description: Creating an Android application that can display roadworks, current incidents,
 * and planned roadworks on three seperate pages.
 * Features Overview: The programming practices used in this application follows the specification
 * implemented with best practices such as Fragments, Asynctask, XMLPullParser, Object Oriented
 * Programming, Event driven programming and landscape and portrait views.
 * Date: 15/04/20
 */
package com.example.android.trafficscotland.controller;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.android.trafficscotland.R;
import com.example.android.trafficscotland.model.RSSItem;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentActivity;

import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);

        // loading the default fragment
        if(savedInstanceState == null) {
            loadFragment(new RoadworksFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.nav_roadworks:
                selectedFragment = new RoadworksFragment();
                break;
            case R.id.nav_current_incidents:
                selectedFragment = new CurrentIncidentFragment();
                break;
            case R.id.nav_planned_roadworks:
                selectedFragment = new PlannedRoadworksFragment();
                break;
        }
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();
        return loadFragment(selectedFragment);
    }

    private boolean loadFragment(Fragment selectedFragment) {
        //switching fragment
        if (selectedFragment != null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, selectedFragment)
                    .commit();
            return true;
        }
        return false;
    }
}
