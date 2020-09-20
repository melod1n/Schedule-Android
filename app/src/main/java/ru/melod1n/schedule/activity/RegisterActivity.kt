package ru.melod1n.schedule.activity

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.base.BaseActivity

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        toolbar.setTitle(R.string.registration)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_backward)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        buttonRegistration.setOnClickListener { tryToRegistrate() }
    }

    private fun displayError(description: String) {
        Snackbar.make((inputLogin.parent as View), String.format("%s: %s", getString(R.string.error), description), Snackbar.LENGTH_LONG).show()
    }

    private fun tryToRegistrate() {
        val login = inputLogin!!.text.toString()
        val password = inputPassword!!.text.toString()
        try {
            var i = 1
            i /= password.toInt()
            i = 1 / login.toInt()
            runOnUiThread {
                setResult(RESULT_OK) //проверка пройдена, успех
                finish()
            }
        } catch (e: Exception) {
            runOnUiThread { displayError("Hueta") }
        }
    }
}