package tisserand.alexey.justquiz.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import tisserand.alexey.justquiz.R;
import tisserand.alexey.justquiz.constants.AppConstants;
import tisserand.alexey.justquiz.data.preference.AppPreference;
import tisserand.alexey.justquiz.utilities.ActionBar;

public class SettingActivity extends ActionBar implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener  {
    private Switch  btn_sw_1,  btn_sw_2,  btn_sw_3,  btn_sw_4;
    private SeekBar sBar_1, sBar_2;
    private ImageButton btn_default;
    private int sizeQuestion, sizeAnswer;
    private boolean isVibrOn, isSoundOn, isBgOn;
    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        initToolbar();
        initVar();
        initView();
        addListenerOnButton();

    }
    private void initVar() {
        mActivity = SettingActivity.this;
        isSoundOn = AppPreference.getInstance(mActivity).getBoolean(AppConstants.KEY_SOUND, true);
        isVibrOn = AppPreference.getInstance(mActivity).getBoolean(AppConstants.KEY_VIBRO, true);
        isBgOn = AppPreference.getInstance(mActivity).getBoolean(AppConstants.KEY_BACKGROUND, false);
        sizeQuestion = AppPreference.getInstance(mActivity).getInt(AppConstants.KEY_SQ, AppConstants.BUNDLE_KEY_DEFAULT_SQ);
        sizeAnswer = AppPreference.getInstance(mActivity).getInt(AppConstants.KEY_SA, AppConstants.BUNDLE_KEY_DEFAULT_SA);

        btn_default = findViewById(R.id.btn_default);
        sBar_1 = findViewById(R.id.sBar_1);
        sBar_2 = findViewById(R.id.sBar_2);
        btn_sw_1 = findViewById(R.id. btn_sw_1);
        btn_sw_2 = findViewById(R.id. btn_sw_2);
        btn_sw_4 = findViewById(R.id. btn_sw_4);

    }
    private void initView(){
        sBar_1.setProgress(sizeQuestion - 15);
        sBar_2.setProgress(sizeAnswer - 10);
        btn_sw_1.setChecked(isVibrOn);
        btn_sw_2.setChecked(isSoundOn);
        //btn_sw_3.setChecked(isThemeDark);
        btn_sw_4.setChecked(isBgOn);
    }
    public void addListenerOnButton() {
        btn_default.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertBuilder(R.string.settings_clear, R.string.close_title);
                    }
                }
        );
        sBar_1.setOnSeekBarChangeListener(this);
        sBar_2.setOnSeekBarChangeListener(this);
        btn_sw_1.setOnCheckedChangeListener(this);
        btn_sw_2.setOnCheckedChangeListener(this);
        //btn_sw_3.setOnCheckedChangeListener(this);
        btn_sw_4.setOnCheckedChangeListener(this);
    }

    public void AlertBuilder(int mess, int title) {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(mActivity);
        a_builder.setMessage(mess)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isSoundOn = false;
                        isVibrOn = false;
                        isBgOn = false;
                        sizeQuestion = AppConstants.BUNDLE_KEY_DEFAULT_SQ;
                        sizeAnswer = AppConstants.BUNDLE_KEY_DEFAULT_SA;

                        initView();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle(title);
        alert.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.btn_sw_1:
                isVibrOn = isChecked;
                break;
            case R.id.btn_sw_2:
                isSoundOn = isChecked;
                break;
            case R.id.btn_sw_3:

                break;
            case R.id.btn_sw_4:
                isBgOn = isChecked;
                break;
        }

        Toast.makeText(this, isChecked ? "Включено" : "Выключено", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.getId() == R.id.sBar_1) {
            sizeQuestion = seekBar.getProgress() + 15;
            Toast.makeText(this,  "Шрифт вопросов: " + String.valueOf(seekBar.getProgress() + 15), Toast.LENGTH_SHORT).show();
        }
        else {
            sizeAnswer = seekBar.getProgress() + 10;
            Toast.makeText(this,  "Шрифт ответов: " + String.valueOf(seekBar.getProgress() + 10), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        AppPreference.getInstance(mActivity).setBoolean(AppConstants.KEY_SOUND, isSoundOn);
        AppPreference.getInstance(mActivity).setBoolean(AppConstants.KEY_VIBRO, isVibrOn);
        AppPreference.getInstance(mActivity).setBoolean(AppConstants.KEY_BACKGROUND, isBgOn);
        AppPreference.getInstance(mActivity).setInt(AppConstants.KEY_SQ, sizeQuestion);
        AppPreference.getInstance(mActivity).setInt(AppConstants.KEY_SA, sizeAnswer);
    }

}
