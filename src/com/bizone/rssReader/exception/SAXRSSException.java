package com.bizone.rssReader.exception;

import com.bizone.rssReader.R;
import android.content.Context;
import android.widget.Toast;

public class SAXRSSException extends RSSException {
    private String _url;
    
    public SAXRSSException() {
	super(R.string.exception_sax);
    }

    public void setUrl(String url) {
	_url = url;
    }

    public void performExceptionHandling(Context context) {
	if (_url != null) {
	    String newMsg = "url-" + _url + context.getResources().getString(R.string.exception_sax);
	    Toast toast = Toast.makeText(context, newMsg, Toast.LENGTH_LONG);
	    toast.show();
	} else {
	    super.performExceptionHandling(context);
	}
    }
}
