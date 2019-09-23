package ru.melod1n.schedule.helper;

import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.melod1n.schedule.common.AppGlobal;

public class FontHelper {

    public static final String PS_MEDIUM = "PS-Medium";
    public static final String PS_REGULAR = "PS-Regular";
    public static final String PS_BOLD = "PS-Bold";
    public static final String PS_THIN = "PS-Thin";
    public static final String PS_LIGHT = "PS-Light";

    public static Typeface getFont(String font) {
        return Typeface.createFromAsset(AppGlobal.context.getAssets(), "fonts/" + font + ".ttf");
    }

    public static void setFont(TextView tv, String font) {
        if (tv == null || font == null) return;
        tv.setTypeface(getFont(font));
    }

    public static void setFont(TextView[] tvs, String font) {
        if (tvs == null || font == null) return;

        for (TextView t : tvs) {
            t.setTypeface(getFont(font));
        }
    }

    public static void setFont(EditText et, String font) {
        if (et == null || font == null) return;
        et.setTypeface(getFont(font));
    }

    public static void setFont(EditText[] ets, String font) {
        if (ets == null || font == null) return;

        for (EditText t : ets) {
            t.setTypeface(getFont(font));
        }
    }

    public static void setFont(Button b, String font) {
        if (b == null || font == null) return;
        b.setTypeface(getFont(font));
    }

    public static void setFont(Button[] btns, String font) {
        if (btns == null || font == null) return;

        for (Button t : btns) {
            t.setTypeface(getFont(font));
        }
    }


}
