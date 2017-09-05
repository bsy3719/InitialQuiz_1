package com.example.bsy.initialquiz_1.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bsy.initialquiz_1.Database.QuizBaseHelper;
import com.example.bsy.initialquiz_1.Item.Quiz;
import com.example.bsy.initialquiz_1.R;
import com.example.bsy.initialquiz_1.databinding.ActivityIntroBinding;

public class IntroActivity extends AppCompatActivity {

    private ActivityIntroBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_intro);

        InitialCheck();

        mBinding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this, QuizActivity.class);
                startActivity(intent);

                finish();
            }
        }, 3000);*/

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

    }
}
