package ru.melod1n.schedule.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class RecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected Context context;
    protected LayoutInflater inflater;
    protected Fragment fragment;
    protected OnItemClickListener click;
    protected OnItemLongClickListener long_click;
    private ArrayList<T> values;
    private ArrayList<T> cleanValues;

    public RecyclerAdapter(Context context, ArrayList<T> values) {
        this.context = context;
        this.values = values;

        this.inflater = LayoutInflater.from(context);
    }

    public RecyclerAdapter(Fragment f, ArrayList<T> values) {
        this.fragment = f;
        this.context = fragment.getActivity();
        this.values = values;
        this.inflater = LayoutInflater.from(context);
    }

    protected @ColorInt
    int getColor(int resId) {
        if (context == null) return -1;

        return context.getResources().getColor(resId);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        updateListeners(holder.itemView, position);
    }

    protected void updateListeners(View v, int i) {
        if (click != null) {
            initClick(v, i);
        }

        if (long_click != null) {
            initLongClick(v, i);
        }
    }

    @Override
    public int getItemCount() {
        if (values == null) return -1;
        return values.size();
    }

    public T getItem(int position) {
        if (values == null) return null;
        return values.get(position);
    }

    public void remove(int i) {
        getValues().remove(i);
    }

    public void changeItems(ArrayList<T> items) {
        this.values = items;
    }

    public void filter(String query) {
        if (values == null) return;
        String lowerQuery = query.toLowerCase();

        if (cleanValues == null) {
            cleanValues = new ArrayList<>(values);
        }
        values.clear();

        if (query.isEmpty()) {
            values.addAll(cleanValues);
        } else {
            for (T value : cleanValues) {
                if (onQueryItem(value, lowerQuery)) {
                    values.add(value);
                }
            }
        }

        notifyDataSetChanged();
    }

    public boolean onQueryItem(T item, String lowerQuery) {
        return false;
    }

    public ArrayList<T> getValues() {
        return values;
    }

    public String getString(@StringRes int resId) {
        return context.getString(resId);
    }

    public String getString(@StringRes int resId, Object... args) {
        return context.getString(resId, args);
    }

    public Drawable getDrawable(int res) {
        return context.getDrawable(res);
    }

    private void initClick(final View v, final int i) {
        v.setOnClickListener(p1 -> click.onItemClick(v, i));
    }

    private void initLongClick(final View v, final int position) {
        v.setOnLongClickListener(p1 -> {
            long_click.onItemLongClick(v, position);
            return click != null;
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.click = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.long_click = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }
}
