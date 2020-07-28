package ru.melod1n.schedule.current;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;

public abstract class FullScreenDialog<T> extends DialogFragment {

    private OnActionListener<T> onActionListener;

    public FullScreenDialog(FragmentManager fragmentManager, T item) {
        display(fragmentManager, item);
    }

    protected OnActionListener<T> getOnActionListener() {
        return onActionListener;
    }

    public void setOnActionListener(OnActionListener<T> onActionListener) {
        this.onActionListener = onActionListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }

        setStyle(DialogFragment.STYLE_NORMAL, ThemeEngine.isDark() ? R.style.AppTheme_FullScreenDialog_Dark : R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(width, height);

                dialog.getWindow().setWindowAnimations(R.style.AppTheme_FullScreenDialog_Slide);

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            }
        }
    }

    protected abstract void display(FragmentManager fragmentManager, T item);

    public interface OnActionListener<T> {

        void onItemInsert(T item);

        void onItemEdit(T item);

        void onItemDelete(T item);
    }

}
