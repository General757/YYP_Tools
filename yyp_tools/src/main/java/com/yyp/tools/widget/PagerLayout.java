package com.yyp.tools.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by YanYan on 2018/12/24.
 * 可滑动
 */

public class PagerLayout extends ViewGroup {
    public interface OnPageListener {
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

    boolean allowSlop;

    int scrollFromX;

    float touchFromX;

    int mTouchSlop;

    int maxumFling;

    int minumFling;

    int pointerId;

    Scroller mScroller;

    VelocityTracker evTracker;

    OnPageListener listener;

    public PagerLayout(Context context) {
        this(context, null);
    }

    public PagerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mScroller = new Scroller(context);

        ViewConfiguration conf = ViewConfiguration.get(context);
        maxumFling = conf.getScaledMaximumFlingVelocity();
        minumFling = conf.getScaledMinimumFlingVelocity();
        mTouchSlop = conf.getScaledTouchSlop();
    }

    public void setOnPageListener(OnPageListener listener) {
        this.listener = listener;
    }

    public void setPosition(int position, boolean refresh) {
        scrollToPage(position, refresh);
    }

    public void setPosition(int position) {
        setPosition(position, true);
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

    public void onPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int id = ev.getPointerId(pointerIndex);

        if (id == pointerId) {
            if (evTracker != null) {
                evTracker.clear();
            }

            int newIndex = pointerIndex == 0 ? 1 : 0;
            pointerId = ev.getPointerId(newIndex);
            touchFromX = ev.getX(newIndex);
            scrollFromX = getScrollX();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                intercept = !mScroller.isFinished();
                pointerId = ev.getPointerId(0);
                scrollFromX = getScrollX();
                touchFromX = ev.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int pointerIndex = ev.findPointerIndex(pointerId);
                int diffX = (int) (touchFromX - ev.getX(pointerIndex));

                if (Math.abs(diffX) > mTouchSlop) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    intercept = true;
                    allowSlop = true;
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                onPointerUp(ev);
                break;
            }
        }

        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (evTracker == null) {
            evTracker = VelocityTracker.obtain();
        }

        evTracker.addMovement(ev);

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                allowSlop = !mScroller.isFinished();

                if (allowSlop) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mScroller.abortAnimation();
                }

                pointerId = ev.getPointerId(0);
                scrollFromX = getScrollX();
                touchFromX = ev.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int pointerIndex = ev.findPointerIndex(pointerId);
                int diffX = (int) (touchFromX - ev.getX(pointerIndex));

                if (!allowSlop && Math.abs(diffX) > mTouchSlop) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    allowSlop = true;
                }

                if (allowSlop) {
                    int scrollX = scrollFromX + diffX;

                    scrollX = Math.min(scrollX, getMaxScrollX());
                    scrollX = Math.max(0, scrollX);
                    scrollTo(scrollX, 0);
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                onPointerUp(ev);
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                onEventFinish();
                break;
            }
            case MotionEvent.ACTION_UP: {
                onEventFinish();
                break;
            }
        }

        return true;
    }

    private void onEventFinish() {
        evTracker.computeCurrentVelocity(1000, maxumFling);
        float velocity = evTracker.getXVelocity();
        evTracker.recycle();
        evTracker = null;

        if (allowSlop) {

            int newPosition = 0;
            int scrollX = getScrollX();
            int width = getMeasuredWidth();

            if (Math.abs(velocity) > minumFling) {
                newPosition = scrollX / width;

                if (velocity < 0) {
                    newPosition++;
                } else if (scrollX < newPosition * width + width / 2) {
                    newPosition--;
                }
            } else {
                newPosition = (scrollX + width / 2) / width;
            }

            int count = getChildCount();
            newPosition = Math.max(0, newPosition);
            newPosition = Math.min(count - 1, newPosition);

            scrollToPage(newPosition);
        }
    }

    private void scrollToPage(int newPosition) {
        scrollToPage(newPosition, true);
    }

    private void scrollToPage(int newPosition, boolean refresh) {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        this.position = newPosition;

        int startX = getScrollX();
        int width = getMeasuredWidth();
        int finalX = position * width;

        int millis = Math.abs(finalX - startX) * 2;
        millis = Math.min(millis, 360);

        mScroller.startScroll(startX, 0, finalX - startX, 0, millis);

        if (refresh && listener != null) {
            listener.onPageChange(newPosition);
        }

        invalidate();
    }

    private int getMaxScrollX() {
        int count = getChildCount();

        int width = getMeasuredWidth();

        return Math.max(0, (count - 1) * width);
    }
}
