package com.bizone.rssReader.http.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;
import android.content.Context;

import com.bizone.rssReader.data.Message;
import com.bizone.rssReader.data.RSSData;
import com.bizone.rssReader.exception.RSSException;
import com.bizone.rssReader.exception.SAXRSSException;

import org.xml.sax.SAXException;
// import org.apache.harmony.xml.ExpatParser.ParseException;

public class BaseFeedParser extends RSSParser {
    // names of the XML tags
    static final String RSS = "rss";
    static final String CHANNEL = "channel";
    static final String ITEM = "item";
	
    static final String PUB_DATE = "pubDate";
    static final String DESCRIPTION = "description";
    static final String LINK = "link";
    static final String TITLE = "title";
	
    public BaseFeedParser(Context context) {
	super(context);
    }
    
    public ArrayList<RSSData> parseToRSSDataList(String data) throws RSSException {
	final Message currentMessage = new Message();
	RootElement root = new RootElement(RSS);
	final ArrayList<RSSData> messages = new ArrayList<RSSData>();
	Element itemlist = root.getChild(CHANNEL);
	Element item = itemlist.getChild(ITEM);
	item.setEndElementListener(new EndElementListener(){
		public void end() {
		    messages.add((RSSData)(currentMessage.copy()));
		}
	    });
	item.getChild(TITLE).setEndTextElementListener(new EndTextElementListener(){
		public void end(String body) {
		    currentMessage.setTitle(body);
		}
	    });
	item.getChild(LINK).setEndTextElementListener(new EndTextElementListener(){
		public void end(String body) {
		    currentMessage.setLink(body);
		}
	    });
	item.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener(){
		public void end(String body) {
		    currentMessage.setDescription(body);
		}
	    });
	item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener(){
		public void end(String body) {
		    currentMessage.setDate(body);
		}
	    });
	try {
	    Xml.parse(data, root.getContentHandler());
	} catch (SAXException e) {
	    throw new SAXRSSException();
	}
	return messages;
    }
}