package ru.melod1n.schedule.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.no_items.*
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.activity.MainActivity
import ru.melod1n.schedule.adapter.UpdateAdapter
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.model.UpdateItem
import java.util.*

class UpdatesFragment : Fragment(), OnRefreshListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnItemLongClickListener {

    private var adapter: UpdateAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_updates, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ButterKnife.bind(this, view)

        noItemsView.setText(R.string.no_updates)

        toolbar.setTitle(R.string.nav_updates)

        val drawerLayout = (requireActivity() as MainActivity).drawerLayout
        val toggle = (requireActivity() as MainActivity).initToggle(toolbar)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        noItemsView.visibility = View.GONE
        refreshLayout.setOnRefreshListener(this)

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        prepareData()
    }

    private fun prepareData() {
        val items = arrayListOf<UpdateItem>()

        for (i in 0..19) {
            items.add(UpdateItem("Это хуйня из списка #${i + 1} (Мне надоело писать заглушки)", "https://vk.com/id${i + 1}"))
        }

        createAdapter(items)
    }

    private fun createAdapter(items: ArrayList<UpdateItem>) {
        adapter = UpdateAdapter(requireContext(), items)
        adapter!!.onItemClickListener = this
        adapter!!.onItemLongClickListener = this

        recyclerView.adapter = adapter
        refreshLayout.isRefreshing = false
    }

    override fun onItemClick(position: Int) {
        val url = adapter!!.values[position].url

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int) {
        val menu = PopupMenu(requireContext(), requireView())

        menu.gravity = Gravity.TOP or Gravity.END
        menu.inflate(R.menu.fragment_updates_card)
        menu.show()
    }

    override fun onRefresh() {
        prepareData()
    }
}