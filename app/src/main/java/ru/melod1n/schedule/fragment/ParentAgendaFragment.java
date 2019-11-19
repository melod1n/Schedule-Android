package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.MainActivity;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.AgendaParentAdapter;
import ru.melod1n.schedule.widget.Toolbar;

public class ParentAgendaFragment extends Fragment {

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MenuItem searchViewItem;

    private SearchView searchView;

    private boolean searchViewCollapsed = true;

    private AgendaFragment visibleFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parent_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        prepareToolbar();

        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setTabMode(TabLayout.MODE_FIXED);

        if (getActivity() == null) return;

        DrawerLayout drawerLayout = ((MainActivity) getActivity()).getDrawerLayout();

        ActionBarDrawerToggle toggle = ((MainActivity) getActivity()).initToggle(toolbar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        createPagerAdapter();
    }

    private void prepareToolbar() {
        toolbar.setTitle(R.string.nav_agenda);
        toolbar.inflateMenu(R.menu.fragment_agenda);

        searchViewItem = toolbar.getMenu().findItem(R.id.agenda_search);

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
                query(newText);
                return true;
            }
        });
    }

    private void query(String text) {
        if (visibleFragment != null) {
            visibleFragment.query(text);
        }
    }

    public MenuItem getSearchViewItem() {
        return searchViewItem;
    }

    public boolean isSearchViewCollapsed() {
        return searchViewCollapsed;
    }

    private void createPagerAdapter() {
        ArrayList<AgendaFragment> fragments = new ArrayList<>(Arrays.asList(new AgendaFragment(0), new AgendaFragment(1)));

        pager.setAdapter(new AgendaParentAdapter(getChildFragmentManager(), fragments));
        tabs.setupWithViewPager(pager);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                visibleFragment = fragments.get(tab.getPosition());

                if (searchView != null && !searchViewCollapsed) {
                    query(searchView.getQuery().toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        visibleFragment = fragments.get(0);
    }

}
