package com.yyp.tools;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

import com.yyp.tools.frame.toast.ToastTools;
import com.yyp.tools.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YanYan on 2019/9/30.
 * edittext工具
 */
public class EditTools {
    /**
     * 输入框条件限制
     */
    public static void filterCondition(final Context mContext, final EditText editText, final boolean supportEmoji, final boolean supportEmpty, final boolean supportNLine, final boolean chatSpecial) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(InputType.TYPE_CLASS_TEXT) {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Pattern speChat = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (!supportEmoji) {
                    Matcher emojiMatcher = emoji.matcher(source);
                    if (emojiMatcher.find()) {
                        ToastTools.showToastShort(mContext, R.string.edit_unsupport_emoji);
                        return "";
                    }
                }
                if (!chatSpecial) {// 禁止EditText输入特殊字符
                    Matcher speChatMatcher = speChat.matcher(source);
                    if (speChatMatcher.find()) {
                        ToastTools.showToastShort(mContext, R.string.edit_unsupport_special_chat);
                        return "";
                    }
                }
                if (!supportEmpty) {
                    if (source.equals(" ")) {// 禁止输入空格
                        ToastTools.showToastShort(mContext, R.string.edit_unsupport_empty);
                        return "";
                    }
                }
                if (!supportNLine) {
                    if (source.toString().contentEquals("\n")) {// 禁止输入换行
                        ToastTools.showToastShort(mContext, R.string.edit_unsupport_next_line);
                        return "";
                    }
                }
                return source;
            }
        };
        editText.setFilters(filters);
    }

    /**
     * contentEditView可输入最大长度限制检测
     *
     * @param max_length 可输入最大长度
     * @param err_msg    达到可输入最大长度时的提示语-false禁止,true-可输入
     */
    public static void filterCharLength(final Context mContext, final EditText editText, final int max_length, final String err_msg, final boolean supportEmoji, final boolean supportEmpty, final boolean supportNLine, final boolean chatSpecial) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(max_length) {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Pattern speChat = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (!supportEmoji) {
                    Matcher emojiMatcher = emoji.matcher(source);
                    if (emojiMatcher.find()) {
                        ToastTools.showToastShort(mContext, R.string.edit_unsupport_emoji);
                        return "";
                    }
                }
                if (!chatSpecial) {// 禁止EditText输入特殊字符
                    Matcher speChatMatcher = speChat.matcher(source);
                    if (speChatMatcher.find()) {
                        ToastTools.showToastShort(mContext, R.string.edit_unsupport_special_chat);
                        return "";
                    }
                }
                if (!supportEmpty) {
                    if (source.equals(" ")) {// 禁止输入空格
                        ToastTools.showToastShort(mContext, R.string.edit_unsupport_empty);
                        return "";
                    }
                }
                if (!supportNLine) {
                    if (source.toString().contentEquals("\n")) {// 禁止输入换行
                        ToastTools.showToastShort(mContext, R.string.edit_unsupport_next_line);
                        return "";
                    }
                }
                int destLen = StringUtils.getCharacterNum(dest.toString()); // 获取字符个数(一个中文算2个字符)
                int sourceLen = StringUtils.getCharacterNum(source.toString());
                int allLen = (dest.toString() + source.toString()).length();

                if (destLen + sourceLen > max_length) {
                    ToastTools.showToastShort(mContext, err_msg);
                    return "";
                }

                return source;
            }
        };
        editText.setFilters(filters);
    }

    public static void filterEditLength(final Context mContext, final EditText editText, final int maxLength, final boolean supportEmoji, final boolean supportEmpty, final boolean supportNLine, final boolean chatSpecial) {
        filterEditLength(mContext, editText, maxLength, null, supportEmoji, supportEmpty, supportNLine, chatSpecial);
    }

    /**
     * EditText可输入限制检测
     *
     * @param mContext     上下文
     * @param editText     输入控件
     * @param maxLength    可输入最大长度
     * @param errMsg       达到可输入的提示语
     * @param supportEmoji emoji：false禁止,true-可输入
     * @param supportEmpty 空格：false禁止,true-可输入
     * @param supportNLine 换行：false禁止,true-可输入
     * @param chatSpecial  特殊字符：false禁止,true-可输入
     */
    public static void filterEditLength(final Context mContext, final EditText editText, final int maxLength, final String errMsg, final boolean supportEmoji, final boolean supportEmpty, final boolean supportNLine, final boolean chatSpecial) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(maxLength) {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Pattern speChat = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (!supportEmoji) {
                    Matcher emojiMatcher = emoji.matcher(source);
                    if (emojiMatcher.find()) {
                        ToastTools.showToastShort(mContext, R.string.edit_unsupport_emoji);
                        return "";
                    }
                }
                if (!chatSpecial) {// 禁止EditText输入特殊字符
                    Matcher speChatMatcher = speChat.matcher(source);
                    if (speChatMatcher.find()) {
                        ToastTools.showToastShort(mContext, R.string.edit_unsupport_special_chat);
                        return "";
                    }
                }
                if (!supportEmpty) {
                    if (source.equals(" ")) {// 禁止输入空格
                        ToastTools.showToastShort(mContext, R.string.edit_unsupport_empty);
                        return "";
                    }
                }
                if (!supportNLine) {
                    if (source.toString().contentEquals("\n")) {// 禁止输入换行
                        ToastTools.showToastShort(mContext, R.string.edit_unsupport_next_line);
                        return "";
                    }
                }
                if (dest.toString().length() + source.toString().length() > maxLength) {// 获取字符个数(一个中文算2个字符)
                    if (!TextUtils.isEmpty(errMsg))
                        ToastTools.showToastShort(mContext, errMsg);
                    if (source.length() > 0 && dest.toString().length() < maxLength) {
                        return source.subSequence(0, maxLength - dest.toString().length());
                    } else
                        return "";
                }

                return source;
            }
        };
        editText.setFilters(filters);
    }
}
