package ru.melod1n.schedule.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import ru.melod1n.schedule.R
import ru.melod1n.schedule.current.BaseAdapter
import ru.melod1n.schedule.current.BaseHolder
import ru.melod1n.schedule.items.UpdateItem
import ru.melod1n.schedule.widget.CardView
import ru.melod1n.schedule.widget.TextPlain
import java.util.*

class UpdateAdapter(context: Context, values: ArrayList<UpdateItem>) : BaseAdapter<UpdateItem, UpdateAdapter.ViewHolder>(context, values) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.fragment_updates_item, parent, false))
    }

    inner class ViewHolder(var v: View) : BaseHolder(v) {

        var info: TextPlain = v.findViewById(R.id.updateInfo)
        var card: CardView = v.findViewById(R.id.updateInfoContainer)

        override fun bind(position: Int) {
            ButterKnife.bind(this, v)

            val item = getItem(position)

            info.text = if (item.changes.length > 256) item.changes.subSequence(0, 256) else item.changes

        }
    }



    override fun destroy() {

    }

}