package com.bizone.rssReader.exception;

import com.bizone.rssReader.R;

public class HttpConnectRSSException extends RSSException {
    public HttpConnectRSSException() {
	super(R.string.exception_http_connect);
    }
}
