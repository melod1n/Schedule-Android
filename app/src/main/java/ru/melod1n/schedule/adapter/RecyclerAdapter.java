package ru.melod1n.schedule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public abstract class RecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected Context context;
    protected LayoutInflater inflater;
    protected Fragment fragment;

    OnItemClickListener onItemClickListener;

    private OnItemLongClickListener onItemLongClickListener;

    private ArrayList<T> values;
    private ArrayList<T> cleanValues;

    public RecyclerAdapter(Context context, ArrayList<T> values) {
        this.context = context;
        this.values = values;

        this.inflater = LayoutInflater.from(context);
    }

    RecyclerAdapter(@NonNull Fragment fragment, ArrayList<T> values) {
        this.fragment = fragment;
        this.context = Objects.requireNonNull(fragment.getActivity());
        this.values = values;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        updateListeners(holder.itemView, position);
    }

    void updateListeners(View v, int i) {
        if (onItemClickListener != null) {
            initClick(v, i);
        }

        if (onItemLongClickListener != null) {
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

    private void initClick(@NonNull final View itemView, final int i) {
        itemView.setOnClickListener(p1 -> onItemClickListener.onItemClick(itemView, i));
    }

    private void initLongClick(@NonNull final View itemView, final int position) {
        itemView.setOnLongClickListener(p1 -> {
            onItemLongClickListener.onItemLongClick(itemView, position);
            return onItemClickListener != null;
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View itemView, int position);
    }
}
