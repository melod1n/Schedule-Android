package ru.melod1n.schedule.app

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import ru.melod1n.schedule.R
import ru.melod1n.schedule.widget.Button

class AlertBuilder(context: Context?) : AlertDialog.Builder(context!!) {

    private var title: TextView? = null
    private var message: TextView? = null
    private var buttonContainer: LinearLayout? = null
    private var neutral: Button? = null
    private var negative: Button? = null
    private var positive: Button? = null
    private var custom: LinearLayout? = null

    private var neutralClick: View.OnClickListener? = null
    private var negativeClick: View.OnClickListener? = null
    private var positiveClick: View.OnClickListener? = null

    override fun setTitle(title: CharSequence?): AlertDialog.Builder {
        val s = title?.toString() ?: ""
        if (s.isEmpty()) {
            this.title!!.text = ""
            this.title!!.visibility = View.GONE
        } else {
            this.title!!.text = s
            this.title!!.visibility = View.VISIBLE
        }
        return this
    }

    override fun setTitle(titleId: Int): AlertDialog.Builder {
        val title = context.getString(titleId)
        setTitle(if (TextUtils.isEmpty(title)) "" else title)
        return this
    }

    override fun setMessage(message: CharSequence?): AlertDialog.Builder {
        val s = message?.toString() ?: ""
        if (s.isEmpty()) {
            this.message!!.text = ""
            this.message!!.visibility = View.GONE
        } else {
            this.message!!.text = s
            this.message!!.visibility = View.VISIBLE
        }

        this.message!!.rootView.visibility = View.VISIBLE

        return this
    }

    override fun setMessage(messageId: Int): AlertDialog.Builder {
        val message = context.getString(messageId)
        setMessage(if (TextUtils.isEmpty(message)) "" else message)
        return this
    }

    fun setPositiveButton(textId: Int, listener: View.OnClickListener?): AlertBuilder {
        buttonContainer!!.visibility = View.VISIBLE
        positive!!.visibility = View.VISIBLE
        positive!!.setText(textId)
        positive!!.setOnClickListener(listener)
        positiveClick = listener
        return this
    }

    fun setPositiveButton(textId: Int): AlertBuilder {
        setPositiveButton(textId, null as View.OnClickListener?)
        return this
    }

    fun setNegativeButton(textId: Int, listener: View.OnClickListener?): AlertBuilder {
        buttonContainer!!.visibility = View.VISIBLE
        negative!!.visibility = View.VISIBLE
        negative!!.setText(textId)
        negative!!.setOnClickListener(listener)
        negativeClick = listener
        return this
    }

    fun setNegativeButton(textId: Int): AlertBuilder {
        setNegativeButton(textId, null as View.OnClickListener?)
        return this
    }

    fun setNeutralButton(textId: Int, listener: View.OnClickListener?): AlertBuilder {
        buttonContainer!!.visibility = View.VISIBLE
        neutral!!.visibility = View.VISIBLE
        neutral!!.setText(textId)
        neutral!!.setOnClickListener(listener)
        neutralClick = listener
        return this
    }

    fun setNeutralButton(textId: Int): AlertBuilder {
        setNeutralButton(textId, null as View.OnClickListener?)
        return this
    }

    override fun show(): AlertDialog {
        val dialog = create()
        dialog.show()

        neutral!!.setOnClickListener {
            if (neutralClick != null) neutralClick!!.onClick(it)
            dialog.dismiss()
        }
        negative!!.setOnClickListener {
            if (negativeClick != null) negativeClick!!.onClick(it)
            dialog.dismiss()
        }
        positive!!.setOnClickListener {
            if (positiveClick != null) positiveClick!!.onClick(it)
            dialog.dismiss()
        }

        return dialog
    }

    fun setCustomView(v: View) {
        if (custom!!.childCount > 0)  custom!!.removeAllViews()

        custom!!.addView(v)
    }

    init {
        val view = LayoutInflater.from(getContext()).inflate(R.layout.abc_popup_dialog, null, false)
        setView(view)

        title = view.findViewById(R.id.dialogTitle)
        message = view.findViewById(R.id.dialogMessage)
        buttonContainer = view.findViewById(R.id.dialogButtonContainer)
        neutral = view.findViewById(R.id.dialogNeutral)
        negative = view.findViewById(R.id.dialogNegative)
        positive = view.findViewById(R.id.dialogPositive)
        custom = view.findViewById(R.id.dialogCustomLayout)
    }
}