package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.MainActivity;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.AgendaAdapter;
import ru.melod1n.schedule.items.HomeworkItem;
import ru.melod1n.schedule.widget.RefreshLayout;
import ru.melod1n.schedule.widget.Toolbar;

public class AgendaFragment extends Fragment {

    @BindView(R.id.list)
    RecyclerView list;

    @BindView(R.id.refresh)
    RefreshLayout refresh;

    @BindView(R.id.no_items_container)
    TextView noItems;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AgendaAdapter adapter;

    private MenuItem searchViewItem;

    private boolean searchViewCollapsed = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, view);

        noItems.setText(R.string.no_agenda);

        prepareToolbar();

        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        refresh.setOnRefreshListener(this::getHomework);

        DrawerLayout drawerLayout = ((MainActivity) getActivity()).getDrawerLayout();

        ActionBarDrawerToggle toggle = ((MainActivity) getActivity()).initToggle(toolbar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getHomework();
    }

    private void prepareToolbar() {
        toolbar.setTitle(R.string.nav_agenda);
        toolbar.inflateMenu(R.menu.fragment_agenda);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);

        searchViewItem = toolbar.getMenu().findItem(R.id.agenda_search);

        SearchView searchView = (SearchView) searchViewItem.getActionView();
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

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(Object[] data) {
    }

    private void getHomework() {
        ArrayList<HomeworkItem> items = new ArrayList<>();

        items.add(new HomeworkItem("Some Homework", "Some Lesson", "22.08.19", 1));


        createAdapter(items);
        checkCount();

        refresh.setRefreshing(false);
    }

    private void createAdapter(ArrayList<HomeworkItem> values) {
        if (adapter == null) {
            adapter = new AgendaAdapter(this, values);
            adapter.setOnItemClickListener(this::onItemClick);
            adapter.setOnItemLongClickListener(this::onItemClick);
            list.setAdapter(adapter);
            return;
        }

        adapter.changeItems(values);
        adapter.notifyDataSetChanged();
    }

    private void onItemClick(View view, int i) {

    }

    private void checkCount() {
        noItems.setVisibility(adapter == null ? View.VISIBLE : adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }
}
