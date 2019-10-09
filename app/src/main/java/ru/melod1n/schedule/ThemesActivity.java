package ru.melod1n.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.adapter.ThemeAdapter;
import ru.melod1n.schedule.common.ThemeManager;
import ru.melod1n.schedule.current.BaseActivity;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.Util;
import ru.melod1n.schedule.widget.Toolbar;

public class ThemesActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.list)
    RecyclerView list;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private ThemeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        ButterKnife.bind(this);

        setContentView(refreshLayout.getRootView());
        applyBackground();

        toolbar.setTitle(ThemeManager.getCurrentTheme().getName());
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        refreshLayout.setOnRefreshListener(this::onRefresh);

        list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        getThemes();

        list.smoothScrollToPosition(getIntent().getIntExtra("position", 0));
    }

    private void onRefresh() {
        getThemes();
    }

    private void getThemes() {
        ThemeManager.insertStockThemes(new ArrayList<>());

        ArrayList<ThemeItem> items = CacheStorage.getThemes();
        createAdapter(items);

        refreshLayout.setRefreshing(false);
    }

    private void createAdapter(ArrayList<ThemeItem> items) {
        if (adapter == null) {
            adapter = new ThemeAdapter(this, items);
            adapter.setOnItemClickListener(this::onItemClick);
            list.setAdapter(adapter);
            return;
        }

        adapter.changeItems(items);
        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }

    private void onItemClick(View v, int position) {
        ThemeItem item = adapter.getItem(position);

        ThemeManager.setCurrentTheme(item.getKey());
        
        startActivity(new Intent(this, MainActivity.class));
        Util.restart(this, new Intent().putExtra("position", position), true);
        finishAffinity();
    }
}
