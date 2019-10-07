package ru.melod1n.schedule.adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.Engine;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.fragment.ScheduleFragment;
import ru.melod1n.schedule.items.LessonItem;
import ru.melod1n.schedule.items.LocationItem;
import ru.melod1n.schedule.items.SubjectItem;

public class ScheduleAdapter extends RecyclerAdapter<LessonItem, ScheduleAdapter.ViewHolder> {

    public ScheduleAdapter(ScheduleFragment f, ArrayList<LessonItem> items) {
        super(f, items);
    }

    public void onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onEndMove(int day) {
        CacheStorage.delete(DatabaseHelper.TABLE_LESSONS, "day=" + day);
        CacheStorage.insert(DatabaseHelper.TABLE_LESSONS, getValues());

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup vg, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_schedule_item, vg, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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

        public void bind(final int position) {
            LessonItem item = getItem(position);

            lessonType.setText(String.format(Locale.getDefault(), "%d: %s", position + 1, getType(new Random().nextInt(4))));

            SubjectItem subject = item.getSubject();
            lessonName.setText(subject.getTitle());

            LocationItem location = item.getClassRoom();
            lessonClassroom.setText(String.format("%s, %s", location.getTitle(), location.getBuilding()));

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            lessonLine.setBackgroundColor(color);
            lessonType.setTextColor(color);


//            TeacherItem teacher = item.getTeacher();
//            lessonTeacher.setText(teacher.getTitle());
//            lessonTeacher.setVisibility(View.GONE);

            lessonStartTime.setText(Engine.getTimeByInt(Engine.getStartTimeAt(item.getOrder())));
            lessonEndTime.setText(Engine.getTimeByInt(Engine.getEndTimeAt(item.getOrder())));
        }
    }

    private String getType(int i) {
        return i == 0 ? "Лекция" : i == 1 ? "Практика" : i == 2 ? "Лабораторная" : "Дополнительное";
    }
}
