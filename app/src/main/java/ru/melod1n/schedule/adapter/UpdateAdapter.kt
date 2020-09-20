package ru.melod1n.schedule.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.ButterKnife
import ru.melod1n.schedule.R
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.base.BaseHolder
import ru.melod1n.schedule.model.UpdateItem
import ru.melod1n.schedule.widget.CardView
import java.util.*

class UpdateAdapter(context: Context, values: ArrayList<UpdateItem>) : BaseAdapter<UpdateItem, UpdateAdapter.ViewHolder>(context, values) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(view(R.layout.fragment_updates_item, parent))
    }

    inner class ViewHolder(var v: View) : BaseHolder(v) {

        var info: TextView = v.findViewById(R.id.updateInfo)

        override fun bind(position: Int) {
            ButterKnife.bind(this, v)

            val item = getItem(position)

            info.text = if (item.changes.length > 256) item.changes.subSequence(0, 256) else item.changes

        }
    }

    override fun destroy() {}

}