package ru.melod1n.schedule;

import android.os.Bundle;

import androidx.annotation.LayoutRes;

import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.current.BaseActivity;
import ru.melod1n.schedule.fragment.SettingsFragment;
import ru.melod1n.schedule.util.Util;
import ru.melod1n.schedule.widget.Toolbar;

public class SettingsActivity extends BaseActivity {

    private SettingsFragment fragment;
    private int layoutId = R.xml.fragment_settings;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_backward);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        layoutId = AppGlobal.preferences.getInt("settings_layout", R.xml.fragment_settings);

        Bundle arguments = new Bundle();
        arguments.putInt("layout_id", layoutId);

        fragment = new SettingsFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).commit();
    }

    @Override
    protected void init() {
        super.init();

        if (!created) return;
        recreate();
    }

    @Override
    public void onBackPressed() {
        if (fragment == null || fragment.onBackPressed())
            super.onBackPressed();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (toolbar != null)
            toolbar.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    public void setFragmentElement(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        AppGlobal.preferences.edit().putInt("settings_layout", layoutId).apply();
    }
}
