package com.bizone.rssReader.widget;

import android.widget.RelativeLayout;
import android.content.Context;
import android.app.Activity;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bizone.rssReader.R;

public class Spinner extends RelativeLayout {
    protected Context mContext = null;
    protected Activity mActivity = null;

    public Spinner(Context context) {
		super(context);
		this.mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.popup_loading, this, true);
    }

    public void start(Activity activity) {
    	if (this.getParent() == null){
    		activity.addContentView(this, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	}
    	setVisibility(View.VISIBLE);
    }
    
    public void stop() {
    	ViewGroup vGroup = (ViewGroup)this.getParent();
    	if (vGroup != null){
        	vGroup.removeView(this);
    	}
    }
}