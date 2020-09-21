package ru.melod1n.schedule.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.base.BaseActivity

class LoginActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        toolbar.setTitle(R.string.login)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_backward)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        buttonRegistration.setOnClickListener { openRegistrationScreen() }
        buttonLogin.setOnClickListener { tryLogin() }
    }

    private fun tryLogin() {
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

    private fun displayError(description: String) {
        Snackbar.make((inputLogin.parent as View), String.format("%s: %s", getString(R.string.error), description), Snackbar.LENGTH_LONG)
                .setAction(R.string.registration) { openRegistrationScreen() }
//                .setActionTextColor(currentTheme!!.colorAccent)
                .show()
    }

    private fun openRegistrationScreen() {
        startActivityForResult(Intent(this, RegisterActivity::class.java), REQUEST_REGISTRATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_REGISTRATION && resultCode == RESULT_OK) { //успешно зарегались
            setResult(RESULT_OK) //успешно авторизовались
            finish()
        }
    }

    companion object {
        private const val REQUEST_REGISTRATION = 1
    }
}