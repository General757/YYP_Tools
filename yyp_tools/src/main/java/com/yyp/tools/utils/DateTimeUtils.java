package com.yyp.tools.utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;

import com.yyp.tools.R;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by generalYan on 2019/1/12.
 * 日期时间相关的工具类
 */

public class DateTimeUtils {
    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static final String FORMAT_SHORT = "yyyy-MM-dd";
    /**
     * 英文全称 如：2010-12-01 23:15:06
     */
    public static final String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    /**
     * 精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * 中文简写 如：2010年12月01日
     */
    public static final String FORMAT_CN_SHORTER = "MM月dd日";
    /**
     * 中文简写 如：2010年12月01日
     */
    public static final String FORMAT_CN_SHORT = "yyyy年MM月dd日";
    /**
     * 中文全称 如：2010年12月01日 23时15分06秒
     */
    public static final String FORMAT_CN_LONG = "yyyy年MM月dd日  HH时mm分ss秒";
    /**
     * 精确到毫秒的完整中文时间
     */
    public static final String FORMAT_CN_FULL = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";

    private DateTimeUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获得默认的 date pattern
     */
    private static String getDatePattern() {
        return FORMAT_LONG;
    }

    /**
     * 根据预设格式返回当前日期
     *
     * @return
     */
    public static String getNow() {
        return format(new Date());
    }

    /**
     * 根据用户格式返回当前日期
     *
     * @param format
     * @return
     */
    public static String getNow(String format) {
        return format(new Date(), format);
    }

    /**
     * 使用预设格式格式化日期
     *
     * @param date
     * @return
     */
    private static String format(Date date) {
        return format(date, getDatePattern());
    }

    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    public static String dtFormat(Date date, String dateFormat) {
        return getFormat(dateFormat).format(date);
    }

    private static SimpleDateFormat getFormat(String format) {
        return new SimpleDateFormat(format);
    }

    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, getFormat(FORMAT_FULL));
    }

    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */
    private static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.CHINA);
            returnValue = df.format(date);
        }
        return returnValue;
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return
     */
    private static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    private static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.CHINA);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     * @return
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     * @return
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    /**
     * 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL, Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 获取日期年份
     *
     * @param date 日期
     * @return
     */
    public static String getYear(Date date) {
        return format(date).substring(0, 4);
    }

    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户给的时间戳获取预设格式的时间
     *
     * @param date    时间戳
     * @param pattern 预设时间格式
     * @return
     */
    public static String getDate(String date, String pattern) {
        Date dates = new Date();
        dates.setTime(Long.parseLong(date));
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(dates);
    }

    /**
     * 格式化时间 判断一个日期 是否为 今天、昨天
     *
     * @param time
     * @return
     */
    public static String formatDateTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (time == null || "".equals(time)) {
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today)) {
            return "今天 " + time.split(" ")[1];
        } else if (current.before(today) && current.after(yesterday)) {

            return "昨天 " + time.split(" ")[1];
        } else {
//            int index = time.indexOf("-")+1;
//            return time.substring(index, time.length());
            int index = time.indexOf(" ");
            return time.substring(0, index);
        }
    }

    /**
     * 将UTC-0时区时间字符串转换成用户时区时间的描述.
     *
     * @param strUtcTime UTC-0时区的时间
     * @param strInFmt   时间的输入格式
     * @param strOutFmt  时间的输出格式，若为null则输出格式与输入格式相同
     * @return 用户时区的时间描述.
     * @throws ParseException 时间转换异常
     */
    public static String getUserZoneString(final String strUtcTime, final String strInFmt, final String strOutFmt)
            throws ParseException {
        if (StringUtils.isNull(strUtcTime)) {
            throw new NullPointerException("参数strDate不能为空");
        } else if (StringUtils.isNull(strInFmt)) {
            throw new NullPointerException("参数strInFmt不能为空");
        }
        long lUserMillis = getUserZoneMillis(strUtcTime, strInFmt);
        String strFmt = strInFmt;
        if (!StringUtils.isNull(strOutFmt)) {
            strFmt = strOutFmt;
        }
        return format(lUserMillis, strFmt);
    }

    /**
     * 格式化时间.
     *
     * @param lMillis  时间参数
     * @param strInFmt 时间格式
     * @return 对应的时间字符串
     */
    public static String format(final long lMillis, final String strInFmt) {
        if (StringUtils.isNull(strInFmt)) {
            throw new NullPointerException("参数strInFmt不能为空");
        }
        return (String) DateFormat.format(strInFmt, lMillis);
    }

    /**
     * 将UTC-0时区时间字符串转换成用户时区时间距离1970-01-01的毫秒数.
     *
     * @param strUtcTime UTC-0时区的时间字符串
     * @param strInFmt   时间格式
     * @return 用户时区时间距离1970-01-01的毫秒数.
     * @throws ParseException 时间转换异常
     */
    @SuppressWarnings("deprecation")
    public static long getUserZoneMillis(final String strUtcTime, final String strInFmt) throws ParseException {
        if (StringUtils.isNull(strUtcTime)) {
            throw new NullPointerException("参数strUtcTime不能为空");
        } else if (StringUtils.isNull(strInFmt)) {
            throw new NullPointerException("参数strInFmt不能为空");
        }
        long lUtcMillis = parseMillis(strUtcTime, strInFmt);
        Time time = new Time();
        time.setToNow();
        long lOffset = time.gmtoff * DateUtils.SECOND_IN_MILLIS;
        long lUserZoneMillis = lUtcMillis + lOffset;
        return lUserZoneMillis;
    }

    /**
     * 转换时间格式，将字符串转换为距离1970-01-01的毫秒数.
     *
     * @param strDate  指定时间的字符串
     * @param strInFmt 时间字符串的格式
     * @return 指定时间字符串距离1970-01-01的毫秒数
     * @throws ParseException 时间转换异常
     */
    public static long parseMillis(final String strDate, final String strInFmt) throws ParseException {
        if (StringUtils.isNull(strDate)) {
            throw new NullPointerException("参数strDate不能为空");
        } else if (StringUtils.isNull(strInFmt)) {
            throw new NullPointerException("参数strInFmt不能为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(strInFmt,
                Locale.getDefault());
        Date date = sdf.parse(strDate);
        return date.getTime();
    }

    public static String utc2BeiJingTime(String message) {
        String beiJingTime = message;
        if (message.contains("#")) {
            String[] loginInfo = message.split("#");
            if (loginInfo != null && loginInfo.length >= 3) {
                try {
                    String utcTime = loginInfo[1];
                    beiJingTime = DateTimeUtils.getUserZoneString(utcTime, "HH:mm", null);
                    String repaceTimeStr = "#" + utcTime + "#";
                    beiJingTime = message.replace(repaceTimeStr, beiJingTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return beiJingTime;
    }

    /**
     * 格式化时间
     *
     * @param createTime
     * @return
     */
    private static final int seconds_of_1minute = 60;//设置每个阶段时间
    private static final int seconds_of_30minutes = 30 * 60;
    private static final int seconds_of_1hour = 60 * 60;
    private static final int seconds_of_1day = 24 * 60 * 60;
    private static final int seconds_of_2days = seconds_of_1day * 2;
    private static final int seconds_of_15days = seconds_of_1day * 15;
    private static final int seconds_of_30days = seconds_of_1day * 30;
    private static final int seconds_of_6months = seconds_of_30days * 6;
    private static final int seconds_of_1year = seconds_of_30days * 12;

    public static String getTimeFormatRange(Context mContext, String createTime) {
        if (TextUtils.isEmpty(createTime))
            return "";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            dateFormat.setTimeZone(timeZone);

            Date currentDate = new Date(System.currentTimeMillis());//获取当前时间
            Date createDate = null;//获取当前时间
            //将时间转化成Date
            currentDate = dateFormat.parse(dateFormat.format(currentDate), new ParsePosition(0));
            createDate = dateFormat.parse(createTime, new ParsePosition(0));

            long between = (currentDate.getTime() - createDate.getTime()) / 1000;//除以1000是为了转换成秒
            int elapsedTime = (int) (between);
            if (elapsedTime < seconds_of_1minute)
//                return "刚刚";
                return mContext.getString(R.string.date_format_just);

//            if (elapsedTime < seconds_of_30minutes)
//                return elapsedTime / seconds_of_1minute + "分钟前";
            if (elapsedTime < seconds_of_1hour)
//                return "半小时前";
//                return elapsedTime / seconds_of_1minute + "分钟前";
                return mContext.getString(R.string.date_format_minutes, String.valueOf(elapsedTime / seconds_of_1minute));

            if (elapsedTime < seconds_of_1day)
//                return elapsedTime / seconds_of_1hour + "小时前";
                return mContext.getString(R.string.date_format_hour, String.valueOf(elapsedTime / seconds_of_1hour));

            if (elapsedTime < seconds_of_2days)
//                return "昨天";
                return mContext.getString(R.string.date_format_yesterday);

//            if (elapsedTime < seconds_of_15days)
//                return elapsedTime / seconds_of_1day + "天前";
//            if (elapsedTime < seconds_of_30days)
//                return "半个月前";
//            if (elapsedTime < seconds_of_6months)
//                return elapsedTime / seconds_of_30days + "月前";

            if (elapsedTime < seconds_of_1year) {
//                return "半年前";
//                SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
                SimpleDateFormat formatter = new SimpleDateFormat(mContext.getString(R.string.date_format_day));
                return formatter.format(createDate);
            }
            if (elapsedTime >= seconds_of_1year) {
//                return elapsedTime / seconds_of_1year + "年前";
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
                SimpleDateFormat formatter = new SimpleDateFormat(mContext.getString(R.string.date_format_year));
                return formatter.format(createDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateFormat(Context mContext, String createTime) {
        if (TextUtils.isEmpty(createTime))
            return "";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            dateFormat.setTimeZone(timeZone);

            Date currentDate = new Date(System.currentTimeMillis());//获取当前时间
            Date createDate = null;//获取当前时间
            //将时间转化成Date
            currentDate = dateFormat.parse(dateFormat.format(currentDate), new ParsePosition(0));
            createDate = dateFormat.parse(createTime, new ParsePosition(0));

            long between = (currentDate.getTime() - createDate.getTime()) / 1000;//除以1000是为了转换成秒
            int elapsedTime = (int) (between);

            if (elapsedTime < seconds_of_1year) {
                SimpleDateFormat formatter = new SimpleDateFormat(mContext.getString(R.string.date_format_day));
                return formatter.format(createDate);
            }
            if (elapsedTime >= seconds_of_1year) {
                SimpleDateFormat formatter = new SimpleDateFormat(mContext.getString(R.string.date_format_year));
                return formatter.format(createDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long day_2 = 2 * day;// 2天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 返回文字描述的日期
     *
     * @param createTime
     * @return
     */
    public static String getTimeFormatRange1(String createTime) {
        if (TextUtils.isEmpty(createTime))
            return "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        dateFormat.setTimeZone(timeZone);

//        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        Date curDate = new Date();//获取当前时间
        Date startTime = null;//获取当前时间
        //将时间转化成Date
//        try {
//            curDate = dateFormat.parse(dateFormat.format(curDate));
        startTime = dateFormat.parse(createTime, new ParsePosition(0));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        long diff = curDate.getTime() - startTime.getTime();
        /**除以1000是为了转换成秒*/
//        long between = (curDate.getTime() - startTime.getTime()) / 1000;
        long r = 0;
        if (diff > year) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            return formatter.format(startTime);
        }
        if (diff > day_2 && diff < year) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
            return formatter.format(startTime);
        }
        if (diff > day && diff < day_2) {
            return "昨天";
        }
        if (diff > hour && diff < day) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute && diff < hour) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    //格式化日期
    public static Date stringToDate(String dateString) {
        try {
            ParsePosition position = new ParsePosition(0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateValue = simpleDateFormat.parse(dateString, position);
            return dateValue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getCurrentTime(long time) {
        Date nowTime = new Date(time);
        SimpleDateFormat sdFormatter = new SimpleDateFormat("HH:mm:dd SSS");
        return sdFormatter.format(nowTime);
    }

    /**
     * 时间格式化
     */
    public static String formattedTime(long second) {
        String hs, ms, ss, dd, formatTime;

        long h, m, s, d;
        h = second / 3600;
        m = (second % 3600) / 60;
        s = (second % 3600) % 60;
        d = (second % 3600) % 60;

        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }

        if (s < 10) {
            dd = "0" + s;
        } else {
            dd = "" + s;
        }

        // if (hs.equals("00")) {
        // formatTime = ms + ":" + ss;
        // } else {
        formatTime = hs + ":" + ms + ":" + ss + ":" + dd;
        // }

        return formatTime;
    }

    /**
     * 时间格式化
     */
    public static String formatValueTime(long second) {
        String hs, ms, ss, formatTime;

        long h, m, s;
        h = second / 3600;
        m = (second % 3600) / 60;
        s = (second % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        // if (hs.equals("00")) {
        // formatTime = ms + ":" + ss;
        // } else {
        formatTime = hs + ":" + ms + ":" + ss;
        // }

        return formatTime;
    }

    public static String formatStringTime(long second) {
        String hs, ms, ss, formatTime;

        long h, m, s;
        h = second / 3600;
        m = (second % 3600) / 60;
        s = (second % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        // if (hs.equals("00")) {
        // formatTime = ms + ":" + ss;
        // } else {
        formatTime = hs + "时" + ms + "分" + ss + "秒";
        // }

        return formatTime;
    }


    public static String formatHMSTime(long second) {
        if (second <= 0)
            return "0:00";

        String hs, ms, ss;

        long h, m, s;
        h = second / 3600;
        m = (second % 3600) / 60;
        s = (second % 3600) % 60;

        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }

        return ms + ":" + ss;
    }

    /*
     * 毫秒转化时分秒毫秒
     */
    public static String formatDateMil(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天 ");
        }
        if (hour > 0) {
            sb.append(hour + "小时 ");
        }
        if (minute > 0) {
            sb.append(minute + "分 ");
        }
        if (second > 0) {
            sb.append(second + "秒 ");
        }
        if (milliSecond > 0) {
            sb.append(milliSecond + "毫秒");
        }
        return sb.toString();
    }

    /*
     * 毫秒转化时分秒
     */
    public static String formatDateSec(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天 ");
        }
        if (hour > 0) {
            sb.append(hour + "小时 ");
        }
        if (minute > 0) {
            sb.append(minute + "分 ");
        }
        if (second > 0) {
            sb.append(second + "秒 ");
        }

        return sb.toString();
    }

    /*
     * 毫秒转化时分秒毫秒
     */
    public static String formatDateMin(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0)
            sb.append(day + "天");
        if (hour > 0)
            sb.append(hour + "小时");
        if (minute > 0)
            sb.append(minute + "分");

        return sb.toString();
    }

    /*
     * 毫秒转化时分秒毫秒
     */
    public static String formatDateMaxMin(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        minute = second > 0 ? minute + 1 : minute;

        StringBuffer sb = new StringBuffer();
        if (day > 0)
            sb.append(day + "天");
        if (hour > 0)
            sb.append(hour + "小时");
        if (minute > 0)
            sb.append(minute + "分");

        return sb.toString();
    }

    public String stringForTime(int timeMs) {

        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    //  格式化时间
    public static String formatTime(int time) {
        int allsecond = time / 1000;
        String minute = allsecond / 60 + "";
        if (minute.length() < 2) {
            minute = "0" + minute;
        }

        String second = allsecond % 60 + "";
        if (second.length() < 2) {
            second = "0" + second;
        }

        return minute + ":" + second;
    }

    /**
     * 时间格式化
     */
    public static String formatAutoHMSTime(long second) {
        String hs, ms, ss, formatTime;

        long h, m, s;
        h = second / 3600;
        m = (second % 3600) / 60;
        s = (second % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }

        if (hs.equals("00")) {
            formatTime = ms + ":" + ss;
        } else {
            formatTime = hs + ":" + ms + ":" + ss;
        }

        return formatTime;
    }

    /**
     * 时间格式化（音频用2.1.2）
     *
     * @param second
     */
    public static String formattTime(int second) {
        String hs, ms, ss, formatTime;

        long h, m, s;
        h = second / 3600;
        m = (second % 3600) / 60;
        s = (second % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        // if (hs.equals("00")) {
        // formatTime = ms + ":" + ss;
        // } else {
        formatTime = ms + ":" + ss;
        // }

        return formatTime;
    }

    // 格式化时间转成毫秒
    public static int parseString(String time) {
        try {
            String[] strs = time.split(":");
            int minute = Integer.parseInt(strs[0]);
            int second = Integer.parseInt(strs[1]);
            return (minute * 60 + second) * 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 获取时间长度 PHP得到的时间戳是10位 Java/Android得到的时间戳是13位 先得到的是按秒计算时间累加数--腾讯时间计算式
     */
    public static int getTimeLong(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        String timeString = sdr.format(time);
        String[] fenge = timeString.split("[年月日时分秒]");

        int times = 0;
        times = times + Integer.parseInt(fenge[5]) + Integer.parseInt(fenge[4]) * 60 + Integer.parseInt(fenge[3]) * 60 * 60 + Integer.parseInt(fenge[2]) * 60 * 60 * 24;
        return times;
    }

    public boolean getFirstSubtractEndTimes(long firstTimes, long endTine) {
        return firstTimes > endTine;
    }

    public static long getSystemTimeDifference(long startTime, long endTime) {//时间差
        return (endTime - startTime) / 1000;
    }

    //日期差
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 == year2) {
            LogUtils.i("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        } else {
            int timeDistance = 0;

            for (int i = year1; i < year2; ++i) {
                if ((i % 4 != 0 || i % 100 == 0) && i % 400 != 0) {
                    timeDistance += 365;
                } else {
                    timeDistance += 366;
                }
            }

            return timeDistance + (day2 - day1);
        }
    }

    public static String dateToString(Date date, String pattern) throws Exception {
        return (new SimpleDateFormat(pattern)).format(date);
    }

    public static Date stringToDate(String dateStr, String pattern) throws Exception {
        return (new SimpleDateFormat(pattern)).parse(dateStr);
    }

    public static String formatDate(Date date, String type) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(type);
            return df.format(date);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static Date parseDate(String dateStr, String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        Date date = null;

        try {
            date = df.parse(dateStr);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return date;
    }

    public static int getDateYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static int getDateMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    public static int getDateDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static String translateDate(Long time) {
        long oneDay = 86400000L;
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayStartTime = today.getTimeInMillis();
        if (time >= todayStartTime && time < todayStartTime + oneDay) {
            return "今天";
        } else if (time >= todayStartTime - oneDay && time < todayStartTime) {
            return "昨天";
        } else if (time >= todayStartTime - oneDay * 2L && time < todayStartTime - oneDay) {
            return "前天";
        } else if (time > todayStartTime + oneDay) {
            return "将来某一天";
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(time);
            return dateFormat.format(date);
        }
    }

    public static String[] translateChatDate(Date paramDate) {
        String[] str = new String[2];
        long l = paramDate.getTime();
        Calendar localCalendar = GregorianCalendar.getInstance();
        localCalendar.setTime(paramDate);
        int year = localCalendar.get(Calendar.YEAR);
        String hhmm = (new SimpleDateFormat("HH:mm", Locale.CHINA)).format(paramDate);
        if (!isSameYear(year)) {
            String paramDate2str = (new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA)).format(paramDate);
            str[0] = paramDate2str;
            str[1] = hhmm;
            return str;
        } else {
            if (isSameDay(l)) {
                str[0] = "今天";
            } else if (isYesterday(l)) {
                str[0] = "昨天";
            } else if (isBeforeYesterday(l)) {
                str[0] = "前天";
            } else {
                str[0] = (new SimpleDateFormat("M.d", Locale.CHINA)).format(paramDate);
            }

            str[1] = hhmm;
            return str;
        }
    }

    private static TimeInfo getTodayStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.set(Calendar.HOUR_OF_DAY, 0);
        localCalendar1.set(Calendar.MINUTE, 0);
        localCalendar1.set(Calendar.SECOND, 0);
        localCalendar1.set(Calendar.MILLISECOND, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.set(Calendar.HOUR_OF_DAY, 23);
        localCalendar2.set(Calendar.MINUTE, 59);
        localCalendar2.set(Calendar.SECOND, 59);
        localCalendar2.set(Calendar.MILLISECOND, 999);
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    private static TimeInfo getYesterdayStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.add(Calendar.DAY_OF_MONTH, -1);
        localCalendar1.set(Calendar.HOUR_OF_DAY, 0);
        localCalendar1.set(Calendar.MINUTE, 0);
        localCalendar1.set(Calendar.SECOND, 0);
        localCalendar1.set(Calendar.MILLISECOND, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.add(Calendar.DAY_OF_MONTH, -1);
        localCalendar2.set(Calendar.HOUR_OF_DAY, 23);
        localCalendar2.set(Calendar.MINUTE, 59);
        localCalendar2.set(Calendar.SECOND, 59);
        localCalendar2.set(Calendar.MILLISECOND, 999);
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    private static TimeInfo getBeforeYesterdayStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.add(Calendar.DAY_OF_MONTH, -2);
        localCalendar1.set(Calendar.HOUR_OF_DAY, 0);
        localCalendar1.set(Calendar.MINUTE, 0);
        localCalendar1.set(Calendar.SECOND, 0);
        localCalendar1.set(Calendar.MILLISECOND, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.add(Calendar.DAY_OF_MONTH, -2);
        localCalendar2.set(Calendar.HOUR_OF_DAY, 23);
        localCalendar2.set(Calendar.MINUTE, 59);
        localCalendar2.set(Calendar.SECOND, 59);
        localCalendar2.set(Calendar.MILLISECOND, 999);
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    private static boolean isSameDay(long paramLong) {
        TimeInfo localTimeInfo = getTodayStartAndEndTime();
        return paramLong > localTimeInfo.getStartTime() && paramLong < localTimeInfo.getEndTime();
    }

    private static boolean isYesterday(long paramLong) {
        TimeInfo localTimeInfo = getYesterdayStartAndEndTime();
        return paramLong > localTimeInfo.getStartTime() && paramLong < localTimeInfo.getEndTime();
    }

    private static boolean isBeforeYesterday(long paramLong) {
        TimeInfo localTimeInfo = getBeforeYesterdayStartAndEndTime();
        return paramLong > localTimeInfo.getStartTime() && paramLong < localTimeInfo.getEndTime();
    }

    private static boolean isSameYear(int year) {
        Calendar cal = Calendar.getInstance();
        int CurYear = cal.get(Calendar.YEAR);
        return CurYear == year;
    }

    public static boolean isToday(Long time) {
        long oneDay = 86400000L;
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayStartTime = today.getTimeInMillis();
        return time >= todayStartTime && time < todayStartTime + oneDay;
    }

    private static class TimeInfo {
        private long startTime;
        private long endTime;

        private TimeInfo() {
        }

        public long getStartTime() {
            return this.startTime;
        }

        public void setStartTime(long paramLong) {
            this.startTime = paramLong;
        }

        public long getEndTime() {
            return this.endTime;
        }

        public void setEndTime(long paramLong) {
            this.endTime = paramLong;
        }
    }
}
