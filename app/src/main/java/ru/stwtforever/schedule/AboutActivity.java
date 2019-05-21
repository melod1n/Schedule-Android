package ru.stwtforever.schedule;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ru.stwtforever.schedule.adapter.AboutAdapter;
import ru.stwtforever.schedule.adapter.items.AboutItem;
import ru.stwtforever.schedule.common.AppGlobal;
import ru.stwtforever.schedule.common.ThemeManager;
import ru.stwtforever.schedule.helper.FontHelper;
import ru.stwtforever.schedule.util.ViewUtil;

public class AboutActivity extends AppCompatActivity {

    private TextView version, name;
    private RecyclerView list;
    private FloatingActionButton close;

    private AboutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ViewUtil.applyWindowStyles(getWindow(), getColor(ThemeManager.isDark() ? R.color.about_dark_primary : R.color.about_primary));
        setTheme(ThemeManager.getAboutTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();

        LinearLayoutManager manager = new LinearLayoutManager(this);

        if (!getResources().getBoolean(R.bool.is_tablet) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager.setOrientation(RecyclerView.HORIZONTAL);
        } else {
            manager.setOrientation(RecyclerView.VERTICAL);
        }

        LinearLayout l = ((LinearLayout) close.getParent());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            l.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        } else {
            l.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        }

        list.setHasFixedSize(true);
        list.setLayoutManager(manager);

        name.setTypeface(FontHelper.getFont(FontHelper.PS_MEDIUM));
        version.setTypeface(FontHelper.getFont(FontHelper.PS_REGULAR));

        String version_ = getString(R.string.version_about, AppGlobal.app_version_name, AppGlobal.app_version_code);
        version.setText(version_);

        @ColorInt int vkColor = ContextCompat.getColor(this, R.color.vk);

        ArrayList<AboutItem> items = new ArrayList<>();
        items.add(new AboutItem(R.drawable.ic_computer, getString(R.string.danil_nikolaev), getString(R.string.creator_programmer_dn), R.drawable.vk_circle, vkColor, "VK", "https://vk.com/id362877006"));
        items.add(new AboutItem(R.drawable.ic_fz_paint, "@dilongs", getString(R.string.creative_designer), R.drawable.vk_circle, vkColor, "VK", "https://vk.com/dilongs"));
        items.add(new AboutItem(R.drawable.krethub, "@krethub", getString(R.string.creative_designer), R.drawable.vk_circle, vkColor, "VK", "https://vk.com/krethub"));
        items.add(new AboutItem(R.drawable.ic_handler, "@handlerug", getString(R.string.api_help_hp), R.drawable.vk_circle, vkColor, "VK", "https://vk.com/handlerug"));

        adapter = new AboutAdapter(this, items);
        list.setAdapter(adapter);

        close.getDrawable().setTint(ThemeManager.isDark() ? Color.WHITE : Color.BLACK);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        close = findViewById(R.id.close);
        version = findViewById(R.id.app_version);
        name = findViewById(R.id.app_name);
        list = findViewById(R.id.list);
    }
}
