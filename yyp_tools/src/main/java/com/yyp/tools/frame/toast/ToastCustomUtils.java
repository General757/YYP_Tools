package com.yyp.tools.frame.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yyp.tools.R;

/**
 * Created by YanYan on 2018/4/25.
 * 自定义Toast提示
 */

public class ToastCustomUtils {

    private static Toast toast = null;

    public static void showToast(Context context, String meassge) {
        showCustomToast(context, meassge, Toast.LENGTH_SHORT, Gravity.BOTTOM, 0);
    }

    public static void showToast(Context context, String meassge, int yOffset) {
        showCustomToast(context, meassge, Toast.LENGTH_SHORT, Gravity.BOTTOM, yOffset);
    }

    public static void showToast(Context context, String meassge, int gravity, int yOffset) {
        showCustomToast(context, meassge, Toast.LENGTH_SHORT, gravity, yOffset);
    }

    public static void showToast(Context context, int meassge) {
        showCustomToast(context, meassge, Toast.LENGTH_SHORT, Gravity.BOTTOM, -1);
    }

    public static void showToast(Context context, int meassge, int yOffset) {
        showCustomToast(context, meassge, Toast.LENGTH_SHORT, Gravity.BOTTOM, yOffset);
    }

    public static void showToast(Context context, int meassge, int gravity, int yOffset) {
        showCustomToast(context, meassge, Toast.LENGTH_SHORT, gravity, yOffset);
    }

    private static void showCustomToast(Context context, String message, int time, int gravity, int yOffset) {

        View toastView = LayoutInflater.from(context).inflate(R.layout.layout_toast_single_custom, null);
        TextView contentView = toastView.findViewById(R.id.toast_message);
        contentView.setText(message);

        if (toast == null)
            toast = new Toast(context);

        if (yOffset != 0)
            toast.setGravity(gravity, 0, yOffset);
        toast.setDuration(time);

        toast.setView(toastView);
        toast.show();

    }

    private static void showCustomToast(Context context, int message, int time, int gravity, int yOffset) {

        View toastView = LayoutInflater.from(context).inflate(R.layout.layout_toast_single_custom, null);
        TextView contentView = toastView.findViewById(R.id.toast_message);
        contentView.setText(context.getResources().getString(message));

        if (toast == null)
            toast = new Toast(context);

        if (yOffset != 0)
            toast.setGravity(gravity, 0, yOffset);
        toast.setDuration(time);

        toast.setView(toastView);
        toast.show();
    }

    public static void showCustomToast(Context context, String title, String message) {

        View toastView = LayoutInflater.from(context).inflate(R.layout.layout_toast_custom, null);
        TextView toast_title = toastView.findViewById(R.id.toast_title);
        TextView toast_message = toastView.findViewById(R.id.toast_message);
        toast_title.setText(title);
        toast_message.setText(message);

        if (toast == null)
            toast = new Toast(context);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);

        toast.setView(toastView);
        toast.show();

    }

    public static void showCustomToast(Context context, int title, int message) {

        View toastView = LayoutInflater.from(context).inflate(R.layout.layout_toast_custom, null);
        TextView toast_title = toastView.findViewById(R.id.toast_title);
        TextView toast_message = toastView.findViewById(R.id.toast_message);
        toast_title.setText(context.getResources().getString(title));
        toast_message.setText(context.getResources().getString(message));

        if (toast == null)
            toast = new Toast(context);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);

        toast.setView(toastView);
        toast.show();

    }

    public static void hideToast() {
        if (toast != null)
            toast.cancel();
    }
}
