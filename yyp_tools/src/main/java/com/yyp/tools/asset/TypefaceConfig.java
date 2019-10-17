//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.asset;

import android.graphics.Typeface;

public class TypefaceConfig {
    public final Typeface typeface;
    public final boolean fakeBold;

    public TypefaceConfig(Typeface _typeface, boolean _fake_bold) {
        this.typeface = _typeface;
        this.fakeBold = _fake_bold;
    }

    public TypefaceConfig(Typeface _typeface) {
        this(_typeface, false);
    }

    public TypefaceConfig() {
        this((Typeface) null, false);
    }
}

