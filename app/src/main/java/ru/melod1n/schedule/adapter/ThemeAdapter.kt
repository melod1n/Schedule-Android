package ru.melod1n.schedule.adapter

import android.content.Context
import android.graphics.Color
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import org.apache.commons.lang3.StringUtils
import ru.melod1n.schedule.R
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.base.BaseHolder
import ru.melod1n.schedule.model.ThemeItem
import ru.melod1n.schedule.util.ColorUtil
import java.util.*

class ThemeAdapter(context: Context, values: ArrayList<ThemeItem>) : BaseAdapter<ThemeItem, ThemeAdapter.ViewHolder>(context, values) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(view(R.layout.theme_engine_layout, parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun destroy() {}

    inner class ViewHolder(v: View) : BaseHolder(v) {

        private var title: TextView = v.findViewById(R.id.themeTitle)
        private var card: CardView = v.findViewById(R.id.themeCard)

        override fun bind(position: Int) {
            val item = getItem(position)

            val colorPrimary = item.colorPrimary

            val text = if (!StringUtils.isEmpty(item.author)) {
                HtmlCompat.fromHtml(String.format("<b>%s</b> – %s", item.title, item.author), HtmlCompat.FROM_HTML_MODE_COMPACT)
            } else {
                HtmlCompat.fromHtml(String.format("<b>%s</b>", item.title), HtmlCompat.FROM_HTML_MODE_COMPACT)
            }

            title.setTextColor(if (ColorUtil.isLight(colorPrimary)) Color.BLACK else Color.WHITE)
            title.text = text

            card.setCardBackgroundColor(colorPrimary)
            card.setOnClickListener {
                if (onItemClickListener != null) {
                    onItemClickListener!!.onItemClick(position)
                }
            }
        }


    }
}