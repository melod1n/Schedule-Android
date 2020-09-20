package ru.melod1n.schedule.widget.horizontalcolorpicker

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import ru.melod1n.schedule.R
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.base.BaseHolder
import ru.melod1n.schedule.common.ThemeEngine.isDark

class HorizontalColorPicker : RecyclerView {

    private var mAdapter: Adapter? = null
    private var selectedPosition = 0
    private var listener: OnSelectedColorListener? = null
    var colors: ArrayList<Int>? = null
        private set

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()

    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        val manager = LinearLayoutManager(context)
        manager.orientation = HORIZONTAL
        setHasFixedSize(true)
        layoutManager = manager
    }

    fun setColors(vararg colors: Int) {
        val items = ArrayList<Int>(colors.size)
        this.colors = items
        for (color in colors) {
            items.add(color)
        }
        createAdapter(items)
    }

    fun setColors(items: ArrayList<Int>) {
        colors = items
        createAdapter(items)
    }

    fun setColors(items: List<Int>) {
        setColors(ArrayList(items))
    }

    private fun createAdapter(items: ArrayList<Int>) {
        val array = ArrayList<Item>(items.size)
        for (color in items) {
            array.add(Item(color, false))
        }

        mAdapter = Adapter(context, array)
        adapter = adapter

        mAdapter?.onItemClickListener = object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                mAdapter?.setSelectedPosition(position)
                if (listener != null) listener!!.onSelectedColor(position, mAdapter!!.selectedColor)
            }
        }
    }

    fun setOnChoosedColorListener(listener: OnSelectedColorListener?) {
        this.listener = listener
    }

    var selectedColor: Int
        get() = mAdapter!!.selectedColor
        set(color) {
            mAdapter!!.selectedColor = color
        }

    override fun getLayoutManager(): LinearLayoutManager? {
        return super.getLayoutManager() as LinearLayoutManager?
    }

    fun getSelectedPosition(): Int {
        return selectedPosition
    }

    fun setSelectedPosition(position: Int) {
        mAdapter!!.setSelectedPosition(position)
    }

    internal inner class Item {
        var color = 0
        var isSelected = false

        constructor()
        constructor(color: Int, selected: Boolean) {
            this.color = color
            isSelected = selected
        }
    }

    private inner class Adapter constructor(context: Context, values: ArrayList<Item>) : BaseAdapter<Item, Adapter.ViewHolder>(context, values) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(view(R.layout.abc_widget_colorpicker_horizontal_item, parent))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.bind(position)
        }

        override fun destroy() {}
        var selectedColor: Int
            get() = getItem(selectedPosition).color
            set(color) {
                for (i in values.indices) {
                    val item = values[i]
                    item.isSelected = item.color == color
                    notifyItemChanged(i, item)
                    if (item.isSelected) selectedPosition = i
                }
                smoothScrollToPosition(selectedPosition)
            }

        fun setSelectedPosition(position: Int) {
            for (i in values.indices) {
                val item = values[i]
                item.isSelected = i == position
                notifyItemChanged(i, item)
                if (item.isSelected) selectedPosition = position
            }
            smoothScrollToPosition(selectedPosition)
        }

        inner class ViewHolder(v: View) : BaseHolder(v) {

            var circle: CircleImageView = v.findViewById(R.id.circle)
            var selected: ImageView = v.findViewById(R.id.selected)

            override fun bind(position: Int) {
                val item = getItem(position)
                circle.setImageDrawable(ColorDrawable(item.color))
                circle.borderWidth = 2
                val borderColor = if (isDark()) Color.WHITE else -0xededee
                circle.borderColor = borderColor
                selected.drawable.setTint(borderColor)
                selected.visibility = if (item.isSelected) VISIBLE else INVISIBLE
            }

        }
    }
}