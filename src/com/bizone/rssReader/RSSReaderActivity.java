package com.bizone.rssReader;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.widget.TabHost;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.bizone.rssReader.DatabaseHelper.ArticleTableHelper;
import com.bizone.rssReader.exception.RSSException;

public class RSSReaderActivity extends TabActivity implements SQLRequestable, DialogInterface.OnClickListener {
    private Context _context;
    private AlertDialog flushConfirmDialog;
    private ArticleTableHelper articlesHelper;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
	
	_context = getApplicationContext();
	
	Resources res = getResources();
	articlesHelper = ArticleTableHelper.getInstance(_context, this);
	TabHost tabHost = getTabHost();
	TabHost.TabSpec spec1 = tabHost.newTabSpec("all_post");
	TabHost.TabSpec spec2 = tabHost.newTabSpec("new_post");

	flushConfirmDialog = new AlertDialog.Builder(this)
	    .setTitle(R.string.alert)
	    .setMessage(R.string.flush_article_really)
	    .setPositiveButton(R.string.yes, this)
	    .setNegativeButton(R.string.no, this)
	    .create();
	
	
	spec1.setIndicator(res.getString(R.string.all_post),
			   res.getDrawable(R.drawable.all_post))
	    .setContent(new Intent(_context, AllPostActivity.class));
	spec2.setIndicator(res.getString(R.string.new_post),
			   res.getDrawable(R.drawable.new_post))
	    .setContent(new Intent(_context, NewPostActivity.class));

	tabHost.addTab(spec1);
	tabHost.addTab(spec2);
	tabHost.setCurrentTab(0);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.rss_menu, menu);
	return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
	switch(item.getItemId()) {
	case R.id.regist_rss:
	    startActivity(new Intent(_context, RegistRSSActivity.class));
	    return true;
	case R.id.help:
	    startActivity(new Intent(_context, HelpActivity.class));
	    return true;
	case R.id.remove_rss:
	    startActivity(new Intent(_context, RemoveRSSActivity.class));
	    return true;
	case R.id.flush_article:
	    flushConfirmDialog.show();
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    public void performExceptionHandling(RSSException e) {
	e.performExceptionHandling(_context);
    }

    public void onClick(DialogInterface log, int which) {
	if (which == AlertDialog.BUTTON1) {
	    articlesHelper.removeAll();
	} else {
	}
    }
}
