package com.yyp.tools.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by YanYan on 2019/4/15.
 * 滚动位置监听的ScrollView
 */

public class MonitorScrollView extends ScrollView {
    private OnScrollListener mListener;

    public MonitorScrollView(Context context) {
        this(context, null);
    }

    public MonitorScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonitorScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (mListener != null)
            mListener.onScroll(y);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mListener = listener;
    }

    public interface OnScrollListener {
        public void onScroll(int y);
    }
}

