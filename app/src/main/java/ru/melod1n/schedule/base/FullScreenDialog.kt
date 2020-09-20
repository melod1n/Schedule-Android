package ru.melod1n.schedule.base

import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.ThemeEngine.isDark

abstract class FullScreenDialog<T>(var frManager: FragmentManager, open var item: T?) : DialogFragment() {

    var onActionListener: OnActionListener<T>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        }
        setStyle(STYLE_NORMAL, if (isDark()) R.style.AppTheme_FullScreenDialog_Dark else R.style.AppTheme_FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()

        dialog?.apply {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT

            window?.let {
                it.setLayout(width, height)
                it.setWindowAnimations(R.style.AppTheme_FullScreenDialog_Slide)
                it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            }
        }
    }

    protected abstract fun display(fragmentManager: FragmentManager, item: T?)

    fun show() {
        display(frManager, item)
    }

    interface OnActionListener<T> {
        fun onItemInsert(item: T)
        fun onItemEdit(item: T)
        fun onItemDelete(item: T)
    }
}