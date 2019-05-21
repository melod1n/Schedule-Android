package ru.stwtforever.schedule.adapter;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.stwtforever.schedule.R;
import ru.stwtforever.schedule.adapter.items.SubjectItem;
import ru.stwtforever.schedule.common.ThemeManager;
import ru.stwtforever.schedule.db.CacheStorage;
import ru.stwtforever.schedule.db.DatabaseHelper;
import ru.stwtforever.schedule.fragment.DayFragment;
import ru.stwtforever.schedule.util.ColorUtil;

public class SubjectAdapter extends RecyclerAdapter<SubjectItem, SubjectAdapter.ViewHolder> {

    public SubjectAdapter(DayFragment f, ArrayList<SubjectItem> items) {
        super(f, items);
    }

    public void onItemDismiss(int position) {
        getValues().remove(position);
        notifyItemRemoved(position);
    }

    public void onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onEndMove(int fromPosition, int toPosition, int day) {
        CacheStorage.delete(DatabaseHelper.TABLE_SUBJECTS, "day=" + day);
        CacheStorage.insert(DatabaseHelper.TABLE_SUBJECTS, getValues());

        notifyItemRangeChanged(0, getItemCount(), getItem(toPosition));
    }

    @NonNull
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup vg, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_day_item, vg, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubjectAdapter.ViewHolder holder, int position) {
        if (position > 99) return;
        super.onBindViewHolder(holder, position);
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, cab, cab_title, num;

        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            cab = v.findViewById(R.id.cab);
            cab_title = v.findViewById(R.id.cab_title);
            num = v.findViewById(R.id.num);

            GradientDrawable background = new GradientDrawable();
            background.setCornerRadius(200);
            background.setColor(ThemeManager.getAccent());

            num.setBackground(background);
        }

        public void bind(final int position) {
            SubjectItem item = getItem(position);

            num.getBackground().setTint(item.getColor() == 0 ? ThemeManager.getAccent() : item.getColor());

            num.setTextColor(ColorUtil.isLight(item.getColor()) ? Color.BLACK : Color.WHITE);

            name.setText(item.getName());
            cab.setText(item.getCab());

            num.setText(String.valueOf(position + 1));

            cab_title.setVisibility(TextUtils.isEmpty(item.getCab()) ? View.INVISIBLE : View.VISIBLE);
        }
    }
}
