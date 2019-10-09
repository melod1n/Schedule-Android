package ru.melod1n.schedule.current;

import androidx.fragment.app.DialogFragment;

public abstract class FullScreenDialog<T> extends DialogFragment {

    private OnActionListener<T> onActionListener;

    public void setOnActionListener(OnActionListener<T> onActionListener) {
        this.onActionListener = onActionListener;
    }

    public OnActionListener<T> getOnActionListener() {
        return onActionListener;
    }

    public interface OnActionListener<T> {

        void onItemInsert(T item);

        void onItemEdit(T item);

        void onItemDelete(T item);
    }

}
