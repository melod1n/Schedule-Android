package ru.melod1n.schedule.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import ru.melod1n.schedule.widget.Button;

public class FontHelper {

    public enum Font {
        PS_REGULAR, PS_MEDIUM, ROBOTO_REGULAR
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

    private static Typeface getFont(Context context, String fontPath) {
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    private static String getFontPath(Font font) {
        if (font == null) return null;

        String path = "fonts/";

        switch (font) {
            case PS_REGULAR:
                path += "ps_regular";
                break;
            case PS_MEDIUM:
                path += "ps_medium";
                break;
            default:
            case ROBOTO_REGULAR:
                path += "roboto_regular";
                break;
        }

        path += ".ttf";

        return path;
    }

}
