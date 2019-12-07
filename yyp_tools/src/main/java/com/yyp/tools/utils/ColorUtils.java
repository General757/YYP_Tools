package com.yyp.tools.utils;

import android.content.res.ColorStateList;

/**
 * Generate thumb and background color state list use tintColor
 * Created by generalYan on 2019/4/17.
 */
public class ColorUtils {
    public static final int WHITE = -1;
    public static final int WHITE_TRANSLUCENT = -2130706433;
    public static final int BLACK = -16777216;
    public static final int BLACK_TRANSLUCENT = -2147483648;
    public static final int TRANSPARENT = 0;
    public static final int RED = -65536;
    public static final int RED_TRANSLUCENT = -2130771968;
    public static final int RED_DARK = -7667712;
    public static final int RED_DARK_TRANSLUCENT = -2138374144;
    public static final int GREEN = -16711936;
    public static final int GREEN_TRANSLUCENT = -2147418368;
    public static final int GREEN_DARK = -16764160;
    public static final int GREEN_DARK_TRANSLUCENT = -2147470592;
    public static final int GREEN_LIGHT = -3342388;
    public static final int GREEN_LIGHT_TRANSLUCENT = -2134048820;
    public static final int BLUE = -16776961;
    public static final int BLUE_TRANSLUCENT = -2147483393;
    public static final int BLUE_DARK = -16777077;
    public static final int BLUE_DARK_TRANSLUCENT = -2147483509;
    public static final int BLUE_LIGHT = -13195805;
    public static final int BLUE_LIGHT_TRANSLUCENT = -2143902237;
    public static final int SKYBLUE = -7876885;
    public static final int SKYBLUE_TRANSLUCENT = -2138583317;
    public static final int SKYBLUE_DARK = -16728065;
    public static final int SKYBLUE_DARK_TRANSLUCENT = -2147434497;
    public static final int SKYBLUE_LIGHT = -7876870;
    public static final int SKYBLUE_LIGHT_TRANSLUCENT = -2138583302;
    public static final int GRAY = -6908266;
    public static final int GRAY_TRANSLUCENT = -2137614698;
    public static final int GRAY_DARK = -5658199;
    public static final int GRAY_DARK_TRANSLUCENT = -2136364631;
    public static final int GRAY_DIM = -9868951;
    public static final int GRAY_DIM_TRANSLUCENT = -2140575383;
    public static final int GRAY_LIGHT = -2894893;
    public static final int GRAY_LIGHT_TRANSLUCENT = -2133601325;
    public static final int ORANGE = -23296;
    public static final int ORANGE_TRANSLUCENT = -2130729728;
    public static final int ORANGE_DARK = -30720;
    public static final int ORANGE_DARK_TRANSLUCENT = -2130737152;
    public static final int ORANGE_LIGHT = -17613;
    public static final int ORANGE_LIGHT_TRANSLUCENT = -2130724045;
    public static final int GOLD = -10496;
    public static final int GOLD_TRANSLUCENT = -2130716928;
    public static final int PINK = -16181;
    public static final int PINK_TRANSLUCENT = -2130722613;
    public static final int FUCHSIA = -65281;
    public static final int FUCHSIA_TRANSLUCENT = -2130771713;
    public static final int GRAYWHITE = -855310;
    public static final int GRAYWHITE_TRANSLUCENT = -2131561742;
    public static final int PURPLE = -8388480;
    public static final int PURPLE_TRANSLUCENT = -2139094912;
    public static final int CYAN = -16711681;
    public static final int CYAN_TRANSLUCENT = -2147418113;
    public static final int CYAN_DARK = -16741493;
    public static final int CYAN_DARK_TRANSLUCENT = -2147447925;
    public static final int YELLOW = -256;
    public static final int YELLOW_TRANSLUCENT = -2130706688;
    public static final int YELLOW_LIGHT = -32;
    public static final int YELLOW_LIGHT_TRANSLUCENT = -2130706464;
    public static final int CHOCOLATE = -2987746;
    public static final int CHOCOLATE_TRANSLUCENT = -2133694178;
    public static final int TOMATO = -40121;
    public static final int TOMATO_TRANSLUCENT = -2130746553;
    public static final int ORANGERED = -47872;
    public static final int ORANGERED_TRANSLUCENT = -2130754304;
    public static final int SILVER = -4144960;
    public static final int SILVER_TRANSLUCENT = -2134851392;
    public static final int HIGHLIGHT = 872415231;
    public static final int LOWLIGHT = 855638016;

    private static final int ENABLE_ATTR = android.R.attr.state_enabled;
    private static final int CHECKED_ATTR = android.R.attr.state_checked;
    private static final int PRESSED_ATTR = android.R.attr.state_pressed;

    public static ColorStateList generateThumbColorWithTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {-ENABLE_ATTR, CHECKED_ATTR},
                {-ENABLE_ATTR},
                {PRESSED_ATTR, -CHECKED_ATTR},
                {PRESSED_ATTR, CHECKED_ATTR},
                {CHECKED_ATTR},
                {-CHECKED_ATTR}
        };

        int[] colors = new int[]{
                tintColor - 0xAA000000,
                0xFFBABABA,
                tintColor - 0x99000000,
                tintColor - 0x99000000,
                tintColor | 0xFF000000,
                0xFFEEEEEE
        };
        return new ColorStateList(states, colors);
    }

    public static ColorStateList generateBackColorWithTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {-ENABLE_ATTR, CHECKED_ATTR},
                {-ENABLE_ATTR},
                {CHECKED_ATTR, PRESSED_ATTR},
                {-CHECKED_ATTR, PRESSED_ATTR},
                {CHECKED_ATTR},
                {-CHECKED_ATTR}
        };

        int[] colors = new int[]{
                tintColor - 0xE1000000,
                0x10000000,
                tintColor - 0xD0000000,
                0x20000000,
                tintColor - 0xD0000000,
                0x20000000
        };
        return new ColorStateList(states, colors);
    }
}

