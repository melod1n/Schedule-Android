package ru.melod1n.schedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.fragment_parent_agenda.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_tabs.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.activity.MainActivity
import ru.melod1n.schedule.adapter.AgendaParentAdapter
import java.util.*

class ParentAgendaFragment : Fragment() {

    var searchViewItem: MenuItem? = null

    private var searchView: SearchView? = null

    var isSearchViewCollapsed = true

    private var visibleFragment: AgendaFragment? = null
    private var currentPosition = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_parent_agenda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ButterKnife.bind(this, view)
        prepareToolbar()

        toolbarTabs.tabGravity = TabLayout.GRAVITY_FILL
        toolbarTabs.tabMode = TabLayout.MODE_FIXED

        val drawerLayout = (activity as MainActivity).getDrawerLayout()
        val toggle = (requireActivity() as MainActivity).initToggle(toolbar)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        createPagerAdapter()
    }

    private fun prepareToolbar() {
        toolbar!!.setTitle(R.string.nav_agenda)
        toolbar!!.inflateMenu(R.menu.fragment_agenda)
        searchViewItem = toolbar!!.menu.findItem(R.id.agenda_search)

        val searchView = searchViewItem!!.actionView as SearchView

        searchView.queryHint = getString(R.string.title)
        searchView.setOnCloseListener {
            isSearchViewCollapsed = true
            false
        }

        searchView.setOnSearchClickListener { isSearchViewCollapsed = false }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query(newText)
                return true
            }
        })

        this.searchView = searchView
    }

    private fun query(text: String) {
        if (visibleFragment != null) {
            visibleFragment!!.query(text)
        }
    }

    private fun createPagerAdapter() {
        val fragments = ArrayList(listOf(AgendaFragment(0), AgendaFragment(1)))
        agendaPager.adapter = AgendaParentAdapter(requireContext(), childFragmentManager, fragments)
        toolbarTabs.setupWithViewPager(agendaPager)
        toolbarTabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                visibleFragment = fragments[tab.position]
                currentPosition = tab.position
                if (searchView != null && !isSearchViewCollapsed) {
                    query(searchView!!.query.toString())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        visibleFragment = fragments[0]
    }
}