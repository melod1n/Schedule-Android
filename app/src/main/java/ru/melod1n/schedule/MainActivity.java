package ru.melod1n.schedule;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.current.BaseActivity;
import ru.melod1n.schedule.fragment.AgendaFragment;
import ru.melod1n.schedule.fragment.MainScheduleFragment;
import ru.melod1n.schedule.fragment.NotesFragment;
import ru.melod1n.schedule.fragment.ScheduleFragment;
import ru.melod1n.schedule.fragment.SettingsFragment;
import ru.melod1n.schedule.fragment.UpdatesFragment;
import ru.melod1n.schedule.util.ArrayUtil;
import ru.melod1n.schedule.util.Util;
import ru.melod1n.schedule.widget.DrawerToggle;
import ru.melod1n.schedule.widget.NavigationDrawer;
import ru.melod1n.schedule.widget.Toolbar;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_LOGIN = 1;

    @BindView(R.id.navigationView)
    BottomNavigationView navView;

    @BindView(R.id.navigationDrawer)
    NavigationDrawer navDrawer;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    private MainScheduleFragment mainScheduleFragment = new MainScheduleFragment();
    private NotesFragment notesFragment = new NotesFragment();
    private AgendaFragment homeworkFragment = new AgendaFragment();
    private UpdatesFragment updatesFragment = new UpdatesFragment();

    private int selectedId;
    private Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        applyBackground();

        checkFirstLaunch(savedInstanceState);

        checkCrash();
        initNavDrawer();

        navDrawer.setNavigationItemSelectedListener(this::onDrawerItemSelected);
        navView.setOnNavigationItemSelectedListener(this::onItemSelected);
    }

    private void initNavDrawer() {
        View headerView = navDrawer.getHeaderView(0);

        headerView.setOnClickListener(v -> openLoginScreen());
    }

    private void openLoginScreen() {
        startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_LOGIN);
    }

    @NonNull
    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public DrawerToggle initToggle(Toolbar toolbar) {
        return new DrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
    }

    private void checkFirstLaunch(Bundle savedInstanceState) {
        if (!Util.isFirstLaunch()) {
            startActivity(new Intent(this, SetupActivity.class));
            finish();
        } else {
            if (savedInstanceState == null) {
                int i = Integer.parseInt(AppGlobal.preferences.getString(SettingsFragment.KEY_OPEN_ON_START, "1"));
                switch (i) {
                    default:
                    case 0:
                    case 1:
                        selectedId = R.id.nav_schedule;
                        break;
                    case 2:
                        selectedId = R.id.nav_notes;
                        break;
                }

                replaceFragment(getFragmentById(selectedId));
                navView.setSelectedItemId(selectedId);
            }

            setupShortcuts();
        }
    }

    private void checkCrash() {
        if (AppGlobal.preferences.getBoolean("isCrashed", false)) {
            final String trace = AppGlobal.preferences.getString("crashLog", "");
            AppGlobal.preferences.edit().putBoolean("isCrashed", false).putString("crashLog", "").apply();

            if (!AppGlobal.preferences.getBoolean(SettingsFragment.KEY_SHOW_ERROR, true)) return;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.warning);

            builder.setMessage(R.string.cause_error);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setNeutralButton(R.string.show, (p1, p2) -> {
                AlertDialog.Builder adb1 = new AlertDialog.Builder(MainActivity.this);
                adb1.setTitle(R.string.error_log);
                adb1.setMessage(trace);
                adb1.setPositiveButton(android.R.string.ok, null);
                adb1.setNeutralButton(R.string.copy, (p11, p21) -> Util.copyText(trace));
                adb1.create().show();
            });
            builder.create().show();
        }
    }

    private void setupShortcuts() {
        if (Build.VERSION.SDK_INT < 100) return;
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

        if (shortcutManager != null) {
            shortcutManager.setDynamicShortcuts(shortcuts);
        }
    }

    public void replaceFragment(Fragment fragment) {
        if (fragment == null || fragment == selectedFragment) return;

        selectedFragment = fragment;

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

    private boolean onItemSelected(@NonNull MenuItem item) {
        selectedId = item.getItemId();

        if (!((getVisibleFragment() != null && getVisibleFragment().getClass().getSimpleName().equals(ScheduleFragment.class.getSimpleName())) && selectedId == R.id.nav_schedule)) {
            replaceFragment(getFragmentById(selectedId));
            return true;
        }

        return false;
    }

    private boolean onDrawerItemSelected(@NonNull MenuItem item) {
        selectedId = item.getItemId();

        replaceFragment(getFragmentById(selectedId));

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Nullable
    private Fragment getVisibleFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.isEmpty()) return null;

        for (Fragment fragment : fragments) {
            if (fragment.isVisible()) return fragment;
        }

        return null;
    }

    private Fragment getFragmentById(int navId) {
        switch (navId) {
            case R.id.nav_notes:
                return notesFragment;
            case R.id.nav_agenda:
                return homeworkFragment;
            case R.id.nav_updates:
                return updatesFragment;
            case R.id.drawer_settings:
                openSettingsScreen();
                return selectedFragment;
            case R.id.drawer_about:
                openAboutScreen();
                return selectedFragment;
            default:
                return mainScheduleFragment;
        }
    }

    private void openSettingsScreen() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void openAboutScreen() {
        startActivity(new Intent(this, AboutActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) { //успешно авторизовались
            Toast.makeText(this, "Successful login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment visibleFragment = getVisibleFragment();
        if (visibleFragment != null && visibleFragment.getClass().getSimpleName().equals(SettingsFragment.class.getSimpleName())) {
            replaceFragment(getFragmentById(navView.getSelectedItemId()));
        } else if (visibleFragment != null && visibleFragment.getClass().getSimpleName().equals(AgendaFragment.class.getSimpleName()) && !((AgendaFragment) visibleFragment).isSearchViewCollapsed()) {
            ((AgendaFragment) visibleFragment).getSearchViewItem().collapseActionView();
        } else if (visibleFragment != null && visibleFragment.getClass().getSimpleName().equals(NotesFragment.class.getSimpleName()) && !((NotesFragment) visibleFragment).isSearchViewCollapsed()) {
            ((NotesFragment) visibleFragment).getSearchViewItem().collapseActionView();
        } else if (visibleFragment != null && visibleFragment.getClass().getSimpleName().equals(MainScheduleFragment.class.getSimpleName()) && !((MainScheduleFragment) visibleFragment).isSearchViewCollapsed()) {
            ((MainScheduleFragment) visibleFragment).getSearchViewItem().collapseActionView();
        } else {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        AppGlobal.saveData();
        super.onDestroy();
    }
}
