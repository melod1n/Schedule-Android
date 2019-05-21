package ru.stwtforever.schedule;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Icon;
import android.os.Build;
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

import ru.stwtforever.schedule.common.AppGlobal;
import ru.stwtforever.schedule.common.ThemeManager;
import ru.stwtforever.schedule.fragment.DayTabFragment;
import ru.stwtforever.schedule.fragment.NotesFragment;
import ru.stwtforever.schedule.fragment.SettingsFragment;
import ru.stwtforever.schedule.fragment.TimetableFragment;
import ru.stwtforever.schedule.util.ArrayUtil;
import ru.stwtforever.schedule.util.Util;
import ru.stwtforever.schedule.util.ViewUtil;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener {

    private BottomNavigationView navView;

    private DayTabFragment df = new DayTabFragment();
    private TimetableFragment tf = new TimetableFragment();
    private NotesFragment nf = new NotesFragment();
    private SettingsFragment sf = new SettingsFragment();

    private int selected_id;
    private Fragment selected_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ViewUtil.setStyles(this, true);
        super.onCreate(savedInstanceState);

        checkExtrasData();

        setContentView(R.layout.activity_main);

        initViews();
        applyStyles();

        checkFirstLaunch();
        checkCrash();

        navView.setOnNavigationItemReselectedListener(this);
        navView.setOnNavigationItemSelectedListener(this);
    }

    private void checkExtrasData() {
        if (!getIntent().hasExtra("shortcut_type")) return;
        String sh_type = getIntent().getStringExtra("shortcut_type");

        int f = -1;

        switch (sh_type) {
            case "timetable":
                f = 0;
                break;
            case "schedule":
                f = 1;
                break;
            case "notes":
                f = 2;
                break;
        }

        startActivity(new Intent(this, ShortcutActivity.class).putExtra("fragment", f));
        finish();
    }

    private void checkFirstLaunch() {
        if (Util.isFirstLaunch()) {
            startSetupActivity();
        } else {
            Intent intent = getIntent();

            if (intent.hasExtra("from_settings")) {
                selected_id = R.id.settings;
                selected_fragment = sf;
                replaceFragment(sf);
                navView.setSelectedItemId(selected_id);
            } else {
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

            if (!AppGlobal.preferences.getBoolean(SettingsFragment.KEY_SHOW_ERROR, false)) return;

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle(R.string.warning);

            adb.setMessage(R.string.cause_error);
            adb.setPositiveButton(android.R.string.ok, null);
            adb.setNeutralButton(R.string.show, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                    adb.setTitle(R.string.error_log);
                    adb.setMessage(trace);
                    adb.setPositiveButton(android.R.string.ok, null);
                    adb.setNeutralButton(R.string.copy, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface p1, int p2) {
                            Util.copyText(trace);
                        }
                    });
                    adb.create().show();
                }

            });
            adb.create().show();
        }
    }

    private void applyStyles() {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{-android.R.attr.state_checked},
        };

        int[] colors = new int[]{
                ThemeManager.getIconsSelected(),
                ThemeManager.getIcons()
        };

        ColorStateList myList = new ColorStateList(states, colors);
        navView.setItemIconTintList(myList);
        navView.setItemTextColor(myList);
    }

    private void initViews() {
        navView = findViewById(R.id.nav_view);
    }

    private void setupShortcuts() {
        if (Build.VERSION.SDK_INT < 25) return;
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

        Icon icon = Icon.createWithResource(this, R.drawable.shortcut_schedule);
        Icon icon3 = Icon.createWithResource(this, R.drawable.shortcut_bells);

        ShortcutInfo timetable = new ShortcutInfo.Builder(this, "timetable")
                .setShortLabel(getString(R.string.timetable))
                .setIcon(icon3)
                .setIntent(new Intent(this, MainActivity.class).setAction(Intent.ACTION_DEFAULT).putExtra("shortcut_type", "timetable"))
                .build();

        ShortcutInfo schedule = new ShortcutInfo.Builder(this, "schedule")
                .setShortLabel(getString(R.string.schedule))
                .setIcon(icon)
                .setIntent(new Intent(this, MainActivity.class).setAction(Intent.ACTION_DEFAULT).putExtra("shortcut_type", "schedule"))
                .build();

        ShortcutInfo notes = new ShortcutInfo.Builder(this, "notes")
                .setShortLabel(getString(R.string.notes))
                .setIcon(icon)
                .setIntent(new Intent(this, MainActivity.class).setAction(Intent.ACTION_DEFAULT).putExtra("shortcut_type", "notes"))
                .build();

        ArrayList<ShortcutInfo> shortcuts = new ArrayList<>(2);
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
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        return;
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
