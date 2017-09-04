package com.example.bsy.initialquiz_1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.bsy.initialquiz_1.Database.QuizBaseHelper;
import com.example.bsy.initialquiz_1.databinding.ActivityQuizBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity implements RewardedVideoAdListener{

    private ActivityQuizBinding binding;

    private ArrayList<Quiz> quizs = new ArrayList<Quiz>();
    private int mCurrentIndex = 0;

    private MethodUtils methodUtils = MethodUtils.getInstance();

    private RewardedVideoAd mAd;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_quiz);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);

        MobileAds.initialize(this, "ca-app-pub-4711336970766023~9674070775");

        //광고 설정 리스너 부분
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        loadAd();

        InitialCheck();

        //초성 추출하여 힌트 셋팅
        binding.initialTextView.setText(methodUtils.getInitial(quizs.get(mCurrentIndex).getAnswer()));
        //binding.initialTextView.setText("테스트");

        binding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String answer = binding.answerEditText.getText().toString();
                Toast toast;

                //정답을 맞췄을 경우
                if (answer.equals(quizs.get(mCurrentIndex).getAnswer())) {

                    toast = Toast.makeText(QuizActivity.this, quizs.get(mCurrentIndex).getId()+"번문제 정답!!!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 150);
                    toast.show();

                    updateQuiz();

                }else{

                    toast = Toast.makeText(QuizActivity.this, "오답!!!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 150);
                    toast.show();

                }
            }
        });

        binding.adButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("@@@", "버튼클릭");
                Log.d("@@@", String.valueOf(mAd.isLoaded()));
               if (mAd.isLoaded()){
                   mAd.show();
                   Log.d("@@@", "광고뜸");
               }
            }
        });

    }
    //onCreate 끝

    private void updateQuiz() {

        binding.answerEditText.setText("");

        mCurrentIndex = (mCurrentIndex + 1) % quizs.size();
        binding.initialTextView.setText(methodUtils.getInitial(quizs.get(mCurrentIndex).getAnswer()));
        binding.hint1TextView.setText(quizs.get(mCurrentIndex).getHint_1());
        binding.hint2TextView.setText(quizs.get(mCurrentIndex).getHint_2());

    }

    private void InitialCheck(){

        //DB활성화
        QuizBaseHelper quizBaseHelper = new QuizBaseHelper(this);

        //최초 실행 여부 판단하는 구문
        SharedPreferences pref = getSharedPreferences("isInitial", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isInitial", false);
        if(first==false) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isInitial", true);
            editor.commit();

            quizBaseHelper.addQuiz(new Quiz("선풍기" , "여름", "회전"));
            quizBaseHelper.addQuiz(new Quiz("모니터", "컴퓨터", "화면"));
            quizBaseHelper.addQuiz(new Quiz("자스민", "커피", "부산대"));
            quizBaseHelper.addQuiz(new Quiz("휴대폰", "삼성", "애플"));
            quizBaseHelper.addQuiz(new Quiz("컴퓨터", "키보드", "마우스"));

            Log.d("@@@", "최초실행");
        }else{
            Log.d("@@@", "최초실행이 아님");
        }

        quizs = quizBaseHelper.getAllQuiz();
        Log.d("@@@", "배열 셋팅");

        binding.initialTextView.setText(methodUtils.getInitial(quizs.get(mCurrentIndex).getAnswer()));
        binding.hint1TextView.setText(quizs.get(mCurrentIndex).getHint_1());
        binding.hint2TextView.setText(quizs.get(mCurrentIndex).getHint_2());

    }

    private void loadAd(){
        if(!mAd.isLoaded()){
            mAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
        }
    }

    //리워드 보상 관련
    @Override
    public void onRewardedVideoAdLoaded() {
        binding.adButton.setEnabled(true);
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        binding.adButton.setEnabled(false);
        loadAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(QuizActivity.this, String.format(Locale.getDefault() , "You got %d %s!", rewardItem.getAmount(), rewardItem.getType()), Toast.LENGTH_SHORT).show();
        //rewardItem.getAmount();

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
