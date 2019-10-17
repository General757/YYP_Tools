//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.bitmap.text;

import android.text.StaticLayout;

import com.yyp.tools.bitmap.AbstractOutlineTextDrawable;

public class TextBoxDrawable extends AbstractOutlineTextDrawable {
    public TextBoxDrawable() {
    }

    protected StaticLayout layout() {
        int var1 = this.mTextHeight;
        float var2 = 0.0F;
        float var3 = (float)var1;
        float var4 = (var2 + var3) / 2.0F;
        StaticLayout var5;
        if (this.mFixTextSize > 0) {
            var5 = this.makeLayout((float)this.mFixTextSize);
        } else {
            while(true) {
                var5 = this.makeLayout(var4);
                if (var4 == var2) {
                    break;
                }

                if (var4 == var3) {
                    var4 = var2;
                } else {
                    int var6 = var5.getHeight();
                    if (var6 < var1) {
                        var2 = var4;
                        var4 = (var4 + var3) / 2.0F;
                    } else {
                        if (var6 <= var1) {
                            break;
                        }

                        var3 = var4;
                        var4 = (var2 + var4) / 2.0F;
                    }
                }
            }
        }

        return var5;
    }
}

