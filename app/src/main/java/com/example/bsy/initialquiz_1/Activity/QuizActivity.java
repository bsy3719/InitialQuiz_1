package com.example.bsy.initialquiz_1.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
    private static final String TAG = "@@@";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);

        Log.d(TAG, "onCreate");
        settingQuiz();

        //전면광고
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest request = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(request);

        //전면광고 reload하는 부분
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mBinding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            }
        });

        //동영상 광고 버튼
        mBinding.rewardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //전면 광고 버튼
        mBinding.interstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
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

        // 앱시작시, Thread 를 시작해서 ProgressBar 를 증가시키기
        thread = new Thread(new Runnable() {
            @Override
            public void run() { // Thread 로 작업할 내용을 구현
                while(true) {
                    try {

                        Log.d(TAG, "쓰레드 실행중");

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

    private void settingQuiz() {
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

        thread.interrupt();
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
        resetQuiz();
        super.onStop();
    }



}
