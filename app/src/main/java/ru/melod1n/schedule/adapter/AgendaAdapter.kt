package ru.melod1n.schedule.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import ru.melod1n.schedule.R
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.base.BaseHolder
import ru.melod1n.schedule.api.model.Agenda
import java.util.*

class AgendaAdapter(
        context: Context,
        values: ArrayList<Agenda>
) : BaseAdapter<Agenda, AgendaAdapter.ViewHolder>(context, values) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(view(R.layout.fragment_agenda_item, parent))
    }

    override fun onQueryItem(item: Agenda, query: String): Boolean {
        return item.title.toLowerCase(Locale.getDefault()).contains(query) ||
                item.text.toLowerCase(Locale.getDefault()).contains(query)
    }

    override fun destroy() {}

    inner class ViewHolder(v: View) : BaseHolder(v) {

        private var homework: TextView = v.findViewById(R.id.agendaText)
        private var date: TextView = v.findViewById(R.id.agendaDate)
        private var subject: TextView = v.findViewById(R.id.agendaSubject)
        private var card: MaterialCardView = v.findViewById(R.id.agendaCard)

        override fun bind(position: Int) {
            val item = getItem(position)

            date.text = item.date

            subject.text = item.title.replace("\n", " ")

            homework.text = if (item.text.length > 450) "${item.text.substring(0, 446)}..." else item.text
        }
    }
}