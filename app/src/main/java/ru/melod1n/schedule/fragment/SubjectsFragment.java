package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.RecyclerAdapter;
import ru.melod1n.schedule.adapter.SubjectAdapter;
import ru.melod1n.schedule.adapter.items.SubjectItem;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.common.ThemeManager;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.util.ArrayUtil;
import ru.melod1n.schedule.view.FullScreenSubjectDialog;

public class SubjectsFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {

    private RecyclerView list;
    private SwipeRefreshLayout refresh;
    private View empty;
    private FloatingActionButton add;

    private SubjectAdapter adapter;

    private int day;

    public SubjectsFragment() {
    }

    public SubjectsFragment(int i) {
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

                CacheStorage.delete(DatabaseHelper.TABLE_SUBJECTS, "day=" + day);
                CacheStorage.insert(DatabaseHelper.TABLE_SUBJECTS, adapter.getValues());
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
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initViews(view);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);

        list.setLayoutManager(manager);

        initDragDrop();

        refresh.setProgressBackgroundColorSchemeColor(ThemeManager.getBackground());
        refresh.setColorSchemeColors(ThemeManager.getMain());
        refresh.setOnRefreshListener(this::getSubjects);

        add.setOnClickListener(v -> showDialog());

        getSubjects();
    }

    private void initDragDrop() {
        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            int dragFrom = -1;
            int dragTo = -1;

            public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPosition = viewHolder.getAdapterPosition();
                final int toPosition = target.getAdapterPosition();


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
                    adapter.onEndMove(dragFrom, dragTo, day);
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
            FullScreenSubjectDialog dialog = FullScreenSubjectDialog.display(getFragmentManager(), position == -1 ? null : adapter.getItem(position));
            dialog.setOnDoneListener(item -> {
                if (position == -1) {
                    item.setDay(day);

                    CacheStorage.insert(DatabaseHelper.TABLE_SUBJECTS, item);
                    getSubjects();
                } else {
                    if (item == null) {
                        CacheStorage.delete(DatabaseHelper.TABLE_SUBJECTS, "id = " + adapter.getItem(position).getId());
                        adapter.remove(position);
                        adapter.notifyItemRemoved(position);
                        checkCount();
                    } else {
                        CacheStorage.update(DatabaseHelper.TABLE_SUBJECTS, item, "id = ?", item.getId());
                        adapter.notifyItemChanged(position, -1);
                    }
                }
                if (position == -1 || item == null)
                    adapter.notifyItemRangeChanged(0, adapter.getItemCount(), -1);
            });
        }
    }

    private void checkCount() {
        empty.setVisibility(adapter == null ? View.VISIBLE : adapter.getValues().size() == 0 ? View.VISIBLE : View.GONE);
    }

    private void getSubjects() {
        ArrayList<SubjectItem> items = CacheStorage.getSubjects(day);
        createAdapter(items);
        checkCount();

        refresh.setRefreshing(false);
    }

    private void createAdapter(ArrayList<SubjectItem> values) {
        if (adapter == null) {
            adapter = new SubjectAdapter(this, values);
            adapter.setOnItemClickListener(this);
            list.setAdapter(adapter);
            return;
        }

        adapter.changeItems(values);
        adapter.notifyItemRangeChanged(0, adapter.getItemCount(), -1);
    }

    private void initViews(View root) {
        empty = root.findViewById(R.id.no_items_container);
        list = root.findViewById(R.id.list);
        refresh = root.findViewById(R.id.refresh);
        add = root.findViewById(R.id.fab_add);
    }
}
