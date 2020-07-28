package ru.melod1n.schedule.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.current.BaseAdapter;
import ru.melod1n.schedule.current.BaseHolder;
import ru.melod1n.schedule.items.Lesson;
import ru.melod1n.schedule.items.Location;
import ru.melod1n.schedule.items.Subject;

public class ScheduleAdapter extends BaseAdapter<Lesson, ScheduleAdapter.ViewHolder> {

    private int[] colors;

    public ScheduleAdapter(Context context, ArrayList<Lesson> items) {
        super(context, items);
        initColors();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup vg, int i) {
        View v = getInflater().inflate(R.layout.fragment_schedule_item, vg, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull @NonNull ScheduleAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(position);
    }

    @Override
    public void destroy() {

    }

    private String getType(int i) {
        return i == 0 ? "Лекция" : i == 1 ? "Практика" : i == 2 ? "Лабораторная" : "Дополнительное";
    }

    public void initColors() {
        colors = !ThemeEngine.isDark() ? ThemeEngine.COLOR_PALETTE_DARK : ThemeEngine.COLOR_PALETTE_LIGHT;
    }

    class ViewHolder extends BaseHolder {

        @BindView(R.id.lessonType)
        TextView lessonType;

        @BindView(R.id.lessonName)
        TextView lessonName;

        @BindView(R.id.lessonClassroom)
        TextView lessonClassroom;

        @BindView(R.id.lessonStartTime)
        TextView lessonStartTime;

        @BindView(R.id.lessonEndTime)
        TextView lessonEndTime;

        @BindView(R.id.lessonLine)
        View lessonLine;

//        @BindView(R.id.lessonTeacher)
//        TextView lessonTeacher;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }

        @Override
        public void bind(final int position) {
            Lesson item = getItem(position);

            lessonType.setText(String.format(Locale.getDefault(), "%d: %s", position + 1, getType(new Random().nextInt(4))));

            Subject subject = item.getSubject();

            assert subject != null;

            lessonName.setText(subject.getTitle());

            Location location = item.getClassRoom();

            assert location != null;

            lessonClassroom.setText(String.format("%s, %s", location.getTitle(), location.getBuilding()));

            int color = colors[new Random().nextInt(colors.length - 1)];

            lessonLine.setBackgroundColor(color);
            lessonType.setTextColor(color);

            lessonStartTime.setText("8:00");
            lessonEndTime.setText("8:40");
        }
    }
}
