package tisserand.alexey.justquiz.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import tisserand.alexey.justquiz.R;
import tisserand.alexey.justquiz.adapters.QuizAdapter;
import tisserand.alexey.justquiz.constants.AppConstants;
import tisserand.alexey.justquiz.data.preference.AppPreference;
import tisserand.alexey.justquiz.listeners.ListItemClickListener;
import tisserand.alexey.justquiz.models.quiz.QuizModel;
import tisserand.alexey.justquiz.utilities.ActionBar;
import tisserand.alexey.justquiz.utilities.ActivityUtilities;
import tisserand.alexey.justquiz.utilities.BeatBox;
import tisserand.alexey.justquiz.utilities.DialogUtilities;
import tisserand.alexey.justquiz.utilities.SoundUtilities;


public class QuizActivity extends ActionBar implements DialogUtilities.OnCompleteListener {

    private Activity mActivity;
    private Context mContext;
    private ImageButton btnSpeaker;
    private Button btnNext;
    private RecyclerView mRecyclerQuiz;
    private TextView tvQuestionText;
    private TextView tvQuestionTitle;
    private TextView tvQuestionTrue;
    private TextView tvLife;


    private QuizAdapter mAdapter = null;
    private List<QuizModel> mItemList;
    ArrayList<String> mOptionList;
    ArrayList<String> mBackgroundColorList;
    private Queue queueSkip;

    private int orientation;
    private int mQuestionPosition = 0;
    private int mQuestionsCount = 0;
    private int mSkip = 0, mAnswers = 0, mScore = 0, mLife = 3;
    private int sizeText, sizeTextButton;
    private boolean mUserHasPressed = false;
    private boolean mIsSkipped = false;
    private String mCategoryId;

    private BeatBox mBeatBox;
    private List<SoundUtilities> mSounds;
    private boolean isSoundOn;
    private Vibrator vibrator;
    private boolean isVibrOn;
    private boolean isBgOn;

    private ProgressBar progress;
    private int progressTime = AppConstants.BUNDLE_KEY_TIME;
    private boolean isTimeGame = false;
    private boolean isCommonGame = false;

    private LinearLayout lifeView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initVar();
        initView();
        loadData();
        initListener();
        if (isTimeGame) {
            timeGame();
        }
    }
    private void timeGame(){
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progressTime > 0){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progress.setProgress(progressTime);
                            }
                        });
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        progressTime--;
                    }
                    if (isTimeGame) {
                        activityClose();
                        isTimeGame = false;
                    }
                }
            }).start();
    }





    private void initVar() {
        mActivity = QuizActivity.this;
        mContext = mActivity.getApplicationContext();
        orientation  = Configuration.ORIENTATION_PORTRAIT;

        Intent intent = getIntent();
        if (intent != null) {
            mCategoryId = intent.getStringExtra(AppConstants.BUNDLE_KEY_INDEX);
        }
        if (mCategoryId.equals(AppConstants.BUNDLE_KEY_ALL)){
            isTimeGame = true;
        }
        else if(mCategoryId.equals(AppConstants.BUNDLE_KEY_COMMON)){
            isCommonGame = true;
        }

        mItemList = new ArrayList<>();
        mOptionList = new ArrayList<>();
        mBackgroundColorList = new ArrayList<>();
        queueSkip = new LinkedList();

        mBeatBox = new BeatBox(mActivity);
        mSounds = mBeatBox.getSounds();
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        isVibrOn = AppPreference.getInstance(mActivity).getBoolean(AppConstants.KEY_VIBRO, false);
        isBgOn = AppPreference.getInstance(mActivity).getBoolean(AppConstants.KEY_BACKGROUND, false);

        sizeText = AppPreference.getInstance(mActivity).getInt(AppConstants.KEY_SQ, AppConstants.BUNDLE_KEY_DEFAULT_SQ);
        sizeTextButton = AppPreference.getInstance(mActivity).getInt(AppConstants.KEY_SA, AppConstants.BUNDLE_KEY_DEFAULT_SA);
    }

    private void initView() {
        setContentView(R.layout.activity_quiz);

        initToolbar();

        progress = findViewById(R.id.progress);

        btnSpeaker = findViewById(R.id.btnSpeaker);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setTextSize(sizeTextButton);

        tvQuestionText = findViewById(R.id.tvQuestionText);
        tvQuestionTitle = findViewById(R.id.tvQuestionTitle);
        tvQuestionTrue = findViewById(R.id.tvQuestionTrue);
        tvLife = findViewById(R.id.tvLife);

        lifeView = findViewById(R.id.lifeView);

        mRecyclerQuiz = findViewById(R.id.rvQuiz);
        mRecyclerQuiz.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new QuizAdapter(mContext, mActivity, mOptionList, mBackgroundColorList);
        mRecyclerQuiz.setAdapter(mAdapter);

        if(isCommonGame){
            progress.setVisibility(View.GONE);
        }
        else if (isTimeGame) {
            lifeView.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        } else{
            lifeView.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
        }

        if (isBgOn) {
            setBackgroundImage(orientation);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        orientation = newConfig.orientation;
        if (isBgOn) {
            setBackgroundImage(orientation);
        }
    }

    private void setBackgroundImage(final int orientation)
    {
        LinearLayout layout;
        layout = findViewById(R.id.mainlayout);

        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            layout.setBackgroundResource(R.drawable.ic_bg_horizontal);
        else if (orientation == Configuration.ORIENTATION_PORTRAIT)
            layout.setBackgroundResource(R.drawable.ic_bg_vertical);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (isBgOn)
            setBackgroundImage(orientation);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadData() {
        isSoundOn = AppPreference.getInstance(mActivity).getBoolean(AppConstants.KEY_SOUND, true);
        setSpeakerImage();
        loadJson();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setSpeakerImage() {
        if (isSoundOn) {
            btnSpeaker.setImageResource(R.drawable.ic_speaker);
        } else {
            btnSpeaker.setImageResource(R.drawable.ic_speaker_not);
        }
    }


    public void initListener() {
        btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                isSoundOn = !isSoundOn;
                AppPreference.getInstance(mActivity).setBoolean(AppConstants.KEY_SOUND, isSoundOn);
                setSpeakerImage();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCommonGame && mLife == 0){
                    activityClose();
                } else if (mQuestionsCount == mAnswers)
                {
                    activityClose();
                }
                else if (!mUserHasPressed) {
                    FragmentManager manager = getSupportFragmentManager();
                    DialogUtilities dialog = DialogUtilities.newInstance(getString(R.string.skip_text), getString(R.string.skip_prompt), getString(R.string.yes), getString(R.string.no), AppConstants.BUNDLE_KEY_SKIP_OPTION);
                    dialog.show(manager, AppConstants.BUNDLE_KEY_DIALOG_FRAGMENT);
                }
                else if (mQuestionPosition == mQuestionsCount - 1)
                {
                    mIsSkipped = true;
                    setNextQuestion();
                }
                else {
                    setNextQuestion();
                }
            }
        });

        mAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (!mUserHasPressed) {
                    int clickedAnswerIndex = position;
                    mAnswers++;
                        for (int currentItemIndex = 0; currentItemIndex < mOptionList.size(); currentItemIndex++) {
                            if (currentItemIndex == clickedAnswerIndex && currentItemIndex == mItemList.get(mQuestionPosition).getCorrectAnswer()) {
                                mBackgroundColorList.set(currentItemIndex, AppConstants.COLOR_GREEN);
                                mScore++;
                                if (isSoundOn) {
                                    mBeatBox.play(mSounds.get(AppConstants.BUNDLE_KEY_ZERO_INDEX));
                                }
                            } else if (currentItemIndex == clickedAnswerIndex && !(currentItemIndex == mItemList.get(mQuestionPosition).getCorrectAnswer())) {
                                mBackgroundColorList.set(currentItemIndex, AppConstants.COLOR_RED);

                                if(isTimeGame)
                                    progressTime -= AppConstants.BUNDLE_KEY_THIRD_INDEX;

                                if(isCommonGame)
                                    mLife--;

                                if(isVibrOn && vibrator.hasVibrator())
                                    vibrator.vibrate(AppConstants.BUNDLE_KEY_VIBRATOR);
                                if (isSoundOn) {
                                    mBeatBox.play(mSounds.get(AppConstants.BUNDLE_KEY_SECOND_INDEX));
                                }

                            } else if (currentItemIndex == mItemList.get(mQuestionPosition).getCorrectAnswer()) {
                                mBackgroundColorList.set(currentItemIndex, AppConstants.COLOR_GREEN);
                                mRecyclerQuiz.getLayoutManager().scrollToPosition(currentItemIndex);
                            }
                        }


                    mUserHasPressed = true;
                    mAdapter.notifyDataSetChanged();

                }
                if (mQuestionsCount == mAnswers || mLife == 0)
                {
                    btnNext.setText(R.string.finish);
                }
                if (isTimeGame) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if(mQuestionPosition == mQuestionsCount - 1) {
                                activityClose();
                                isTimeGame = false;
                    }else setNextQuestion();

                        }
                    }, 500);
                }


            }
        });


    }



    public void setNextQuestion() {
        if (isSoundOn) {
            mBeatBox.play(mSounds.get(AppConstants.BUNDLE_KEY_FIRST_INDEX));
        }

        mUserHasPressed = false;

        if (mIsSkipped) {
            mQuestionPosition = (int)queueSkip.remove();
            mSkip--;
        } else {
            mQuestionPosition++;
        }
        updateQuestionsAndAnswers();
    }

    public void updateQuestionsAndAnswers() {
        mOptionList.clear();
        mBackgroundColorList.clear();
        mRecyclerQuiz.getLayoutManager().scrollToPosition(AppConstants.BUNDLE_KEY_ZERO_INDEX);

        mOptionList.addAll(mItemList.get(mQuestionPosition).getAnswers());
        mBackgroundColorList.addAll(mItemList.get(mQuestionPosition).getBackgroundColors());
        mAdapter.notifyDataSetChanged();

        String questionText = mItemList.get(mQuestionPosition).getQuestion();

        tvQuestionText.setText(Html.fromHtml(questionText));
        tvQuestionText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeText);
        if(isBgOn)
            tvQuestionText.setTextColor(Color.WHITE);

        tvQuestionTitle.setText(getString(R.string.quiz_question_title_1,  mQuestionPosition + 1));

        tvQuestionTrue.setText(" " + getString(R.string.quiz_question_title,  mScore));

        tvLife.setText(" " + getString(R.string.quiz_question_title,  mLife));
    }


    public void activityClose() {
        AppPreference.getInstance(mActivity).setInt(AppConstants.PREF_SCORE, mScore);
        ActivityUtilities.getInstance().invokeCommonQuizActivity(mActivity, ResultActivity.class, mCategoryId,  true);
    }

    public void quizActivityClosePrompt() {
        FragmentManager manager = getSupportFragmentManager();
        DialogUtilities dialog = DialogUtilities.newInstance(getString(R.string.exit), getString(R.string.cancel_prompt), getString(R.string.yes), getString(R.string.no), AppConstants.BUNDLE_KEY_CLOSE_OPTION);
        dialog.show(manager, AppConstants.BUNDLE_KEY_DIALOG_FRAGMENT);
    }

    private void loadJson() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(AppConstants.QUESTION_FILE)));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        parseJson(sb.toString());
    }

    public void parseJson(String jsonData) {
        try {

            JSONObject jsonObjMain = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObjMain.getJSONArray(AppConstants.JSON_KEY_QUESTIONNAIRY);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                String question = jsonObj.getString(AppConstants.JSON_KEY_QUESTION);
                int correctAnswer = Integer.parseInt(jsonObj.getString(AppConstants.JSON_KEY_CORRECT_ANS));
                String categoryId = jsonObj.getString(AppConstants.JSON_KEY_CATEGORY_ID);

                Log.d("TAG", categoryId);

                JSONArray jsonArray2 = jsonObj.getJSONArray(AppConstants.JSON_KEY_ANSWERS);
                ArrayList<String> contents = new ArrayList<>();
                ArrayList<String> backgroundColors = new ArrayList<>();
                for (int j = 0; j < jsonArray2.length(); j++) {
                    String item_title = jsonArray2.get(j).toString();
                    contents.add(item_title);
                    backgroundColors.add(AppConstants.COLOR_WHITE);
                }
                if (mCategoryId.equals(AppConstants.BUNDLE_KEY_COMMON) || mCategoryId.equals(AppConstants.BUNDLE_KEY_ALL)){
                    mItemList.add(new QuizModel(question, contents, correctAnswer, categoryId, backgroundColors));
                }

                if (mCategoryId.equals(categoryId)) {
                    mItemList.add(new QuizModel(question, contents, correctAnswer, categoryId, backgroundColors));
                }
            }
            Collections.shuffle(mItemList);

            if(mCategoryId.equals(AppConstants.BUNDLE_KEY_COMMON) || mCategoryId.equals(AppConstants.BUNDLE_KEY_ALL)) {
                while(mItemList.size() != 15)
                    mItemList.remove(0);
            }
            mQuestionsCount = mItemList.size();
            updateQuestionsAndAnswers();

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                quizActivityClosePrompt();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        quizActivityClosePrompt();
    }

    @Override
    public void onComplete(Boolean isOkPressed, String viewIdText) {
        if (isOkPressed && viewIdText.equals(AppConstants.BUNDLE_KEY_CLOSE_OPTION)) {
            activityClose();
            isTimeGame = false;
        }
        else if (isOkPressed) {
            mSkip++;
            if (mQuestionPosition == mQuestionsCount - 1)
                mIsSkipped = true;

            queueSkip.add(mQuestionPosition);
            setNextQuestion();
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }
}
