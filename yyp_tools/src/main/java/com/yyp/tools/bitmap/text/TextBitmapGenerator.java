//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.bitmap.text;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Join;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.yyp.tools.Visible;
import com.yyp.tools.asset.TypefaceConfig;
import com.yyp.tools.bitmap.AbstractOutlineTextDrawable;
import com.yyp.tools.bitmap.BitmapGenerator;

import java.io.File;
import java.io.FilenameFilter;

@Visible
public class TextBitmapGenerator implements BitmapGenerator {
    private final Canvas mCanvas = new Canvas();
    private final TextBoxDrawable mTextBox = new TextBoxDrawable();
    private TextBitmap mTextBitmap;

    public TextBitmapGenerator() {
    }

    public void updateTextBitmap(TextBitmap text) {
        this.mTextBitmap = text;
    }

    public Bitmap generateTextBitmap(TextBitmap text) {
        this.mTextBitmap = text;
        this.configureLayout(this.mTextBox, text);
        this.mTextBox.layout();
        return this.getImageAligned(this.mTextBox, text.mBmpWidth, text.mBmpHeight);
    }

    private void configureLayout(AbstractOutlineTextDrawable drawable, TextBitmap text) {
        TypefaceConfig var3;
        if (TextUtils.isEmpty(text.mFontPath)) {
            var3 = new TypefaceConfig();
        } else {
            try {
                File var4 = new File(text.mFontPath);
                File var5 = var4.getParentFile();
                String[] var6 = var5.list(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".ttf");
                    }
                });
                if (var6 != null && var6.length > 0) {
                    File var7 = new File(var5, var6[0]);
                    if (!var7.exists()) {
                        Log.e("AliYunLog", "Font file[" + var7.getAbsolutePath() + "] not exist!");
                        var3 = new TypefaceConfig();
                    } else {
                        var3 = new TypefaceConfig(Typeface.createFromFile(var7));
                    }
                } else {
                    var3 = new TypefaceConfig();
                }
            } catch (Exception var8) {
                Log.e("AliYunLog", "Load font error!", var8);
                var3 = new TypefaceConfig();
            }
        }

        drawable.setTextOffSet(text.mTextPaddingX, text.mTextPaddingY);
        drawable.setFixTextSize(text.mTextSize);
        drawable.setTextSize(text.mTextWidth, text.mTextHeight);
        drawable.setSize(text.mBmpWidth, text.mBmpHeight);
        drawable.setText(text.mText);
        drawable.setFillColor(text.mTextColor);
        drawable.setAlignment(text.mTextAlignment);
        drawable.setStrokeColor(text.mTextStrokeColor);
        drawable.setStrokeJoin(Join.ROUND);
        drawable.setBackgroundColor(text.mBackgroundColor);
        drawable.setBackgroundBitmap(text.mBackgroundBmp);
        drawable.setTypeface(var3.typeface);
        drawable.setFakeBoldText(var3.fakeBold);
        drawable.setMaxLines(text.mMaxLines);
    }

    private Bitmap getImageAligned(Drawable drawable, int w, int h) {
        return this.rasterize(drawable, w, h);
    }

    private Bitmap rasterize(Drawable drawable, int w, int h) {
        Bitmap var4 = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        this.mCanvas.setBitmap(var4);
        drawable.setBounds(0, 0, w, h);
        int var5 = this.mCanvas.save();
        drawable.draw(this.mCanvas);
        this.mCanvas.restoreToCount(var5);
        return var4;
    }

    public int align2(int x, int align) {
        return x + align - 1 & ~(align - 1);
    }

    public Bitmap generateBitmap(int bmpWidth, int bmpHeight) {
        if (this.mTextBitmap != null) {
            this.mTextBitmap.mBmpWidth = bmpWidth;
            this.mTextBitmap.mBmpHeight = bmpHeight;
        }

        this.configureLayout(this.mTextBox, this.mTextBitmap);
        this.mTextBox.layout();
        return this.getImageAligned(this.mTextBox, bmpWidth, bmpHeight);
    }
}

