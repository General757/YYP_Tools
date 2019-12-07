package com.yyp.tools.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by generalYan on 2018/12/24.
 * 只能点击不可滑动
 */

public class PagesLayout extends ViewGroup {
    public interface OnPagesListener {
        public void onPageChange(int position);
    }

    public class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }

    int position;

    Scroller mScroller;

    OnPagesListener listener;

    public PagesLayout(Context context) {
        this(context, null);
    }

    public PagesLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagesLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mScroller = new Scroller(context);

    }

    public void setOnPagesListener(OnPagesListener listener) {
        this.listener = listener;
    }

    public void setPosition(int position) {
        scrollToPage(position);
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int count = getChildCount();
        View child = null;

        int maxChildWidth = 0, maxChildHeight = 0;

        for (int index = 0; index < count; index++) {
            child = getChildAt(index);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), lp.width);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), lp.height);

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            maxChildWidth = Math.max(maxChildWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
            maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
        }

        int useWidth = widthMode == MeasureSpec.EXACTLY ? widthSize : getPaddingLeft() + getPaddingRight() + maxChildWidth;
        int useHeight = heightMode == MeasureSpec.EXACTLY ? heightSize : getPaddingTop() + getPaddingBottom() + maxChildHeight;

        setMeasuredDimension(useWidth, useHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        View child = null;

        int width = getMeasuredWidth();

        for (int index = 0; index < count; index++) {
            child = getChildAt(index);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int left = index * width + getPaddingLeft() + lp.leftMargin;
            int top = getPaddingTop() + lp.topMargin;

            int right = left + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();

            child.layout(left, top, right, bottom);
        }

    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int sx = mScroller.getCurrX();
            int sy = mScroller.getCurrY();

            scrollTo(sx, sy);
            invalidate();
        }
    }

    private void scrollToPage(int newPosition) {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        if (listener != null) {
            listener.onPageChange(newPosition);
        }

        this.position = newPosition;

        int startX = getScrollX();
        int width = getResources().getDisplayMetrics().widthPixels;
        // int width = getMeasuredWidth();
        int finalX = position * width;

        int millis = Math.abs(finalX - startX) * 2;
        millis = Math.min(millis, 360);

        mScroller.startScroll(startX, 0, finalX - startX, 0, millis);
        invalidate();
    }
}
