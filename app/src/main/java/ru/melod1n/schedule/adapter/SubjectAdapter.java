package ru.melod1n.schedule.adapter;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.items.SubjectItem;
import ru.melod1n.schedule.common.ThemeManager;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.fragment.SubjectsFragment;
import ru.melod1n.schedule.util.ColorUtil;

public class SubjectAdapter extends RecyclerAdapter<SubjectItem, SubjectAdapter.ViewHolder> {

    public SubjectAdapter(SubjectsFragment f, ArrayList<SubjectItem> items) {
        super(f, items);
    }

    public void onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onEndMove(int fromPosition, int toPosition, int day) {
        CacheStorage.delete(DatabaseHelper.TABLE_SUBJECTS, "day=" + day);
        CacheStorage.insert(DatabaseHelper.TABLE_SUBJECTS, getValues());

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup vg, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_day_item, vg, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.cab)
        TextView cab;

        @BindView(R.id.cab_title)
        TextView cab_title;

        @BindView(R.id.num)
        TextView num;

        @BindView(R.id.subject_title)
        TextView subject_title;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            GradientDrawable background = new GradientDrawable();
            background.setCornerRadius(200);
            background.setColor(ThemeManager.getAccent());

            num.setBackground(background);

            name.setTextColor(getColor(ThemeManager.isDark() ? R.color.dark_subject_title : R.color.subject_title));
            cab.setTextColor(getColor(ThemeManager.isDark() ? R.color.dark_cab_title : R.color.cab_title));
        }

        public void bind(final int position) {
            SubjectItem item = getItem(position);
            int color = ThemeManager.isDark() ? ThemeManager.COLOR_PALETTE_DARK[item.getPosition()] : ThemeManager.COLOR_PALETTE_LIGHT[item.getPosition()];

            num.getBackground().setTint(color == 0 ? ThemeManager.getAccent() : color);

            num.setTextColor(ColorUtil.isLight(color) ? Color.BLACK : Color.WHITE);
            num.setText(String.valueOf(position + 1));

            name.setText(item.getName());
            cab.setText(item.getCab());


            String time = Html.fromHtml(context.getString(R.string.bell, item.getStartFull(), item.getEndFull())) + "";
            String title = (time.contains("00:00") ? getString(R.string.subject) : getString(R.string.bell_text, item.getStartFull(), item.getEndFull()));
            subject_title.setText(title);

            cab_title.setVisibility(TextUtils.isEmpty(item.getCab()) ? View.INVISIBLE : View.VISIBLE);
        }
    }
}
