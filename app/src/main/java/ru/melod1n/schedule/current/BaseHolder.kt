package ru.melod1n.schedule.current

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseHolder(v: View) : RecyclerView.ViewHolder(v) {
    abstract fun bind(position: Int)
}