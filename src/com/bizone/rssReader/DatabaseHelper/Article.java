package com.bizone.rssReader.DatabaseHelper;

public class Article {
    private String _id;
    private String _url;

    public Article(String id, String url) {
	setId(id);
	setUrl(url);
    }
    
    public void setId(String id) {
	_id = id;
    }

    public void setUrl(String url) {
	_url = url;
    }

    public String getId() {
	return _id;
    }

    public String getUrl() {
	return _url;
    }
}