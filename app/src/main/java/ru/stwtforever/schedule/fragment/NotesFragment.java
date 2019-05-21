package ru.stwtforever.schedule.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import ru.stwtforever.schedule.R;
import ru.stwtforever.schedule.adapter.NoteAdapter;
import ru.stwtforever.schedule.adapter.RecyclerAdapter;
import ru.stwtforever.schedule.adapter.items.NoteItem;
import ru.stwtforever.schedule.common.AppGlobal;
import ru.stwtforever.schedule.common.ThemeManager;
import ru.stwtforever.schedule.db.CacheStorage;
import ru.stwtforever.schedule.db.DatabaseHelper;
import ru.stwtforever.schedule.util.ViewUtil;
import ru.stwtforever.schedule.view.FullScreenNoteDialog;

public class NotesFragment extends Fragment implements RecyclerAdapter.OnItemClickListener, Toolbar.OnMenuItemClickListener {

    private RecyclerView list;
    private ItemTouchHelper ith;

    private StaggeredGridLayoutManager manager;
    private SwipeRefreshLayout refresh;
    private FloatingActionButton add;
    private View empty;

    private NoteAdapter adapter;

    private Toolbar tb;

    private boolean twoColumns;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        twoColumns = AppGlobal.preferences.getBoolean("two_columns", false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initViews(view);

        tb.setTitle(R.string.notes);
        tb.setOnMenuItemClickListener(this);

        manager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        manager.setSpanCount(twoColumns ? 1 : 2);
        list.setHasFixedSize(true);

        list.setLayoutManager(manager);

        initDragDrop();

        MenuItem item = tb.getMenu().add(R.string.set_one_column);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(twoColumns ? R.drawable.grid_large : R.drawable.grid);
        item.getIcon().setTint(ThemeManager.isLight() ? Color.BLACK : Color.WHITE);

        ViewUtil.applyToolbarStyles(tb);

        refresh.setProgressBackgroundColorSchemeColor(ThemeManager.getBackground());
        refresh.setColorSchemeColors(ThemeManager.getMain());
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotes();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) showDialog();
            }
        });

        getNotes();
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
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | (manager.getSpanCount() == 2 ? (ItemTouchHelper.START | ItemTouchHelper.END) : 0));
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
                    adapter.onEndMove(dragFrom, dragTo);
                }

                dragFrom = dragTo = -1;
            }
        };

        ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(list);
    }

    private void getNotes() {
        refresh.setRefreshing(true);
        ArrayList<NoteItem> items = CacheStorage.getNotes();
        createAdapter(items);
        checkCount();

        refresh.setRefreshing(false);
    }

    private void showDialog() {
        showDialog(-1);
    }

    private void showDialog(final int position) {
        FullScreenNoteDialog dialog = FullScreenNoteDialog.display(getFragmentManager(), position == -1 ? null : adapter.getItem(position));
        dialog.setOnDoneListener(new FullScreenNoteDialog.OnDoneListener() {

            @Override
            public void onDone(NoteItem item) {
                if (position == -1) {
                    adapter.getValues().add(item);
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                    CacheStorage.insert(DatabaseHelper.TABLE_NOTES, item);
                    checkCount();
                } else {
                    if (item == null) {
                        CacheStorage.delete(DatabaseHelper.TABLE_NOTES, "id = " + adapter.getItem(position).getId());
                        adapter.remove(position);
                        adapter.notifyItemRemoved(position);
                        checkCount();
                    } else {
                        CacheStorage.update(DatabaseHelper.TABLE_NOTES, item, "id = ?", item.getId());
                        adapter.notifyItemChanged(position);
                    }
                }

                if (position == -1 || item == null)
                    adapter.notifyItemRangeChanged(0, adapter.getItemCount() - 1, -1);
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (manager == null) return false;

        twoColumns = manager.getSpanCount() == 2;
        AppGlobal.preferences.edit().putBoolean("two_columns", twoColumns).apply();

        item.setIcon(twoColumns ? R.drawable.grid_large : R.drawable.grid);
        item.setTitle(twoColumns ? R.string.set_two_columns : R.string.set_one_column);

        item.getIcon().setTint(ThemeManager.isLight() ? Color.BLACK : Color.WHITE);

        manager.setSpanCount(twoColumns ? 1 : 2);
        ith.attachToRecyclerView(null);
        initDragDrop();

        return true;
    }

    private void createAdapter(ArrayList<NoteItem> values) {
        if (adapter == null) {
            adapter = new NoteAdapter(getActivity(), values);
            adapter.setOnItemClickListener(this);
            list.setAdapter(adapter);
            return;
        }

        adapter.changeItems(values);
        adapter.notifyItemRangeChanged(0, adapter.getItemCount() - 1, -1);
    }

    private void initViews(View root) {
        tb = root.findViewById(R.id.toolbar);
        empty = root.findViewById(R.id.no_items_container);
        add = root.findViewById(R.id.fab_add);
        refresh = root.findViewById(R.id.refresh);
        list = root.findViewById(R.id.list);
    }

    private void checkCount() {
        tb.getMenu().getItem(0).setVisible(adapter != null && adapter.getItemCount() > 0);
        empty.setVisibility(adapter == null ? View.VISIBLE : adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(View v, final int position) {
        showDialog(position);
    }
}
