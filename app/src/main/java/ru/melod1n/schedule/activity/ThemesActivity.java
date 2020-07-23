package ru.melod1n.schedule.activity;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.ThemeAdapter;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.current.BaseActivity;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.widget.Toolbar;

public class ThemesActivity extends BaseActivity {

    public static final int REQUEST_PICK_THEME = 0;
    public static final int REQUEST_PICK_DAY_THEME = 1;
    public static final int REQUEST_PICK_NIGHT_THEME = 2;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView list;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private ThemeAdapter adapter;
    private int request;

    private volatile boolean animating;
    private Drawable navigationIcon;

    private View rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        ButterKnife.bind(this);

        request = getIntent().getIntExtra("request", -1);

        applyBackground();
        applyTitle();

        rootView = toolbar.getRootView();

        navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_backward);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        refreshLayout.setOnRefreshListener(this::onRefresh);

        list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        getThemes();

        if (savedInstanceState == null) {
            scrollToCurrentTheme();
        }
    }

    private void scrollToCurrentTheme() {
        int index = 0;

        for (int i = 0; i < adapter.getItemCount(); i++) {
            ThemeItem item = adapter.getItem(i);

            if (item.equals(ThemeEngine.getCurrentTheme())) index = i;
        }

        list.smoothScrollToPosition(index);
    }

    private void applyTitle() {
        ThemeItem theme = ThemeEngine.getCurrentTheme();
        ThemeItem dayTheme = ThemeEngine.getDayTheme();
        ThemeItem nightTheme = ThemeEngine.getNightTheme();

        String formatString = "%s – %s";

        String title;

        int subtitle;

        switch (request) {
            default:
            case REQUEST_PICK_THEME:
                subtitle = -1;
                title = String.format(formatString, theme.getTitle(), theme.getAuthor());
                break;
            case REQUEST_PICK_DAY_THEME:
                subtitle = R.string.pref_appearance_day_time_theme_title;
                title = String.format(formatString, dayTheme.getTitle(), dayTheme.getAuthor());
                break;
            case REQUEST_PICK_NIGHT_THEME:
                subtitle = R.string.pref_appearance_night_time_theme_title;
                title = String.format(formatString, nightTheme.getTitle(), nightTheme.getAuthor());
                break;
        }

        toolbar.setTitle(title);

        if (subtitle == -1)
            toolbar.setSubtitle("");
        else
            toolbar.setSubtitle(subtitle);
    }

    private void onRefresh() {
        getThemes();
    }

    private void getThemes() {
        ArrayList<ThemeItem> items = new ArrayList<>(ThemeEngine.themes);
        ArrayList<ThemeItem> deleteItems = new ArrayList<>();

        for (ThemeItem item : items) {
            if (request == REQUEST_PICK_DAY_THEME && item.isDark()) {
                deleteItems.add(item);
            } else if (request == REQUEST_PICK_NIGHT_THEME && !item.isDark()) {
                deleteItems.add(item);
            }
        }

        items.removeAll(deleteItems);

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

        if (ThemeEngine.isThemeCompatible(item)) {
            if (animating ||
                    (request == REQUEST_PICK_THEME && item.equals(theme)) ||
                    (request == REQUEST_PICK_DAY_THEME && item.equals(ThemeEngine.getDayTheme())) ||
                    (request == REQUEST_PICK_NIGHT_THEME && item.equals(ThemeEngine.getNightTheme())))
                return;

            if (request == REQUEST_PICK_THEME) {
                ThemeEngine.setCurrentTheme(item.getId());
            } else if (request == REQUEST_PICK_DAY_THEME) {
                ThemeEngine.setDayTheme(item.getId());
            } else if (request == REQUEST_PICK_NIGHT_THEME) {
                ThemeEngine.setNightTheme(item.getId());
            }

            applyTitle();

            fadeLayout();

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

    private void fadeLayout() {
        animating = true;

        rootView.setAlpha(0);
        rootView.animate().alpha(1).setDuration(500).withEndAction(() -> animating = false).start();
    }
}
