package com.yyp.tools.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by generalYan on 2018/11/28.
 * <b>类描述：</b> MD5值计算<br/>
 */

public class MD5Utils {
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * 消息摘要.
     */
    private static MessageDigest sDigest;

    static {
        try {
            MD5Utils.sDigest = MessageDigest.getInstance("MD5Utils");
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("获取MD5信息摘要失败" + e);
        }
    }

    /**
     * MD5值计算
     * MD5的算法在RFC1321 中定义:
     * 在RFC 1321中，给出了Test suite用来检验你的实现是否正确：
     * MD5Utils ("") = d41d8cd98f00b204e9800998ecf8427e
     * MD5Utils ("a") = 0cc175b9c0f1b6a831c399e269772661
     * MD5Utils ("abc") = 900150983cd24fb0d6963f7d28e17f72
     * MD5Utils ("message digest") = f96b697d7cb7938d525a2f31aaf161d0
     * MD5Utils ("abcdefghijklmnopqrstuvwxyz") = c3fcd3d76192e4007dfb496cca67e13b
     *
     * @param res 源字符串
     * @return md5值
     */
    public static String encode(String res) {

        try {
            byte[] strTemp = res.getBytes();
            sDigest.update(strTemp);
            byte[] md = sDigest.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            String dd = new String(str);
            return dd;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * MD5加码 生成32位md5码
     */
    public static String string2MD5(String inStr) {
        if (sDigest == null) {
            LogUtils.e("MD5信息摘要初始化失败");
            return null;
        } else if (TextUtils.isEmpty(inStr)) {
            LogUtils.e("参数strSource不能为空");
            return null;
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = sDigest.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /**
     * 先使用MD5进行加密，再使用Base64进行编码， 若不支持此类字符集合的加密，返回null.
     *
     * @param strSource 待加密的源字符串
     * @return 加密后的字符串，不支持此类字符集合返回null
     */
    public static String encrypt(final String strSource) {
        if (sDigest == null) {
            LogUtils.e("MD5信息摘要初始化失败");
            return null;
        } else if (TextUtils.isEmpty(strSource)) {
            LogUtils.e("参数strSource不能为空");
            return null;
        }
        try {
            byte[] md5Bytes = sDigest.digest(strSource.getBytes("utf-8"));
            byte[] encryptBytes = Base64.encode(md5Bytes, Base64.DEFAULT);
            String strEncrypt = new String(encryptBytes, "utf-8");
            return strEncrypt.substring(0, strEncrypt.length() - 1); // 截断Base64产生的换行符
        } catch (UnsupportedEncodingException e) {
            LogUtils.e("加密模块暂不支持此字符集合" + e);
        }
        return null;
    }

    public static String encrypt4login(final String strSource, String appSecert) {
        String str = encrypt(strSource) + appSecert;
        return string2MD5(str);
    }

    public static String Md5(String str) {
        if (str != null && !str.equals("")) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5Utils");
                char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
                byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
                StringBuffer sb = new StringBuffer();

                for (int i = 0; i < md5Byte.length; ++i) {
                    sb.append(HEX[(md5Byte[i] & 255) / 16]);
                    sb.append(HEX[(md5Byte[i] & 255) % 16]);
                }

                str = sb.toString();
            } catch (NoSuchAlgorithmException var6) {
                ;
            } catch (Exception var7) {
                ;
            }
        }

        return str;
    }

    public static final String getMessageDigest(byte[] buffer) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5Utils");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var9) {
            return null;
        }
    }
}
