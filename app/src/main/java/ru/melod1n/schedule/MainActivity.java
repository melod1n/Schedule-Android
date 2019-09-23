package ru.melod1n.schedule;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.fragment.ParentSubjectsFragment;
import ru.melod1n.schedule.fragment.NotesFragment;
import ru.melod1n.schedule.fragment.SettingsFragment;
import ru.melod1n.schedule.fragment.TimetableFragment;
import ru.melod1n.schedule.util.ArrayUtil;
import ru.melod1n.schedule.util.Util;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigationView)
    BottomNavigationView navView;

    private ParentSubjectsFragment df = new ParentSubjectsFragment();
    private TimetableFragment tf = new TimetableFragment();
    private NotesFragment nf = new NotesFragment();
    private SettingsFragment sf = new SettingsFragment();

    private int selected_id;
    private Fragment selected_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkFirstLaunch(savedInstanceState);

        checkCrash();

        navView.setOnNavigationItemReselectedListener(null);
        navView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void checkFirstLaunch(Bundle savedInstanceState) {
        if (Util.isFirstLaunch()) {
            startSetupActivity();
        } else {
            Intent intent = getIntent();

            if (intent.hasExtra("open_settings")) {
                selected_id = R.id.settings;
                selected_fragment = sf;
                replaceFragment(sf);
                navView.setSelectedItemId(selected_id);
            } else {
                if (savedInstanceState == null) {
                    int i = Integer.parseInt(AppGlobal.preferences.getString(SettingsFragment.KEY_OPEN_ON_START, "1"));
                    switch (i) {
                        default:
                        case 0:
                            selected_id = R.id.timetable;
                            selected_fragment = tf;
                            break;
                        case 1:
                            selected_id = R.id.schedule;
                            selected_fragment = df;
                            break;
                        case 2:
                            selected_id = R.id.notes;
                            selected_fragment = nf;
                            break;
                    }

                    replaceFragment(selected_fragment);
                    navView.setSelectedItemId(selected_id);
                }
            }

            setupShortcuts();
        }
    }

    private void startSetupActivity() {
        startActivity(new Intent(this, SetupActivity.class));
        finish();
    }

    private void checkCrash() {
        if (AppGlobal.preferences.getBoolean("isCrashed", false)) {
            final String trace = AppGlobal.preferences.getString("crashLog", "");
            AppGlobal.preferences.edit().putBoolean("isCrashed", false).putString("crashLog", "").apply();

            if (!AppGlobal.preferences.getBoolean(SettingsFragment.KEY_SHOW_ERROR, true)) return;

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle(R.string.warning);

            adb.setMessage(R.string.cause_error);
            adb.setPositiveButton(android.R.string.ok, null);
            adb.setNeutralButton(R.string.show, (p1, p2) -> {
                AlertDialog.Builder adb1 = new AlertDialog.Builder(MainActivity.this);
                adb1.setTitle(R.string.error_log);
                adb1.setMessage(trace);
                adb1.setPositiveButton(android.R.string.ok, null);
                adb1.setNeutralButton(R.string.copy, (p11, p21) -> Util.copyText(trace));
                adb1.create().show();
            });
            adb.create().show();
        }
    }

    private void setupShortcuts() {
        if (true) return;
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);


        ShortcutInfo timetable = new ShortcutInfo.Builder(this, "timetable")
                .setShortLabel(getString(R.string.timetable))
                .setIntent(new Intent(this, MainActivity.class).setAction(Intent.ACTION_DEFAULT).putExtra("shortcut_type", "timetable"))
                .build();

        ShortcutInfo schedule = new ShortcutInfo.Builder(this, "schedule")
                .setShortLabel(getString(R.string.schedule))
                .setIntent(new Intent(this, MainActivity.class).setAction(Intent.ACTION_DEFAULT).putExtra("shortcut_type", "schedule"))
                .build();

        ShortcutInfo notes = new ShortcutInfo.Builder(this, "notes")
                .setShortLabel(getString(R.string.notes))
                .setIntent(new Intent(this, MainActivity.class).setAction(Intent.ACTION_DEFAULT).putExtra("shortcut_type", "notes"))
                .build();

        ArrayList<ShortcutInfo> shortcuts = new ArrayList<>(3);
        shortcuts.add(timetable);
        shortcuts.add(schedule);
        shortcuts.add(notes);

        shortcutManager.setDynamicShortcuts(shortcuts);
    }

    public void replaceFragment(Fragment fragment) {
        if (fragment == null) return;

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        List<String> classesNames = new ArrayList<>(fragments.size());

        int FRAGMENT_CONTAINER = R.id.fragment_container;
        if (ArrayUtil.isEmpty(fragments)) {
            transaction.add(FRAGMENT_CONTAINER, fragment, fragment.getClass().getSimpleName());
        } else {
            for (Fragment f : fragments) {
                transaction.hide(f);
                classesNames.add(f.getClass().getSimpleName());
            }
            if (classesNames.contains(fragment.getClass().getSimpleName())) {
                for (Fragment f : fragments)
                    if (f.getClass().getSimpleName().equals(fragment.getClass().getSimpleName())) {
                        transaction.show(f);
                        break;
                    }
            } else {
                transaction.add(FRAGMENT_CONTAINER, fragment, fragment.getClass().getSimpleName());
            }
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notes:
                selected_fragment = nf;
                break;
            case R.id.schedule:
                selected_fragment = df;
                break;
            case R.id.timetable:
                selected_fragment = tf;
                break;
            case R.id.settings:
                selected_fragment = sf;
                break;
        }

        selected_id = item.getItemId();
        replaceFragment(selected_fragment);

        return true;
    }

    @Override
    protected void onDestroy() {
        AppGlobal.saveData();
        super.onDestroy();
    }

    public View getView() {
        return findViewById(R.id.container);
    }
}
