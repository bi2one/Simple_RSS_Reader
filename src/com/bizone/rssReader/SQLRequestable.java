package com.bizone.rssReader;

import com.bizone.rssReader.exception.RSSException;

public interface SQLRequestable {
    public void performExceptionHandling(RSSException e);
}