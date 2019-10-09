package ru.melod1n.schedule.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.MainActivity;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.ThemeAdapter;
import ru.melod1n.schedule.items.ThemeItem;

public class UpdatesFragment extends Fragment {

    @BindView(R.id.no_items_container)
    TextView noItems;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.list)
    RecyclerView list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_updates, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        noItems.setText(R.string.no_updates);

        toolbar.setTitle(R.string.nav_updates);

        DrawerLayout drawerLayout = ((MainActivity) getActivity()).getDrawerLayout();

        ActionBarDrawerToggle toggle = ((MainActivity) getActivity()).initToggle(toolbar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ThemeItem light = new ThemeItem();
        light.setSelected(true);
        light.setName("Light");
        light.setEngineVersion(1);
        light.setMadeBy("μSchedule Dev Team");
        light.setColorSurface("#ffffff");
        light.setColorPrimary("#ffffff");
        light.setColorPrimaryDark("#f8f8f8");
        light.setColorAccent("#FF4081");
        light.setColorBackground("#ffffff");
        light.setColorTabsText("#515151");
        light.setColorControlNormal("#121212");
        light.setColorTextPrimary("#121212");
        light.setColorTextSecondary("#707070");
        light.setColorTextPrimaryInverse("#ffffff");
        light.setColorTextSecondaryInverse("#cccccc");

        ThemeItem dark = new ThemeItem();
        dark.setName("Dark");
        dark.setEngineVersion(1);
        dark.setMadeBy("μSchedule Dev Team");
        dark.setDark(true);
        dark.setColorSurface("#121212");
        dark.setColorPrimary("#161616");
        dark.setColorPrimaryDark("#000000");
        dark.setColorAccent("#7C4DFF");
        dark.setColorBackground("#000000");
        dark.setColorTabsText("#B4B4B4");
        dark.setColorControlNormal("#ffffff");
        dark.setColorTextPrimary("#ffffff");
        dark.setColorTextSecondary("#cccccc");
        dark.setColorTextPrimaryInverse("#121212");
        dark.setColorTextSecondaryInverse("#707070");

        ArrayList<ThemeItem> items = new ArrayList<>(Arrays.asList(light, dark));

        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        ThemeAdapter adapter = new ThemeAdapter(this, items);
        list.setAdapter(adapter);

        noItems.setVisibility(View.GONE);

    }
}
