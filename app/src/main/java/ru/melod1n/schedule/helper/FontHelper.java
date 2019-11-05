package ru.melod1n.schedule.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.widget.Button;

public class FontHelper {

    public enum Font {
        PS_REGULAR, PS_MEDIUM, ROBOTO_REGULAR, ROBOTO_MEDIUM
    }

    public static void applyFont(View view, Font font) {
        if (view instanceof Button) {
            Button button = (Button) view;
            button.setTypeface(getFont(button.getContext(), getFontPath(font)));
        } else if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setTypeface(getFont(textView.getContext(), getFontPath(font)));
        }
    }

    private static Typeface getFont(Context context, int fontPath) {
        return ResourcesCompat.getFont(context, fontPath);
    }

    private static int getFontPath(Font font) {
        if (font == null) return -1;

        switch (font) {
            case PS_REGULAR:
                return R.font.ps_regular;
            case PS_MEDIUM:
                return R.font.ps_medium;
            default:
            case ROBOTO_REGULAR:
                return R.font.roboto_regular;
            case ROBOTO_MEDIUM:
                return R.font.roboto_medium;
        }
    }

}