package com.yyp.aps;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Selection;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * Created by generalYan on 2019/10/28.
 */
public class PageDialog implements OnTouchListener {

    public interface OnClickListener {
        void onClick(PageDialog dialog);
    }

    boolean closeTouchOutSide;

    long tokenTime;

    PageActivity pageToken;

    FrameLayout container;

    Handler mHandler;

    public PageDialog(PageActivity pageToken) {
        this.pageToken = pageToken;
        closeTouchOutSide = false;
        tokenTime = System.currentTimeMillis();

        container = new FrameLayout(getContext());
        container.setBackgroundColor(Color.parseColor("#60000000"));
        container.setOnTouchListener(this);
    }

    public void setCloseTouchOutSide(boolean closeTouchOutSide) {
        this.closeTouchOutSide = closeTouchOutSide;
    }

    public Context getContext() {
        return pageToken.getApplicationContext();
    }

    public Resources getResources() {
        return pageToken.getResources();
    }

    public LayoutInflater getLayoutInflater() {
        return pageToken.getLayoutInflater();
    }

    public FrameLayout.LayoutParams getDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public int getDefaultLayoutGravity() {
        return Gravity.CENTER;
    }

    public void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(container.getWindowToken(), 0);
    }

    public void openInputMethod(final EditText edit) {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (!imm.isActive()) {
                    imm.toggleSoftInput(InputMethod.SHOW_EXPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                }

                Editable etext = edit.getText();
                Selection.setSelection(etext, etext.length());

                edit.setFocusable(true);
                edit.requestFocus();

                imm.showSoftInput(edit, InputMethodManager.SHOW_FORCED);
            }
        }, 500);
    }

    public void setContentView(int layout) {
        LayoutInflater inflater = pageToken.getLayoutInflater();
        View contentView = inflater.inflate(layout, null);
        container.removeAllViews();

        if (contentView != null) {
            FrameLayout.LayoutParams params = getDefaultLayoutParams();
            params.gravity = getDefaultLayoutGravity();
            container.addView(contentView, params);
        }
    }

    public View findViewById(int id) {
        return container.findViewById(id);
    }

    public void show() {
        if (container.getParent() == null) {
            pageToken.showDialog(this);
        }
    }

    public void hide() {
        if (container.getParent() != null) {
            pageToken.hideDialog(this);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hide();
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_UP && closeTouchOutSide) {
            hide();
        }
        return true;
    }

}

