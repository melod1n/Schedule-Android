package ru.melod1n.schedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.no_items.*
import kotlinx.android.synthetic.main.recycler_view.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.adapter.ScheduleAdapter
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.common.AppGlobal
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.TaskManager
import ru.melod1n.schedule.model.ScheduleItem

class ScheduleFragment() : Fragment(), BaseAdapter.OnItemClickListener {

    private var adapter: ScheduleAdapter? = null
    var day = 0

    constructor(i: Int) : this() {
        day = i
    }

    override fun onItemClick(position: Int) {
        showDialog(position)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        noItemsView.setText(R.string.no_lessons)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        refreshLayout.setOnRefreshListener { getSubjects() }

        fabAdd.setOnClickListener { showDialog() }

        getSubjects()
    }

    private fun checkCount() {
        noItemsView.visibility = if (adapter == null) View.VISIBLE else if (adapter!!.values.size == 0) View.VISIBLE else View.GONE
    }

    private fun showDialog(position: Int = -1) {

    }

    private fun getSubjects() {
        TaskManager.executeNow {
            val lessons = AppGlobal.database.lessons.getAll()

            val items = arrayListOf<ScheduleItem>().apply {
                for (lesson in lessons) add(ScheduleItem(lesson))
            }

            requireActivity().runOnUiThread {
                createAdapter(items)
                checkCount()
                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun createAdapter(values: ArrayList<ScheduleItem>) {
        if (adapter == null) {
            adapter = ScheduleAdapter(requireContext(), values)
            adapter!!.onItemClickListener = this
            recyclerView.adapter = adapter
            return
        }

        adapter!!.setItems(values)
        adapter!!.notifyDataSetChanged()
    }

}