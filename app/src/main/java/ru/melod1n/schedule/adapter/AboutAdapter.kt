package ru.melod1n.schedule.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import ru.melod1n.schedule.R
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.base.BaseHolder
import ru.melod1n.schedule.model.About
import java.util.*

class AboutAdapter(context: Context, items: ArrayList<About>) : BaseAdapter<About, AboutAdapter.ViewHolder>(context, items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(view(R.layout.activity_about_item, parent))
    }

    override fun destroy() {}

    inner class ViewHolder(v: View) : BaseHolder(v) {
        private var icon: ImageView = v.findViewById(R.id.aboutIcon)
        private var name: TextView = v.findViewById(R.id.aboutName)
        private var job: TextView = v.findViewById(R.id.aboutJob)
        private var card: CardView = v.findViewById(R.id.aboutCard)

        override fun bind(position: Int) {
            val item = getItem(position)

            Picasso.get().load(item.icon).resize(128, 128).config(Bitmap.Config.ARGB_8888).into(icon)

            name.text = item.name

            job.text = item.job

            card.setOnClickListener {
                context.startActivity(Intent(
                        Intent.ACTION_VIEW, Uri.parse(item.link)
                ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })
            }
        }
    }
}