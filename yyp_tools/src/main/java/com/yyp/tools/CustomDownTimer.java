package com.yyp.tools;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by YanYan on 2019/9/26.
 * 倒计时-工具
 * start()、 cancel()、 pause()、resume()
 */
public class CustomDownTimer {
    /**
     * 倒计时监听器
     */
    public interface OnDownTimerListener {
        void onTimerStart();//当倒计时开始

        void onTimerComplete();//当倒计时结束

        /**
         * @param millisUntilFinished 剩余时间
         */
        void onTick(long millisUntilFinished);
    }

    private OnDownTimerListener mCountDownListener;

    public void setOnDownTimerListener(OnDownTimerListener countDownListener) {
        this.mCountDownListener = countDownListener;
    }

    private static final int MSG = 1;

    private long mMillisInFuture;//倒计时
    private long mCountdownInterval = 1000;//跳转时间间隔

    //暂停时，当时剩余时间
    private long mCurrentMillisLeft;
    private long mStopTimeInFuture;
    private boolean mCancelled = false;//是否取消
    private boolean mPause = false;//是否暂停

    public CustomDownTimer() {
    }

    public CustomDownTimer(long millisInFuture, long countdownInterval) {
        this.mMillisInFuture = millisInFuture;
        this.mCountdownInterval = countdownInterval;
    }

    public CustomDownTimer(long millisInFuture, long countdownInterval, OnDownTimerListener countDownListener) {
        this.mMillisInFuture = millisInFuture;
        this.mCountdownInterval = countdownInterval;
        this.mCountDownListener = countDownListener;
    }

    public void setMillisInFuture(long millisInFuture) {
        this.mMillisInFuture = millisInFuture;
    }

    public void setCountdownInterval(long countdownInterval) {
        this.mCountdownInterval = countdownInterval;
    }

    /**
     * 开始
     */
    public synchronized final void start() {
        if (mMillisInFuture <= 0 && mCountdownInterval <= 0) {
            throw new RuntimeException("you must set the millisInFuture > 0 or countdownInterval >0");
        } else {
            mCancelled = false;
            mPause = false;
            mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
            mHandler.sendMessage(mHandler.obtainMessage(MSG));
        }
    }

    /**
     * 重新开始
     */
    public void resume() {
        if (mMillisInFuture <= 0 && mCountdownInterval <= 0) {
            throw new RuntimeException("you must set the millisInFuture > 0 or countdownInterval >0");
        } else {
            if (mCancelled)
                return;

            //剩余时长少于
            if (mCurrentMillisLeft < mCountdownInterval || !mPause) {
                return;
            }
            mStopTimeInFuture = SystemClock.elapsedRealtime() + mCurrentMillisLeft;
            mHandler.sendMessage(mHandler.obtainMessage(MSG));
            mPause = false;
        }
    }

    public void restart() {
        if (mMillisInFuture <= 0 && mCountdownInterval <= 0) {
            throw new RuntimeException("you must set the millisInFuture > 0 or countdownInterval >0");
        } else {
            mCancelled = false;
            mPause = false;
            mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
            mHandler.sendMessage(mHandler.obtainMessage(MSG));
        }
    }

    /**
     * 取消
     */
    public synchronized final void cancel() {
        if (mHandler != null) {
            mCancelled = true;
            mHandler.removeMessages(MSG);
        }
    }

    /**
     * 暂停
     */
    public synchronized final void pause() {
        if (mHandler != null) {
            if (mCancelled)
                return;

            if (mCurrentMillisLeft < mCountdownInterval)
                return;

            mPause = true;
            mHandler.removeMessages(MSG);
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            synchronized (CustomDownTimer.this) {
                if (mCancelled)
                    return;

                //剩余毫秒数
                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    mCurrentMillisLeft = 0;
                    if (mCountDownListener != null) {
                        mCountDownListener.onTimerComplete();
                    }
                } else if (millisLeft < mCountdownInterval) {
                    mCurrentMillisLeft = 0;
                    // 剩余时间小于一次时间间隔的时候，不再通知，只是延迟一下
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {//有多余的时间
                    long lastTickStart = SystemClock.elapsedRealtime();
                    if (mCountDownListener != null)
                        mCountDownListener.onTick(millisLeft);

                    mCurrentMillisLeft = millisLeft;
                    // 考虑用户的onTick需要花费时间,处理用户onTick执行的时间
                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();

                    // 特殊情况：用户的onTick方法花费的时间比interval长，那么直接跳转到下一次interval
                    while (delay < 0) delay += mCountdownInterval;

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}
