//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yan on 2019/10/11.
 */
public class StringUtils {
    public StringUtils() {
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0) && isBlank(str);
    }

    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    public static String nullStrToEmpty(Object str) {
        return str == null ? "" : (str instanceof String ? (String) str : str.toString());
    }

    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        } else {
            char var1 = str.charAt(0);
            return Character.isLetter(var1) && !Character.isUpperCase(var1) ? (new StringBuilder(str.length())).append(Character.toUpperCase(var1)).append(str.substring(1)).toString() : str;
        }
    }

    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException var2) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", var2);
            }
        } else {
            return str;
        }
    }

    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException var3) {
                return defultReturn;
            }
        } else {
            return str;
        }
    }

    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        } else {
            String var1 = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
            Pattern var2 = Pattern.compile(var1, 2);
            Matcher var3 = var2.matcher(href);
            return var3.matches() ? var3.group(1) : href;
        }
    }

    public static String htmlEscapeCharsToString(String source) {
        return isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        } else {
            char[] var1 = s.toCharArray();

            for (int var2 = 0; var2 < var1.length; ++var2) {
                if (var1[var2] == 12288) {
                    var1[var2] = ' ';
                } else if (var1[var2] >= '！' && var1[var2] <= '～') {
                    var1[var2] -= 'ﻠ';
                } else {
                    var1[var2] = var1[var2];
                }
            }

            return new String(var1);
        }
    }

    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        } else {
            char[] var1 = s.toCharArray();

            for (int var2 = 0; var2 < var1.length; ++var2) {
                if (var1[var2] == ' ') {
                    var1[var2] = 12288;
                } else if (var1[var2] >= '!' && var1[var2] <= '~') {
                    var1[var2] += 'ﻠ';
                } else {
                    var1[var2] = var1[var2];
                }
            }

            return new String(var1);
        }
    }

    public static String sqliteEscape(String keyWord) {
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&", "/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

    public static String subString(String src) {
        int var1 = src.lastIndexOf(47);
        if (var1 >= 0) {
            src = src.substring(var1 + 1);
        }

        return src;
    }
}

