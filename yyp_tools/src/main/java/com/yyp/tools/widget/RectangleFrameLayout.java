package com.yyp.tools.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.yyp.tools.R;

/**
 * Created by YanYan on 2019/10/17.
 * 矩形控件
 */
public class RectangleFrameLayout extends FrameLayout implements SizeChangedNotifier {
    private int rectangleWidth = 0;
    private int rectangleHeight = 0;
    private float rectangleScale = 1;

    public RectangleFrameLayout(Context context) {
        this(context, null);
    }

    public RectangleFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RectangleFrameLayout);
        rectangleWidth = array.getDimensionPixelSize(R.styleable.RectangleFrameLayout_rectangle_width, rectangleWidth);
        rectangleHeight = array.getDimensionPixelSize(R.styleable.RectangleFrameLayout_rectangle_height, rectangleHeight);
        array.recycle();

        if (rectangleWidth > 0 && rectangleHeight > 0)
            rectangleScale = ((float) rectangleHeight / (float) rectangleWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSize == 0 && heightSize == 0) {
            // If there are no constraints on size, let FrameLayout measure
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // Now use the smallest of the measured dimensions for both dimensions
            final int minSize = Math.min(getMeasuredWidth(), getMeasuredHeight());

            setMeasuredDimension(minSize, (int) (minSize * rectangleScale));
            return;
        }

        if (widthMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.EXACTLY) {
            if (widthMode == MeasureSpec.EXACTLY) {
                heightSize = (int) (widthSize * rectangleScale);
            } else {
                widthSize = Math.min(widthSize, heightSize);
                heightSize = (int) (widthSize * rectangleScale);
            }
        } else {
            widthSize = Math.max(widthSize, heightSize);
            heightSize = (int) (widthSize * rectangleScale);
        }

        final int newWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        final int newHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(newWidthSpec, newHeightSpec);
    }

    private OnSizeChangedListener _OnSizeChangedListener;

    @Override
    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        _OnSizeChangedListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (_OnSizeChangedListener != null) {
            _OnSizeChangedListener.onSizeChanged(this, w, h, oldw, oldh);
        }
    }
}


