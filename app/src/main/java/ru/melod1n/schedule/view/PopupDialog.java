package ru.melod1n.schedule.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;

public class PopupDialog extends DialogFragment {

    private CharSequence title;
    private CharSequence message;

    @BindView(R.id.alertTitle)
    TextView titleTextView;

    @BindView(R.id.message)
    TextView messageTextView;

    private boolean created;
    private boolean needUpdate;

    public void show(FragmentManager fragmentManager) {
        PopupDialog dialog = new PopupDialog();
        dialog.show(fragmentManager, PopupDialog.class.getSimpleName());
        if (needUpdate) {
            needUpdate = false;
            dialog.update();
        }
    }

    public static PopupDialog getInstance() {
        return new PopupDialog();
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout root = (LinearLayout) inflater.inflate(R.layout.abc_modal_dialog, container);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//
//        TextView textView = new TextView(getContext());
//        textView.setText("Hui");
//
//        LinearLayout layout = new LinearLayout(getContext());
//        layout.addView(textView);
//        layout.addView(root);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        created = true;
    }

    private void update() {
        setTitle();
        setMessage();
    }

    private void setTitle() {
        if (created) {
            titleTextView.setText(title);
        } else {
            needUpdate = true;
        }
    }

    private void setMessage() {
        if (created) {
            messageTextView.setText(message);
        } else {
            needUpdate = true;
        }
    }

    public void setTitle(CharSequence title) {
        this.title = title;
        setTitle();
    }

    public void setMessage(CharSequence message) {
        this.message = message;
        setMessage();
    }
}
