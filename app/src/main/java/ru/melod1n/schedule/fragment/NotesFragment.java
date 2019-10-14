package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
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
import ru.melod1n.schedule.current.FullScreenDialog;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.items.NoteItem;
import ru.melod1n.schedule.view.FullScreenNoteDialog;
import ru.melod1n.schedule.widget.Toolbar;

public class NotesFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {

    private ItemTouchHelper ith;

    private StaggeredGridLayoutManager manager;

    private NoteAdapter adapter;

    private boolean oneColumn;

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


    private SearchView searchView;
    private MenuItem searchViewItem;

    private boolean searchViewCollapsed = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        oneColumn = AppGlobal.preferences.getBoolean("two_columns", false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        noItems.setText(R.string.no_notes);

        prepareToolbar();

        manager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        manager.setSpanCount(oneColumn ? 1 : 2);
        list.setHasFixedSize(true);

        list.setLayoutManager(manager);

        initDragDrop();

        refresh.setOnRefreshListener(this::getNotes);

        add.setOnClickListener(v -> showDialog());

        DrawerLayout drawerLayout = ((MainActivity) getActivity()).getDrawerLayout();

        ActionBarDrawerToggle toggle = ((MainActivity) getActivity()).initToggle(toolbar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getNotes();
    }

    private void prepareToolbar() {
        toolbar.setTitle(R.string.nav_notes);
        toolbar.inflateMenu(R.menu.fragment_notes);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);

        toolbar.getMenu().findItem(R.id.notes_columns).setTitle(oneColumn ? R.string.set_two_columns : R.string.set_one_column);

        searchViewItem = toolbar.getMenu().findItem(R.id.notes_search);

        searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint(getString(R.string.title));

        searchView.setOnCloseListener(() -> {
            searchViewCollapsed = true;
            return false;
        });

        searchView.setOnSearchClickListener(view -> searchViewCollapsed = false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                checkCount();
                return true;
            }
        });
    }

    public MenuItem getSearchViewItem() {
        return searchViewItem;
    }

    public boolean isSearchViewCollapsed() {
        return searchViewCollapsed;
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

        FullScreenNoteDialog dialog = new FullScreenNoteDialog(getFragmentManager(), position == -1 ? null : adapter.getItem(position));
        dialog.setOnActionListener(new FullScreenDialog.OnActionListener<NoteItem>() {
            @Override
            public void onItemInsert(NoteItem item) {
                CacheStorage.insert(DatabaseHelper.TABLE_NOTES, item);

                adapter.getValues().add(item);

                adapter.notifyItemInserted(adapter.getItemCount() - 1);
                adapter.notifyItemRangeChanged(0, adapter.getItemCount() - 1, -1);

                checkCount();
                getNotes();
            }

            @Override
            public void onItemEdit(NoteItem item) {
                CacheStorage.update(DatabaseHelper.TABLE_NOTES, item, "id = ?", item.getId());

                adapter.notifyItemChanged(position);
            }

            @Override
            public void onItemDelete(NoteItem item) {
                CacheStorage.delete(DatabaseHelper.TABLE_NOTES, "id = " + item.getId());

                adapter.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(0, adapter.getItemCount() - 1, -1);

                checkCount();
            }
        });
    }


    private boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.notes_columns) {
            if (manager == null) return false;

            oneColumn = manager.getSpanCount() == 2;
            AppGlobal.preferences.edit().putBoolean("two_columns", oneColumn).apply();

            manager.setSpanCount(oneColumn ? 1 : 2);
            ith.attachToRecyclerView(null);

            initDragDrop();

            item.setTitle(oneColumn ? R.string.set_two_columns : R.string.set_one_column);
        }

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
