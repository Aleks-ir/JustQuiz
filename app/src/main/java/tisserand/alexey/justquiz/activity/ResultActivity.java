package tisserand.alexey.justquiz.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import tisserand.alexey.justquiz.R;
import tisserand.alexey.justquiz.adapters.RecordAdapter;
import tisserand.alexey.justquiz.constants.AppConstants;
import tisserand.alexey.justquiz.data.preference.AppPreference;
import tisserand.alexey.justquiz.utilities.ActionBar;
import tisserand.alexey.justquiz.utilities.ActivityUtilities;

public class ResultActivity extends ActionBar {
    private TextView txtShow, txtMessage;
    private String mCategoryId;
    private String Name = "";
    private Button btn_main_menu, btn_repeat, btn_record;
    final Context context = this;
    private Activity activity;
    private int mScore;
    private boolean isTimeGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initToolbar();
        initVar();
        initView();
        checkRecords();
        addListenerOnButton();
    }
    private void initVar(){
        activity = ResultActivity.this;
        mScore = AppPreference.getInstance(activity).getInt(AppConstants.PREF_SCORE, AppConstants.BUNDLE_KEY_ZERO_INDEX);
        mCategoryId = getIntent().getStringExtra(AppConstants.BUNDLE_KEY_INDEX);

        btn_main_menu = findViewById(R.id.btn_main_menu);
        btn_repeat = findViewById(R.id.btn_repear);
        btn_record = findViewById(R.id.btn_record);
        txtShow = findViewById(R.id.text_score);

    }

    public void initView(){

        btn_main_menu.setCompoundDrawablesWithIntrinsicBounds(
                AppCompatResources.getDrawable(this, R.drawable.ic_main_menu), null, null, null);


        btn_repeat.setCompoundDrawablesWithIntrinsicBounds(
                AppCompatResources.getDrawable(this, R.drawable.ic_repeat), null, null, null);

        btn_record.setCompoundDrawablesWithIntrinsicBounds(
                AppCompatResources.getDrawable(this, R.drawable.ic_record), null, null, null);


        txtShow.setText(String.valueOf(mScore));
        txtMessage = findViewById(R.id.text_message);
        if(mScore >= 10 && mScore < 15)
            txtMessage.setText(R.string.greeting_text2);
        else if (mScore >= 15)
            txtMessage.setText(R.string.greeting_text3);
    }
    public void addListenerOnButton() {
        btn_main_menu.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtilities.getInstance().invokeNewActivity(activity, FirstActivity.class, true);
                    }
                });
        btn_repeat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtilities.getInstance().invokeCommonQuizActivity(activity, QuizActivity.class, mCategoryId, true);
                    }
                });
        btn_record.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtilities.getInstance().invokeNewActivity(activity, RecordActivity.class, false);
                    }
                });
    }


    public void checkRecords(){
        if (mCategoryId.equals(AppConstants.BUNDLE_KEY_ALL) || mCategoryId.equals(AppConstants.BUNDLE_KEY_COMMON))
            if(mScore != 0)
                writeRecord();
    }

    public void writeRecord(){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt_record, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(promptsView);
        final EditText userInput = promptsView.findViewById(R.id.input_text);
        userInput.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RecordAdapter ra = new RecordAdapter (context);
                                Name = userInput.getText().toString().trim();
                                if(!Name.equals("")){
                                    // Добавляем новые значения
                                    ra.addRecord(Name, mScore);
                                    // Записываем рекорды в файл
                                    ra.writeRecords();
                                }
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        ActivityUtilities.getInstance().invokeNewActivity(ResultActivity.this, FirstActivity.class, true);
    }
}
