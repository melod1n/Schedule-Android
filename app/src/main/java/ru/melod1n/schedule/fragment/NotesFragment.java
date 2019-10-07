package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.MainActivity;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.NoteAdapter;
import ru.melod1n.schedule.adapter.RecyclerAdapter;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.common.ThemeManager;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.items.NoteItem;
import ru.melod1n.schedule.view.FullScreenNoteDialog;

public class NotesFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {

    private ItemTouchHelper ith;

    private StaggeredGridLayoutManager manager;

    private NoteAdapter adapter;

    private boolean twoColumns;

    @BindView(R.id.list)
    RecyclerView list;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.fab_add)
    FloatingActionButton add;

    @BindView(R.id.no_items_container)
    TextView noItems;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
        ButterKnife.bind(this, view);

        noItems.setText(R.string.no_notes);

        toolbar.setTitle(R.string.nav_notes);
        toolbar.inflateMenu(R.menu.activity_main);
        toolbar.getMenu().add("");
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);

        toolbar.getMenu().getItem(1).setTitle(twoColumns ? R.string.set_two_columns : R.string.set_one_column);

        manager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        manager.setSpanCount(twoColumns ? 1 : 2);
        list.setHasFixedSize(true);

        list.setLayoutManager(manager);

        initDragDrop();

        refresh.setProgressBackgroundColorSchemeColor(ThemeManager.getBackground());
        refresh.setColorSchemeColors(ThemeManager.getMain());
        refresh.setOnRefreshListener(this::getNotes);

        add.setOnClickListener(v -> showDialog());

        getNotes();
    }

    private void initDragDrop() {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
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
            public boolean isItemViewSwipeEnabled() {
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

        ith = new ItemTouchHelper(callback);
        ith.attachToRecyclerView(list);
    }

    private void getNotes() {
        ArrayList<NoteItem> items = CacheStorage.getNotes();
        createAdapter(items);
        checkCount();

        refresh.setRefreshing(false);
    }

    private void showDialog() {
        showDialog(-1);
    }

    private void showDialog(final int position) {
        if (adapter == null) return;

        FullScreenNoteDialog dialog = FullScreenNoteDialog.display(getFragmentManager(), position == -1 ? null : adapter.getItem(position));
        dialog.setOnDoneListener(item -> {
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
        });
    }


    private boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            if (getActivity() == null) return false;

            ((MainActivity) getActivity()).replaceFragment(new SettingsFragment());
            return true;
        }

        if (manager == null) return false;

        twoColumns = manager.getSpanCount() == 2;
        AppGlobal.preferences.edit().putBoolean("two_columns", twoColumns).apply();

        manager.setSpanCount(twoColumns ? 1 : 2);
        ith.attachToRecyclerView(null);

        initDragDrop();

        item.setTitle(twoColumns ? R.string.set_two_columns : R.string.set_one_column);
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
        adapter.notifyItemRangeChanged(0, adapter.getItemCount(), -1);
    }

    private void checkCount() {
        toolbar.getMenu().getItem(0).setVisible(adapter != null && adapter.getItemCount() > 0);
        noItems.setVisibility(adapter == null ? View.VISIBLE : adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(View v, final int position) {
        showDialog(position);
    }
}
