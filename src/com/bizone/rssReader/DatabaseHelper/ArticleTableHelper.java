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

public class ArticleTableHelper {
    private static final String C_TABLE = "articles";

    private static volatile ArticleTableHelper _helper;
    private static Context _context;
    private RSSDBHelper _dbHelper;
    private SQLiteDatabase _writableDB;
    private SQLiteDatabase _readableDB;
    private SQLRequestable _requestable;

    private ArticleTableHelper(Context context, SQLRequestable requestable) {
	_context = context;
	_dbHelper = new RSSDBHelper(context);
	_writableDB = _dbHelper.getWritableDatabase();
	_readableDB = _dbHelper.getReadableDatabase();
	_requestable = requestable;
    }

    public static ArticleTableHelper getInstance(Context context, SQLRequestable requestable) {
	if (_context != context) {
	    _helper = null;
	}
	
	if (_helper == null) {
	    synchronized(ArticleTableHelper.class) {
		if (_helper == null) {
		    _helper = new ArticleTableHelper(context, requestable);
		}
	    }
	}
	return _helper;
    }

    public Article save(Article article) {
	String articleId = article.getId();
	String articleUrl = article.getUrl();
	ContentValues row = new ContentValues();

	if (articleUrl == null)
	    return null;

	if (articleId == null) {
	    long id;
	    row.put("url", articleUrl);

	    try {
		id = _writableDB.insertOrThrow(C_TABLE, null, row);
	    } catch (SQLException e) {
		_requestable.performExceptionHandling(new SQLUrlExistException());
		return null;
	    }
	    
	    return findById(id + "");
	} else {
	    if (isExist(articleId)) {
		row.put("url", articleUrl);
		
		_writableDB.update(C_TABLE, row, "_id=" + articleId, null);
		return findById(articleId);
	    } else {
		row.put("_id", articleId);
		row.put("url", articleUrl);

		_writableDB.insert(C_TABLE, null, row);
		return findById(articleId);
	    }
	}
    }

    public ArrayList<Article> findAll() {
	Cursor cursor = _readableDB.rawQuery("SELECT * FROM " + C_TABLE, null);
	Article article = getArticleFromCursor(cursor);
	ArrayList<Article> result = new ArrayList<Article>();
	while(article != null) {
	    result.add(article);
	    article = getArticleFromCursor(cursor);
	}
        return result;
    }
    
    public Article findByUrl(String url) {
	Cursor cursor = simpleFind("url", "'" + url + "'");
	return getArticleFromCursor(cursor);
    }

    public void removeAll() {
	_writableDB.execSQL("DELETE FROM " + C_TABLE + " WHERE 1=1");
    }

    public Article findById(String id) {
	Cursor cursor = simpleFind("_id", id);
	return getArticleFromCursor(cursor);
    }

    public boolean isExist(String id) {
	Cursor cursor = simpleFind("_id", id);
	return cursor.moveToNext();
    }

    public boolean isExistByUrl(String url) {
	return findByUrl(url) != null;
    }

    public Article saveByUrl(String url) {
	Article article = new Article(null, url);
	return save(article);
    }

    private Cursor simpleFind(String field, String value) {
	return _readableDB.rawQuery("SELECT * FROM " + C_TABLE + " WHERE " + field + "=" + value, null);
    }

    private Article getArticleFromCursor(Cursor cursor) {
	if (cursor.moveToNext()) {
	    String id = cursor.getString(0);
	    String url = cursor.getString(1);
	    return new Article(id, url);
	} else {
	    return null;
	}
    }    
}