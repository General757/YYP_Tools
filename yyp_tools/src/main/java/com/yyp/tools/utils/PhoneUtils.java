//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by generalYan on 2019/10/21.
 * 手机工具
 */
public class PhoneUtils {

    public static void sendMessage(Context activity, String phoneNumber, String smsContent) {
        if (phoneNumber != null && phoneNumber.length() >= 4) {
            Uri uri = Uri.parse("smsto:" + phoneNumber);
            Intent it = new Intent("android.intent.action.SENDTO", uri);
            it.putExtra("sms_body", smsContent);
            it.setFlags(268435456);
            activity.startActivity(it);
        }
    }

    public static void callPhones(Context context, String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() >= 1) {
            Uri uri = Uri.parse("tel:" + phoneNumber);
            Intent intent = new Intent("android.intent.action.CALL", uri);
            intent.setFlags(268435456);
            context.startActivity(intent);
        }
    }
}
