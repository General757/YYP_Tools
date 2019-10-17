package com.yyp.tools.utils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

//json解析辅助工具
public class JsonUtils {

    /* 移除bom */
    public static final String removeBOM(String data) {
        if (TextUtils.isEmpty(data))
            return data;

        if (data.startsWith("\ufeff"))
            return data.substring(1);
        else
            return data;
    }

    /*
     * json字段是Boolean
     */
    public static boolean getJSONBoolean(JSONObject json, String name) throws JSONException {
        if (json.has(name))
            return json.getBoolean(name);
        else
            return false;
    }

    /*
     * json字段是Double
     */
    public static double getJSONDouble(JSONObject json, String name) throws JSONException {
        if (json.has(name)) {
            if (TextUtils.isEmpty(json.getString(name)))
                return 0;
            else
                return Double.valueOf(json.getString(name));
        } else
            return 0;
    }

    /*
     * json字段是Float
     */
    public static float getJSONFloat(JSONObject json, String name) throws JSONException {
        if (json.has(name)) {
            if (TextUtils.isEmpty(json.getString(name)))
                return 0;
            else
                return Float.valueOf(json.getString(name));
        } else
            return 0;
    }

    /*
     * json字段是Int
     */
    public static int getJSONInt(JSONObject json, String name) throws JSONException {
        if (json.has(name)) {
            if (TextUtils.isEmpty(json.getString(name)))
                return 0;
            else
                return json.getInt(name);
        } else
            return 0;
    }

    /*
     * json字段是LOng
     */
    public static long getJSONLong(JSONObject json, String name) throws JSONException {
        if (json.has(name)) {
            if (TextUtils.isEmpty(json.getString(name)))
                return 0;
            else
                return json.getLong(name);
        } else
            return 0;
    }

    /*
     * json字段是String
     */
    public static String getJSONString(JSONObject json, String name) throws JSONException {
        if (json.has(name)) {
            String value = json.getString(name);
            if (TextUtils.isEmpty(value))
                return "";
            else
                return value;
        } else
            return "";
    }

    /*
     * json字段是否为空
     */
    public static boolean getJSONObjectText(JSONObject json, String name) throws JSONException {
        if (json.has(name)) {
            String value = json.getString(name);
            if (TextUtils.isEmpty(value))
                return false;
            else if (value.equals("[]"))
                return false;
            else if (value.equals("{}"))
                return false;
            else
                return true;
        } else
            return false;
    }

    /*
     * json数组是否为空
     */
    public static String getJSONArrayText(JSONObject json, String name) throws JSONException {
        if (json.has(name)) {
            String value = json.getString(name);
            if (TextUtils.isEmpty(value))
                return "[]";
            else if (value.equals("{}"))
                return "[]";
            else
                return value;
        } else
            return "[]";
    }

}
