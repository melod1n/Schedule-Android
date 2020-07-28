package ru.melod1n.schedule.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.current.BaseAdapter;
import ru.melod1n.schedule.current.BaseHolder;
import ru.melod1n.schedule.items.Agenda;

public class AgendaAdapter extends BaseAdapter<Agenda, AgendaAdapter.ViewHolder> {

    public AgendaAdapter(Context context, ArrayList<Agenda> values) {
        super(context, values);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.fragment_agenda_item, parent, false));
    }

    @Override
    public boolean onQueryItem(Agenda item, String lowerQuery) {
        if (item.getTitle().toLowerCase().contains(lowerQuery) || item.getText().toLowerCase().contains(lowerQuery))
            return true;

        return super.onQueryItem(item, lowerQuery);
    }

    @Override
    public void destroy() {

    }

    class ViewHolder extends BaseHolder {


        @BindView(R.id.homework_text)
        TextView homework;

        @BindView(R.id.homework_date)
        TextView date;

        @BindView(R.id.homework_subject)
        TextView subject;

        @BindView(R.id.homework_card)
        MaterialCardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(int position) {
            Agenda item = getItem(position);

            cardView.setCardBackgroundColor(ThemeEngine.getCurrentTheme().getColorSurface());

            date.setText(item.getDate());
            subject.setText(item.getTitle().replace("\n", " "));

            String homework = item.getText();
            if (homework.length() > 450) {
                homework = homework.substring(0, 446) + "...";
            }

            this.homework.setText(homework);
        }
    }
}
