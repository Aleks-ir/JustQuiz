package tisserand.alexey.justquiz.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import tisserand.alexey.justquiz.R;
import tisserand.alexey.justquiz.constants.AppConstants;
import tisserand.alexey.justquiz.utilities.ActionBar;
import tisserand.alexey.justquiz.utilities.ActivityUtilities;

public class SecondActivity extends ActionBar {

    private Button btn_all_play, btn_category, btn_common;
    private ImageButton btn_info;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        activity = SecondActivity.this;

        initToolbar();
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        btn_info = (ImageButton) findViewById(R.id.btn_info);
        btn_info.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertBuilder(R.string.info_modes);
                    }
                });

        btn_common = findViewById(R.id.btn_common);
        btn_common.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtilities.getInstance().invokeCommonQuizActivity(activity, QuizActivity.class, AppConstants.BUNDLE_KEY_COMMON, true);
                    }
                }
        );
        btn_all_play = findViewById(R.id.btn_all_play);
        btn_all_play.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtilities.getInstance().invokeCommonQuizActivity(activity, QuizActivity.class, AppConstants.BUNDLE_KEY_ALL, true);
                    }
                }
        );
        btn_category = findViewById(R.id.btn_category);
        btn_category.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtilities.getInstance().invokeNewActivity(activity, MainActivity.class, false);
                    }
                }
        );
    }

    public void AlertBuilder(int mess) {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(activity);
        a_builder.setMessage(mess)
                .setCancelable(false)
                .setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.show();
    }
}
