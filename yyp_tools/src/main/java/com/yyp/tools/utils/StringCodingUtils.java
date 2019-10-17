package com.yyp.tools.utils;

import android.os.Build.VERSION;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by YanYan on 2019/10/11.
 */
public class StringCodingUtils {
    public StringCodingUtils() {
    }

    public static byte[] getBytes(String src, Charset charSet) {
        if (VERSION.SDK_INT < 9) {
            try {
                return src.getBytes(charSet.name());
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return src.getBytes(charSet);
        }
    }
}
