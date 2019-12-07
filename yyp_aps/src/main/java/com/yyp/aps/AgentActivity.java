package com.yyp.aps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.util.Vector;

/**
 * Created by generalYan on 2019/10/28.
 */
public abstract class AgentActivity extends Activity implements PageInterface {
    class Finallize implements Runnable {
        SingleActivity instance;

        public Finallize(SingleActivity instance) {
            super();
            this.instance = instance;
        }

        @Override
        public void run() {
            container.removeView(instance.decorView);
            instance.decorView.removeAllViews();
        }
    }

    class FinishTask implements Runnable {
        @Override
        public void run() {
            for (SingleActivity page : pagestack) {
                destroyInstance(page);
            }
        }
    }

    final int REQUEST_GESTURE = 0x01;

    Handler uiHandler = new Handler();

    int pageIndex = 0;

    int animation = 360;

    boolean isResumed = false;

    boolean onBackcode = false;

    SingleActivity eventPage = null;

    PageDialog keyDialog = null;

    FrameLayout container = null;

    Vector<SingleActivity> pagestack = null;

    boolean startGesture = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseApplication app = (BaseApplication) getApplication();
        if (app.canResume()) {
            app.onResumed(this);
        } else {
            pagestack = new Vector<SingleActivity>();
            container = new FrameLayout(getApplicationContext());
            setContentView(container);

            startMain();

            startLaunchAnim();
        }
        app.currAgent = this;

        showImView(getIntent().getExtras());
    }

    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;

        SingleActivity topInstance = getTopInstance();
        if (topInstance != null)
            topInstance.doFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        isResumed = false;

        SingleActivity topInstance = getTopInstance();
        if (topInstance != null)
            topInstance.doPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseApplication app = (BaseApplication) getApplication();
        app.currAgent = null;
        if (!app.canResume())
            uiHandler.postDelayed(new FinishTask(), 2000);
    }


    protected abstract void startMain();//开始启动页面

    protected abstract void startLaunchAnim();//开始启动动画

    protected abstract void showImView(Bundle bundle);//开始启动view

    @Override
    public void onPagementResult(int requestCode, int resultCode, Bundle data) {
    }

    @Override
    public void onNewIntent(Intent intent) {
        showImView(intent.getExtras());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BaseApplication app = (BaseApplication) getApplication();
        app.onRestore(this);
    }

    void sendMessage(int message) {
        for (SingleActivity page : pagestack) {
            page.onMessage(message);
        }
    }

    void showDialog(PageDialog dialog) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        container.addView(dialog.container, params);
    }

    void hideDialog(PageDialog dialog) {
        container.removeView(dialog.container);
    }

    void destoryAll() {
        int size = pagestack.size();
        for (int i = size - 1; i > 0; i--) {
            SingleActivity pagement = pagestack.get(i);
            destroyInstance(pagement);
        }
    }

    void destroyInstance(SingleActivity instance) {
        destroyInstance(instance, SingleActivity.ANIM_TO_RIGHT);
    }

    void destroyInstance(SingleActivity instance, int animType) {
        if (!pagestack.contains(instance)) {
            return;
        }

        SingleActivity toppage = getTopInstance();

        if (toppage == instance) {
            SingleActivity prepage = getPreInstance();

            if (prepage != null) {
                prepage.doSlide(true, SingleActivity.ANIM_NO_MOVE, animation);
                toppage.doSlide(false, animType, animation);

                toppage.doPause();
                toppage.doClose();

                if (isResumed) {
                    prepage.doFocus();
                }
            } else {
                toppage.doPause();
                toppage.doClose();
                finish();
            }

            pagestack.remove(instance);
            uiHandler.postDelayed(new Finallize(instance), animation);
        } else {
            instance.doPause();
            instance.doClose();
            pagestack.remove(instance);
            uiHandler.post(new Finallize(instance));
        }

    }

    SingleActivity getPreInstance() {
        int size = pagestack.size();
        return size > 1 ? pagestack.get(size - 2) : null;
    }

    SingleActivity getTopInstance() {
        int size = pagestack.size();
        return size > 0 ? pagestack.get(size - 1) : null;
    }

    public void startPagement(PageIntent intent) {
        startPagementForResult(intent, -1, SingleActivity.ANIM_TO_LEFT);
    }

    public void startPagement(PageIntent intent, int animType) {
        startPagementForResult(intent, -1, animType);
    }

    public void startPagementForResult(PageIntent intent, int requestCode) {
        startPagementForResult(intent, requestCode, SingleActivity.ANIM_TO_LEFT);
    }

    public void startPagementForResult(PageIntent intent, int requestCode, int animType) {
        try {
            pageIndex++;
            SingleActivity oldInstance = getTopInstance();

            Constructor<? extends SingleActivity> constructor = intent.openObject.getConstructor();
            SingleActivity newInstance = constructor.newInstance();

            newInstance.extrasData = intent.extrasData;
            newInstance.callObject = intent.callObject;
            newInstance.requestCode = requestCode;
            newInstance.doSetup(pageIndex, this);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            int count = container.getChildCount();
            container.addView(newInstance.decorView, count, params);
            pagestack.add(newInstance);

            newInstance.doBuild();

            if (oldInstance == null) {
                newInstance.doSlide(true, animType, 0);
            } else {
                newInstance.doSlide(true, animType, animation);
                oldInstance.doSlide(false, SingleActivity.ANIM_NO_MOVE, animation);
            }

            if (isResumed) {
                if (oldInstance != null) {
                    oldInstance.doPause();
                }

                newInstance.doFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int index = requestCode >> 16;

        if (index > 0) {
            SingleActivity receive = null;

            for (SingleActivity page : pagestack) {

                if (page instanceof GroupActivity) {
                    GroupActivity group = (GroupActivity) page;
                    receive = group.findPageByIndex(index);
                } else {
                    if (page.pageIndex == index) {
                        receive = page;
                    }
                }

                if (receive != null) {
                    break;
                }
            }

            if (receive != null) {
                requestCode = requestCode & 0xffff;
                receive.onActivityResult(requestCode, resultCode, data);
            }

            return;
        }

        if (requestCode == REQUEST_GESTURE) {
            startGesture = false;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackcode = true;

            eventPage = getTopInstance();

            if (eventPage != null) {
                keyDialog = eventPage.getTopDialog();
            }

            if (keyDialog != null) {
                keyDialog.onKeyDown(keyCode, event);
                onBackcode = false;
            } else if (eventPage != null) {
                onBackcode = !eventPage.onKeyDown(keyCode, event);
            }

            if (onBackcode) {
                eventPage = null;
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    long oldTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyDialog != null) {
                keyDialog.onKeyUp(keyCode, event);
            } else if (eventPage != null) {
                eventPage.onKeyUp(keyCode, event);
            } else if (onBackcode) {
                if (System.currentTimeMillis() - oldTime < 2 * 1000) {
                    finish();
                    System.exit(0);
                } else {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                }

                oldTime = System.currentTimeMillis();
            }

            onBackcode = false;
            keyDialog = null;
            eventPage = null;

            return true;
        }

        return super.onKeyUp(keyCode, event);
    }
}

