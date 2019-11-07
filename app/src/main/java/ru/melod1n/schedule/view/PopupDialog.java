package ru.melod1n.schedule.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;

public class PopupDialog extends DialogFragment {

    private CharSequence title;
    private CharSequence message;

    private ThemeItem theme;

    public void show(FragmentManager fragmentManager) {
        PopupDialog dialog = new PopupDialog();
        dialog.show(fragmentManager, PopupDialog.class.getSimpleName());
    }

    public static PopupDialog getInstance() {
        return new PopupDialog();
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        theme = ThemeEngine.getCurrentTheme();

        if (getDialog() != null && getDialog().getWindow() != null) {
            Drawable background = getContext().getDrawable(theme.isMd2() ? R.drawable.popup_dialog_bg : R.drawable.popup_dialog_bg_md1);

            getDialog().getWindow().setBackgroundDrawable(background);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return inflater.inflate(R.layout.abc_popup_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleTextView = view.findViewById(R.id.title);
        TextView messageTextView = view.findViewById(R.id.message);

        titleTextView.setText(title);
        messageTextView.setText(message);
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public void setTitle(@StringRes int title) {
        if (getContext() != null)
            setTitle(getString(title));
    }

    public void setMessage(CharSequence message) {
        this.message = message;
    }

    public void setMessage(@StringRes int message) {
        if (getContext() != null)
            setMessage(getString(message));
    }
}
