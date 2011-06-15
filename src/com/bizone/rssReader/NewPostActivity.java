package com.bizone.rssReader;

import android.app.ListActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;
import android.net.Uri;

import com.bizone.rssReader.exception.RSSException;
import com.bizone.rssReader.exception.SAXRSSException;
import com.bizone.rssReader.data.RSSData;
import com.bizone.rssReader.data.Message;
import com.bizone.rssReader.http.HttpRequestManager;
import com.bizone.rssReader.http.request.FeedRequest;
import com.bizone.rssReader.adapter.RSSListAdapter;
import com.bizone.rssReader.DatabaseHelper.FeedTableHelper;
import com.bizone.rssReader.DatabaseHelper.ArticleTableHelper;
import com.bizone.rssReader.DatabaseHelper.Feed;

import java.util.ArrayList;

public class NewPostActivity extends ListActivity implements Requestable,
							     SQLRequestable {
    private Context _context;
    private HttpRequestManager manager;
    private RSSListAdapter adapter;
    private ArrayList<RSSData> rssList;
    private ArrayList<Feed> feedsList;
    private FeedTableHelper feedsHelper;
    private ArticleTableHelper articlesHelper;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_post);
	
	_context = getApplicationContext();
	rssList = new ArrayList<RSSData>();
	adapter = new RSSListAdapter(_context, R.layout.list_item_rss, rssList);
	feedsHelper = FeedTableHelper.getInstance(_context, this);
	articlesHelper = ArticleTableHelper.getInstance(_context, this);
	manager = new HttpRequestManager(this, this);
	setListAdapter(adapter);

	reload();
    }

    public void reload() {
	rssList.clear();

	feedsList = feedsHelper.findAll();
	for (Feed feed : feedsList) {
	    FeedRequest request = new FeedRequest(_context);
	    request.actionUrl(feed.getUrl());
	    manager.request(this, request, 0);
	}
    }

    protected void onRestart() {
	super.onRestart();
	reload();
    }

    public void requestCallBack(int tag, ArrayList<RSSData> data) {
	// 여기서, 넣는 data를 손봐줘야한다..
	for (RSSData msg : data) {
	    if (!articlesHelper.isExistByUrl(((Message)msg).getLink().toString())) {
		rssList.add(msg);
	    }
	}
	adapter.notifyDataSetChanged();
    }

    public void requestExceptionCallBack(int tag, RSSException e) {
	// switch(tag) {
	// case 0:
	    // if (e instanceof SAXRSSException) {
		// ((SAXRSSException)e).setUrl("http://naver.com");
	//     }
	// }
	e.performExceptionHandling(_context);
    }

    public void performExceptionHandling(RSSException e) {
	e.performExceptionHandling(_context);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
	// Log.d("====", "clicked!!");
	Message msg = (Message)rssList.get(position);
	String url = msg.getLink().toString();
	articlesHelper.saveByUrl(url);
	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	startActivity(browserIntent);
    }

    public void onReloadClick(View v) {
	reload();
    }
}
