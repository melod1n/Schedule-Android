package ru.melod1n.schedule.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.melod1n.schedule.R
import ru.melod1n.schedule.fragment.AgendaFragment
import java.util.*

class AgendaParentAdapter(private val context: Context, fragmentManager: FragmentManager?, private val fragments: ArrayList<AgendaFragment>) : FragmentStatePagerAdapter(fragmentManager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemPosition(o: Any): Int {
        return (o as AgendaFragment).type
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(if (position == 0) R.string.homework else R.string.events)
    }
}