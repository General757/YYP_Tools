/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */
package com.yyp.tools.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by generalYan on 2019/10/17.
 * 正方形控件
 */
public class SquareFrameLayout extends FrameLayout implements SizeChangedNotifier {

    public SquareFrameLayout(Context context) {
        super(context);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSize == 0 && heightSize == 0) {
            // If there are no constraints on size, let FrameLayout measure
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // Now use the smallest of the measured dimensions for both dimensions
            final int minSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(minSize, minSize);
            return;
        }

        final int size;
        if (widthSize == 0 || heightSize == 0) {
            // If one of the dimensions has no restriction on size, set both dimensions to be the
            // on that does
            size = Math.max(widthSize, heightSize);
        } else {
            // Both dimensions have restrictions on size, set both dimensions to be the
            // smallest of the two
            size = Math.min(widthSize, heightSize);
        }

        final int newMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(newMeasureSpec, newMeasureSpec);
    }

    private SizeChangedNotifier.OnSizeChangedListener mOnSizeChangedListener;

    @Override
    public void setOnSizeChangedListener(SizeChangedNotifier.OnSizeChangedListener listener) {
        mOnSizeChangedListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mOnSizeChangedListener != null) {
            mOnSizeChangedListener.onSizeChanged(this, w, h, oldw, oldh);
        }
    }
}

