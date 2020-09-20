package ru.melod1n.schedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.no_items.*
import kotlinx.android.synthetic.main.recycler_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.melod1n.schedule.R
import ru.melod1n.schedule.adapter.ScheduleAdapter
import ru.melod1n.schedule.common.AppGlobal
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.TaskManager
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.base.FullScreenDialog
import ru.melod1n.schedule.database.CacheStorage
import ru.melod1n.schedule.model.Lesson
import ru.melod1n.schedule.view.FullScreenLessonDialog
import java.util.*

class ScheduleFragment() : Fragment(), BaseAdapter.OnItemClickListener {

    private var adapter: ScheduleAdapter? = null
    var day = 0

    constructor(i: Int) : this() {
        day = i
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onReceive(info: EventInfo<*>) {
        val key = info.key
        if (EventInfo.KEY_THEME_UPDATE == key) {
            adapter?.createColors()

            getSubjects()
        }
    }

    override fun onItemClick(position: Int) {
//        val builder = AlertBuilder(requireContext())
//        builder.setTitle("Hello")
//        builder.setMessage("It's message")
//        builder.show()

        showDialog(position)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
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
        if (adapter == null) return

        val dialog = FullScreenLessonDialog(parentFragmentManager, if (position == -1) null else adapter!!.getItem(position))
        dialog.onActionListener = object : FullScreenDialog.OnActionListener<Lesson> {
            override fun onItemEdit(item: Lesson) {
                CacheStorage.updateLesson(item)
                adapter!!.notifyItemChanged(position)
            }

            override fun onItemInsert(item: Lesson) {
                CacheStorage.insertLesson(item)

                adapter!!.add(item)
                adapter!!.notifyDataSetChanged()
//                if (position == -1) adapter!!.values.add(item) else adapter!!.values.add(position, item)

//                adapter!!.notifyItemInserted(if (position == -1) adapter!!.itemCount - 1 else position)

//                adapter!!.notifyItemRangeChanged(0, adapter!!.itemCount - 1, -1)

                checkCount()
            }

            override fun onItemDelete(item: Lesson) {
                CacheStorage.deleteLesson(item)
                adapter!!.removeAt(position)
                adapter!!.notifyDataSetChanged()
//                adapter!!.notifyItemRemoved(position)
//                adapter!!.notifyItemRangeChanged(0, adapter!!.itemCount - 1, -1)
                checkCount()
                Snackbar.make(recyclerView!!, R.string.note_delete_title, Snackbar.LENGTH_LONG).setAction(android.R.string.cancel) { onItemInsert(item) }.show()
            }
        }
    }

    private fun getSubjects() {
        TaskManager.execute {
            val lessons = AppGlobal.database.lessons.getAll()
            requireActivity().runOnUiThread {
                createAdapter(ArrayList(lessons))
                checkCount()
                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun createAdapter(values: ArrayList<Lesson>) {
        if (adapter == null) {
            adapter = ScheduleAdapter(requireContext(), values)
            adapter!!.onItemClickListener = this
            recyclerView.adapter = adapter
            return
        }

        adapter!!.setItems(values)
        adapter!!.notifyItemRangeChanged(0, adapter!!.itemCount, -1)
    }

}