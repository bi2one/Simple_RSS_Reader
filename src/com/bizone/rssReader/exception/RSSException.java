package com.bizone.rssReader.exception;

import android.content.Context;
import android.widget.Toast;

public class RSSException extends Exception {
    private int msgRef;

    public RSSException(int msgRef) {
	setMsg(msgRef);
    }

    protected void setMsg(int msgRef) {
	this.msgRef = msgRef;
    }

    public int getMsg() {
	return msgRef;
    }

    public void showToastMsg(Context context) {
	Toast toast = Toast.makeText(context, getMsg(), Toast.LENGTH_SHORT);
	toast.show();
    }

    public void performExceptionHandling(Context context) {
	showToastMsg(context);
    }    
}