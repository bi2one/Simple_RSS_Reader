package com.bizone.rssReader.DatabaseHelper;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bizone.rssReader.SQLRequestable;
import com.bizone.rssReader.exception.SQLUrlExistException;

import java.util.ArrayList;

public class FeedTableHelper {
    private static final String C_TABLE = "feeds";
    
    private static volatile FeedTableHelper _helper;
    private static Context _context;
    private RSSDBHelper _dbHelper;
    private SQLiteDatabase _writableDB;
    private SQLiteDatabase _readableDB;
    private SQLRequestable _requestable;

    private FeedTableHelper(Context context, SQLRequestable requestable) {
	_context = context;
	_dbHelper = new RSSDBHelper(context);
	_writableDB = _dbHelper.getWritableDatabase();
	_readableDB = _dbHelper.getReadableDatabase();
	_requestable = requestable;
    }

    public static FeedTableHelper getInstance(Context context, SQLRequestable requestable) {
	if (_context != context) {
	    _helper = null;
	}
	
	if (_helper == null) {
	    synchronized(FeedTableHelper.class) {
		if (_helper == null) {
		    _helper = new FeedTableHelper(context, requestable);
		}
	    }
	}
	return _helper;
    }

    public Feed save(Feed feed) {
	String feedId = feed.getId();
	String feedUrl = feed.getUrl();
	String feedName = feed.getName();
	ContentValues row = new ContentValues();

	if (feedUrl == null)
	    return null;

	if (feedId == null) {
	    long id;
	    row.put("url", feedUrl);
	    row.put("name", feedName);

	    try {
		id = _writableDB.insertOrThrow(C_TABLE, null, row);
	    } catch (SQLException e) {
		_requestable.performExceptionHandling(new SQLUrlExistException());
		return null;
	    }
	    
	    return findById(id + "");
	} else {
	    if (isExist(feed.getId())) {
		row.put("url", feedUrl);
		row.put("name", feedName);
		
		_writableDB.update(C_TABLE, row, "_id=" + feedId, null);
		return findById(feedId);
	    } else {
		row.put("_id", feedId);
		row.put("url", feedUrl);
		row.put("name", feedName);

		_writableDB.insert(C_TABLE, null, row);
		return findById(feedId);
	    }
	}
    }

    public ArrayList<Feed> findAll() {
	Cursor cursor = _readableDB.rawQuery("SELECT * FROM " + C_TABLE, null);
	Feed feed = getFeedFromCursor(cursor);
	ArrayList<Feed> result = new ArrayList<Feed>();
	while(feed != null) {
	    result.add(feed);
	    feed = getFeedFromCursor(cursor);
	}
        return result;
    }

    public Feed findByUrl(String url) {
	Cursor cursor = simpleFind("url", "'" + url + "'");
	return getFeedFromCursor(cursor);
    }

    public void removeAll() {
	_writableDB.execSQL("DELETE FROM " + C_TABLE + " WHERE 1=1");
    }

    public void removeById(String id) {
	_writableDB.execSQL("DELETE FROM " + C_TABLE + " WHERE _id=" + id);
    }

    public Feed findById(String id) {
	Cursor cursor = simpleFind("_id", id);
	return getFeedFromCursor(cursor);
    }

    public boolean isExist(String id) {
	Cursor cursor = simpleFind("_id", id);
	return cursor.moveToNext();
    }

    private Cursor simpleFind(String field, String value) {
	return _readableDB.rawQuery("SELECT * FROM " + C_TABLE + " WHERE " + field + "=" + value, null);
    }

    private Feed getFeedFromCursor(Cursor cursor) {
	if (cursor.moveToNext()) {
	    String id = cursor.getString(0);
	    String url = cursor.getString(1);
	    String name = cursor.getString(2);
	    return new Feed(id, url, name);
	} else {
	    return null;
	}
    }
}