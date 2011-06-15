package com.bizone.rssReader;

import android.app.ListActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bizone.rssReader.DatabaseHelper.Feed;
import com.bizone.rssReader.DatabaseHelper.FeedTableHelper;
import com.bizone.rssReader.exception.RSSException;
import com.bizone.rssReader.adapter.FeedListAdapter;

import java.util.ArrayList;

public class RemoveRSSActivity extends ListActivity implements SQLRequestable, DialogInterface.OnClickListener {
    private Context _context;
    private FeedTableHelper _feedHelper;
    private ArrayList<Feed> feedsList;
    private FeedListAdapter adapter;
    private AlertDialog removeConfirmDialog;
    private String removeId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_rss);

	_context = getApplicationContext();
	_feedHelper = FeedTableHelper.getInstance(_context, this);
	feedsList = new ArrayList<Feed>();
	adapter = new FeedListAdapter(_context, R.layout.list_item_feed, feedsList);
	feedsList.addAll(_feedHelper.findAll());
	setListAdapter(adapter);
	adapter.notifyDataSetChanged();

	removeConfirmDialog = new AlertDialog.Builder(this)
	    .setTitle(R.string.alert)
	    .setMessage(R.string.remove_really)
	    .setPositiveButton(R.string.yes, this)
	    .setNegativeButton(R.string.no, this)
	    .create();
    }

    public void performExceptionHandling(RSSException e) {
	e.performExceptionHandling(_context);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

	Feed feed = feedsList.get(position);

	removeId = feed.getId();
	removeConfirmDialog.show();

    }

    public void onClick(DialogInterface dialog, int which) {
	if (which == AlertDialog.BUTTON1) {
	    _feedHelper.removeById(removeId);
	    finish();
	} else {
	}
    }
}
