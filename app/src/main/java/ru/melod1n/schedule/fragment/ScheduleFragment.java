package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.current.FullScreenDialog;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.items.DayItem;
import ru.melod1n.schedule.items.LessonItem;
import ru.melod1n.schedule.items.LocationItem;
import ru.melod1n.schedule.items.SubjectItem;
import ru.melod1n.schedule.items.TeacherItem;
import ru.melod1n.schedule.util.ArrayUtil;
import ru.melod1n.schedule.view.FullScreenLessonDialog;

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
        if (ArrayUtil.isEmpty(data)) return;

        String key = (String) data[0];
        if (key.equals("bells_update")) {
            int bellsCount = AppGlobal.preferences.getInt(SettingsFragment.KEY_BELLS_COUNT, 0);

            if (adapter == null) return;

            if (adapter.getItemCount() > bellsCount) {
                adapter.changeItems(new ArrayList<>(adapter.getValues().subList(0, bellsCount)));
                adapter.notifyDataSetChanged();

                CacheStorage.delete(DatabaseHelper.TABLE_LESSONS, "day=" + day);
                CacheStorage.insert(DatabaseHelper.TABLE_LESSONS, adapter.getValues());
            } else {
                getSubjects();
            }
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        showDialog(position);
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

        initDragDrop();

        refresh.setOnRefreshListener(this::getSubjects);

        add.setOnClickListener(v -> showDialog());

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
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP);
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

    private void showDialog() {
        showDialog(-1);
    }

    private void showDialog(final int position) {
        if (adapter == null) return;

        int bellsCount = AppGlobal.preferences.getInt(SettingsFragment.KEY_BELLS_COUNT, 0);

        if (adapter.getItemCount() == bellsCount && position == -1) {
            if (bellsCount == 10) {
                Toast.makeText(getActivity(), "Достигнуто максимальное количество уроков.", Toast.LENGTH_LONG).show();
            } else {
                if (getActivity() == null) return;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.warning);
                builder.setMessage("Количество уроков превышает количество Ваших звонков. Увеличить количество звонков на 1?");
                builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    AppGlobal.preferences.edit().putInt(SettingsFragment.KEY_BELLS_COUNT, bellsCount + 1).apply();
                    SettingsFragment.updateBellsCount();
                    showDialog(position);
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.show();
            }
        } else {
            FullScreenLessonDialog dialog = new FullScreenLessonDialog(getFragmentManager(), position == -1 ? null : adapter.getItem(position));
            dialog.setOnActionListener(new FullScreenDialog.OnActionListener<LessonItem>() {
                @Override
                public void onItemInsert(LessonItem item) {
                    item.setOrder(day);

//                    if (!item.getHomework().isEmpty())
//                        EventBus.getDefault().postSticky(new Object[]{"add_subject", item});

                    CacheStorage.insert(DatabaseHelper.TABLE_LESSONS, item);

                    adapter.getValues().add(item);
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                    adapter.notifyItemRangeChanged(0, adapter.getItemCount(), -1);

                    checkCount();
                }

                @Override
                public void onItemEdit(LessonItem item) {
                    CacheStorage.update(DatabaseHelper.TABLE_LESSONS, item, "id = ?", item.getOrder());

                    adapter.notifyItemChanged(position, -1);
                }

                @Override
                public void onItemDelete(LessonItem item) {
                    CacheStorage.delete(DatabaseHelper.TABLE_LESSONS, "id = " + adapter.getItem(position).getOrder());

                    adapter.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(0, adapter.getItemCount(), -1);

                    checkCount();
                }
            });
        }
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
}
