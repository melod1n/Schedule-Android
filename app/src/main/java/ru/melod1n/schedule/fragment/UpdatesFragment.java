package ru.melod1n.schedule.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.MainActivity;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.widget.Toolbar;

public class UpdatesFragment extends Fragment {

    @BindView(R.id.no_items_container)
    TextView noItems;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.list)
    RecyclerView list;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_updates, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        noItems.setText(R.string.no_updates);

        toolbar.setTitle(R.string.nav_updates);

        DrawerLayout drawerLayout = ((MainActivity) getActivity()).getDrawerLayout();

        ActionBarDrawerToggle toggle = ((MainActivity) getActivity()).initToggle(toolbar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        noItems.setVisibility(View.GONE);

        refreshLayout.setOnRefreshListener(null);

        list.setHasFixedSize(true);
        list.setLayoutFrozen(true);

        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

}
