package com.yyp.tools.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yyp.tools.R;

/**
 * Created by generalYan on 2019/8/19.
 * 关注-自定义view
 */
public class FollowAnimView extends View {

    public final static int STATE_NORMAL = 0;//默认(未关注)
    public final static int STATE_UNCONCERNED = 1;//未关注
    public final static int STATE_CONCERNED = 2;//已关注

    private final static int DEFAULT_DURATION = 250;
    private Paint mBgPaint, mLinePaint;

    private int mBgColor = Color.YELLOW;//背景颜色
    private int mBgStartColor;//背景渐变起始颜色-Color.parseColor("#ffef00")
    private int mBgEndColor;//背景渐变结束颜色-Color.parseColor("#ffc400")
    private int mAnimDuration = DEFAULT_DURATION; //动画时长
    private int mLineWidth = 16;//字体线宽
    private int mState = STATE_NORMAL;//当前状态
    private boolean isAnim = false;//当前动画状态

    //默认展示宽高
    private int mScreenWidth = 60;
    private int mScreenHeight = 30;

    //属性值
    private int mAnimValue = 0;

    public FollowAnimView(Context context) {
        this(context, null);
    }

    public FollowAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FollowAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.FollowAnimView);
        mBgColor = typeArray.getColor(R.styleable.FollowAnimView_fa_bg_color, mBgColor);//背景色
        mBgStartColor = typeArray.getColor(R.styleable.FollowAnimView_fa_bg_start_color, mBgStartColor);//背景色
        mBgEndColor = typeArray.getColor(R.styleable.FollowAnimView_fa_bg_end_color, mBgEndColor);//背景色
        mLineWidth = (int) typeArray.getDimension(R.styleable.FollowAnimView_fa_line_width, mLineWidth);
        mAnimDuration = typeArray.getInt(R.styleable.FollowAnimView_fa_duration, mAnimDuration);
        typeArray.recycle();

        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int measuredWidth, measuredHeight;
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize;
            measuredHeight = heightSize;
        } else if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize;
            measuredHeight = widthSize / 2;
        } else if (heightMode == MeasureSpec.EXACTLY) {
            measuredWidth = heightSize * 2;
            measuredHeight = heightSize;
        } else {
            measuredWidth = mScreenWidth;
            measuredHeight = mScreenHeight;
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int screenWidth = getMeasuredWidth();
        final int screenHeight = getMeasuredHeight();

        //加号
        float line1_startX = screenWidth * 5 / 16;//screenWidth / 2 - screenHeight * 3 / 4 / 2;
        float line1_startY = screenHeight / 2;
        float line1_stopX = screenWidth * 11 / 16;//screenWidth / 2 + screenHeight * 3 / 4 / 2;
        float line1_stopY = screenHeight / 2;
        PointRect plus1Rect = new PointRect(line1_startX, line1_startY, line1_stopX, line1_stopY);

        float line2_startX = screenWidth / 2;
        float line2_startY = screenHeight / 8;//(screenHeight - screenHeight * 3 / 4) / 2;
        float line2_stopX = screenWidth / 2;
        float line2_stopY = screenHeight * 7 / 8;//screenHeight - (screenHeight - screenHeight * 3 / 4) / 2;
        PointRect plus2Rect = new PointRect(line2_startX, line2_startY, line2_stopX, line2_stopY);

        //对号
        float line3_startX = screenWidth / 8;//(screenWidth / 2) / 4;
        float line3_startY = screenHeight / 2;
        float line3_stopX = screenWidth / 4;//(screenWidth / 2) / 2;
        float line3_stopY = screenHeight * 3 / 4;
        PointRect yes1Rect = new PointRect(line3_startX, line3_startY, line3_stopX, line3_stopY);

        float line4_startX = screenWidth * 3 / 8;//(screenWidth / 2) * 3 / 4;
        float line4_startY = screenHeight / 4;
        float line4_stopX = screenWidth / 4;//(screenWidth / 2) / 2;
        float line4_stopY = screenHeight * 3 / 4;// screenHeight - (screenHeight - screenHeight * 3 / 4) / 2;
        PointRect yes2Rect = new PointRect(line4_startX, line4_startY, line4_stopX, line4_stopY);

        if (mState == STATE_NORMAL) {
            drawToNormal(canvas, screenWidth, screenHeight, plus1Rect, plus2Rect, yes1Rect, yes2Rect);
        } else if (mState == STATE_UNCONCERNED) {
            drawToUnconcerned(canvas, screenWidth, screenHeight, plus1Rect, plus2Rect, yes1Rect, yes2Rect);
        } else {
            drawToConcerned(canvas, screenWidth, screenHeight, plus1Rect, plus2Rect, yes1Rect, yes2Rect);
        }
    }

    /**
     * 绘制正常(未关注)
     *
     * @param canvas 画板
     */
    private void drawToNormal(Canvas canvas, int screenWidth, int screenHeight, PointRect plus1Rect, PointRect plus2Rect, PointRect yes1Rect, PointRect yes2Rect) {
        if (mBgStartColor != 0 && mBgEndColor != 0)
            mBgPaint.setShader(new LinearGradient(0, 0, screenWidth, screenHeight, mBgStartColor, mBgEndColor, Shader.TileMode.CLAMP));
        canvas.drawRoundRect(new RectF(0, 0, screenWidth, screenHeight), screenHeight / 2, screenHeight / 2, mBgPaint);

        canvas.drawLine(plus1Rect.startX, plus1Rect.startY, plus1Rect.stopX, plus1Rect.stopY, mLinePaint);
        canvas.drawLine(plus2Rect.startX, plus2Rect.startY, plus2Rect.stopX, plus2Rect.stopY, mLinePaint);
    }

    /**
     * 绘制未关注
     *
     * @param canvas 画板
     */
    private void drawToUnconcerned(Canvas canvas, int screenWidth, int screenHeight, PointRect plus1Rect, PointRect plus2Rect, PointRect yes1Rect, PointRect yes2Rect) {
        int width = screenWidth - (screenHeight - mAnimValue);
        if (mBgStartColor != 0 && mBgEndColor != 0)
            mBgPaint.setShader(new LinearGradient(0, 0, width, screenHeight, mBgStartColor, mBgEndColor, Shader.TileMode.CLAMP));
        canvas.drawRoundRect(new RectF(0, 0, width, screenHeight), screenHeight / 2, screenHeight / 2, mBgPaint);

        float animScale = (float) mAnimValue / screenHeight;//0->screenHeight

        float A_startX = yes1Rect.startX + (plus1Rect.startX - yes1Rect.startX) * animScale;
        float A_startY = plus1Rect.startY;
        float A_stopX = yes1Rect.stopX + (plus1Rect.stopX - yes1Rect.stopX) * animScale;
        float A_stopY = yes1Rect.stopY - (yes1Rect.stopY - plus1Rect.stopY) * animScale;

        float B_startX = yes2Rect.startX + (plus2Rect.startX - yes2Rect.startX) * animScale;
        float B_startY = yes2Rect.startY - (yes2Rect.startY - plus2Rect.startY) * animScale;
        float B_stopX = yes2Rect.stopX + (plus2Rect.stopX - yes2Rect.stopX) * animScale;
        float B_stopY = yes2Rect.stopY + (plus2Rect.stopY - yes2Rect.stopY) * animScale;

        canvas.drawLine(A_startX, A_startY, A_stopX, A_stopY, mLinePaint);
        canvas.drawLine(B_startX, B_startY, B_stopX, B_stopY, mLinePaint);
    }

    /**
     * 绘制已关注
     *
     * @param canvas 画板
     */
    private void drawToConcerned(Canvas canvas, int screenWidth, int screenHeight, PointRect plus1Rect, PointRect plus2Rect, PointRect yes1Rect, PointRect yes2Rect) {
        int width = screenWidth - (screenHeight - mAnimValue);
        if (mBgStartColor != 0 && mBgEndColor != 0)
            mBgPaint.setShader(new LinearGradient(0, 0, width, screenHeight, mBgStartColor, mBgEndColor, Shader.TileMode.CLAMP));
        canvas.drawRoundRect(new RectF(0, 0, width, screenHeight), screenHeight / 2, screenHeight / 2, mBgPaint);

        float animScale = (1 - (float) mAnimValue / screenHeight);//screenHeight->0

        float line1_startX = plus1Rect.startX - (plus1Rect.startX - yes1Rect.startX) * animScale;
        float line1_startY = plus1Rect.startY;
        float line1_stopX = plus1Rect.stopX - (plus1Rect.stopX - yes1Rect.stopX) * animScale;
        float line1_stopY = plus1Rect.stopY + (yes1Rect.stopY - plus1Rect.stopY) * animScale;

        float line2_startX = plus2Rect.startX - (plus2Rect.startX - yes2Rect.startX) * animScale;
        float line2_startY = plus2Rect.startY + (yes2Rect.startY - plus2Rect.startY) * animScale;
        float line2_stopX = plus2Rect.stopX - (plus2Rect.stopX - yes2Rect.stopX) * animScale;
        float line2_stopY = plus2Rect.stopY - (plus2Rect.stopY - yes2Rect.stopY) * animScale;

        canvas.drawLine(line1_startX, line1_startY, line1_stopX, line1_stopY, mLinePaint);
        canvas.drawLine(line2_startX, line2_startY, line2_stopX, line2_stopY, mLinePaint);
    }

    class PointRect {
        float startX;
        float startY;
        float stopX;
        float stopY;

        public PointRect(float startX, float startY, float stopX, float stopY) {
            super();
            this.startX = startX;
            this.startY = startY;
            this.stopX = stopX;
            this.stopY = stopY;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAnim)
            return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mState == STATE_NORMAL) {
                    startFollowAnim(true);
                } else if (mState == STATE_UNCONCERNED) {
                    startFollowAnim(true);
                } else if (mState == STATE_CONCERNED) {
                    int screenWidth = getMeasuredWidth();
                    int screenHeight = getMeasuredHeight();
                    if (isPointInCircle(new PointF(event.getX(), event.getY()), new PointF(screenHeight / 2, screenHeight / 2), screenHeight / 2)) {
                        startFollowAnim(false);
                    }
                }
                return true;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setFollowDefault(int state) {
        if (state == STATE_NORMAL || state == STATE_CONCERNED)
            mAnimValue = 0;
        else
            mAnimValue = getMeasuredHeight();
        mState = state;
        invalidate();
    }

    private void startFollowAnim(final boolean isFollow) {
        ValueAnimator valueAnimator;
        final int screenWidth = getMeasuredWidth();
        final int screenHeight = getMeasuredHeight();

        if (isFollow) {
            valueAnimator = ValueAnimator.ofInt(screenHeight, 0);
        } else
            valueAnimator = ValueAnimator.ofInt(0, screenHeight);

        valueAnimator.setDuration(mAnimDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimValue = (int) valueAnimator.getAnimatedValue();
                if (isFollow) {
                    mState = STATE_CONCERNED;
                    isAnim = mAnimValue != 0;
                    invalidate();
                    if (mAnimValue == 0)
                        postDelayed(mRunnable, 100);
                } else {
                    mState = STATE_UNCONCERNED;
                    isAnim = mAnimValue != screenHeight;
                    invalidate();
                }
            }
        });
        valueAnimator.start();
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mListener != null)
                mListener.onFollow(true);
        }
    };

    /**
     * 判断点是否在圆内
     *
     * @param pointF 待确定点
     * @param circle 圆心
     * @param radius 半径
     * @return true在圆内
     */
    private boolean isPointInCircle(PointF pointF, PointF circle, float radius) {
        return Math.pow((pointF.x - circle.x), 2) + Math.pow((pointF.y - circle.y), 2) <= Math.pow(radius, 2);
    }

    //获取Text高度
    private int getTextHeight(String str, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return (int) (rect.height() / 33f * 29);
    }

    //获取Text宽度
    private int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public interface OnFollowListener {
        void onFollow(boolean isFollow);
    }

    private OnFollowListener mListener;

    public void setOnFollowListener(OnFollowListener listener) {
        this.mListener = listener;
    }
}
