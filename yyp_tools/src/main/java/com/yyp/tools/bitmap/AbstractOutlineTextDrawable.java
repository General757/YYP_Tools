//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;

public abstract class AbstractOutlineTextDrawable extends Drawable {
    private StaticLayout mLayout;
    protected final TextPaint mTextPaint = new TextPaint(1);
    private int mFillColor;
    private int mBackgroundColor = 0;
    private Bitmap mBackgroundBitmap = null;
    private int mStrokeColor;
    private float mStrokeWidth;
    protected float mLineSpacingAdd = 0.0F;
    protected float mLineSpacingMultiplier = 1.0F;
    protected boolean mIncludeFontPadding = true;
    protected int mWidth;
    protected int mHeight;
    protected int mTextWidth;
    protected int mTextHeight;
    protected int mFixTextSize = 0;
    protected int mTextOffSetX = 0;
    protected int mTextOffsetY = 0;
    protected int mMaxLines = 0;
    protected CharSequence mText;
    public Alignment mAlignment;

    public AbstractOutlineTextDrawable() {
        this.mAlignment = Alignment.ALIGN_CENTER;
    }

    protected abstract StaticLayout layout();

    protected final void invalidateLayout() {
        this.mLayout = null;
        this.invalidateSelf();
    }

    public float getTextSize() {
        return this.mTextPaint.getTextSize();
    }

    public void setPaint(TextPaint paint) {
        this.mTextPaint.set(paint);
        this.invalidateLayout();
    }

    public void setFakeBoldText(boolean value) {
        this.mTextPaint.setFakeBoldText(value);
    }

    public void setStrokeJoin(Join join) {
        this.mTextPaint.setStrokeJoin(join);
    }

    public void setStrokeCap(Cap cap) {
        this.mTextPaint.setStrokeCap(cap);
    }

    public TextPaint getPaint() {
        return this.mTextPaint;
    }

    public void setTypeface(Typeface face) {
        this.mTextPaint.setTypeface(face);
        this.invalidateLayout();
    }

    public void setBackgroundColor(int color) {
        this.mBackgroundColor = color;
        this.invalidateSelf();
    }

    public void setBackgroundBitmap(Bitmap bitmap) {
        this.mBackgroundBitmap = bitmap;
        this.invalidateLayout();
    }

    public void setFillColor(int color) {
        this.mFillColor = color;
        this.invalidateSelf();
    }

    public void setStrokeColor(int color) {
        this.mStrokeColor = color;
        this.invalidateSelf();
    }

    private static float getStrokeWidth(float value) {
        return Math.max(2.0F, (value - 42.0F) / 15.0F);
    }

    public float getStrokeWidth() {
        return this.mStrokeWidth;
    }

    private void applyStrokeWidth() {
        float var1 = this.mTextPaint.getTextSize();
        this.mStrokeWidth = getStrokeWidth(var1);
        if (VERSION.SDK_INT == 19) {
            float var2 = var1 <= 256.0F ? this.mStrokeWidth : this.mStrokeWidth / 5.0F;
            this.mTextPaint.setStrokeWidth(var2);
        } else {
            this.mTextPaint.setStrokeWidth(this.mStrokeWidth);
        }

        this.invalidateSelf();
    }

    public void setAlignment(Alignment alignment) {
        if (alignment != null) {
            this.mAlignment = alignment;
        }

    }

    public void setText(CharSequence text) {
        this.mText = text;
        this.invalidateLayout();
    }

    public void setLineSpacing(float add, float mult) {
        this.mLineSpacingAdd = add;
        this.mLineSpacingMultiplier = mult;
        this.invalidateLayout();
    }

    public void setIncludeFontPadding(boolean value) {
        this.mIncludeFontPadding = value;
        this.invalidateLayout();
    }

    public int getIntrinsicWidth() {
        return this.mWidth;
    }

    public int getIntrinsicHeight() {
        return this.mHeight;
    }

    public void setSize(int w, int h) {
        this.mWidth = w;
        this.mHeight = h;
        this.invalidateLayout();
    }

    public void setTextSize(int w, int h) {
        this.mTextWidth = w;
        this.mTextHeight = h;
    }

    public void setFixTextSize(int textSize) {
        this.mFixTextSize = textSize;
    }

    public void setTextOffSet(int offsetX, int offsetY) {
        this.mTextOffSetX = offsetX;
        this.mTextOffsetY = offsetY;
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mLayout = null;
    }

    public void draw(Canvas canvas) {
        if (this.mLayout == null) {
            this.mLayout = this.layout();
            if (this.mMaxLines > 0 && this.mLayout.getLineCount() > this.mMaxLines) {
                int var2 = this.mLayout.getLineStart(this.mMaxLines);
                this.mText = this.mText.subSequence(0, var2);
                this.mLayout = this.layout();
            }

            this.applyStrokeWidth();
        }

        canvas.save();
        Rect var7 = this.getBounds();
        canvas.drawColor(this.mBackgroundColor);
        if (this.mBackgroundBitmap != null) {
            int var3 = this.mBackgroundBitmap.getWidth();
            int var4 = this.mBackgroundBitmap.getHeight();
            Rect var5 = new Rect();
            var5.left = 0;
            var5.top = 0;
            var5.right = var3;
            var5.bottom = var4;
            Rect var6 = new Rect();
            var6.left = 0;
            var6.right = this.mWidth;
            var6.top = 0;
            var6.bottom = this.mHeight;
            canvas.drawBitmap(this.mBackgroundBitmap, var5, var6, (Paint) null);
        }

        canvas.translate((float) var7.left, (float) var7.top);
        canvas.scale((float) var7.width() / (float) this.mWidth, (float) var7.height() / (float) this.mHeight);
        if (this.mAlignment == Alignment.ALIGN_CENTER) {
            canvas.translate((float) ((this.mWidth - this.mTextOffSetX - this.mLayout.getWidth()) / 2 + this.mTextOffSetX), (float) ((this.mHeight - this.mTextOffsetY - this.mLayout.getHeight()) / 2 + this.mTextOffsetY));
        } else {
            canvas.translate((float) this.mTextOffSetX, (float) this.mTextOffsetY);
        }

        TextPaint var8 = this.mLayout.getPaint();
        if (this.mStrokeColor != 0) {
            var8.setColor(this.mStrokeColor);
            var8.setStyle(Style.FILL_AND_STROKE);
            this.mLayout.draw(canvas);
        }

        if (this.mFillColor != 0) {
            var8.setColor(this.mFillColor);
            var8.setStyle(Style.FILL);
            this.mLayout.draw(canvas);
        }

        canvas.restore();
    }

    public void setAlpha(int alpha) {
    }

    public void setColorFilter(ColorFilter cf) {
    }

    public int getOpacity() {
        return -2;
    }

    protected StaticLayout makeLayout(float size) {
        this.mTextPaint.setTextSize(size);
        return new StaticLayout(this.mText, this.mTextPaint, this.mTextWidth, this.mAlignment, this.mLineSpacingMultiplier, this.mLineSpacingAdd, this.mIncludeFontPadding);
    }

    protected StaticLayout makeLayout(float size, int width) {
        this.mTextPaint.setTextSize(size);
        return new StaticLayout(this.mText, this.mTextPaint, width, this.mAlignment, this.mLineSpacingMultiplier, this.mLineSpacingAdd, this.mIncludeFontPadding);
    }

    protected StaticLayout makeLayout(CharSequence text, float size) {
        this.mTextPaint.setTextSize(size);
        return new StaticLayout(text, this.mTextPaint, this.mTextWidth, this.mAlignment, this.mLineSpacingMultiplier, this.mLineSpacingAdd, this.mIncludeFontPadding);
    }

    public void setMaxLines(int maxLines) {
        this.mMaxLines = maxLines;
    }
}

