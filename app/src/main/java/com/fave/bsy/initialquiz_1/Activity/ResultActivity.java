package com.fave.bsy.initialquiz_1.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fave.bsy.initialquiz_1.Database.RankBaseHelper;
import com.fave.bsy.initialquiz_1.Item.MethodUtils;
import com.fave.bsy.initialquiz_1.Item.Rank;
import com.fave.bsy.initialquiz_1.R;
import com.fave.bsy.initialquiz_1.databinding.ActivityResultBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding mBinding;
    private InterstitialAd mInterstitialAd;
    private MethodUtils methodUtils = MethodUtils.getInstance();

    private int mAnswerCnt = 0;
    private boolean mEnrollFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_result);

        Intent intent = getIntent();
        mAnswerCnt = Integer.parseInt(intent.getStringExtra("answerCnt"));

        mBinding.resultTextView.setText(String.valueOf(mAnswerCnt)+"개");

        methodUtils.setPlayCnt(methodUtils.getPlayCnt() +1);

        //전면광고
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3992465302306146/9324258970");

        AdRequest request = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(request);

        mBinding.adView.loadAd(request);

        //전면광고 reload하는 부분
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (methodUtils.getPlayCnt() == 2 && mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                    methodUtils.setPlayCnt(0);
                }
            }
        });

        mBinding.restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mBinding.homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        mBinding.listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, ListActivity.class);
                startActivity(intent);
                //finish();

            }
        });

        mBinding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEnrollFlag == false){
                    addRank();

                    Toast.makeText(ResultActivity.this, "기록이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ResultActivity.this, IntroActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(ResultActivity.this, "기록은 한번만 등록 가능합니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void addRank(){

        if (mEnrollFlag == false){
            String name = mBinding.nameEditText.getText().toString();

            if(name==null||name.equals("")){

                name = "이름없음";
                RankBaseHelper rankBaseHelper = new RankBaseHelper(this);
                rankBaseHelper.addRank(new Rank(mAnswerCnt, name));
            }else{
                RankBaseHelper rankBaseHelper = new RankBaseHelper(this);
                rankBaseHelper.addRank(new Rank(mAnswerCnt, name));
            }

        }

        mEnrollFlag = true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        addRank();
        super.onStop();
    }
}
