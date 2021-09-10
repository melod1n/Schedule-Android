package ru.melod1n.schedule.base

import androidx.fragment.app.Fragment
import org.json.JSONObject
import ru.melod1n.schedule.model.ThemeItem
import java.util.*

object Extensions {

    fun Fragment.runOnUiThread(runnable: Runnable) {
        requireActivity().runOnUiThread(runnable)
    }

    fun Fragment.runOnUiThread(runnable: () -> Unit) {
        requireActivity().runOnUiThread(runnable)
    }

    fun JSONObject?.isEmpty(): Boolean {
        return this == null || length() == 0
    }

    fun JSONObject?.isNotEmpty(): Boolean {
        return !isEmpty()
    }

    fun ThemeItem.isEquals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is ThemeItem) return false
        if (other === this) return true

        return id.toLowerCase(Locale.getDefault()) == other.id.toLowerCase(Locale.getDefault())
    }

}