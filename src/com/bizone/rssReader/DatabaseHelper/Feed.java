package com.bizone.rssReader.DatabaseHelper;

public class Feed {
    private String _id;
    private String _url;
    private String _name;

    public Feed(String id, String url, String name) {
	setId(id);
	setUrl(url);
	setName(name);
    }

    public void setId(String id) {
	_id = id;
    }

    public void setUrl(String url) {
	_url = url;
    }

    public void setName(String name) {
	_name = name;
    }

    public String getId() {
	return _id;
    }

    public String getUrl() {
	return _url;
    }

    public String getName() {
	return _name;
    }
}