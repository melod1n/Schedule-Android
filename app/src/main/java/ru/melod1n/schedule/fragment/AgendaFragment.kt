package ru.melod1n.schedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.no_items.*
import kotlinx.android.synthetic.main.recycler_view.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.adapter.AgendaAdapter
import ru.melod1n.schedule.api.model.Agenda
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.base.BaseFragment
import java.util.*

class AgendaFragment() : BaseFragment(), BaseAdapter.OnItemClickListener {

    private var adapter: AgendaAdapter? = null

    var type = 0

    internal constructor(type: Int) : this() {
        this.type = type
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_agenda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ButterKnife.bind(this, view)
        noItemsView.setText(R.string.no_agenda)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        refreshLayout.setOnRefreshListener { getHomework() }
        getHomework()
    }

    fun query(text: CharSequence) {
        adapter!!.filter(text.toString())
        checkCount()
    }

    private fun getHomework() {
        val items = ArrayList<Agenda>()
        if (type == TYPE_HOMEWORK) {
            items.add(Agenda("Some Homework Content", "Some Homework Title", "09.05.45", 1))
        } else if (type == TYPE_EVENTS) {
            items.add(Agenda("Some Event Content", "Some Event Title", "12.12.12", 1))
        }

        createAdapter(items)
        checkCount()

        refreshLayout.isRefreshing = false
    }

    private fun createAdapter(values: ArrayList<Agenda>) {
        if (adapter == null) {
            adapter = AgendaAdapter(requireContext(), values).apply {
                onItemClickListener = this@AgendaFragment
            }

            recyclerView.adapter = adapter
            return
        }

        adapter!!.setItems(values)
        adapter!!.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {

    }

    private fun checkCount() {
        noItemsView.isVisible = adapter != null && adapter!!.isEmpty()
    }

    companion object {
        private const val TYPE_HOMEWORK = 0
        private const val TYPE_EVENTS = 1
    }
}