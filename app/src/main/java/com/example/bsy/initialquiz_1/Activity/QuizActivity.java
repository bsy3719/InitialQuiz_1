package com.example.bsy.initialquiz_1.Activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsy.initialquiz_1.Database.QuizBaseHelper;
import com.example.bsy.initialquiz_1.Item.MethodUtils;
import com.example.bsy.initialquiz_1.Item.Quiz;
import com.example.bsy.initialquiz_1.R;
import com.example.bsy.initialquiz_1.databinding.ActivityQuizBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private ActivityQuizBinding mBinding;

    private ArrayList<Quiz> quizs = new ArrayList<Quiz>();

    private MethodUtils methodUtils = MethodUtils.getInstance();

    private InterstitialAd mInterstitialAd;

    private int mCurrentIndex = 0;
    private int time = 60; // progressBar 값
    private int add = 1; // 증가량, 방향
    private int answerCnt = 0;
    private int adCnt = 0;

    private Handler handler; // Thread 에서 화면에 그리기 위해서 필요
    private Thread thread;
    private Boolean isRunning = true;
    private static final String TAG = "@@@";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);

        Log.d(TAG, "onCreate");
        settingDB();

        //완료버튼으로 처리하기
        mBinding.answerEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mBinding.answerEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                String answer = mBinding.answerEditText.getText().toString();
                Toast toast;

                //정답을 맞췄을 경우
                if (answer.equals(quizs.get(mCurrentIndex).getAnswer())) {

                    answerCnt = ++answerCnt;
                    mBinding.answerTextView.setText(String.valueOf(answerCnt));

                    toast = Toast.makeText(QuizActivity.this, String.valueOf(quizs.get(mCurrentIndex).getId()) +"번문제 정답!!!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 150);
                    toast.show();

                    updateQuiz();

                } else {

                    toast = Toast.makeText(QuizActivity.this, "오답!!!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 150);
                    toast.show();

                }

                return true;
            }

        });

        //동영상 광고 버튼
        mBinding.interstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("answerCnt", String.valueOf(answerCnt));
                startActivity(intent);
                finish();
            }
        });

        /*handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(QuizActivity.this, "0초", Toast.LENGTH_SHORT).show();
            }
        };*/

    }
    //onCreate 끝

    private void runProgressBar(){
        handler = new Handler();
        isRunning = true;

        // 앱시작시, Thread 를 시작해서 ProgressBar 를 증가시키기
        thread = new Thread(new Runnable() {
            @Override
            public void run() { // Thread 로 작업할 내용을 구현
                while(isRunning) {
                    try {

                        //Log.d(TAG, "쓰레드 실행중");

                        Thread.sleep(1000); // 시간지연

                        time = time - add;

                        //handler.sendMessage(handler.obtainMessage());
                        /*mBinding.timerProgressBar.setProgress(time);
                        mBinding.timerTextView.setText(String.valueOf(time));*/

                        handler.post(new Runnable() {
                            @Override
                            public void run() { // 화면에 변경하는 작업을 구현
                                mBinding.timerProgressBar.setProgress(time);
                                mBinding.timerTextView.setText(String.valueOf(time));
                            }
                        });


                    } catch (InterruptedException e) { }

                    if (time == 0){
                        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                        intent.putExtra("answerCnt", String.valueOf(answerCnt));
                        startActivity(intent);
                        finish();
                        break;
                    }

                } // end of while
            }
        });

        thread.start(); // 쓰레드 시작

    }

    private void updateQuiz() {

        Random random = new Random();

        mBinding.answerEditText.setText("");

        //mCurrentIndex = (mCurrentIndex + 1) % quizs.size();
        mCurrentIndex = random.nextInt(quizs.size());
        mBinding.initialTextView.setText(methodUtils.getInitial(quizs.get(mCurrentIndex).getAnswer()));
        mBinding.hint1TextView.setText(quizs.get(mCurrentIndex).getHint_1());
        mBinding.hint2TextView.setText(quizs.get(mCurrentIndex).getHint_2());

    }

    private void settingDB() {
        //DB활성화
        QuizBaseHelper quizBaseHelper = new QuizBaseHelper(this);

        quizs = quizBaseHelper.getAllQuiz();
    }

    private void resetQuiz(){
        time = 60;
        answerCnt = 0;

        mBinding.timerProgressBar.setProgress(time);
        mBinding.timerTextView.setText(String.valueOf(time));
        mBinding.answerTextView.setText(String.valueOf(answerCnt));

        isRunning = false;
        thread.interrupt();
        thread = null;
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        updateQuiz();
        runProgressBar();
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");

        //키보드 자동실행
        mBinding.answerEditText.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager manager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                manager.showSoftInput(mBinding.answerEditText, 0);
            }
        }, 100);

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        resetQuiz();
        super.onDestroy();
    }

}
