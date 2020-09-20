package ru.melod1n.schedule.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.melod1n.schedule.R
import ru.melod1n.schedule.fragment.ScheduleFragment

class ScheduleParentAdapter(private val context: Context, fragmentManager: FragmentManager?) : FragmentStatePagerAdapter(fragmentManager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return NUM_ITEMS
    }

    override fun getItem(position: Int): Fragment {
        return ScheduleFragment(position)
    }

    override fun getItemPosition(o: Any): Int {
        return (o as ScheduleFragment).day
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getStringArray(R.array.days)[position]
    }

    companion object {
        private const val NUM_ITEMS = 6
    }
}