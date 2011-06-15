package com.bizone.rssReader.http.parser;

import android.content.Context;
import com.bizone.rssReader.data.RSSData;
import java.util.ArrayList;
    
public class TestParser extends RSSParser {
    public TestParser(Context context) {
	super(context);
    }
    
    public ArrayList<RSSData> parseToRSSDataList(String data) {
	return null;
    }
}