package ru.melod1n.schedule;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.current.BaseActivity;
import ru.melod1n.schedule.widget.Button;
import ru.melod1n.schedule.widget.TextArea;
import ru.melod1n.schedule.widget.Toolbar;

public class RegistrationActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.button_registration)
    Button buttonRegistration;

    @BindView(R.id.input_login)
    TextArea inputLogin;

    @BindView(R.id.input_password)
    TextArea inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this);

        toolbar.setTitle(R.string.registration);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_backward);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        buttonRegistration.setOnClickListener(v -> tryToRegistrate());
    }

    private void displayError(String description) {
        Snackbar.make((View) inputLogin.getParent(), String.format("%s: %s", getString(R.string.error), description), Snackbar.LENGTH_LONG).show();
    }

    private void tryToRegistrate() {
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


}
