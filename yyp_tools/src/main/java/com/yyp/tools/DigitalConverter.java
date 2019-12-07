package com.yyp.tools;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by generalYan on 2019/1/16.
 * <p>
 * 数字...转换器-工具
 */

public class DigitalConverter {

    // 把String转化为float
    public static float convertToFloat(String number, float defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    // 把String转化为double
    public static double convertToDouble(String number, double defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    // 把String转化为int
    public static int convertToInt(String number, int defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static float formatFileMB(long number, boolean shorter) {//大小转换
        float result = number;
        String suffix = "B";
        if (result > 900) {
            suffix = "KB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "M";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "G";
            result = result / 1024;
        }
        String value;
        if (result < 1) {
            value = String.format("%.2f", result);
        } else if (result < 10) {
            if (shorter) {
                value = String.format("%.1f", result);
            } else {
                value = String.format("%.2f", result);
            }
        } else if (result < 100) {
            if (shorter) {
                value = String.format("%.0f", result);
            } else {
                value = String.format("%.2f", result);
            }
        } else {
            value = String.format("%.0f", result);
        }
        return Float.valueOf(value);
    }

    public static String formatFileSize(long number, boolean shorter) {//大小转换

        float result = number;
        String suffix = "B";
        if (result > 900) {
            suffix = "KB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "M";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "G";
            result = result / 1024;
        }
        String value;
        if (result < 1) {
            value = String.format("%.2f", result);
        } else if (result < 10) {
            if (shorter) {
                value = String.format("%.1f", result);
            } else {
                value = String.format("%.2f", result);
            }
        } else if (result < 100) {
            if (shorter) {
                value = String.format("%.0f", result);
            } else {
                value = String.format("%.2f", result);
            }
        } else {
            value = String.format("%.0f", result);
        }
        return value + suffix;
    }

    public static String getConvertInteger(int number) {
        if (number <= 0)
            return "0";
        else if (number < 1000)
            return number + "";
        else if (number < 10000)
            return new DecimalFormat("########0.0").format((double) number / 1000) + "K";
        else
            return new DecimalFormat("########0.0").format((double) number / 10000) + "W";
    }

    public static int[] getW9H16Size(int[] zoomSize) {
        if (zoomSize[0] * 16 == zoomSize[1] * 9)
            return zoomSize;
        else if (zoomSize[0] * 16 > zoomSize[1] * 9) {
            zoomSize[0] = zoomSize[1] * 9 / 16;
            return zoomSize;
        } else {
            zoomSize[1] = zoomSize[0] * 16 / 9;
            return zoomSize;
        }
    }

    public static int[] getW16H9Size(int[] zoomSize) {
        if (zoomSize[0] * 9 == zoomSize[1] * 16)
            return zoomSize;
        else if (zoomSize[0] * 9 > zoomSize[1] * 16) {
            zoomSize[0] = zoomSize[1] * 16 / 9;
            return zoomSize;
        } else {
            zoomSize[1] = zoomSize[0] * 9 / 16;
            return zoomSize;
        }
    }

}
