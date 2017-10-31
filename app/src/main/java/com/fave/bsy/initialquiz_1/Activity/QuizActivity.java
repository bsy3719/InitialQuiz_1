package com.fave.bsy.initialquiz_1.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.fave.bsy.initialquiz_1.Database.QuizBaseHelper;
import com.fave.bsy.initialquiz_1.Item.MethodUtils;
import com.fave.bsy.initialquiz_1.Item.Quiz;
import com.fave.bsy.initialquiz_1.R;
import com.fave.bsy.initialquiz_1.databinding.ActivityQuizBinding;
import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private ActivityQuizBinding mBinding;

    private ArrayList<Quiz> quizs = new ArrayList<Quiz>();

    private int mCurrentIndex = 0;
    private int time = 60; // progressBar 값
    private int add = 1; // 증가량, 방향
    private int answerCnt = 0;
    private int addTime = 0;
    private boolean addFlag = false;


    private Handler handler; // Thread 에서 화면에 그리기 위해서 필요
    private Thread thread;
    private Boolean isRunning = true;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);

        //
        SharedPreferences pref = getSharedPreferences("coin", Activity.MODE_PRIVATE);
        int coin = pref.getInt("coin", 100);


        //프로그레스바 스레드
        handler = new Handler();
        runProgressBar();

        //배너 광고
        AdRequest adRequest = new AdRequest.Builder().build();
        mBinding.adView.loadAd(adRequest);

        //시작 셋팅
        setting();
        updateQuiz();

        //완료버튼으로 처리하기
        mBinding.answerEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mBinding.answerEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                checkQuiz();

                return true;
            }

        });

        mBinding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              checkQuiz();

            }
        });

        mBinding.keyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.keyImageView.setEnabled(false);

                SharedPreferences pref = getSharedPreferences("coin", Activity.MODE_PRIVATE);
                int coin = pref.getInt("coin", 100);

                if (coin>30){
                    mBinding.hint3TextView.setVisibility(View.VISIBLE);
                    mBinding.hint3ImageView.setVisibility(View.VISIBLE);

                    coin = coin -30;

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("coin", coin);
                    editor.commit();

                    mBinding.coinTextView.setText(String.valueOf(coin));
                }else{

                    toast = Toast.makeText(QuizActivity.this, "코인이 부족합니다.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 150);
                    toast.show();

                }

            }
        });

        mBinding.lightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lightImageView.setEnabled(false);

                SharedPreferences pref = getSharedPreferences("coin", Activity.MODE_PRIVATE);
                int coin = pref.getInt("coin", 100);

                if (coin>30){
                    mBinding.initialTextView.setText(MethodUtils.getHintInitial(quizs.get(mCurrentIndex).getAnswer()));

                    coin = coin -30;

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("coin", coin);
                    editor.commit();

                    mBinding.coinTextView.setText(String.valueOf(coin));
                }else{

                    toast = Toast.makeText(QuizActivity.this, "코인이 부족합니다.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 150);
                    toast.show();


                }

            }
        });

        mBinding.passImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mBinding.addTimeTextView.setText("-3");
                mBinding.addTimeTextView.setVisibility(View.VISIBLE);
                addFlag = true;
                addTime = 0;

                time = time - 3;
                quizs.remove(mCurrentIndex);
                updateQuiz();

            }
        });

    }
    //onCreate 끝

    private void runProgressBar(){
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

                    } catch (InterruptedException e) { }

                    handler.post(new Runnable() {
                        @Override
                        public void run() { // 화면에 변경하는 작업을 구현
                            mBinding.timerProgressBar.setProgress(time);
                            mBinding.timerTextView.setText(String.valueOf(time));

                            if (time <= 10){
                                mBinding.timerProgressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                            }

                            if (addTime >= 3){
                                mBinding.addTimeTextView.setVisibility(View.INVISIBLE);
                                addFlag = false;
                            }
                        }
                    });

                    if (addFlag = true){
                        addTime = addTime + 1;
                    }

                    if (time <= 0){
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

        mBinding.lightImageView.setEnabled(true);
        mBinding.keyImageView.setEnabled(true);

        Random random = new Random();
        mCurrentIndex = random.nextInt(quizs.size());

        mBinding.answerEditText.setText("");

        switch (quizs.get(mCurrentIndex).getType()){
            case 1:
                mBinding.categoryTextView.setText("우리말");
                break;
        }

        mBinding.initialTextView.setText(MethodUtils.getInitial(quizs.get(mCurrentIndex).getAnswer()));
        mBinding.hint1TextView.setText(quizs.get(mCurrentIndex).getHint_1());
        mBinding.hint2TextView.setText(quizs.get(mCurrentIndex).getHint_2());
        mBinding.hint3TextView.setText(quizs.get(mCurrentIndex).getHint_3());

        mBinding.hint3TextView.setVisibility(View.INVISIBLE);
        mBinding.hint3ImageView.setVisibility(View.INVISIBLE);

    }

    private void setting() {
        //DB활성화
        QuizBaseHelper quizBaseHelper = new QuizBaseHelper(this);

        quizs = quizBaseHelper.getAllQuiz();

        //코인 셋팅
        SharedPreferences pref = getSharedPreferences("coin", Activity.MODE_PRIVATE);
        int coin = pref.getInt("coin", 100);

        mBinding.coinTextView.setText(String.valueOf(coin));
    }

    private void resetQuiz(){
        time = 60;
        answerCnt = 0;

        mBinding.timerProgressBar.setProgress(time);
        mBinding.timerTextView.setText(String.valueOf(time));
        mBinding.answerTextView.setText(String.valueOf(answerCnt)+"개");
    }

    private void checkQuiz(){

        String answer = mBinding.answerEditText.getText().toString();

        //정답을 맞췄을 경우
        if (answer.equals(quizs.get(mCurrentIndex).getAnswer())) {

            answerCnt = ++answerCnt;
            mBinding.answerTextView.setText(String.valueOf(answerCnt)+"개");

            mBinding.addTimeTextView.setText("+5");
            mBinding.addTimeTextView.setVisibility(View.VISIBLE);
            addFlag = true;
            addTime = 0;

            time = time + 5;
            mBinding.timerTextView.setText(String.valueOf(time));

            toast = Toast.makeText(QuizActivity.this, "정답!!!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 150);
            toast.show();

            quizs.remove(mCurrentIndex);
            updateQuiz();

        } else {

            toast = Toast.makeText(QuizActivity.this, "오답!!!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 150);
            toast.show();

        }

    }

    @Override
    protected void onStart() {
        if (isRunning == false){
            isRunning = true;
            runProgressBar();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {

        /*//키보드 자동실행
        mBinding.answerEditText.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager manager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                manager.showSoftInput(mBinding.answerEditText, 0);
            }
        }, 100);*/

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        isRunning = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        isRunning = false;
        thread.interrupt();
        thread = null;
        resetQuiz();
        super.onDestroy();
    }

}
