package com.bizone.rssReader.http.request;

import com.bizone.rssReader.http.parser.TestParser;
import com.bizone.rssReader.http.parser.BaseFeedParser;
import com.bizone.rssReader.http.parser.RSSParser;
import com.bizone.rssReader.http.request.HttpUtility.SimpleHttpResponse;
import com.bizone.rssReader.data.RSSData;
import com.bizone.rssReader.exception.RSSException;

import java.util.ArrayList;
import java.net.URL;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class FeedRequest extends HttpRequest {
    private RSSParser parser;
    private String _url;

    public FeedRequest(Context context) {
	super(context);
	parser = new TestParser(context);
    }
    
    public void actionUrl(String url) {
	_url = url;
	
	if (!url.startsWith("http://") && !url.startsWith("https://"))
	    _url = "http://" + url;
	
	parser = new BaseFeedParser(context);
    }

    public ArrayList<RSSData> request() throws RSSException {
	SimpleHttpResponse response = requestHttpResponseGet(_url, null, null);
	// "http://www.notcot.com/index.xml"
	String resultBody = response.getHttpResponseBodyAsString();

	// Log.d("====RESULT", parser.parseToRSSDataList(resultBody).toString());
	return parser.parseToRSSDataList(resultBody);
    }
}