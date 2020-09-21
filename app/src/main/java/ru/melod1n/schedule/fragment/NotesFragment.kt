package ru.melod1n.schedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.no_items.*
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.activity.MainActivity
import ru.melod1n.schedule.adapter.NoteAdapter
import ru.melod1n.schedule.api.model.Note
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.common.AppGlobal
import ru.melod1n.schedule.common.TaskManager
import ru.melod1n.schedule.database.CacheStorage
import java.util.*

class NotesFragment : Fragment(), BaseAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private var ith: ItemTouchHelper? = null

    private var manager: StaggeredGridLayoutManager? = null

    private var adapter: NoteAdapter? = null

    private var oneColumn = false

    private var searchView: SearchView? = null

    var searchViewItem: MenuItem? = null

    var isSearchViewCollapsed = true

    override fun onRefresh() {
        loadNotes()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oneColumn = AppGlobal.preferences.getBoolean("two_columns", false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ButterKnife.bind(this, view)
        noItemsView.setText(R.string.no_notes)
        prepareToolbar()
        prepareList()

        prepareRefreshLayout()

        addNote.setOnClickListener { showDialog() }

        val drawerLayout = (activity as MainActivity?)!!.getDrawerLayout()
        val toggle: ActionBarDrawerToggle = (activity as MainActivity?)!!.initToggle(toolbar)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        onRefresh()
    }

    private fun prepareRefreshLayout() {
        refreshLayout.setOnRefreshListener(this)
    }

    private fun prepareList() {
        manager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        manager!!.spanCount = if (oneColumn) 1 else 2

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = manager
        initDragDrop()
    }

    private fun prepareToolbar() {
        toolbar.setTitle(R.string.nav_notes)
        toolbar.inflateMenu(R.menu.fragment_notes)
        toolbar.setOnMenuItemClickListener { item: MenuItem -> onMenuItemClick(item) }
        toolbar.menu.findItem(R.id.notes_columns).setTitle(if (oneColumn) R.string.set_two_columns else R.string.set_one_column)

        searchViewItem = toolbar.menu.findItem(R.id.notes_search)
        searchView = searchViewItem!!.actionView as SearchView

        searchView!!.queryHint = getString(R.string.title)

        searchView!!.setOnCloseListener {
            isSearchViewCollapsed = true

            false
        }
        searchView!!.setOnSearchClickListener { isSearchViewCollapsed = false }
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter!!.filter(newText)
                checkCount()
                return true
            }
        })
    }

    private fun initDragDrop() {
        val callback: ItemTouchHelper.Callback = object : ItemTouchHelper.Callback() {

            var dragFrom = -1
            var dragTo = -1

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                if (dragFrom == -1) {
                    dragFrom = fromPosition
                }
                dragTo = toPosition
                if (fromPosition < toPosition) {
                    for (i in fromPosition until toPosition) {
                        Collections.swap(adapter!!.values, i, i + 1)
                    }
                } else {
                    for (i in fromPosition downTo toPosition + 1) {
                        Collections.swap(adapter!!.values, i, i - 1)
                    }
                }
                adapter!!.onItemMove(fromPosition, toPosition)
                return true
            }

            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN or ItemTouchHelper.UP or if (manager!!.spanCount == 2) ItemTouchHelper.START or ItemTouchHelper.END else 0)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
                    adapter!!.onEndMove(dragTo)
                }
                dragTo = -1
                dragFrom = dragTo
            }
        }
        ith = ItemTouchHelper(callback)
        ith!!.attachToRecyclerView(recyclerView)
    }

    private fun loadNotes() {
        TaskManager.execute {
            val notes = CacheStorage.getNotes()

            requireActivity().runOnUiThread {
                createAdapter(ArrayList(notes))
                checkCount()

                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun showDialog(position: Int = -1) {

    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId == R.id.notes_columns) {
            if (manager == null) return false

            oneColumn = manager!!.spanCount == 2

            AppGlobal.preferences.edit().putBoolean("two_columns", oneColumn).apply()

            manager!!.spanCount = if (oneColumn) 1 else 2

            ith!!.attachToRecyclerView(null)

            initDragDrop()

            item.setTitle(if (oneColumn) R.string.set_two_columns else R.string.set_one_column)
        }
        return true
    }

    private fun createAdapter(values: ArrayList<Note>) {
        if (adapter == null) {
            adapter = NoteAdapter(requireContext(), values).apply {
                onItemClickListener = this@NotesFragment
            }

            recyclerView.adapter = adapter
            return
        }
        adapter!!.setItems(values)
        adapter!!.notifyDataSetChanged()
    }

    private fun checkCount() {
        toolbar.menu.getItem(0).isVisible = adapter != null && adapter!!.isNotEmpty()

        noItemsView.isVisible = adapter != null && adapter!!.isEmpty()
    }

    override fun onItemClick(position: Int) {
        showDialog(position)
    }
}