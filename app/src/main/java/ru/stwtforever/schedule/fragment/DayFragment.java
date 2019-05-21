package ru.stwtforever.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import ru.stwtforever.schedule.R;
import ru.stwtforever.schedule.adapter.RecyclerAdapter;
import ru.stwtforever.schedule.adapter.SubjectAdapter;
import ru.stwtforever.schedule.adapter.items.SubjectItem;
import ru.stwtforever.schedule.common.ThemeManager;
import ru.stwtforever.schedule.db.CacheStorage;
import ru.stwtforever.schedule.db.DatabaseHelper;
import ru.stwtforever.schedule.view.FullScreenSubjectDialog;

public class DayFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {

    private RecyclerView list;
    private SwipeRefreshLayout refresh;
    private View empty;
    private FloatingActionButton add;

    private SubjectAdapter adapter;

    private int day;

    public DayFragment() {
    }

    public DayFragment(int i) {
        this.day = i;
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initViews(view);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);

        list.setLayoutManager(manager);

        initDragDrop();

        refresh.setProgressBackgroundColorSchemeColor(ThemeManager.getBackground());
        refresh.setColorSchemeColors(ThemeManager.getMain());
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSubjects();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        getSubjects();
    }

    private void initDragDrop() {
        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            int dragFrom = -1;
            int dragTo = -1;

            public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPosition = viewHolder.getAdapterPosition();
                final int toPosition = target.getAdapterPosition();

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
                return true;
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
        FullScreenSubjectDialog dialog = FullScreenSubjectDialog.display(getFragmentManager(), position == -1 ? null : adapter.getItem(position));
        dialog.setOnDoneListener(new FullScreenSubjectDialog.OnDoneListener() {

            @Override
            public void onDone(SubjectItem item) {
                if (position == -1) {
                    item.setDay(day);
                    adapter.getValues().add(item);
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                    CacheStorage.insert(DatabaseHelper.TABLE_SUBJECTS, item);
                    checkCount();
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
                    adapter.notifyItemRangeChanged(0, adapter.getItemCount() - 1, -1);
            }
        });
    }

    private void checkCount() {
        empty.setVisibility(adapter == null ? View.VISIBLE : adapter.getValues().size() == 0 ? View.VISIBLE : View.GONE);
    }

    private void getSubjects() {
        refresh.setRefreshing(true);

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
