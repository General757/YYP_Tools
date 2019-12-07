package com.yyp.aps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by generalYan on 2019/10/28.
 */
class DecorView extends FrameLayout {

    public interface OnCloseListener {
        void onPrepareClose();
    }

    static final int STATUS_SETUP = 0x00;

    static final int STATUS_FOCUS = 0x01;

    static final int STATUS_PAUSE = 0x02;

    int stateNow;
    int animType;
    int animTime;

    boolean touchClose;

    Scroller scroller;
    int pointerId;

    int minumFling;
    int maxumFling;
    int mTouchSlop;
    float touchFromX;

    int scrollFromX;
    boolean scrollFirst;
    boolean allowSlop;

    VelocityTracker vcTracker;

    OnCloseListener mListener;

    public DecorView(Context context) {
        this(context, null);
    }

    public DecorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DecorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        touchClose = false;

        ViewConfiguration conf = ViewConfiguration.get(getContext());
        mTouchSlop = conf.getScaledTouchSlop();
        minumFling = conf.getScaledMinimumFlingVelocity();
        maxumFling = conf.getScaledMaximumFlingVelocity();

        stateNow = STATUS_SETUP;
        scroller = new Scroller(context);
        scrollTo(Integer.MAX_VALUE, 0);
    }

    public void setState(int stateNow) {
        this.stateNow = stateNow;
    }

    public void doSlide(int animType, int animTime) {
        this.animType = animType;
        this.animTime = animTime;
        requestLayout();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (stateNow != STATUS_SETUP) {
            scrollOnMeasure();
        }
    }

    public void setOnCloseListener(OnCloseListener mListener) {
        this.mListener = mListener;
    }

    public int getCurrentX() {
        return scroller.isFinished() ? getScrollX() : scroller.getFinalX();
    }

    public int getCurrentY() {
        return scroller.isFinished() ? getScrollY() : scroller.getFinalY();
    }

    public void scrollOnMeasure() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int startX = 0, startY = 0, finalX = 0, finalY = 0;

        switch (animType) {
            case SingleActivity.ANIM_TO_LEFT: {
                startX = stateNow == STATUS_FOCUS ? -width : 0;
                finalX = stateNow == STATUS_FOCUS ? 0 : width;
                break;
            }
            case SingleActivity.ANIM_TO_RIGHT: {
                startX = stateNow == STATUS_FOCUS ? width : 0;
                finalX = stateNow == STATUS_FOCUS ? 0 : -width;
                break;
            }
            case SingleActivity.ANIM_TO_TOP: {
                startY = stateNow == STATUS_FOCUS ? -height : 0;
                finalY = stateNow == STATUS_FOCUS ? 0 : height;
                animTime = animTime * height / width;
                break;
            }
            case SingleActivity.ANIM_TO_BOTTOM: {
                startY = stateNow == STATUS_FOCUS ? height : 0;
                finalY = stateNow == STATUS_FOCUS ? 0 : -height;
                animTime = animTime * height / width;
                break;
            }
            default: {
                startX = 0;
                finalX = 0;
            }
        }

        if (getCurrentX() != finalX || getCurrentY() != finalY) {
            if (!scroller.isFinished()) {
                scroller.abortAnimation();
            }

            if (animTime > 0 && (startX != finalX || startY != finalY)) {
                scroller.startScroll(startX, startY, finalX - startX, finalY - startY, animTime);
                invalidate();
            } else {
                scrollTo(finalX, finalY);
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN && stateNow != STATUS_FOCUS) {
            return false;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int sx = scroller.getCurrX();
            int sy = scroller.getCurrY();
            scrollTo(sx, sy);
            invalidate();
        }
    }

    public void onPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int id = ev.getPointerId(pointerIndex);

        if (id == pointerId) {
            if (vcTracker != null) {
                vcTracker.clear();
            }

            int newIndex = pointerIndex == 0 ? 1 : 0;
            pointerId = ev.getPointerId(newIndex);
            touchFromX = ev.getX(newIndex);
            scrollFromX = getScrollX();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            scrollFirst = !scroller.isFinished();
        }

        if (scrollFirst || stateNow != STATUS_FOCUS || !touchClose) {
            return false;
        }

        boolean intercept = false;

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                pointerId = ev.getPointerId(0);
                scrollFromX = getScrollX();
                touchFromX = ev.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int pointerIndex = ev.findPointerIndex(pointerId);
                int diffX = (int) (touchFromX - ev.getX(pointerIndex));

                if (Math.abs(diffX) > mTouchSlop) {
                    allowSlop = true;
                    intercept = true;
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
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            scrollFirst = !scroller.isFinished();
        }

        if (scrollFirst || stateNow != STATUS_FOCUS || !touchClose) {
            return false;
        }

        if (vcTracker == null) {
            vcTracker = VelocityTracker.obtain();
        }

        vcTracker.addMovement(ev);

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                pointerId = ev.getPointerId(0);
                scrollFromX = getScrollX();
                touchFromX = ev.getX();
                allowSlop = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int pointerIndex = ev.findPointerIndex(pointerId);
                int diffX = (int) (touchFromX - ev.getX(pointerIndex));

                if (allowSlop) {
                    int scrollX = scrollFromX + diffX;

                    if (scrollX > 0) {
                        touchFromX = ev.getX(pointerIndex);
                        scrollFromX = 0;
                        scrollX = 0;
                    }

                    scrollTo(scrollX, 0);
                } else if (Math.abs(diffX) > mTouchSlop) {
                    allowSlop = true;
                }

                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                onPointerUp(ev);
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                eventFinish();
                break;
            }
            case MotionEvent.ACTION_UP: {
                eventFinish();
                break;
            }
        }

        return true;
    }

    private void eventFinish() {
        vcTracker.computeCurrentVelocity(1000, maxumFling);
        int velocityX = (int) vcTracker.getXVelocity();
        vcTracker.recycle();
        vcTracker = null;

        if (allowSlop) {
            int width = getMeasuredWidth();
            int startX = getScrollX();
            int finalX = 0;

            if (Math.abs(velocityX) > minumFling) {
                stateNow = velocityX > 0 ? STATUS_PAUSE : STATUS_FOCUS;
                finalX = velocityX > 0 ? -width : 0;
            } else if (startX < -width / 2) {
                stateNow = STATUS_PAUSE;
                finalX = -width;
            } else {
                stateNow = STATUS_FOCUS;
                finalX = 0;
            }

            int millis = Math.abs(finalX - startX);
            millis = Math.min(millis, 480);

            scroller.startScroll(startX, 0, finalX - startX, 0, millis);
            invalidate();

            if (stateNow == STATUS_PAUSE && mListener != null) {
                mListener.onPrepareClose();
            }
        }
    }
}
