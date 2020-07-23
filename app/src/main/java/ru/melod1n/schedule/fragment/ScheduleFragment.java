package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.ScheduleAdapter;
import ru.melod1n.schedule.app.AlertBuilder;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.current.BaseAdapter;
import ru.melod1n.schedule.olddatabase.OldCacheStorage;
import ru.melod1n.schedule.items.DayItem;
import ru.melod1n.schedule.items.LessonItem;
import ru.melod1n.schedule.items.LocationItem;
import ru.melod1n.schedule.items.SubjectItem;
import ru.melod1n.schedule.items.TeacherItem;

public class ScheduleFragment extends Fragment implements BaseAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView list;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.noItemsView)
    TextView noItems;

    @BindView(R.id.fab_add)
    FloatingActionButton add;

    private ScheduleAdapter adapter;

    private int day;

    public ScheduleFragment() {
    }

    public ScheduleFragment(int i) {
        this.day = i;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(EventInfo info) {
        String key = info.getKey();
        if (EventInfo.KEY_THEME_UPDATE.equals(key)) {
            getSubjects();
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        AlertBuilder builder = new AlertBuilder(requireContext());
        builder.setTitle("Hello");
        builder.setMessage("It\'s message");
        builder.show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        noItems.setText(R.string.no_lessons);

        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        refresh.setOnRefreshListener(this::getSubjects);

        add.setOnClickListener(v -> {
            add.hide();
            Toast.makeText(getContext(), R.string.in_progress_title, Toast.LENGTH_SHORT).show();
        });


        getSubjects();
    }

    private void checkCount() {
        noItems.setVisibility(adapter == null ? View.VISIBLE : adapter.getValues().size() == 0 ? View.VISIBLE : View.GONE);
    }

    private void getSubjects() {
        ArrayList<DayItem> a = OldCacheStorage.getDays(day);
        ArrayList<LessonItem> lessons = new ArrayList<>();

        int i = 1;
        while (i < 7) {
            LessonItem item = new LessonItem();
            item.setOrder(i - 1);
            item.setLessonStringType("Тип №" + i);

            LocationItem location = new LocationItem();
            location.setTitle(String.valueOf(3 * (int) Math.pow(i, 2)));
            location.setBuilding("Здание №" + i);

            item.setClassRoom(location);

            TeacherItem teacher = new TeacherItem();
            teacher.setTitle("Владимир Путин #" + i);

            item.setTeacher(teacher);

            SubjectItem subject = new SubjectItem();
            subject.setTitle("Предмет #" + i);
            subject.setColorPosition(new Random().nextInt(16));

            item.setSubject(subject);

            lessons.add(item);
            i++;
        }

        if (a.isEmpty()) {
            DayItem item = new DayItem();
            item.setLessons(day % 2 == 0 ? lessons : new ArrayList<>());
            a.add(item);
        }

        DayItem item = a.get(0);
        createAdapter(item.getLessons());
        checkCount();

        refresh.setRefreshing(false);
    }

    private void createAdapter(ArrayList<LessonItem> values) {
        if (adapter == null) {
            adapter = new ScheduleAdapter(requireContext(), values);
            adapter.setOnItemClickListener(this);
            list.setAdapter(adapter);
            return;
        }

        adapter.changeItems(values);
        adapter.notifyItemRangeChanged(0, adapter.getItemCount(), -1);
    }

    public int getDay() {
        return day;
    }
}
