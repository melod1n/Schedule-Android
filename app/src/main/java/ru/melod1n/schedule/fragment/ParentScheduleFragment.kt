package ru.melod1n.schedule.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_parent_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_tabs.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.activity.MainActivity
import ru.melod1n.schedule.adapter.ScheduleParentAdapter
import ru.melod1n.schedule.common.AppGlobal
import ru.melod1n.schedule.common.Engine.getCurrentDate
import ru.melod1n.schedule.common.Engine.getInterim
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.util.Util

class ParentScheduleFragment : Fragment() {

    var searchViewItem: MenuItem? = null
    var isSearchViewCollapsed = true
    private var currentPosition = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_parent_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ButterKnife.bind(this, view)
        prepareToolbar()

        toolbarTabs.tabMode = TabLayout.MODE_SCROLLABLE


        val drawerLayout = (requireActivity() as MainActivity).getDrawerLayout()
        val toggle = (requireActivity() as MainActivity).initToggle(toolbar)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        createPagerAdapter()
    }

    private fun prepareToolbar() {
        toolbar!!.setTitle(R.string.nav_schedule)
        toolbar!!.inflateMenu(R.menu.fragment_main_schedule)
        searchViewItem = toolbar!!.menu.findItem(R.id.schedule_search)

        val searchView = searchViewItem!!.actionView as SearchView

        searchView.queryHint = getString(R.string.title)
        searchView.setOnCloseListener {
            isSearchViewCollapsed = true
            false
        }

        searchView.setOnSearchClickListener { isSearchViewCollapsed = false }
        setToolbarSubtitle(null)
    }

    private fun setToolbarSubtitle(bool: Boolean?) {
        val b = bool ?: AppGlobal.preferences.getBoolean(SettingsFragment.KEY_SHOW_DATE, false)
        val subtitle = if (b) getCurrentDate() else getInterim()
        val string = SpannableString(subtitle)
        string.setSpan(AbsoluteSizeSpan(Util.px(14f)), 0, subtitle.length, 0)
        toolbar!!.subtitle = string
    }

    private fun createPagerAdapter() {
        schedulePager.adapter = ScheduleParentAdapter(requireContext(), childFragmentManager)
        schedulePager.offscreenPageLimit = 5
        toolbarTabs.setupWithViewPager(schedulePager)
        schedulePager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        if (AppGlobal.preferences.getBoolean(SettingsFragment.KEY_SELECT_CURRENT_DAY, false)) schedulePager.currentItem = Util.numOfCurrentDay
    }
}