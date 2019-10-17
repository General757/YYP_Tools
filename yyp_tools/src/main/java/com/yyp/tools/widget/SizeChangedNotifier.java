/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */
package com.yyp.tools.widget;

import android.view.View;

public interface SizeChangedNotifier {

    interface OnSizeChangedListener {
        void onSizeChanged(View view, int w, int h, int oldw, int oldh);
    }

    void setOnSizeChangedListener(OnSizeChangedListener listener);

}

