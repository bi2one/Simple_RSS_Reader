package com.bizone.rssReader.adapter;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.graphics.Typeface;
import android.util.Log;

import com.bizone.rssReader.DatabaseHelper.ArticleTableHelper;
import com.bizone.rssReader.exception.RSSException;
import com.bizone.rssReader.exception.SQLUrlExistException;
import com.bizone.rssReader.data.RSSData;
import com.bizone.rssReader.data.Message;
import com.bizone.rssReader.SQLRequestable;
import com.bizone.rssReader.R;

import java.util.ArrayList;

public class RSSListAdapter extends BaseAdapter implements SQLRequestable {
    private LayoutInflater _inflater;
    private Context _context;
    private ArrayList<RSSData> _messageList;
    private int _layoutRef;
    private ArticleTableHelper articleTableHelper;

    public RSSListAdapter(Context context, int layoutRef, ArrayList<RSSData> messageList) {
	_context = context;
	_inflater = LayoutInflater.from(context);
	_layoutRef = layoutRef;
	_messageList = messageList;
	articleTableHelper = ArticleTableHelper.getInstance(context, this);
    }

    public int getCount() {
	return _messageList.size();
    }

    public Object getItem(int position) {
	return _messageList.get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	RSS rss;

	if (convertView == null) {
	    convertView = _inflater.inflate(_layoutRef, null);

	    rss = new RSS();
	    rss.title = (TextView)convertView.findViewById(R.id.item_rss_title);
	    rss.url = (TextView)convertView.findViewById(R.id.item_rss_url);
	    rss.date = (TextView)convertView.findViewById(R.id.item_rss_date);

	    convertView.setTag(rss);
	} else {
	    rss = (RSS) convertView.getTag();
	}

	Message rssMessage = (Message)getItem(position);
	rss.title.setText(rssMessage.getTitle());

	String url = rssMessage.getLink().toString();
	if (!articleTableHelper.isExistByUrl(url)) {
	    rss.title.setTypeface(Typeface.create((String)null, Typeface.BOLD));
	    rss.title.setTextColor(_context.getResources().getColor(R.color.rss_subject_strong));
	} else {
	    rss.title.setTypeface(Typeface.create((String)null, Typeface.NORMAL));
	    rss.title.setTextColor(_context.getResources().getColor(R.color.rss_subject));
	}
	
	rss.url.setText(url);
	rss.date.setText(rssMessage.getDate());

	return convertView;
    }

    static class RSS {
	TextView title;
	TextView url;
	TextView date;
    }

    public void performExceptionHandling(RSSException e) {
	if (e instanceof SQLUrlExistException)
	    return ;
	else
	    e.performExceptionHandling(_context);
    }
}