package com.example.bsy.initialquiz_1;

import android.app.Activity;
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
import com.example.bsy.initialquiz_1.databinding.ActivityQuizBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private ActivityQuizBinding binding;

    private ArrayList<Quiz> quizs = new ArrayList<Quiz>();
    private int mCurrentIndex = 0;

    private MethodUtils methodUtils = MethodUtils.getInstance();

    private InterstitialAd mInterstitialAd;

    int value = 60; // progressBar 값
    int add = 1; // 증가량, 방향

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_quiz);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);

        //전면광고
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest request = new AdRequest.Builder().
                addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
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

        //최초 실행인지 체크
        InitialCheck();
        //프로그레스바 실행
        runProgressBar();

        binding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String answer = binding.answerEditText.getText().toString();
                Toast toast;

                //정답을 맞췄을 경우
                if (answer.equals(quizs.get(mCurrentIndex).getAnswer())) {

                    toast = Toast.makeText(QuizActivity.this, quizs.get(mCurrentIndex).getId() + "번문제 정답!!!", Toast.LENGTH_SHORT);
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
        binding.rewardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //전면 광고 버튼
        binding.interstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(QuizActivity.this, "0초", Toast.LENGTH_SHORT).show();
            }
        };

    }
    //onCreate 끝

    private void runProgressBar(){

        handler = new Handler();
        binding.timerProgressBar.setProgress(value);

        // 앱시작시, Thread 를 시작해서 ProgressBar 를 증가시키기
        // Thread 내부에서 화면에 작업을 하려면 Handler 를 사용해야한다
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() { // Thread 로 작업할 내용을 구현
                while(true) {
                    try {

                        Thread.sleep(1000); // 시간지연

                        value = value - add;

                        binding.timerProgressBar.setProgress(value);

                    } catch (InterruptedException e) { }

                    if (value == 0){
                        handler.sendMessage(handler.obtainMessage());
                        break;
                    }

                } // end of while
            }
        });

        thread.start(); // 쓰레드 시작

    }

    private void updateQuiz() {

        binding.answerEditText.setText("");

        mCurrentIndex = (mCurrentIndex + 1) % quizs.size();
        binding.initialTextView.setText(methodUtils.getInitial(quizs.get(mCurrentIndex).getAnswer()));
        binding.hint1TextView.setText(quizs.get(mCurrentIndex).getHint_1());
        binding.hint2TextView.setText(quizs.get(mCurrentIndex).getHint_2());

    }

    private void InitialCheck() {

        //DB활성화
        QuizBaseHelper quizBaseHelper = new QuizBaseHelper(this);

        //최초 실행 여부 판단하는 구문
        SharedPreferences pref = getSharedPreferences("isInitial", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isInitial", false);
        if (first == false) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isInitial", true);
            editor.commit();

            quizBaseHelper.addQuiz(new Quiz("선풍기", "여름", "회전"));
            quizBaseHelper.addQuiz(new Quiz("모니터", "컴퓨터", "화면"));
            quizBaseHelper.addQuiz(new Quiz("자스민", "커피", "부산대"));
            quizBaseHelper.addQuiz(new Quiz("휴대폰", "삼성", "애플"));
            quizBaseHelper.addQuiz(new Quiz("컴퓨터", "키보드", "마우스"));

            Log.d("@@@", "최초실행");
        } else {
            Log.d("@@@", "최초실행이 아님");
        }

        quizs = quizBaseHelper.getAllQuiz();
        Log.d("@@@", "배열 셋팅");

        binding.initialTextView.setText(methodUtils.getInitial(quizs.get(mCurrentIndex).getAnswer()));
        binding.hint1TextView.setText(quizs.get(mCurrentIndex).getHint_1());
        binding.hint2TextView.setText(quizs.get(mCurrentIndex).getHint_2());

    }

}
