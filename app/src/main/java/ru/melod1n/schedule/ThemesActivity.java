package ru.melod1n.schedule;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.adapter.ThemeAdapter;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.current.BaseActivity;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.widget.Toolbar;

public class ThemesActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.list)
    RecyclerView list;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private ThemeAdapter adapter;

    private Drawable navigationIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        ButterKnife.bind(this);

        applyBackground();
        applyTitle();

        navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_backward);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        refreshLayout.setOnRefreshListener(this::onRefresh);

        list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        getThemes();
    }

    private void applyTitle() {
        toolbar.setTitle(ThemeEngine.getCurrentTheme().getTitle());
        toolbar.setSubtitle(ThemeEngine.getCurrentTheme().getAuthor());
    }

    private void onRefresh() {
        getThemes();
    }

    private void getThemes() {
        ThemeEngine.insertStockThemes(new ArrayList<>());

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

        if (ThemeEngine.isThemeValid(item)) {
            ThemeEngine.setCurrentTheme(item.getId());
            applyTitle();

            navigationIcon.setTint(Color.RED); //я не знаю почему, но работает лишь так
            toolbar.setNavigationIcon(navigationIcon);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.warning);
            builder.setMessage(item.getEngineVersion() > ThemeEngine.ENGINE_VERSION ? R.string.warning_theme_new : R.string.warning_theme_old);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }

    }
}
