package tisserand.alexey.justquiz.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tisserand.alexey.justquiz.R;
import tisserand.alexey.justquiz.utilities.ActivityUtilities;

public class FirstActivity extends AppCompatActivity {

    private Button btn_play, btn_record, btn_info, btn_setting;
    private static long back_pressed;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        activity = FirstActivity.this;

        addListenerOnButton();

    }

    public void addListenerOnButton() {
        btn_play = findViewById(R.id.btn_play);
        btn_play.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtilities.getInstance().invokeNewActivity(activity, SecondActivity.class,  false);
                    }
                }
        );
        btn_record = findViewById(R.id.btn_record);
        btn_record.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtilities.getInstance().invokeNewActivity(activity, RecordActivity.class,  false);
                    }
                }
        );
        btn_info = findViewById(R.id.btn_info);
        btn_info.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtilities.getInstance().invokeNewActivity(activity, AboutAppActivity.class,  false);
                    }
                }
        );
        btn_setting = findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtilities.getInstance().invokeNewActivity(activity, SettingActivity.class,  false);
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), R.string.tap_again, Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}