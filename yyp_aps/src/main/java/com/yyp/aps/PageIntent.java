package com.yyp.aps;

import android.os.Bundle;

/**
 * Created by generalYan on 2019/10/28.
 */

public class PageIntent {

    Bundle extrasData;

    PageInterface callObject;

    Class<? extends SingleActivity> openObject;

    public PageIntent(PageInterface callObject, Class<? extends SingleActivity> openObject) {
        super();

        this.callObject = callObject;
        this.openObject = openObject;
    }

    public void setExtras(Bundle extras) {
        this.extrasData = extras;
    }

}
