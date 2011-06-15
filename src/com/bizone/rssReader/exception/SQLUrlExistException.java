package com.bizone.rssReader.exception;

import com.bizone.rssReader.R;

public class SQLUrlExistException extends RSSException {
    public SQLUrlExistException() {
	super(R.string.exception_sql_url_exist);
    }
}