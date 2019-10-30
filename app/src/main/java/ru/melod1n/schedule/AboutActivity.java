package ru.melod1n.schedule;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.adapter.AboutAdapter;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.current.BaseActivity;
import ru.melod1n.schedule.items.AboutItem;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.app_version)
    TextView version;

    @BindView(R.id.app_name)
    TextView name;

    @BindView(R.id.list)
    RecyclerView list;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        LinearLayoutManager manager = new LinearLayoutManager(this);

        if (!getResources().getBoolean(R.bool.is_tablet) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager.setOrientation(RecyclerView.HORIZONTAL);
        } else {
            manager.setOrientation(RecyclerView.VERTICAL);
        }

        list.setHasFixedSize(true);
        list.setLayoutManager(manager);

        String version_ = getString(R.string.version_about, AppGlobal.app_version_name, AppGlobal.app_version_code);
        version.setText(version_);

        ArrayList<AboutItem> items = new ArrayList<>();
        items.add(new AboutItem(R.drawable.ic_computer, getString(R.string.danil_nikolaev), getString(R.string.main_developer), "https://t.me/melod1n"));
        items.add(new AboutItem(R.drawable.innomax, getString(R.string.max_hubach), getString(R.string.backend_logic_developer), "https://t.me/innomaxx"));

        AboutAdapter adapter = new AboutAdapter(this, items);
        list.setAdapter(adapter);
    }
}
