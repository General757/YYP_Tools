//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools;

/**
 * Created by generalYan on 2019/10/21.
 */
public class HanziInitial {
    private static int BEGIN = 45217;
    private static int END = 63486;
    private static char[] chartable = new char[]{'啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝'};
    private static int[] table = new int[27];
    private static char[] initialtable = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 't', 't', 'w', 'x', 'y', 'z'};

    public HanziInitial() {
    }

    public static String cn2py(String SourceStr) {
        String Result = "";
        int StrLength = SourceStr.length();

        try {
            for (int i = 0; i < StrLength; ++i) {
                Result = Result + Char2Initial(SourceStr.charAt(i));
            }
        } catch (Exception var5) {
            Result = "";
            var5.printStackTrace();
        }

        return Result;
    }

    private static char Char2Initial(char ch) {
        if (ch >= 'a' && ch <= 'z') {
            return (char) (ch - 97 + 65);
        } else if (ch >= 'A' && ch <= 'Z') {
            return ch;
        } else {
            int gb = gbValue(ch);
            if (gb >= BEGIN && gb <= END) {
                int i;
                for (i = 0; i < 26 && (gb < table[i] || gb >= table[i + 1]); ++i) {
                    ;
                }

                if (gb == END) {
                    i = 25;
                }

                return initialtable[i];
            } else {
                return ch;
            }
        }
    }

    private static int gbValue(char ch) {
        String str = new String();
        str = str + ch;

        try {
            byte[] bytes = str.getBytes("GB2312");
            return bytes.length < 2 ? 0 : (bytes[0] << 8 & '\uff00') + (bytes[1] & 255);
        } catch (Exception var3) {
            return 0;
        }
    }

    static {
        for (int i = 0; i < 26; ++i) {
            table[i] = gbValue(chartable[i]);
        }

        table[26] = END;
    }
}

