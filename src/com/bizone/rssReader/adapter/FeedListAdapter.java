package com.bizone.rssReader.adapter;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.graphics.Typeface;
import android.util.Log;

import com.bizone.rssReader.exception.RSSException;
import com.bizone.rssReader.exception.SQLUrlExistException;
import com.bizone.rssReader.DatabaseHelper.Feed;
import com.bizone.rssReader.data.RSSData;
import com.bizone.rssReader.data.Message;
import com.bizone.rssReader.SQLRequestable;
import com.bizone.rssReader.R;

import java.util.ArrayList;

public class FeedListAdapter extends BaseAdapter {
    private LayoutInflater _inflater;
    private Context _context;
    private ArrayList<Feed> _feedList;
    private int _layoutRef;

    public FeedListAdapter(Context context, int layoutRef, ArrayList<Feed> feedList) {
	_context = context;
	_inflater = LayoutInflater.from(context);
	_layoutRef = layoutRef;
	_feedList = feedList;
    }

    public int getCount() {
	return _feedList.size();
    }

    public Object getItem(int position) {
	return _feedList.get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	FeedView feedView;

	if (convertView == null) {
	    convertView = _inflater.inflate(_layoutRef, null);

	    feedView = new FeedView();
	    feedView.url = (TextView)convertView.findViewById(R.id.item_feed_url);

	    convertView.setTag(feedView);
	} else {
	    feedView = (FeedView) convertView.getTag();
	}

	Feed feed = (Feed)getItem(position);
	feedView.url.setText(feed.getUrl());
	
	return convertView;
    }

    static class FeedView {
	TextView url;
    }
}