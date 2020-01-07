package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.AgendaAdapter;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.items.AgendaItem;
import ru.melod1n.schedule.widget.RefreshLayout;

public class AgendaFragment extends Fragment {

    @BindView(R.id.list)
    RecyclerView list;

    @BindView(R.id.refresh)
    RefreshLayout refresh;

    @BindView(R.id.no_items_container)
    TextView noItems;

    private AgendaAdapter adapter;

    private int type;

    private static final int TYPE_HOMEWORK = 0;
    private static final int TYPE_EVENTS = 1;

    public AgendaFragment() {
    }

    AgendaFragment(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

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

        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        refresh.setOnRefreshListener(this::getHomework);

        getHomework();
    }

    void query(@NotNull CharSequence text) {
        adapter.filter(text.toString());
        checkCount();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(EventInfo info) {
    }

    private void getHomework() {
        ArrayList<AgendaItem> items = new ArrayList<>();

        if (type == TYPE_HOMEWORK) {
            items.add(new AgendaItem("Some Homework Content", "Some Homework Title", "09.05.45", 1));
        } else if (type == TYPE_EVENTS) {
            items.add(new AgendaItem("Some Event Content", "Some Event Title", "12.12.12", 1));
        }

        createAdapter(items);
        checkCount();

        refresh.setRefreshing(false);
    }

    private void createAdapter(ArrayList<AgendaItem> values) {
        if (adapter == null) {
            adapter = new AgendaAdapter(this, values);
            adapter.setOnItemClickListener(this::onItemClick);
            adapter.setOnItemLongClickListener(this::onItemClick);
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
}
