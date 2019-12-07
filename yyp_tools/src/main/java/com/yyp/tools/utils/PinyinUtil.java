//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by generalYan on 2019/10/21.
 * 拼音字母处理工具
 */
public class PinyinUtil {
    public PinyinUtil() {
    }

    public static final String getPinYinHeadChar(String str) {
        String convert = "";

        for (int j = 0; j < str.length(); ++j) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert = convert + pinyinArray[0].charAt(0);
            } else {
                convert = convert + word;
            }
        }

        return convert.toUpperCase();
    }

    public static final String getPinYin(String str) {
        String convert = "";
        HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        for (int j = 0; j < str.length(); ++j) {
            char word = str.charAt(j);
            String[] pinyinArray = null;

            try {
                pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word, hanyuPinyinOutputFormat);
                if (pinyinArray != null && pinyinArray.length != 0) {
                    convert = convert + pinyinArray[0];
                } else {
                    convert = convert + word;
                }
            } catch (BadHanyuPinyinOutputFormatCombination var7) {
                var7.printStackTrace();
            }
        }

        return convert.toUpperCase();
    }
}

