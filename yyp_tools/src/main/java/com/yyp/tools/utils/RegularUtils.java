package com.yyp.tools.utils;

import android.net.ParseException;
import android.text.TextUtils;

import com.yyp.tools.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by generalYan on 2019/9/17.
 * 正则工具
 */
public class RegularUtils {

    /**
     * 判断字符串是否是数字
     *
     * @param src
     * @return boolean
     */
    public static boolean isNumeric(String src) {
        if (TextUtils.isEmpty(src))
            return false;

        Pattern numericPattern = Pattern.compile("^[0-9\\-]+$");
        boolean return_value = false;
        Matcher m = numericPattern.matcher(src);
        if (m.find())
            return_value = true;

        return return_value;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
//    private static boolean isNumeric(String str) {
//        Pattern pattern = Pattern.compile("[0-9]*");
//        Matcher isNum = pattern.matcher(str);
//        if (isNum.matches()) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    /**
     * 只允许字母、数字和汉字
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母、数字和汉字
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static boolean isUserName(String data) {
        return length(data) <= 20;
    }

    public static boolean isPassword(String data) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+~ ]{6,20}$");
        Matcher matcher = pattern.matcher(data);
        return matcher.matches();
    }

    public static boolean isPassword_input(String data) {
        Pattern pattern = Pattern.compile("^[\\w!@#$%^&*()_+~ ]+$");
        Matcher matcher = pattern.matcher(data);
        return matcher.matches();
    }

    /* 手机号正则表达式 */
    public static boolean isMobileNumber(String mobiles) {
        if (StringUtils.isEmpty(mobiles))
            return false;

        Pattern pattern = Pattern.compile("^1[0-9][0-9]\\d{8}$");
        Matcher matcher = pattern.matcher(mobiles);
        return matcher.matches();
    }

    /**
     * 判断邮箱格式是否正确
     */
    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isMobileNumber2(String data) {
        if (StringUtils.isEmpty(data)) {
            return false;
        } else {
            String expr = "^((1[135678][0-9])|(14[5678])|(19[89]))\\d{8}$";
            return data.matches(expr);
        }
    }

    public static boolean isNumberLetter(String data) {
        String expr = "^[A-Za-z0-9]+$";
        return data.matches(expr);
    }

    public static boolean isNumber(String data) {
        String expr = "^[0-9]+$";
        return data.matches(expr);
    }

    public static boolean isLetter(String data) {
        String expr = "^[A-Za-z]+$";
        return data.matches(expr);
    }

    public static boolean isChinese(String data) {
        String expr = "^[Α-￥]+$";
        return data.matches(expr);
    }

    public static boolean isContainChinese(String data) {
        String chinese = "[Α-￥]";
        if (StringUtils.isEmpty(data)) {
            for (int i = 0; i < data.length(); ++i) {
                String temp = data.substring(i, i + 1);
                boolean flag = temp.matches(chinese);
                if (flag) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isDianWeiShu(String data, int length) {
        String expr = "^[1-9][0-9]+\\.[0-9]{" + length + "}$";
        return data.matches(expr);
    }

    public static boolean isCard(String data) {
        String expr = "^[0-9]{17}[0-9xX]$";
        return data.matches(expr);
    }

    public static boolean isPostCode(String data) {
        String expr = "^[0-9]{6,10}";
        return data.matches(expr);
    }

    public static boolean isUrl(String data) {
        String expr = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        return data.matches(expr);
    }

    public static int length(String data) {
        if (data == null) {
            return 0;
        } else {
            char[] c = data.toCharArray();
            int len = 0;

            for (int i = 0; i < c.length; ++i) {
                ++len;
                if (!isLetter(c[i])) {
                    ++len;
                }
            }

            return len;
        }
    }

    public static boolean isLetter(char c) {
        int k = 128;
        return c / k == 0;
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     */
    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0 || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 身份证正则
     *
     * @param IDStr
     * @return
     * @throws ParseException
     */
    public static boolean IDCardValidate(String IDStr) throws ParseException, java.text.ParseException {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "X", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为18位。";
            return false;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return false;
        }
        // =======================(end)========================
        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
            errorInfo = "身份证生日不在有效范围。";
            return false;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return false;
        }
        // =====================(end)=====================
        // ================ 地区码时候有效 ================
        // Hashtable h = GetAreaCode();
        // if (h.get(Ai.substring(0, 2)) == null)
        // {
        // errorInfo = "身份证地区编码错误。";
        // return errorInfo;
        // }
        // ==============================================
        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;
        String strVerifyCode1 = ValCodeArr[modValue + 1];
        String Ai1 = Ai.substring(0, 17) + strVerifyCode1;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false && Ai1.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return false;
            }
        } else {
            return true;
        }
        return true;
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        // regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    //提交之前的数据,使表情编程数字数组 [-16, -97, -104, -95]
    public static String filterEmoji(String source) {
        if (!containsEmoji(source))
            return source;// 如果不包含,直接返回

        String string = "";
        StringBuilder buf = null;
        int len = source.length();
        System.out.println("filter running len = " + len);
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (buf == null)
                buf = new StringBuilder(source.length());
            if (!isEmojiCharacter(codePoint))
                string = String.valueOf(codePoint);
            else {
                try {
                    StringBuilder builder = new StringBuilder(2);
                    byte[] str = builder.append(String.valueOf(codePoint)).append(String.valueOf(source.charAt(i + 1))).toString().getBytes("UTF-8");
                    String strin = Arrays.toString(str);
                    String newString = strin.substring(1, strin.length() - 1);
                    string = "Γ" + newString + "Γ";
                    System.out.println("filters running newStr = " + string);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i++;
            }
            buf.append(string + "⅞");
        }
        if (buf == null)
            return "";
        else {
            if (buf.length() == len) {// 这里的意义在于尽可能少的toString,因为会重新生成字符串
                buf = null;
                return source;
            } else {
                System.out.println("filter running buf.toString() = " + buf.toString());
                String bufStr = buf.toString();
                String newBufStr = bufStr.substring(0, bufStr.length() - 1);
                return newBufStr;
            }
        }
    }

    // emoji验证(判别是否包含Emoji表情)
    private static boolean containsEmoji(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(str.charAt(i))) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    //emoji判断
    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }
}
