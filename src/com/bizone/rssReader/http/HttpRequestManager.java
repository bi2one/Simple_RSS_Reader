package com.bizone.rssReader.http;

import android.os.AsyncTask;
import android.app.Activity;
import android.util.Log;
import android.content.Context;

import com.bizone.rssReader.Requestable;
import com.bizone.rssReader.widget.Spinner;
import com.bizone.rssReader.widget.NormalSpinner;
import com.bizone.rssReader.http.request.HttpRequest;
import com.bizone.rssReader.data.RSSData;
import com.bizone.rssReader.exception.RSSException;

import java.util.ArrayList;
import java.util.Stack;

public class HttpRequestManager {
    private Context context;
    private Requestable requestable;
    private volatile static Spinner spinner;
    private HttpAsyncTask httpAsyncTask;
    private Stack<RequestTagPair> requestPool;
    private Stack<DataTagPair> dataPool;
    private RSSException lastOccuredException = null;
    private Activity mParentActivity;
    private boolean isRunning = false;

    public HttpRequestManager(Context context, Requestable requestable) {
	this.context = context;
	this.requestable = requestable;
	requestPool = new Stack<RequestTagPair>();
	dataPool = new Stack<DataTagPair>();
    }

    public void request(Activity parentActivity, HttpRequest request, int tag) {
	this.mParentActivity = parentActivity;
		
	spinner = getSpinner();
	requestPool.push(new RequestTagPair(request, tag));

	this.lastOccuredException = null;
	//request.setContext(context);
	httpAsyncTask = new HttpAsyncTask();
	httpAsyncTask.execute(tag);
    }

    private Spinner getSpinner() {
	if (spinner == null){
	    synchronized(NormalSpinner.class) {
		if (spinner == null) {
		    spinner = new NormalSpinner(context);
		}
	    }
	}
	return spinner;
    }

    public boolean isRunning() {
	return isRunning;
    }

    public void cancel() {
	if (httpAsyncTask != null) {
	    if (!httpAsyncTask.isCancelled()) {
		httpAsyncTask.cancel(true);
	    }
	    httpAsyncTask = null;
	}
    }

    private void onPreRequestData(int tag) {
	// Log.d("Request_Test", "pre request data!!");
    }

    private void onRequestData(int tag) {

	try {
	    synchronized(Stack.class) {
		RequestTagPair pair = requestPool.pop();
		dataPool.push(new DataTagPair(pair.getRequest().request(), pair.getTag()));
	    }
	} catch(RSSException e) {
	    lastOccuredException = e;
	}
	 Log.d("refresh", "on request data!!");
    }

    private void onPostRequestData(int tag) {
    	
	if (lastOccuredException != null){
	    requestable.requestExceptionCallBack(tag, lastOccuredException);
	}
	synchronized(Stack.class) {
	    while(dataPool.size() > 0) {
		DataTagPair pair = dataPool.pop();
		requestable.requestCallBack(pair.getTag(), pair.getData());
	    }
	}
    }

    private void startSpinner() {
	if (!requestPool.empty()) {
	    isRunning = true;
	    if (spinner != null) {
		Log.d("refresh", "Start spinner");
		spinner.start(mParentActivity);
	    }
	}
    }

    private void stopSpinner() {
	if (requestPool.empty()) {
	    isRunning = false;
	    if (spinner != null) {
		Log.d("refresh", "Stop spinner");
		spinner.stop();
	    }
	}
    }

    private class HttpAsyncTask extends AsyncTask<Integer, Integer, Integer> {
	protected int mId = 0;

	protected Integer doInBackground(Integer... params) {
		Log.d("refresh", "doin " + params.length);
		mId = params[0];
	    onRequestData(mId);
	    Log.d("refresh", "doin end");
	    return mId;
	}

	protected void onCancelled() {
	    stopSpinner();
	}

	protected void onPostExecute(Integer result) {
		Log.d("refresh", "onPostExecute");
	    stopSpinner();
	    onPostRequestData(mId);
	    Log.d("refresh", "onPostExecute end");
	}

	protected void onPreExecute() {
	    startSpinner();
	    onPreRequestData(mId);
	}

	protected void onProgressUpdate(Integer... values) { }
    }

    private class DataTagPair {
	private ArrayList<RSSData> data;
	private int tag;

	public DataTagPair(ArrayList<RSSData> data, int tag) {
	    this.data = data;
	    this.tag = tag;
	}

	public ArrayList<RSSData> getData() {
	    return data;
	}

	public int getTag() {
	    return tag;
	}
    }
	
    private class RequestTagPair {
	private HttpRequest request;
	private int tag;

	public RequestTagPair(HttpRequest request, int tag) {
	    this.request = request;
	    this.tag = tag;
	}

	public HttpRequest getRequest() {
	    return request;
	}

	public int getTag() {
	    return tag;
	}
    }    
}