package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.MainActivity;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.AgendaAdapter;
import ru.melod1n.schedule.common.ThemeManager;
import ru.melod1n.schedule.items.HomeworkItem;

public class AgendaFragment extends Fragment {

    @BindView(R.id.list)
    RecyclerView list;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

//    @BindView(R.id.fab_add)
//    FloatingActionButton add;

    @BindView(R.id.no_items_container)
    TextView noItems;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AgendaAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, view);

        noItems.setText(R.string.no_agenda);

        toolbar.setTitle(R.string.nav_agenda);
//        toolbar.inflateMenu(R.menu.activity_main);
//        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);

        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        refresh.setProgressBackgroundColorSchemeColor(ThemeManager.getBackground());
        refresh.setColorSchemeColors(ThemeManager.getMain());
        refresh.setOnRefreshListener(this::getHomework);

        DrawerLayout drawerLayout = ((MainActivity) getActivity()).getDrawerLayout();

        ActionBarDrawerToggle toggle = ((MainActivity) getActivity()).initToggle(toolbar, view);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getHomework();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(Object[] data) {
        switch ((String) data[0]) {
//            case "add_subject":
//                LessonItem item = (LessonItem) data[1];
//                adapter.getValues().add(new HomeworkItem(item.getHomework(), item.getName(), "22.08.19", item.getPosition()));
//                adapter.notifyItemInserted(adapter.getItemCount() - 1);
//                adapter.notifyItemRangeChanged(0, adapter.getItemCount(), -1);
//                break;
//            case "delete_subject":
//                String homework = (String) data[1];
//                for (int i = 0; i < adapter.getItemCount(); i++) {
//                    HomeworkItem homeworkItem = adapter.getItem(i);
//                    if (homeworkItem.text.equals(homework)) {
//                        adapter.getValues().remove(i);
//                        adapter.notifyItemRemoved(i);
//                        adapter.notifyItemRangeChanged(0, adapter.getItemCount(), -1);
//                        break;
//                    }
//                }
//                break;
        }
    }

    private void getHomework() {
        ArrayList<HomeworkItem> items = new ArrayList<>();

        items.add(new HomeworkItem("Some Homework", "Some Lesson", "22.08.19", 1));


        createAdapter(items);
        checkCount();

        refresh.setRefreshing(false);
    }

    private void createAdapter(ArrayList<HomeworkItem> values) {
        if (adapter == null) {
            adapter = new AgendaAdapter(this, values);
            adapter.setOnItemClickListener(this::onItemClick);
            list.setAdapter(adapter);
            return;
        }

        adapter.changeItems(values);
        adapter.notifyDataSetChanged();
    }

    private void onItemClick(View view, int i) {

    }

    private void checkCount() {
        noItems.setVisibility(adapter == null ? View.VISIBLE : adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.settings) {
            if (getActivity() == null) return false;

            ((MainActivity) getActivity()).replaceFragment(new SettingsFragment());
            return true;
        }

        return false;
    }
}
