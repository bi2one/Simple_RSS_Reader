package com.bizone.rssReader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bizone.rssReader.DatabaseHelper.Feed;
import com.bizone.rssReader.DatabaseHelper.FeedTableHelper;
import com.bizone.rssReader.exception.RSSException;

public class RegistRSSActivity extends Activity implements SQLRequestable {
    private Context _context;
    private EditText _editText;
    private FeedTableHelper _feedHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_rss);

	_context = getApplicationContext();
	_editText = (EditText)findViewById(R.id.rss_text);
	_feedHelper = FeedTableHelper.getInstance(_context, this);
    }

    public void onOkClicked(View v) {
	Feed feed = new Feed(null, _editText.getText().toString(), null);
	feed = _feedHelper.save(feed);
	if (feed != null) {
	    _editText.setText("");
	    Toast.makeText(_context, R.string.regist_rss_ok, Toast.LENGTH_SHORT).show();
	    finish();
	}
    }

    public void onCancelClicked(View v) {
	finish();
    }

    public void performExceptionHandling(RSSException e) {
	e.performExceptionHandling(_context);
    }
}
