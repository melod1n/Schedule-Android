package ru.melod1n.schedule.base

import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject

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

    fun TextInputLayout.text(trim: Boolean = true): String {
        return if (trim) editText?.text.toString().trim()
        else editText?.text.toString()
    }

}