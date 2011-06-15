package com.bizone.rssReader;

import java.util.ArrayList;

import com.bizone.rssReader.data.RSSData;
import com.bizone.rssReader.exception.RSSException;

public interface Requestable {
    public void requestCallBack(int tag, ArrayList<RSSData> data);
    public void requestExceptionCallBack(int tag, RSSException e) ;
}
