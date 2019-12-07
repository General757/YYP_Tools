//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Yan on 2019/10/11.
 * 处理字符串的工具类
 */
public class StringUtils {
    public StringUtils() {
    }

    /**
     * is null or its length is 0 or it is made by space
     * <p>
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0) && isBlank(str);
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.trim().length() > 0;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str null、“ ”、“null”都返回true
     * @return boolean
     */
    public static boolean isNullString(String str) {
        return (null == str || isBlank(str.trim()) || "null".equals(str.trim().toLowerCase()));
    }

    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[一-龥]";

        for (int i = 0; i < value.length(); ++i) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                ++valueLength;
            }
        }

        return valueLength;
    }

    /**
     * null Object to empty string
     * <p>
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str.toString()));
    }

    /**
     * 是否是Utf-8字符串
     * encoded in utf-8
     * <p>
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!TextUtils.isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * 使用utf-8转码
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!TextUtils.isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * 获取<a></a>标签内的内容
     * get innerHtml from href
     * <p>
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return <ul>
     * <li>if href is null, return ""</li>
     * <li>if not match regx, return source</li>
     * <li>return the last string that match regx</li>
     * </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        } else {
            String var1 = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
            Pattern var2 = Pattern.compile(var1, Pattern.CASE_INSENSITIVE);
            Matcher var3 = var2.matcher(href);
            return var3.matches() ? var3.group(1) : href;
        }
    }

    /**
     * Html文本格式转码
     * process special char in html
     * <p>
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return TextUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * 全角字符 转为为半角字符
     * transform full width char to half width char
     * <p>
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (TextUtils.isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * 将半角字符转换为全角字符
     * transform half width char to full width char
     * <p>
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (TextUtils.isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
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

    public static String substringBefore(String str, String separator) {
        if (!isEmpty(str) && separator != null) {
            if (separator.length() == 0) {
                return "";
            } else {
                int pos = str.indexOf(separator);
                return pos == -1 ? str : str.substring(0, pos);
            }
        } else {
            return str;
        }
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        byte[] var2 = data;
        int var3 = data.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            int v = b & 255;
            if (v < 16) {
                sb.append('0');
            }

            sb.append(Integer.toHexString(v));
        }

        return sb.toString().toUpperCase(Locale.getDefault());
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return d;
    }

    /**
     * 处理十个字换行
     *
     * @param text editText
     * @return 换行了的文字
     */
    public static String lineFeedText(String text) {
        StringBuilder buffer = new StringBuilder(text);
        char[] chars = text.toCharArray();
        int temp = 0;
        int feedCount = 0;
        int index = 0;
        while (index <= chars.length) {
            if (temp >= 11) {
                buffer.insert(index + feedCount - 1, (char) 10);
                feedCount++;
                temp = 1;
            }
            if (index == chars.length) {
                break;
            }
            if (chars[index] != 10) {
                temp++;
            } else {
                temp = 0;
            }
            index++;
        }
        return buffer.toString();
    }

    /* 输入流转String */
    public static String stringFromStream(InputStream is) {
        try {
            int readlen = 0;
            byte[] buff = new byte[20480];

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            while ((readlen = is.read(buff)) != -1) {
                outStream.write(buff, 0, readlen);
            }

            return new String(outStream.toByteArray(), "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    /* 文件转String */
    public static String stringFromFile(String filepath) {
        try {
            FileInputStream fis = new FileInputStream(filepath);
            String string = stringFromStream(fis);
            fis.close();
            return string;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断字符串是否为空或空字符
     *
     * @param strSource 源字符串
     * @return true表示为空，false表示不为空
     */
    public static boolean isNull(final String strSource) {
        return strSource == null || "".equals(strSource.trim());
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        } else {
            if (start < 0) {
                start += str.length();
            }

            if (start < 0) {
                start = 0;
            }

            return start > str.length() ? "" : str.substring(start);
        }
    }

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        } else {
            if (end < 0) {
                end += str.length();
            }

            if (start < 0) {
                start += str.length();
            }

            if (end > str.length()) {
                end = str.length();
            }

            if (start > end) {
                return "";
            } else {
                if (start < 0) {
                    start = 0;
                }

                if (end < 0) {
                    end = 0;
                }

                return str.substring(start, end);
            }
        }
    }

    /**
     * 获取字符串长度的安全方法
     * get length of CharSequence
     * <p>
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     *
     * @param str
     * @return if str is null or empty, return 0, else return {@link CharSequence#length()}.
     */
    public static int size(CharSequence str) {
        return str == null ? 0 : str.length();
    }


    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 格式化字符串 如果为空，返回“”
     *
     * @param str
     * @return String
     */
    public static String formatString(String str) {
        if (isNullString(str)) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 获得文件名称
     *
     * @param path
     * @return String
     */
    public static String getFileName(String path) {
        if (isNullString(path))
            return null;
        int bingindex = path.lastIndexOf("/");
        return path.substring(bingindex + 1, path.length());
    }

    /**
     * 获得文件名称前缀
     *
     * @param path
     * @return String
     */
    public static String getFileNamePrefix(String path) {
        if (isNullString(path))
            return null;
        int bingindex = path.lastIndexOf("/");
        int endindex = path.lastIndexOf(".");
        return path.substring(bingindex + 1, endindex);
    }

    /**
     * 自动命名文件,命名文件格式如：IP地址+时间戳+三位随机数 .doc
     *
     * @param ip       ip地址
     * @param fileName 文件名
     * @return String
     */
    public static String getIPTimeRandName(String ip, String fileName) {
        StringBuilder buf = new StringBuilder();
        if (ip != null) {
            String str[] = ip.split("\\.");
            for (String aStr : str) {
                buf.append(addZero(aStr, 3));
            }
        }// 加上IP地址
        buf.append(getTimeStamp());// 加上日期
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            buf.append(random.nextInt(10));// 取三个随机数追加到StringBuffer
        }
        buf.append(".").append(getFileExt(fileName));// 加上扩展名
        return buf.toString();

    }

    /**
     * 自动命名文件,命名文件格式如：时间戳+三位随机数 .doc
     *
     * @param fileName
     * @return String
     */
    public static String getTmeRandName(String fileName) {
        return getIPTimeRandName(null, fileName);
    }

    public static String addZero(String str, int len) {
        StringBuilder s = new StringBuilder();
        s.append(str);
        while (s.length() < len) {
            s.insert(0, "0");
        }
        return s.toString();
    }

    /**
     * 获得时间戳 也可以用 ：commons-lang.rar 下的：DateFormatUtils类 更为简单
     *
     * @return String
     */
    public static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    /**
     * 获得文件扩展名
     *
     * @param filename
     * @return String
     */
    public static String getFileExt(String filename) {
        int i = filename.lastIndexOf(".");// 返回最后一个点的位置
        String extension = filename.substring(i + 1);// 取出扩展名
        return extension;
    }

    /**
     * 将url进行utf-8编码
     *
     * @param url
     * @return String
     */
    public static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将url进行utf-8解码
     *
     * @param url
     * @return String
     */
    public static String decodeURL(String url) {
        try {
            return URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 格式化日期字符串 也可以用 ：commons-lang.rar 下的：DateFormatUtils类 更为简单
     *
     * @param date
     * @param pattern
     * @return String
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 格式化日期字符串 也可以用 ：commons-lang.rar 下的：DateFormatUtils类 更为简单
     */
    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_DATE_PATTERN);
    }

    /**
     * 获取当前时间 也可以用 ：commons-lang.rar 下的：DateFormatUtils类 更为简单
     *
     * @return String
     */
    public static String getDate() {
        return formatDate(new Date(), DEFAULT_DATE_PATTERN);
    }

    /**
     * 获取当前时间
     *
     * @return String
     */
    public static String getDateTime() {
        return formatDate(new Date(), DEFAULT_DATETIME_PATTERN);
    }

    /**
     * 格式化日期时间字符串
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, DEFAULT_DATETIME_PATTERN);
    }

    /**
     * 格式化json格式日期
     *
     * @param date
     * @return String
     */
    @SuppressWarnings("deprecation")
    public static String formatJsonDateTime(JSONObject date) {
        Date result = null;
        try {
            result = new Date(date.getInt("year"), date.getInt("month"),
                    date.getInt("date"), date.getInt("hours"),
                    date.getInt("minutes"), date.getInt("seconds"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == null ? "" : formatDateTime(result);
    }

    /**
     * 将字符串集合 变为以 separator 分割的字符串
     *
     * @param array     字符串集合
     * @param separator 分隔符
     * @return String
     */
    public static String join(final ArrayList<String> array, String separator) {
        StringBuilder result = new StringBuilder();
        if (array != null && array.size() > 0) {
            for (String str : array) {
                result.append(str);
                result.append(separator);
            }
            result.delete(result.length() - 1, result.length());
        }
        return result.toString();
    }

    /**
     * 压缩字符串
     *
     * @param str
     * @return String
     * @throws IOException
     */
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    /**
     * 解压缩字符串
     *
     * @param str
     * @return String
     * @throws IOException
     */
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(
                str.getBytes("ISO-8859-1"));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString("UTF-8");
    }

    /**
     * <b>description :</b> 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param input
     * @return String
     */
    public static String stringFilter(String input) {
        if (input == null)
            return null;
        input = input.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        return m.replaceAll("").trim();
    }

    /**
     * <b>description :</b> 半角字符转全角字符
     *
     * @param input
     * @return String
     */
    public static String ToDBC(String input) {
        if (input == null)
            return null;
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 判断字符串"oldString"是否为null
     *
     * @param oldString 需要判断的字符串
     * @return String 如果"oldString"为null返回空值"",否则返回"oldString"
     */
    public static String getString(String oldString) {
        if (oldString == null || "null".equals(oldString)) {
            return "";
        } else {
            return oldString.trim();
        }
    }

    /**
     * 将一实数转换成字符串并返回
     *
     * @param d 实数
     * @return String
     */
    public static String getString(double d) {
        return String.valueOf(d);
    }

    /**
     * 得到文件的后缀名(扩展名)
     *
     * @param name
     * @return String 后缀名
     */
    public static String getAfterPrefix(String name) throws Exception {
        return name.substring(name.lastIndexOf(".") + 1, name.length());
    }

    /**
     * 分割字符串
     *
     * @param values 要分割的内容
     * @param limit  分隔符 例：以“,”分割
     * @return String[] 返回数组，没有返回null
     */
    public static String[] spilctMoreSelect(String values, String limit) {
        if (isNullOrEmpty(values)) {
            return null;
        }
        return values.trim().split(limit);
    }

    /**
     * 将字符串数组转化为字符串
     *
     * @param needvalue
     * @return String 返回字符串，否则返回null
     */
    public static String arr2Str(String[] needvalue) {
        String str = "";
        if (needvalue != null) {
            int len = needvalue.length;
            for (int i = 0; i < len; i++) {
                if (i == len - 1) {
                    str += needvalue[i];
                } else {
                    str += needvalue[i] + ",";
                }
            }
            return str;
        } else {
            return null;
        }
    }

    public static int arr2int(String[] arr) {
        if (arr != null && arr.length > 0) {
            return Integer.parseInt(arr[1]);
        }
        return -1;
    }

    /**
     * 判断字符串是否为空或空符串。
     *
     * @param str 要判断的字符串。
     * @return String 返回判断的结果。如果指定的字符串为空或空符串，则返回true；否则返回false。
     */
    public static boolean isNullOrEmpty(String str) {

        return (str == null) || (str.trim().length() == 0);
    }

    /**
     * 去掉字符串两端的空白字符。因为String类里边的trim()方法不能出现null.trim()的情况，因此这里重新写一个工具方法。
     *
     * @param str 要去掉空白的字符串。
     * @return String 返回去掉空白后的字符串。如果字符串为null，则返回null；否则返回str.trim()。 *
     */
    public static String trim(String str) {

        return str == null ? null : str.trim();
    }

    /**
     * 更具配置的string.xml 里的id，得到内容
     *
     * @param context
     * @param id
     * @return String
     */
    public static String getValueById(Context context, int id) {
        return context.getResources().getString(id);
    }

    /**
     * 用于文中强制换行的处理
     *
     * @param oldstr
     * @return String
     */
    public static String replaceStr(String oldstr) {
        oldstr = oldstr.replaceAll("\n", "<br>");// 替换换行
        oldstr = oldstr.replaceAll("\r\n", "<br>");// 替换回车换行
        oldstr = oldstr.replaceAll(" ", "&nbsp;" + " ");// 替换空格
        return oldstr;
    }

    /**
     * 判断是否是数字
     *
     * @param c
     * @return boolean
     */
    public static boolean isNum(char c) {
        return c >= 48 && c <= 57;
    }

    /**
     * 获得题号 例如：2.本文选自哪篇文章？ 提取题号中的数字 2
     *
     * @param content
     * @return int
     */
    public static int getThemeNum(String content) {
        int tnum = -1;
        if (isNullOrEmpty(content))
            return tnum;
        int a = content.indexOf(".");
        if (a > 0) {
            String num = content.substring(0, a);
            try {
                tnum = Integer.parseInt(num);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return tnum;
            }
        }
        return tnum;
    }

    // 添加自己的字符串操作

    public static String dealDigitalFlags(String str) {
        String result = "";
        if (str == null || str.length() < 0) {
            return null;
        } else {
            int len = str.length();
            for (int i = 0; i < len; i++) {
                String tmp = str.substring(i, i + 1);
                if (tmp.equals("+") || tmp.equals("*") || tmp.equals("=")) {
                    tmp = " " + tmp + " ";
                }
                result += tmp;
            }
        }
        return result;
    }

    /**
     * 截取序号 例如：01026---->26
     *
     * @param oldnum
     * @return String
     */
    public static String detailNum(String oldnum) {
        if (isNullOrEmpty(oldnum))
            return oldnum;
        int newnum = Integer.parseInt(oldnum);
        return newnum + ".";
    }

    public static String[] getStoreArr(String[] arr) throws Exception {
        String temp;
        for (int i = 0; i < arr.length; i++) {
            for (int j = arr.length - 1; j > i; j--) {
                int a = Integer.parseInt(arr[i]);
                int b = Integer.parseInt(arr[j]);
                if (a > b) {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        return arr;
    }

    /**
     * 给数字字符串排序 如：3，1，2 --->1，2，3
     *
     * @param str
     * @return String
     * @throws Exception
     */
    public static String resetStoreNum(String str) {
        String value = "";
        try {
            if (str == null || str.length() < 1)
                return value;
            String[] results = str.split(",");
            String[] newarr = getStoreArr(results);
            for (String aNewarr : newarr) {
                value += aNewarr + ",";
            }
            value = value.substring(0, value.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 判断数组中是否包含某个值
     *
     * @param srcValue
     * @param values
     * @return boolean
     */
    public static boolean arrIsValue(String srcValue, String[] values) {
        if (values == null) {
            return false;
        }
        for (String value : values) {
            if (value.equals(srcValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得"."之后的所有内容
     *
     * @param content 原字符串
     * @return String
     */
    public static String DeleteOriNumber(String content) {
        if (content.trim().length() > 1) {
            int index = content.indexOf(".");
            String AfterStr = content.substring(index + 1, content.length());
            return AfterStr;
        } else {
            return content;
        }
    }

    /**
     * GBK编码
     *
     * @param content
     * @return String
     */
    public static String convertToGBK(String content) {
        if (!isEmpty(content)) {
            try {
                content = new String(content.getBytes(), "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    private static String trimSpaces(String IP) {// 去掉IP字符串前后所有的空格
        while (IP.startsWith(" ")) {
            IP = IP.substring(1, IP.length()).trim();
        }
        while (IP.endsWith(" ")) {
            IP = IP.substring(0, IP.length() - 1).trim();
        }
        return IP;
    }

    /**
     * 判断是否是一个IP
     *
     * @param IP
     * @return boolean
     */
    public static boolean isIp(String IP) {
        boolean b = false;
        IP = trimSpaces(IP);
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = IP.split("\\.");
            if (Integer.parseInt(s[0]) < 255)
                if (Integer.parseInt(s[1]) < 255)
                    if (Integer.parseInt(s[2]) < 255)
                        if (Integer.parseInt(s[3]) < 255)
                            b = true;
        }
        return b;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return Date
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 方法: distanceSize
     * 描述: 计算距离
     *
     * @param distance 距离数 单位千米
     * @return String  转换后的距离
     */
    public static String distanceSize(double distance) {
        if (distance < 1.0) return (int) (distance * 1000) + "m";
        String dd = "0";
        try {
            DecimalFormat fnum = new DecimalFormat("##0.00");
            dd = fnum.format(distance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dd + "km";
    }

    /**
     * 方法: replaceResult
     * 描述: 替换结果字符串
     *
     * @param content
     * @return String    返回类型
     */
    public static String replaceResult(String content) {
        if (!isEmpty(content))
            content = content.replace("\\", "").replace("\"{", "{").replace("}\"", "}");
        return content;
    }

    /**
     * 方法: checkPhone
     * 描述: 提取电话号码
     *
     * @param content
     * @return ArrayList<String>    返回类型
     */
    public static ArrayList<String> checkPhone(String content) {
        ArrayList<String> list = new ArrayList<String>();
        if (isEmpty(content)) return list;
        Pattern p = Pattern.compile("1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}");
        Matcher m = p.matcher(content);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }

    /**
     * <p>描述:保留一位小数</p>
     *
     * @param value
     * @return 设定文件
     */
    public static String parseStr(String value) {
        if (isNullString(value)) return "0.0";
        DecimalFormat df = new DecimalFormat("######0.0");
        double mvalue = Double.parseDouble(value);
        return df.format(mvalue);
    }

    public static String parseStr2(String value) {
        if (isNullString(value)) return "--";
        DecimalFormat df = new DecimalFormat("######0.0");
        double mvalue = Double.parseDouble(value);
        String mStr = df.format(mvalue);
        if (mStr.equals("0") || mStr.equals("0.0")) {
            return "--";
        }
        return mStr;
    }

    public static String parseStr(double value) {
        if (value == 0) return "0.0";
        DecimalFormat df = new DecimalFormat("######0.0");
        return df.format(Double.parseDouble(String.valueOf(value)));
    }

    /**
     * 处理自动换行问题
     *
     * @param input 字符串
     * @return 设定文件
     */
    public static String ToWrap(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 时间显示转换
     *
     * @param duration   时间区间 0-59
     * @param isShowZero 小于10是否显示0 如：09
     * @return
     */
    public static String durationShow(int duration, boolean isShowZero) {
        String showStr;
        if (isShowZero) {
            if (duration < 10) {
                showStr = "0" + String.valueOf(duration);
            } else {
                showStr = String.valueOf(duration);
            }
        } else {
            showStr = String.valueOf(duration);
        }
        return showStr;
    }

    public static long fromTimeString(String s) {
        if (s.lastIndexOf(".") != -1) {
            s = s.substring(0, s.lastIndexOf("."));
        }
        String[] split = s.split(":");
        if (split.length == 3) {
            return Long.parseLong(split[0]) * 3600L + Long.parseLong(split[1]) * 60L + Long.parseLong(split[2]);
        } else if (split.length == 2) {
            return Long.parseLong(split[0]) * 60L + Long.parseLong(split[0]);
        } else {
            throw new IllegalArgumentException("Can\'t parse time string: " + s);
        }
    }

    public static String toTimeString(long seconds) {
        seconds = seconds / 1000;
        long hours = seconds / 3600L;
        long remainder = seconds % 3600L;
        long minutes = remainder / 60L;
        long secs = remainder % 60L;
        if (hours == 0) {
            return (minutes < 10L ? "0" : "") + minutes + ":" + (secs < 10L ? "0" : "") + secs;
        }
        return (hours < 10L ? "0" : "") + hours + ":" + (minutes < 10L ? "0" : "") + minutes + ":" + (secs < 10L ? "0" : "") + secs;
    }

    /**
     * 是否含有表情符
     * false 为含有表情符
     */
    public static boolean checkFace(String checkString) {
        String reg = "^([a-z]|[A-Z]|[0-9]|[\u0000-\u00FF]|[\u2000-\uFFFF]){1,}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(checkString.replaceAll(" ", ""));
        return matcher.matches();
    }

    // 字符串截断
    public static String getLimitString(String source, int length) {
        if (null != source && source.length() > length) {
            // int reallen = 0;
            return source.substring(0, length) + "...";
        }
        return source;
    }

    /**
     * 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
     */
    public static int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }
    }

    /**
     * 返回字符串里中文字或者全角字符的个数
     */
    public static int getChineseNum(String s) {
        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

    /**
     * 按照中文占2个字符 英文占1个字符规则 获取字符串占位符
     *
     * @param source
     * @return
     */
    public static int getSpaceCount(String source) {
        int count = 0;
        count = source.length() + getSpaceChineseCount(source);
        return count;
    }

    /**
     * 获取中文个数
     *
     * @param source
     * @return
     */
    public static int getSpaceChineseCount(String source) {
        String regEx = "[\\u4e00-\\u9fa5]";
        int count = 0;
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(source);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count = count + 1;
            }
        }
        return count;
    }

    static String[] charString = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public static String getRandomChar(int size) {
        String charStr = "";
        for (int index = 0; index < size; index++) {
            charStr = charStr + charString[new Random().nextInt(charString.length - 1)];
        }
        return charStr;
    }

    public static String md5_string(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5Utils");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            return "";
        }
    }

    final static char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String deleteWhitespace(String str) {
        StringBuilder buffer = new StringBuilder();
        int sz = str.length();

        for (int i = 0; i < sz; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                buffer.append(str.charAt(i));
            }
        }

        return buffer.toString();
    }

    public static int indexOfAny(String str, String[] searchStrs) {
        if (str != null && searchStrs != null) {
            int ret = 2147483647;
            String[] var5 = searchStrs;
            int var6 = searchStrs.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                String searchStr = var5[var7];
                int tmp = str.indexOf(searchStr);
                if (tmp != -1 && tmp < ret) {
                    ret = tmp;
                }
            }

            return ret == 2147483647 ? -1 : ret;
        } else {
            return -1;
        }
    }

    public static int lastIndexOfAny(String str, String[] searchStrs) {
        if (str != null && searchStrs != null) {
            int ret = -1;
            String[] var5 = searchStrs;
            int var6 = searchStrs.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                String searchStr = var5[var7];
                int tmp = str.lastIndexOf(searchStr);
                if (tmp > ret) {
                    ret = tmp;
                }
            }

            return ret;
        } else {
            return -1;
        }
    }

    public static String left(String str, int len) {
        if (len < 0) {
            throw new IllegalArgumentException("Requested String length " + len + " is less than zero");
        } else {
            return str != null && str.length() > len ? str.substring(0, len) : str;
        }
    }

    public static String right(String str, int len) {
        if (len < 0) {
            throw new IllegalArgumentException("Requested String length " + len + " is less than zero");
        } else {
            return str != null && str.length() > len ? str.substring(str.length() - len) : str;
        }
    }

    public static String mid(String str, int pos, int len) {
        if (pos >= 0 && (str == null || pos <= str.length())) {
            if (len < 0) {
                throw new IllegalArgumentException("Requested String length " + len + " is less than zero");
            } else if (str == null) {
                return null;
            } else {
                return str.length() <= pos + len ? str.substring(pos) : str.substring(pos, pos + len);
            }
        } else {
            throw new StringIndexOutOfBoundsException("String index " + pos + " is out of bounds");
        }
    }

    public static String[] split(String str) {
        return split(str, (String) null, -1);
    }

    public static String[] split(String text, String separator) {
        return split(text, separator, -1);
    }

    public static String[] split(String str, String separator, int max) {
        StringTokenizer tok = null;
        if (separator == null) {
            tok = new StringTokenizer(str);
        } else {
            tok = new StringTokenizer(str, separator);
        }

        int listSize = tok.countTokens();
        if (max > 0 && listSize > max) {
            listSize = max;
        }

        String[] list = new String[listSize];
        int i = 0;
        for (int lastTokenEnd = 0; tok.hasMoreTokens(); ++i) {
            int lastTokenBegin;
            if (max > 0 && i == listSize - 1) {
                String endToken = tok.nextToken();
                lastTokenBegin = str.indexOf(endToken, lastTokenEnd);
                list[i] = str.substring(lastTokenBegin);
                break;
            }

            list[i] = tok.nextToken();
            lastTokenBegin = str.indexOf(list[i], lastTokenEnd);
            lastTokenEnd = lastTokenBegin + list[i].length();
        }

        return list;
    }

    public static String concatenate(Object[] array) {
        return join(array, "");
    }

    public static String join(Object[] array, String separator) {
        if (separator == null) {
            separator = "";
        }

        int arraySize = array.length;
        int bufSize = arraySize == 0 ? 0 : (array[0].toString().length() + separator.length()) * arraySize;
        StringBuilder buf = new StringBuilder(bufSize);

        for (int i = 0; i < arraySize; ++i) {
            if (i > 0) {
                buf.append(separator);
            }

            buf.append(array[i]);
        }

        return buf.toString();
    }

    public static String join(Iterator iterator, String separator) {
        if (separator == null) {
            separator = "";
        }

        StringBuilder buf = new StringBuilder(256);

        while (iterator.hasNext()) {
            buf.append(iterator.next());
            if (iterator.hasNext()) {
                buf.append(separator);
            }
        }

        return buf.toString();
    }

    public static String replaceOnce(String text, char repl, char with) {
        return replace(text, repl, with, 1);
    }

    public static String replace(String text, char repl, char with) {
        return replace(text, repl, with, -1);
    }

    public static String replace(String text, char repl, char with, int max) {
        return replace(text, String.valueOf(repl), String.valueOf(with), max);
    }

    public static String replaceOnce(String text, String repl, String with) {
        return replace(text, repl, with, 1);
    }

    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }

    public static String replace(String text, String repl, String with, int max) {
        if (text != null && repl != null && with != null && repl.length() != 0) {
            StringBuilder buf = new StringBuilder(text.length());
            int start = 0;
            boolean var6 = false;

            int end;
            while ((end = text.indexOf(repl, start)) != -1) {
                buf.append(text.substring(start, end)).append(with);
                start = end + repl.length();
                --max;
                if (max == 0) {
                    break;
                }
            }

            buf.append(text.substring(start));
            return buf.toString();
        } else {
            return text;
        }
    }

    public static String overlayString(String text, String overlay, int start, int end) {
        return text.substring(0, start) + overlay + text.substring(end);
    }

    public static String center(String str, int size) {
        return center(str, size, " ");
    }

    public static String center(String str, int size, String delim) {
        int sz = str.length();
        int p = size - sz;
        if (p < 1) {
            return str;
        } else {
            str = leftPad(str, sz + p / 2, delim);
            str = rightPad(str, size, delim);
            return str;
        }
    }

    public static String chomp(String str) {
        return chomp(str, "\n");
    }

    public static String chomp(String str, String sep) {
        int idx = str.lastIndexOf(sep);
        return idx != -1 ? str.substring(0, idx) : str;
    }

    public static String chompLast(String str) {
        return chompLast(str, "\n");
    }

    public static String chompLast(String str, String sep) {
        if (str.length() == 0) {
            return str;
        } else {
            String sub = str.substring(str.length() - sep.length());
            return sep.equals(sub) ? str.substring(0, str.length() - sep.length()) : str;
        }
    }

    public static String getChomp(String str, String sep) {
        int idx = str.lastIndexOf(sep);
        if (idx == str.length() - sep.length()) {
            return sep;
        } else {
            return idx != -1 ? str.substring(idx) : "";
        }
    }

    public static String prechomp(String str, String sep) {
        int idx = str.indexOf(sep);
        return idx != -1 ? str.substring(idx + sep.length()) : str;
    }

    public static String getPrechomp(String str, String sep) {
        int idx = str.indexOf(sep);
        return idx != -1 ? str.substring(0, idx + sep.length()) : "";
    }

    public static String chop(String str) {
        if ("".equals(str)) {
            return "";
        } else if (str.length() == 1) {
            return "";
        } else {
            int lastIdx = str.length() - 1;
            String ret = str.substring(0, lastIdx);
            char last = str.charAt(lastIdx);
            return last == '\n' && ret.charAt(lastIdx - 1) == '\r' ? ret.substring(0, lastIdx - 1) : ret;
        }
    }

    public static String chopNewline(String str) {
        int lastIdx = str.length() - 1;
        char last = str.charAt(lastIdx);
        if (last == '\n') {
            if (str.charAt(lastIdx - 1) == '\r') {
                --lastIdx;
            }
        } else {
            ++lastIdx;
        }

        return str.substring(0, lastIdx);
    }

    public static String escape(String str) {
        int sz = str.length();
        StringBuilder buffer = new StringBuilder(2 * sz);

        for (int i = 0; i < sz; ++i) {
            char ch = str.charAt(i);
            if (ch > 4095) {
                buffer.append("\\u").append(Integer.toHexString(ch));
            } else if (ch > 255) {
                buffer.append("\\u0").append(Integer.toHexString(ch));
            } else if (ch > 127) {
                buffer.append("\\u00").append(Integer.toHexString(ch));
            } else if (ch < ' ') {
                switch (ch) {
                    case '\b':
                        buffer.append('\\');
                        buffer.append('b');
                        break;
                    case '\t':
                        buffer.append('\\');
                        buffer.append('t');
                        break;
                    case '\n':
                        buffer.append('\\');
                        buffer.append('n');
                        break;
                    case '\u000b':
                    default:
                        if (ch > 15) {
                            buffer.append("\\u00").append(Integer.toHexString(ch));
                        } else {
                            buffer.append("\\u000").append(Integer.toHexString(ch));
                        }
                        break;
                    case '\f':
                        buffer.append('\\');
                        buffer.append('f');
                        break;
                    case '\r':
                        buffer.append('\\');
                        buffer.append('r');
                }
            } else {
                switch (ch) {
                    case '"':
                        buffer.append('\\');
                        buffer.append('"');
                        break;
                    case '\'':
                        buffer.append('\\');
                        buffer.append('\'');
                        break;
                    case '\\':
                        buffer.append('\\');
                        buffer.append('\\');
                        break;
                    default:
                        buffer.append(ch);
                }
            }
        }

        return buffer.toString();
    }

    public static String repeat(String str, int repeat) {
        StringBuilder buffer = new StringBuilder(repeat * str.length());

        for (int i = 0; i < repeat; ++i) {
            buffer.append(str);
        }

        return buffer.toString();
    }

    public static String rightPad(String str, int size) {
        return rightPad(str, size, " ");
    }

    public static String rightPad(String str, int size, String delim) {
        size = (size - str.length()) / delim.length();
        if (size > 0) {
            str = str + repeat(delim, size);
        }

        return str;
    }

    public static String leftPad(String str, int size) {
        return leftPad(str, size, " ");
    }

    public static String leftPad(String str, int size, String delim) {
        size = (size - str.length()) / delim.length();
        if (size > 0) {
            str = repeat(delim, size) + str;
        }

        return str;
    }

    public static String strip(String str) {
        return strip(str, (String) null);
    }

    public static String strip(String str, String delim) {
        str = stripStart(str, delim);
        return stripEnd(str, delim);
    }

    public static String[] stripAll(String[] strs) {
        return stripAll(strs, (String) null);
    }

    public static String[] stripAll(String[] strs, String delimiter) {
        if (strs != null && strs.length != 0) {
            int sz = strs.length;
            String[] newArr = new String[sz];

            for (int i = 0; i < sz; ++i) {
                newArr[i] = strip(strs[i], delimiter);
            }

            return newArr;
        } else {
            return strs;
        }
    }

    public static String stripEnd(String str, String strip) {
        if (str == null) {
            return null;
        } else {
            int end = str.length();
            if (strip == null) {
                while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                    --end;
                }
            } else {
                while (end != 0 && strip.indexOf(str.charAt(end - 1)) != -1) {
                    --end;
                }
            }

            return str.substring(0, end);
        }
    }

    public static String stripStart(String str, String strip) {
        if (str == null) {
            return null;
        } else {
            int start = 0;
            int sz = str.length();
            if (strip == null) {
                while (start != sz && Character.isWhitespace(str.charAt(start))) {
                    ++start;
                }
            } else {
                while (start != sz && strip.indexOf(str.charAt(start)) != -1) {
                    ++start;
                }
            }

            return str.substring(start);
        }
    }

    public static String upperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static String lowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String uncapitalise(String str) {
        if (str == null) {
            return null;
        } else {
            return str.length() == 0 ? "" : Character.toLowerCase(str.charAt(0)) + str.substring(1);
        }
    }

    public static String capitalise(String str) {
        if (str == null) {
            return null;
        } else {
            return str.length() == 0 ? "" : Character.toTitleCase(str.charAt(0)) + str.substring(1);
        }
    }

    public static String swapCase(String str) {
        if (str == null) {
            return null;
        } else {
            int sz = str.length();
            StringBuilder buffer = new StringBuilder(sz);
            boolean whitespace = false;

            for (int i = 0; i < sz; ++i) {
                char ch = str.charAt(i);
                char tmp;
                if (Character.isUpperCase(ch)) {
                    tmp = Character.toLowerCase(ch);
                } else if (Character.isTitleCase(ch)) {
                    tmp = Character.toLowerCase(ch);
                } else if (Character.isLowerCase(ch)) {
                    if (whitespace) {
                        tmp = Character.toTitleCase(ch);
                    } else {
                        tmp = Character.toUpperCase(ch);
                    }
                } else {
                    tmp = ch;
                }

                buffer.append(tmp);
                whitespace = Character.isWhitespace(ch);
            }

            return buffer.toString();
        }
    }

    public static String capitaliseAllWords(String str) {
        if (str == null) {
            return null;
        } else {
            int sz = str.length();
            StringBuilder buffer = new StringBuilder(sz);
            boolean space = true;

            for (int i = 0; i < sz; ++i) {
                char ch = str.charAt(i);
                if (Character.isWhitespace(ch)) {
                    buffer.append(ch);
                    space = true;
                } else if (space) {
                    buffer.append(Character.toTitleCase(ch));
                    space = false;
                } else {
                    buffer.append(ch);
                }
            }

            return buffer.toString();
        }
    }

    public static String uncapitaliseAllWords(String str) {
        if (str == null) {
            return null;
        } else {
            int sz = str.length();
            StringBuilder buffer = new StringBuilder(sz);
            boolean space = true;

            for (int i = 0; i < sz; ++i) {
                char ch = str.charAt(i);
                if (Character.isWhitespace(ch)) {
                    buffer.append(ch);
                    space = true;
                } else if (space) {
                    buffer.append(Character.toLowerCase(ch));
                    space = false;
                } else {
                    buffer.append(ch);
                }
            }

            return buffer.toString();
        }
    }

    public static String getNestedString(String str, String tag) {
        return getNestedString(str, tag, tag);
    }

    public static String getNestedString(String str, String open, String close) {
        if (str == null) {
            return null;
        } else {
            int start = str.indexOf(open);
            if (start != -1) {
                int end = str.indexOf(close, start + open.length());
                if (end != -1) {
                    return str.substring(start + open.length(), end);
                }
            }

            return null;
        }
    }

    public static int countMatches(String str, String sub) {
        if (sub.equals("")) {
            return 0;
        } else if (str == null) {
            return 0;
        } else {
            int count = 0;

            for (int idx = 0; (idx = str.indexOf(sub, idx)) != -1; idx += sub.length()) {
                ++count;
            }

            return count;
        }
    }

    public static boolean isAlpha(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isLetter(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isWhitespace(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isAlphaSpace(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isLetter(str.charAt(i)) && str.charAt(i) != ' ') {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isAlphanumeric(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isLetterOrDigit(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isAlphanumericSpace(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isLetterOrDigit(str.charAt(i)) && str.charAt(i) != ' ') {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isNumericSpace(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != ' ') {
                    return false;
                }
            }

            return true;
        }
    }

    public static String defaultString(Object obj) {
        return defaultString(obj, "");
    }

    public static String defaultString(Object obj, String defaultString) {
        return obj == null ? defaultString : obj.toString();
    }

    public static String reverse(String str) {
        return str == null ? null : (new StringBuilder(str)).reverse().toString();
    }

    public static String reverseDelimitedString(String str, String delimiter) {
        String[] strs = split(str, delimiter);
        reverseArray(strs);
        return join((Object[]) strs, delimiter);
    }

    private static void reverseArray(Object[] array) {
        int i = 0;

        for (int j = array.length - 1; j > i; ++i) {
            Object tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            --j;
        }

    }

    public static String abbreviate(String s, int maxWidth) {
        return abbreviate(s, 0, maxWidth);
    }

    public static String abbreviate(String s, int offset, int maxWidth) {
        if (maxWidth < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        } else if (s.length() <= maxWidth) {
            return s;
        } else {
            if (offset > s.length()) {
                offset = s.length();
            }

            if (s.length() - offset < maxWidth - 3) {
                offset = s.length() - (maxWidth - 3);
            }

            if (offset <= 4) {
                return s.substring(0, maxWidth - 3) + "...";
            } else if (maxWidth < 7) {
                throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
            } else {
                return offset + (maxWidth - 3) < s.length() ? "..." + abbreviate(s.substring(offset), maxWidth - 3) : "..." + s.substring(s.length() - (maxWidth - 3));
            }
        }
    }

    public static String difference(String s1, String s2) {
        int at = differenceAt(s1, s2);
        return at == -1 ? "" : s2.substring(at);
    }

    public static int differenceAt(String s1, String s2) {
        int i;
        for (i = 0; i < s1.length() && i < s2.length() && s1.charAt(i) == s2.charAt(i); ++i) {
            ;
        }

        return i >= s2.length() && i >= s1.length() ? -1 : i;
    }

    public static String interpolate(String text, Map namespace) {
        Iterator keys = namespace.keySet().iterator();

        while (keys.hasNext()) {
            String key = keys.next().toString();
            Object obj = namespace.get(key);
            if (obj == null) {
                throw new NullPointerException("The value of the key '" + key + "' is null.");
            }

            String value = obj.toString();
            text = replace(text, "${" + key + "}", value);
            if (!key.contains(" ")) {
                text = replace(text, "$" + key, value);
            }
        }

        return text;
    }

    public static String removeAndHump(String data, String replaceThis) {
        StringBuilder out = new StringBuilder();
        StringTokenizer st = new StringTokenizer(data, replaceThis);

        while (st.hasMoreTokens()) {
            String element = (String) st.nextElement();
            out.append(capitalizeFirstLetter(element));
        }

        return out.toString();
    }

    /**
     * 转换首字母为大写
     * capitalize first letter
     * <p>
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param data
     * @return
     */
    public static String capitalizeFirstLetter(String data) {
        char firstLetter = Character.toTitleCase(data.substring(0, 1).charAt(0));
        String restLetters = data.substring(1);
        return firstLetter + restLetters;
    }
//    public static String capitalizeFirstLetter(String data) {
//        if (isEmpty(data)) {
//            return data;
//        } else {
//            char var1 = data.charAt(0);
//            return Character.isLetter(var1) && !Character.isUpperCase(var1) ? (new StringBuilder(data.length())).append(Character.toUpperCase(var1)).append(data.substring(1)).toString() : data;
//        }
//    }


    public static String lowercaseFirstLetter(String data) {
        char firstLetter = Character.toLowerCase(data.substring(0, 1).charAt(0));
        String restLetters = data.substring(1);
        return firstLetter + restLetters;
    }

    public static String addAndDeHump(String input) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.length(); ++i) {
            if (i != 0 && Character.isUpperCase(input.charAt(i))) {
                sb.append('-');
            }

            sb.append(input.charAt(i));
        }

        return sb.toString().trim().toLowerCase(Locale.ENGLISH);
    }

    public static String quoteAndEscape(String source, char quoteChar) {
        return quoteAndEscape(source, quoteChar, new char[]{quoteChar}, new char[]{' '}, '\\', false);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] quotingTriggers) {
        return quoteAndEscape(source, quoteChar, new char[]{quoteChar}, quotingTriggers, '\\', false);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] escapedChars, char escapeChar, boolean force) {
        return quoteAndEscape(source, quoteChar, escapedChars, new char[]{' '}, escapeChar, force);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] escapedChars, char[] quotingTriggers, char escapeChar, boolean force) {
        if (source == null) {
            return null;
        } else if (!force && source.startsWith(Character.toString(quoteChar)) && source.endsWith(Character.toString(quoteChar))) {
            return source;
        } else {
            String escaped = escape(source, escapedChars, escapeChar);
            boolean quote = false;
            if (force) {
                quote = true;
            } else if (!escaped.equals(source)) {
                quote = true;
            } else {
                char[] var8 = quotingTriggers;
                int var9 = quotingTriggers.length;

                for (int var10 = 0; var10 < var9; ++var10) {
                    char quotingTrigger = var8[var10];
                    if (escaped.indexOf(quotingTrigger) > -1) {
                        quote = true;
                        break;
                    }
                }
            }

            return quote ? quoteChar + escaped + quoteChar : escaped;
        }
    }

    public static String escape(String source, char[] escapedChars, char escapeChar) {
        if (source == null) {
            return null;
        } else {
            char[] eqc = new char[escapedChars.length];
            System.arraycopy(escapedChars, 0, eqc, 0, escapedChars.length);
            Arrays.sort(eqc);
            StringBuilder buffer = new StringBuilder(source.length());
            int escapeCount = 0;

            for (int i = 0; i < source.length(); ++i) {
                char c = source.charAt(i);
                int result = Arrays.binarySearch(eqc, c);
                if (result > -1) {
                    buffer.append(escapeChar);
                    ++escapeCount;
                }

                buffer.append(c);
            }

            return buffer.toString();
        }
    }

    public static String removeDuplicateWhitespace(String s) {
        StringBuilder result = new StringBuilder();
        int length = s.length();
        boolean isPreviousWhiteSpace = false;

        for (int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            boolean thisCharWhiteSpace = Character.isWhitespace(c);
            if (!isPreviousWhiteSpace || !thisCharWhiteSpace) {
                result.append(c);
            }

            isPreviousWhiteSpace = thisCharWhiteSpace;
        }

        return result.toString();
    }

    public static String unifyLineSeparators(String s) {
        return unifyLineSeparators(s, System.getProperty("line.separator"));
    }

    public static String unifyLineSeparators(String s, String ls) {
        if (s == null) {
            return null;
        } else {
            if (ls == null) {
                ls = System.getProperty("line.separator");
            }

            if (!ls.equals("\n") && !ls.equals("\r") && !ls.equals("\r\n")) {
                throw new IllegalArgumentException("Requested line separator is invalid.");
            } else {
                int length = s.length();
                StringBuilder buffer = new StringBuilder(length);

                for (int i = 0; i < length; ++i) {
                    if (s.charAt(i) == '\r') {
                        if (i + 1 < length && s.charAt(i + 1) == '\n') {
                            ++i;
                        }

                        buffer.append(ls);
                    } else if (s.charAt(i) == '\n') {
                        buffer.append(ls);
                    } else {
                        buffer.append(s.charAt(i));
                    }
                }

                return buffer.toString();
            }
        }
    }

    public static boolean contains(String str, char searchChar) {
        if (isEmpty(str)) {
            return false;
        } else {
            return str.indexOf(searchChar) >= 0;
        }
    }

    public static boolean contains(String str, String searchStr) {
        return str != null && searchStr != null ? str.contains(searchStr) : false;
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isMobileNo(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static String getNullValue(String s) {
        return isEmpty(s) ? "" : s;
    }

    public static int getStringTrueLenght(String inputStr) {
        int orignLen = inputStr.length();
        int resultLen = 0;
        String temp = null;

        for (int i = 0; i < orignLen; ++i) {
            temp = inputStr.substring(i, i + 1);

            try {
                if (temp.getBytes("utf-8").length == 3) {
                    resultLen += 2;
                } else {
                    ++resultLen;
                }
            } catch (UnsupportedEncodingException var6) {
                var6.printStackTrace();
            }
        }

        return resultLen;
    }

    public static boolean containsEmoji(String str) {
        int len = str.length();

        for (int i = 0; i < len; ++i) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmojiCharacter(char codePoint) {
        return codePoint != 0 && codePoint != '\t' && codePoint != '\n' && codePoint != '\r' && (codePoint < ' ' || codePoint > '\ud7ff') && (codePoint < '\ue000' || codePoint > '�') && (codePoint < 65536 || codePoint > 1114111);
    }

    public static boolean isChineseInput(String inputStr) {
        int orignLen = inputStr.length();
        String temp = null;

        for (int i = 0; i < orignLen; ++i) {
            temp = inputStr.substring(i, i + 1);

            try {
                if (temp.getBytes("utf-8").length == 3) {
                    return true;
                }
            } catch (UnsupportedEncodingException var5) {
                var5.printStackTrace();
            }
        }

        return false;
    }

    public static boolean isTherLetter(char firstChar) {
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(firstChar + "");
        return m.matches();
    }

    public static String stringToMd5(String s) {
        byte[] value = s.getBytes();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5Utils");
            md.update(value);
            byte[] temp = md.digest();
            StringBuilder sb = new StringBuilder();
            byte[] var5 = temp;
            int var6 = temp.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                byte b = var5[var7];
                sb.append(Integer.toHexString(b & 255));
            }

            String md5Version = sb.toString();
            return md5Version;
        } catch (NoSuchAlgorithmException var9) {
            var9.printStackTrace();
            return null;
        }
    }
}

