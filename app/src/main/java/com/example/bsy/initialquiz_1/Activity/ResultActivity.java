package com.example.bsy.initialquiz_1.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bsy.initialquiz_1.Item.MethodUtils;
import com.example.bsy.initialquiz_1.R;
import com.example.bsy.initialquiz_1.databinding.ActivityResultBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding mBinding;
    private InterstitialAd mInterstitialAd;
    private MethodUtils methodUtils = MethodUtils.getInstance();
    private int playCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_result);

        Intent intent = getIntent();
        mBinding.answerTextView.setText(intent.getStringExtra("answerCnt")+"개");

        //playCnt  = methodUtils.getPlayCnt() +1;
        methodUtils.setPlayCnt(methodUtils.getPlayCnt() +1);

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

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (methodUtils.getPlayCnt() == 3 && mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                    methodUtils.setPlayCnt(0);
                }
            }
        });

        mBinding.restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent tempIntent = new Intent(ResultActivity.this, QuizActivity.class);
                startActivity(tempIntent);
                finish();*/
            }
        });

        mBinding.homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
