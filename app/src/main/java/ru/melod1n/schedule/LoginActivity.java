package ru.melod1n.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.current.BaseActivity;
import ru.melod1n.schedule.widget.Button;
import ru.melod1n.schedule.widget.TextArea;
import ru.melod1n.schedule.widget.Toolbar;

public class LoginActivity extends BaseActivity {

    private static final int REQUEST_REGISTRATION = 1;

    @BindView(R.id.button_registration)
    Button buttonRegistration;

    @BindView(R.id.button_login)
    Button buttonLogin;

    @BindView(R.id.input_login)
    TextArea inputLogin;

    @BindView(R.id.input_password)
    TextArea inputPassword;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        toolbar.setTitle(R.string.login);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_backward);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        buttonRegistration.setOnClickListener(v -> openRegistrationScreen());
        buttonLogin.setOnClickListener(v -> tryLogin());
    }

    private void tryLogin() {
        String login = inputLogin.getText().toString();
        String password = inputPassword.getText().toString();

        try {
            int i = 1;
            i /= Integer.parseInt(password);

            i = 1 / Integer.parseInt(login);

            runOnUiThread(() -> {
                setResult(RESULT_OK); //проверка пройдена, успех
                finish();
            });
        } catch (Exception e) {
            runOnUiThread(() -> displayError("Hueta"));
        }

    }

    private void displayError(String description) {
        Snackbar.make((View) inputLogin.getParent(), String.format("%s: %s", getString(R.string.error), description), Snackbar.LENGTH_LONG)
                .setAction(R.string.registration, v -> openRegistrationScreen())
                .setActionTextColor(ThemeEngine.getCurrentTheme().getColorAccent())
                .show();
    }

    private void openRegistrationScreen() {
        startActivityForResult(new Intent(this, RegistrationActivity.class), REQUEST_REGISTRATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_REGISTRATION && resultCode == RESULT_OK) { //успешно зарегались
            setResult(RESULT_OK); //успешно авторизовались
            finish();
        }
    }
}
