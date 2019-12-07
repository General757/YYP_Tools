package com.yyp.aps;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.LinkedList;

/**
 * Created by generalYan on 2019/10/28.
 */
interface PageInterface {
    public void onPagementResult(int requestCode, int resultCode, Bundle data);
}

public class SingleActivity implements PageInterface {

    static final int ANIM_NO_MOVE = 0x00;
    /* 从右往左 */
    public static final int ANIM_TO_LEFT = 0x01;
    /* 从左往右 */
    public static final int ANIM_TO_RIGHT = 0x02;
    /* 从下往上 */
    public static final int ANIM_TO_TOP = 0x03;
    /* 从上往下 */
    public static final int ANIM_TO_BOTTOM = 0x04;

    /* 页面创建状态 */
    static final int STATUS_BUILD = 0x01;

    /* 获得焦点状态 */
    static final int STATUS_FOCUS = 0X02;

    /* 失去焦点状态 */
    static final int STATUS_PAUSE = 0x03;

    /* 页面销毁状态 */
    static final int STATUS_CLOSE = 0x04;

    PageInterface callObject = null;

    Bundle extrasData = null;

    int requestCode = -1;

    int pageIndex;

    int pageState;

    AgentActivity pageAgent;

    DecorView decorView;

    LinkedList<PageDialog> dialogset;

    PageDialog keydialog;

    void doSetup(int pageIndex, AgentActivity agent) {
        this.pageIndex = pageIndex;
        this.pageAgent = agent;
        this.pageState = 0;

        dialogset = new LinkedList<PageDialog>();
        decorView = new DecorView(getApplicationContext());

        decorView.setOnCloseListener(new DecorView.OnCloseListener() {
            @Override
            public void onPrepareClose() {
                close();
            }
        });

    }

    void doSlide(boolean show, int anim, int time) {
        decorView.setState(show ? DecorView.STATUS_FOCUS : DecorView.STATUS_PAUSE);
        decorView.doSlide(anim, time);
    }

    void doBuild() {
        if (pageState == 0) {
            pageState = STATUS_BUILD;
            onBuild();
        }
    }

    void doFocus() {
        if (pageState == STATUS_BUILD || pageState == STATUS_PAUSE) {
            // decorView.setFocusable(true);
            decorView.requestFocus();
            pageState = STATUS_FOCUS;
            onFocus();
        }
    }

    void doPause() {
        if (pageState == STATUS_FOCUS) {
            pageState = STATUS_PAUSE;
            onPause();
        }
    }

    void doClose() {
        if (pageState == STATUS_BUILD || pageState == STATUS_PAUSE) {
            pageState = STATUS_CLOSE;
            onClose();

            int count = dialogset.size();

            for (int index = count - 1; index >= 0; index--) {
                PageDialog dialog = dialogset.get(index);
                hideDialog(dialog);
            }
        }
    }

    void doResume(AgentActivity agent) {
        this.pageAgent = agent;
    }

    void showDialog(PageDialog dialog) {
        if (pageState != STATUS_CLOSE) {
            dialogset.add(dialog);
            pageAgent.showDialog(dialog);
        }
    }

    void hideDialog(PageDialog dialog) {
        if (dialogset.contains(dialog)) {
            dialogset.remove(dialog);
            pageAgent.hideDialog(dialog);
        }
    }

    PageDialog getTopDialog() {
        int count = dialogset.size();
        return count > 0 ? dialogset.get(count - 1) : null;
    }

    public void onBuild() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.systembar_tint_resource);// 通知栏所需颜色
        // tintManager.setTintColor(getResources().getColor(R.color.headbar_bg));//
        // 通知栏所需颜色
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getActivity().getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void onFocus() {

    }

    public void onPause() {

    }

    public void onClose() {

    }

    public void onMessage(int message) {

    }

    public void closeAll() {
        hideInputMethod();
        pageAgent.destoryAll();
    }

    public void close() {
        hideInputMethod();
        pageAgent.destroyInstance(this);
    }

    public void close(int animType) {
        hideInputMethod();
        pageAgent.destroyInstance(this, animType);
    }

    public void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        Application app = pageAgent.getApplication();
        app.registerReceiver(receiver, filter);
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        Application app = pageAgent.getApplication();
        app.unregisterReceiver(receiver);
    }

    public void setOrientation(int orientation) {
        pageAgent.setRequestedOrientation(orientation);
    }

    public void setContentView(int layout) {
        LayoutInflater inflater = (LayoutInflater) pageAgent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layout, null);
        setContentView(contentView);

    }

    public void setContentView(View contentView) {
        decorView.removeAllViews();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            params.topMargin = getStatusHeight(getActivity()) / 2;

        decorView.addView(contentView, params);
    }

    public void doToast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void doToast(int res) {
        Toast toast = Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) pageAgent.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(decorView.getWindowToken(), 0);
    }

    public void hideInputOnEnter(EditText edit) {
        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        hideInputMethod();
                    }

                    return true;
                }

                return false;
            }
        });
    }

    public View findViewById(int id) {
        return decorView.findViewById(id);
    }

    public Intent createActivityIntent(Class<?> mClass) {
        return new Intent(pageAgent, mClass);
    }

    public Bundle getExtras() {
        return extrasData;
    }

    public Resources getResources() {
        return pageAgent.getResources();
    }

    public Activity getActivity() {
        return pageAgent;
    }

    public Application getApplication() {
        return pageAgent.getApplication();
    }

    public Context getApplicationContext() {
        return pageAgent.getApplicationContext();
    }

    public LayoutInflater getLayoutInflater() {
        return (LayoutInflater) pageAgent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setResult(int resultCode, Bundle data) {
        if (callObject != null && requestCode > -1) {
            callObject.onPagementResult(requestCode, resultCode, data);
        }
    }

    public void startPagement(PageIntent intent) {
        hideInputMethod();
        pageAgent.startPagement(intent);
    }

    public void startPagement(PageIntent intent, int animType) {
        hideInputMethod();
        pageAgent.startPagement(intent, animType);
    }

    public void startPagementForResult(PageIntent intent, int requestCode) {
        if (requestCode < 0) {
            throw new IllegalArgumentException("requestCode must over zero");
        }

        hideInputMethod();
        pageAgent.startPagementForResult(intent, requestCode);
    }

    public void startPagementForResult(PageIntent intent, int requestCode, int animType) {
        if (requestCode < 0) {
            throw new IllegalArgumentException("requestCode must over zero");
        }

        hideInputMethod();
        pageAgent.startPagementForResult(intent, requestCode, animType);
    }

    @Override
    public void onPagementResult(int requestCode, int resultCode, Bundle data) {

    }

    public void startActivity(Intent intent) {
        pageAgent.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        if ((requestCode & 0xffff0000) != 0) {
            throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
        }

        int code = (pageIndex << 16) + (requestCode & 0xffff);
        pageAgent.startActivityForResult(intent, code);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            close();
        }

        return true;
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}

