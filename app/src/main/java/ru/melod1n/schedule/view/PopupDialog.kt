package ru.melod1n.schedule.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.model.ThemeItem

class PopupDialog : DialogFragment() {
    private var title: CharSequence? = null
    private var message: CharSequence? = null
    private var theme: ThemeItem? = null

    fun show(fragmentManager: FragmentManager) {
        val dialog = PopupDialog()
        dialog.show(fragmentManager, PopupDialog::class.java.simpleName)
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        theme = currentTheme
        if (dialog != null && dialog!!.window != null) {
            val background = ContextCompat.getDrawable(requireContext(), R.drawable.popup_dialog_bg)
            dialog!!.window!!.setBackgroundDrawable(background)
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return inflater.inflate(R.layout.abc_popup_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleTextView = view.findViewById<TextView>(R.id.dialogTitle)
        val messageTextView = view.findViewById<TextView>(R.id.dialogMessage)
        titleTextView.text = title
        messageTextView.text = message
    }

    fun setTitle(title: CharSequence?) {
        this.title = title
    }

    fun setTitle(@StringRes title: Int) {
        if (context != null) setTitle(getString(title))
    }

    fun setMessage(message: CharSequence?) {
        this.message = message
    }

    fun setMessage(@StringRes message: Int) {
        if (context != null) setMessage(getString(message))
    }

    companion object {
        val instance: PopupDialog
            get() = PopupDialog()
    }
}