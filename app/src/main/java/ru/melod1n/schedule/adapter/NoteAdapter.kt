package ru.melod1n.schedule.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import ru.melod1n.schedule.R
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.base.BaseHolder
import ru.melod1n.schedule.model.Note
import ru.melod1n.schedule.util.ColorUtil
import java.util.*

class NoteAdapter(context: Context, items: ArrayList<Note>) : BaseAdapter<Note, NoteAdapter.ViewHolder>(context, items) {

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        getItem(fromPosition).position = toPosition
        notifyItemMoved(fromPosition, toPosition)
    }

    fun onEndMove(toPosition: Int) {
        //TODO: не сохраняются новые позиции

        //OldCacheStorage.delete(DatabaseHelper.TABLE_NOTES);
        //OldCacheStorage.insert(DatabaseHelper.TABLE_NOTES, getValues());
        //CacheStorage.updateNotesWithDelete(getValues());
        //notifyItemRangeChanged(0, getItemCount(), getItem(toPosition));
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(view(R.layout.fragment_notes_item, viewGroup))
    }

    override fun onQueryItem(item: Note, query: String): Boolean {
        return item.title.toLowerCase(Locale.getDefault()).contains(query) || item.body.toLowerCase(Locale.getDefault()).contains(query)
    }

    override fun destroy() {}

    inner class ViewHolder(v: View) : BaseHolder(v) {
        private var title: TextView = v.findViewById(R.id.noteTitle)
        private var text: TextView = v.findViewById(R.id.noteText)
        private var card: CardView = v.findViewById(R.id.noteCard)

        init {
            card.cardElevation = 4f
        }

        override fun bind(position: Int) {
            val item = getItem(position)
            val color = item.color

            card.setCardBackgroundColor(color)

            val textColor = if (ColorUtil.isDark(color)) Color.WHITE else Color.BLACK

            title.setTextColor(textColor)
            text.setTextColor(textColor)

            title.text = if (item.title.length > 35) "${item.title.substring(0, 35)}..." else item.title
            text.text = if (item.body.length > 120) "${item.body.substring(0, 120)}..." else item.body

            text.visibility = if (text.text.toString().trim().isEmpty()) View.GONE else View.VISIBLE
            title.visibility = if (title.text.toString().trim().isEmpty()) View.GONE else View.VISIBLE
        }


    }
}