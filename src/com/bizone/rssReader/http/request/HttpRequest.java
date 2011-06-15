package com.bizone.rssReader.http.request;

import com.bizone.rssReader.http.request.HttpUtility.SimpleHttpResponse;
import com.bizone.rssReader.data.RSSData;
import com.bizone.rssReader.exception.HttpConnectRSSException;
import com.bizone.rssReader.exception.RSSException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.*;
import android.content.Context;

enum HttpMethod { HTTP_POST, HTTP_GET }

public abstract class HttpRequest {
    protected Context context = null;
    
    protected Hashtable<String, Object> postHashtable;
    protected Hashtable<String, String> getHashtable;
    protected HttpMethod httpMethod;

    @SuppressWarnings("unused")
	private HttpRequest(){}
	
    public HttpRequest(Context context) {
	this.context = context;
	postHashtable = new Hashtable<String, Object>();
	getHashtable = new Hashtable<String, String>();
    }
	
    public abstract ArrayList<RSSData> request() throws RSSException;
        
    protected SimpleHttpResponse requestHttpResponseGet(String url, Map<String, String> header, Map<String, String> param)
	throws HttpConnectRSSException {
	return requestHttpResponse(url, header, null, param, HttpUtility.ASYNC_METHOD_GET);
    }

    protected SimpleHttpResponse requestHttpResponsePost(String url, Map<String, String> header, Map<String, Object> param)
	throws HttpConnectRSSException {
	return requestHttpResponse(url, header, param, null, HttpUtility.ASYNC_METHOD_POST);
    }
    
    private SimpleHttpResponse requestHttpResponse(String url,
						   Map<String, String> header,
						   Map<String, Object> postParam,
						   Map<String, String> getParam,
						   int method) throws HttpConnectRSSException {
	SimpleHttpResponse httpResponse = null;
	ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo netInfo_mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	NetworkInfo netInfo_wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
	if((netInfo_mobile.getState() == NetworkInfo.State.CONNECTED) ||
	   (netInfo_wifi.getState() == NetworkInfo.State.CONNECTED)) {
	    Map<String,String> baseHeader = new HashMap<String,String>();

	    baseHeader.put("Agent", "MAJTI_ANDROID");
	    baseHeader.put("Content-Type", "text/xml");
	
	    if(header != null) {
		baseHeader.putAll(header);
	    }

	    if(method == HttpUtility.ASYNC_METHOD_POST) {
		httpResponse = HttpUtility.getInstance().post(url, baseHeader, postParam);
	    } else {
		httpResponse = HttpUtility.getInstance().get(url, baseHeader, getParam);
	    }
	} else {
	    httpResponse = null;
	}

	if (httpResponse == null)
	    throw new HttpConnectRSSException();
	else
	    return httpResponse;
    }
}