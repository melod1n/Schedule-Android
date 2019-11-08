package ru.melod1n.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.fragment.MainScheduleFragment;
import ru.melod1n.schedule.fragment.NotesFragment;

public class ShortcutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);

        Intent i = getIntent();
        int fragment = i.getIntExtra("fragment", -1);
        Fragment f;

        switch (fragment) {
            default:
            case 1:
                f = new MainScheduleFragment();
                break;
            case 2:
                f = new NotesFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(@NonNull Object[] data) {
        String key = (String) data[0];
        if (key.equals("theme_update")) {
            recreate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
