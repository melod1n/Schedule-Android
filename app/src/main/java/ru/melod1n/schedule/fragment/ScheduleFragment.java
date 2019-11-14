package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.RecyclerAdapter;
import ru.melod1n.schedule.adapter.ScheduleAdapter;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.items.DayItem;
import ru.melod1n.schedule.items.LessonItem;
import ru.melod1n.schedule.items.LocationItem;
import ru.melod1n.schedule.items.SubjectItem;
import ru.melod1n.schedule.items.TeacherItem;
import ru.melod1n.schedule.view.PopupDialog;

public class ScheduleFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {

    @BindView(R.id.list)
    RecyclerView list;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.no_items_container)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(Object[] data) {

    }

    @Override
    public void onItemClick(View v, int position) {
        PopupDialog dialogFragment = new PopupDialog();
        dialogFragment.setTitle("Тест");
        dialogFragment.setMessage("Пожалуйста, не беспокойтесь");
        if (getActivity() != null)
            dialogFragment.show(getActivity().getSupportFragmentManager());
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

        //initDragDrop();

        refresh.setOnRefreshListener(this::getSubjects);

        add.setOnClickListener(v -> {
            add.hide();
            Toast.makeText(getContext(), R.string.in_progress_title, Toast.LENGTH_SHORT).show();
        });


        getSubjects();
    }

    private void initDragDrop() {
        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            int dragFrom = -1;
            int dragTo = -1;

            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPosition, @NonNull RecyclerView.ViewHolder target, int toPosition, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPosition, target, toPosition, x, y);

                if (dragFrom == -1) {
                    dragFrom = fromPosition;
                }

                dragTo = toPosition;

                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(adapter.getValues(), i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(adapter.getValues(), i, i - 1);
                    }
                }

                adapter.onItemMove(fromPosition, toPosition);
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN | ItemTouchHelper.UP);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
                    adapter.onEndMove(day);
                }

                dragFrom = dragTo = -1;
            }
        };

        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(list);
    }

    private void checkCount() {
        noItems.setVisibility(adapter == null ? View.VISIBLE : adapter.getValues().size() == 0 ? View.VISIBLE : View.GONE);
    }

    private void getSubjects() {
        ArrayList<DayItem> a = CacheStorage.getDays(day);
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
            adapter = new ScheduleAdapter(this, values);
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
