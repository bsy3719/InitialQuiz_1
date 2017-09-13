package com.example.bsy.initialquiz_1.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bsy.initialquiz_1.Database.RankBaseHelper;
import com.example.bsy.initialquiz_1.Font.FontBaseActivity;
import com.example.bsy.initialquiz_1.Item.MethodUtils;
import com.example.bsy.initialquiz_1.Item.Rank;
import com.example.bsy.initialquiz_1.ListView.RankAdapter;
import com.example.bsy.initialquiz_1.R;
import com.example.bsy.initialquiz_1.databinding.ActivityResultBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding mBinding;
    private InterstitialAd mInterstitialAd;
    private MethodUtils methodUtils = MethodUtils.getInstance();

    private int mAnswerCnt;
    private boolean mEnrollCheck = false;

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
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

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
                Intent tempIntent = new Intent(ResultActivity.this, QuizActivity.class);
                startActivity(tempIntent);
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
                if (mEnrollCheck == false){
                    addRank();
                    Toast.makeText(ResultActivity.this, "기록이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ResultActivity.this, "기록은 한번만 등록 가능합니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void addRank(){

        if (mEnrollCheck == false){

            Log.d("@@@", "DB 추가 완료");
            String name = mBinding.nameEditText.getText().toString();

            RankBaseHelper rankBaseHelper = new RankBaseHelper(this);

            rankBaseHelper.addRank(new Rank(mAnswerCnt, name));

        }

        mEnrollCheck = true;



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
