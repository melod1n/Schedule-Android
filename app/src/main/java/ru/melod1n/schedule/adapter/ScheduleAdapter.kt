package ru.melod1n.schedule.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.melod1n.schedule.R
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.base.BaseHolder
import ru.melod1n.schedule.model.ScheduleItem
import java.util.*

class ScheduleAdapter(context: Context, items: ArrayList<ScheduleItem>) : BaseAdapter<ScheduleItem, ScheduleAdapter.ViewHolder>(context, items) {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(view(R.layout.fragment_schedule_item, parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bind(position)
    }

    override fun destroy() {}

    private fun getType(i: Int): String {
        return if (i == 0) "Лекция" else if (i == 1) "Практика" else if (i == 2) "Лабораторная" else "Дополнительное"
    }

    inner class ViewHolder(v: View) : BaseHolder(v) {
        private var type: TextView = v.findViewById(R.id.lessonType)
        private var name: TextView = v.findViewById(R.id.lessonName)
        private var classroom: TextView = v.findViewById(R.id.lessonClassroom)
        private var startTime: TextView = v.findViewById(R.id.lessonStartTime)
        private var endTime: TextView = v.findViewById(R.id.lessonEndTime)
        private var line: View = v.findViewById(R.id.lessonLine)

        override fun bind(position: Int) {
            val item = getItem(position).lesson

            type.text = String.format(Locale.getDefault(), "%d: %s", position + 1, getType(Random().nextInt(4)))

            item.subject?.let {
                name.text = it.title
            }

            item.classroom?.let {
                classroom.text = String.format("%s, %s", it.title, it.building)
            }


            this.startTime.text = "8:00"
            this.endTime.text = "8:40"
        }
    }
}