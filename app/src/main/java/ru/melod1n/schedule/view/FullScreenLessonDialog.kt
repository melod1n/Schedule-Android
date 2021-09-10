package ru.melod1n.schedule.view

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import butterknife.BindView
import butterknife.ButterKnife
import ru.melod1n.schedule.R
import ru.melod1n.schedule.base.FullScreenDialog
import ru.melod1n.schedule.common.ThemeEngine
import ru.melod1n.schedule.common.ThemeEngine.isDark
import ru.melod1n.schedule.model.Lesson
import ru.melod1n.schedule.widget.horizontalcolorpicker.HorizontalColorPicker
import ru.melod1n.schedule.widget.horizontalcolorpicker.OnSelectedColorListener

class FullScreenLessonDialog(fragmentManager: FragmentManager, item: Lesson?) : FullScreenDialog<Lesson>(fragmentManager, item) {

    @JvmField
    @BindView(R.id.toolbar)
    var toolbar: Toolbar? = null

    @JvmField
    @BindView(R.id.subject_name)
    var name: AppCompatEditText? = null

    @JvmField
    @BindView(R.id.subject_cab)
    var cab: AppCompatEditText? = null

    @JvmField
    @BindView(R.id.subject_homework)
    var homework: AppCompatEditText? = null

    @JvmField
    @BindView(R.id.picker)
    var picker: HorizontalColorPicker? = null

    private var edit = false
    private var color = 0

    override fun display(fragmentManager: FragmentManager, item: Lesson?) {
        this.item = item
        edit = item != null

        this.show(fragmentManager, TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        picker!!.setColors(*if (isDark()) ThemeEngine.COLOR_PALETTE_DARK else ThemeEngine.COLOR_PALETTE_LIGHT)
        if (edit) {
//            picker.setSelectedPosition(item.getPosition());
            color = picker!!.selectedColor
        } else {
            picker!!.selectedColor = Color.BLACK
            color = Color.BLACK
        }
        picker!!.setOnChoosedColorListener(object : OnSelectedColorListener {
            override fun onSelectedColor(position: Int, color: Int) {
                this@FullScreenLessonDialog.color = color
            }

        })

        if (edit) {
//            name.setText(item.getName());
//            cab.setText(item.getCab());
//            homework.setText(item.getHomework());
            name!!.setSelection(name!!.text!!.length)
        }
        toolbar!!.setNavigationOnClickListener { dismiss() }
        val color = if (isDark()) Color.WHITE else Color.BLACK
        toolbar!!.navigationIcon!!.setTint(color)
        toolbar!!.inflateMenu(R.menu.fragment_schedule_alert)
        for (i in 0 until toolbar!!.menu.size()) {
            val item = toolbar!!.menu.getItem(i)
            if (item.icon != null) item.icon.setTint(color)
        }
        toolbar!!.setOnMenuItemClickListener { i: MenuItem ->
            when (i.itemId) {
                R.id.subject_delete -> showConfirmDeleteDialog()
                R.id.subject_save -> trySave()
            }
            true
        }
        name!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p1: CharSequence, p2: Int, p3: Int, p4: Int) {}
            override fun onTextChanged(p1: CharSequence, p2: Int, p3: Int, p4: Int) {
                toolbar!!.menu.findItem(R.id.subject_save).isVisible = !name!!.text.toString().trim { it <= ' ' }.isEmpty()
                if (name!!.lineCount == 3) {
                    cab!!.requestFocus()
                    name!!.setText(name!!.text.toString().trim { it <= ' ' })
                }
            }

            override fun afterTextChanged(p1: Editable) {}
        })

        toolbar!!.menu.findItem(R.id.subject_save).isVisible = name!!.text.toString().trim().isNotEmpty()
    }


    //TODO: доделать
    private fun trySave() {
        val name_ = name!!.text.toString().trim()
        val cab_ = cab!!.text.toString().trim()
        val hw_ = homework!!.text.toString().trim()
        if (!edit) item = Lesson()


//        item.setName(name_);
//        item.setCab(cab_);
//        item.setHomework(hw_);
//        item.setPosition(picker.getSelectedPosition());
        if (onActionListener != null) {
            if (edit) {
                onActionListener!!.onItemEdit(item!!)
            } else {
                onActionListener!!.onItemInsert(item!!)
            }
        }
        if (dialog != null) dialog!!.dismiss()
    }

    private fun showConfirmDeleteDialog() {
        val adb = AlertDialog.Builder(requireActivity())
        adb.setMessage(R.string.confirm_delete_text)
        adb.setNegativeButton(R.string.no, null)
        adb.setPositiveButton(R.string.yes) { _: DialogInterface?, _: Int ->
            if (edit && onActionListener != null) {
                onActionListener!!.onItemDelete(item!!)
            }

//            if (!item.getHomework().isEmpty())
//                EventBus.getDefault().postSticky(new Object[]{"delete_subject", item.getHomework()});
            dismiss()
        }
        adb.create().show()
    }

    companion object {
        private const val TAG = "fullscreen_lesson_dialog"
    }
}