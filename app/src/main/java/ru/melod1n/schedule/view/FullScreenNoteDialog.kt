package ru.melod1n.schedule.view

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import butterknife.BindView
import butterknife.ButterKnife
import ru.melod1n.schedule.R
import ru.melod1n.schedule.base.FullScreenDialog
import ru.melod1n.schedule.common.ThemeEngine
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.common.ThemeEngine.isDark
import ru.melod1n.schedule.model.Note
import ru.melod1n.schedule.util.ColorUtil.darkenColor
import ru.melod1n.schedule.util.ViewUtil.applyWindowStyles
import ru.melod1n.schedule.widget.horizontalcolorpicker.HorizontalColorPicker
import ru.melod1n.schedule.widget.TextArea
import ru.melod1n.schedule.widget.horizontalcolorpicker.OnSelectedColorListener
import java.util.*

class FullScreenNoteDialog(fragmentManager: FragmentManager, item: Note?) : FullScreenDialog<Note>(fragmentManager, item) {

    @JvmField
    @BindView(R.id.note_title)
    var title: TextArea? = null

    @JvmField
    @BindView(R.id.note_text)
    var text: TextArea? = null

    @JvmField
    @BindView(R.id.toolbar)
    var toolbar: Toolbar? = null

    @JvmField
    @BindView(R.id.note_dialog_root)
    var root: LinearLayout? = null

    @JvmField
    @BindView(R.id.picker)
    var picker: HorizontalColorPicker? = null

    private var edit = false
    private var color = 0

    override fun display(fragmentManager: FragmentManager, item: Note?) {
        this.item = item
        edit = item != null

        this.show(fragmentManager, TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notes_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        //TODO: говно, надо переделать
        val colors: MutableList<Int> = ArrayList()
        if (isDark()) {
            for (color in ThemeEngine.COLOR_PALETTE_DARK) {
                colors.add(color)
            }
        } else {
            for (color in ThemeEngine.COLOR_PALETTE_LIGHT) {
                colors.add(color)
            }
        }
        picker!!.setColors(colors)

//        picker.setColors(ThemeEngine.isDark() ? ThemeEngine.COLOR_PALETTE_DARK : ThemeEngine.COLOR_PALETTE_LIGHT);
        color = try {
            if (!edit) DEFAULT_COLOR else item!!.color
        } catch (e: Exception) {
            DEFAULT_COLOR
        }
        picker!!.selectedColor = color
        setColor(color)
        picker!!.setOnChoosedColorListener(object : OnSelectedColorListener {
            override fun onSelectedColor(position: Int, color: Int) {
                this@FullScreenNoteDialog.color = color
                setColor(color)
            }

        })

        toolbar!!.setNavigationOnClickListener { p1: View? -> dismiss() }
        val navigationColor = if (isDark()) Color.WHITE else Color.BLACK
        toolbar!!.navigationIcon!!.setTint(navigationColor)
        if (edit) {
            title!!.setText(item!!.title)
            text!!.setText(item!!.body)
            title!!.setSelection(title!!.text!!.length)
        }
        val delete = toolbar!!.menu.add(R.string.delete)
        delete.setIcon(R.drawable.ic_trash_outline)
        delete.icon.setTint(navigationColor)
        delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        toolbar!!.setOnMenuItemClickListener { i: MenuItem ->
            if (i.title.toString() == getString(R.string.delete)) {
                showConfirmDeleteDialog()
            }
            true
        }
    }

    private fun setColor(primary: Int) {
        color = primary
        if (dialog == null || dialog!!.window == null) return
        val w = dialog!!.window
        val primaryDark = darkenColor(primary)
        applyWindowStyles(w!!, primaryDark)
        w.setBackgroundDrawable(ColorDrawable(primary))
        var visibility = 0
        if (!currentTheme!!.isDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                visibility += View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                }
            }
        }
        w.decorView.systemUiVisibility = visibility
    }

    private fun showConfirmDeleteDialog() {
        if (onActionListener != null && edit) onActionListener!!.onItemDelete(item!!)
        dismiss()
        //        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
//        adb.setMessage(R.string.confirm_delete_text);
//        adb.setNegativeButton(R.string.no, null);
//        adb.setPositiveButton(R.string.yes, (dialog, which) -> {
//
//        });
//
//        adb.create().show();
    }

    override fun onDismiss(dialog: DialogInterface) {
        val title_ = title!!.text.toString().trim { it <= ' ' }
        val text_ = text!!.text.toString().trim { it <= ' ' }
        if (title_.isEmpty() && text_.isEmpty()) {
            super.onDismiss(dialog)
            return
        }
        if (color == 0) color = DEFAULT_COLOR
        if (!edit) {
            item = Note(0, "", "", "", 0, "")
        }
        item!!.title = title_
        item!!.body = text_
        item!!.color = picker!!.selectedColor
        if (onActionListener != null) {
            if (edit) {
                onActionListener!!.onItemEdit(item!!)
            } else {
                onActionListener!!.onItemInsert(item!!)
            }
        }
        super.onDismiss(dialog)
    }

    companion object {
        private const val TAG = "fullscreen_note_dialog"
        private val DEFAULT_COLOR = if (isDark()) ThemeEngine.COLOR_PALETTE_DARK[0] else ThemeEngine.COLOR_PALETTE_LIGHT[0]
    }
}