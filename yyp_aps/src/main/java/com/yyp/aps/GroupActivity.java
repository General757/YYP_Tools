package com.yyp.aps;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import java.lang.reflect.Constructor;

/**
 * Created by generalYan on 2019/10/28.
 */
public class GroupActivity extends SingleActivity {

    boolean pageFocus = false;

    SingleActivity childPage = null;

    FrameLayout container = null;

    @Override
    void doBuild() {
        super.doBuild();

        if (childPage != null) {
            childPage.doBuild();
        }
    }

    @Override
    void doFocus() {
        super.doFocus();
        pageFocus = true;

        if (childPage != null) {
            childPage.doFocus();
        }
    }

    @Override
    void doPause() {
        super.doPause();

        if (childPage != null) {
            childPage.doPause();
        }

        pageFocus = false;
    }

    @Override
    void doClose() {
        super.doClose();
        closeCurrent();
    }

    @Override
    public void onMessage(int message) {
        if (childPage != null) {
            childPage.onMessage(message);
        }
    }

    @Override
    void doResume(AgentActivity agent) {
        super.doResume(agent);

        if (childPage != null) {
            childPage.doResume(agent);
        }
    }

    int findMaxIndex() {
        if (childPage == null) {
            return pageIndex;
        }

        int result = pageIndex;

        if (childPage instanceof GroupActivity) {
            GroupActivity group = (GroupActivity) childPage;
            result = Math.max(result, group.findMaxIndex());
        } else {
            result = Math.max(result, childPage.pageIndex);
        }

        return result;
    }

    SingleActivity findPageByIndex(int index) {
        SingleActivity result = null;

        if (pageIndex == index) {
            result = this;
        } else if (childPage != null) {
            if (childPage instanceof GroupActivity) {
                GroupActivity group = (GroupActivity) childPage;
                result = group.findPageByIndex(index);
            } else {
                result = childPage.pageIndex == index ? childPage : null;
            }
        }

        return result;
    }

    @Override
    PageDialog getTopDialog() {
        PageDialog groupDialog = super.getTopDialog();

        PageDialog result = null;

        if (childPage != null) {
            PageDialog childDialog = childPage.getTopDialog();

            if (groupDialog != null && childDialog != null) {
                result = groupDialog.tokenTime > childDialog.tokenTime ? groupDialog : childDialog;
            } else if (groupDialog != null) {
                result = groupDialog;
            } else {
                result = childDialog;
            }
        } else {
            result = groupDialog;
        }

        return result;
    }

    void closeCurrent() {
        if (childPage != null) {
            container.removeView(childPage.decorView);
            childPage.decorView.removeAllViews();
            childPage.doPause();
            childPage.doClose();
            childPage = null;
        }
    }

    public void setContainer(FrameLayout container) {
        this.container = container;
    }

    public void displayChild(Class<? extends SingleActivity> mClass, Bundle data) {
        if (container == null) {
            throw new RuntimeException("You Must Call setContainer For " + getClass().getName() + " At First");
        }

        if (childPage != null && childPage.getClass().equals(mClass)) {
            return;
        }

        try {
            closeCurrent();
            pageAgent.pageIndex++;

            Constructor<? extends SingleActivity> constructor = mClass.getConstructor();
            childPage = constructor.newInstance();
            childPage.extrasData = data;
            childPage.doSetup(pageAgent.pageIndex, pageAgent);
            childPage.doSlide(true, 0, 0);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            container.addView(childPage.decorView, params);
            childPage.doBuild();

            if (pageFocus) {
                childPage.doFocus();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}

