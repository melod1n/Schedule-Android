package ru.melod1n.schedule;

import android.os.Bundle;

import ru.melod1n.schedule.current.BaseActivity;
import ru.melod1n.schedule.fragment.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    private SettingsFragment fragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).commit();
    }

    @Override
    protected void init() {
        super.init();

        if (!created) return;
        recreate();
    }
}
