package com.example.android.trafficscotland.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RSSItem
{
    private String title = null;
    private String description = null;
    private String link = null;
    private String pubDate = null;
    private String startDate = null;
    private String endDate = null;
    private String georss = null;
    private SimpleDateFormat dateOutFormat =
            new SimpleDateFormat("EEEE h:mm a (MMM d)");   // Only includes date, not time

    private SimpleDateFormat dateInFormat =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");     // Only includes date, not time

    public void setTitle(String title)     {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description)     {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getPubDateFormatted() {
        try {
            if (pubDate != null) {              // make sure pubDate exists
                Date date = dateInFormat.parse(pubDate);
                String pubDateFormatted = dateOutFormat.format(date);
                return pubDateFormatted;
            }
            else {
                return "No date in RSS feed";
            }
        }
        catch (ParseException e) {
            return "No date in RSS feed";      // don't throw exception
        }
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDateAsString(String endDate) {
        this.endDate = endDate;
    }

    public String getGeorss() {
        return georss;
    }

    public void setGeorss(String georss) {
        this.georss = georss;
    }
}
