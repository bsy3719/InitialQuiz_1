package com.example.bsy.initialquiz_1.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.bsy.initialquiz_1.Database.QuizBaseHelper;
import com.example.bsy.initialquiz_1.Item.Quiz;
import com.example.bsy.initialquiz_1.R;
import com.example.bsy.initialquiz_1.databinding.ActivityIntroBinding;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IntroActivity extends AppCompatActivity implements RewardedVideoAdListener {

    private ActivityIntroBinding mBinding;
    private RewardedVideoAd mRewardedAd;

    private static final String APP_ID = "ca-app-pub-3940256099942544~3347511713";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_intro);

        InitialCheck();

        MobileAds.initialize(this, APP_ID);
        mRewardedAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

        mBinding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        mBinding.listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

        mBinding.adsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mRewardedAd.isLoaded()) {
                    mRewardedAd.show();
                }

            }
        });

    }

    public void loadDB() {

        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("DB6.csv");
            inputStream.skip(3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] splitedStr = null;

        QuizBaseHelper quizBaseHelper = new QuizBaseHelper(this);

        try {
            //한글 깨짐현상 때문에 인코딩
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String line = null;
            splitedStr = null;

            while ((line = reader.readLine()) != null) {

                splitedStr = null;
                splitedStr = line.split(",");

                for (int i = 0; i < splitedStr.length; i++) {
                    splitedStr[i] = splitedStr[i].trim();
                }

                //자른 데이터를 원하는 형식에 맞게 넣기
                quizBaseHelper.addQuiz(new Quiz(Integer.parseInt(splitedStr[0]), splitedStr[1], splitedStr[2], splitedStr[3], splitedStr[4]));

            }

            reader.close();

        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void InitialCheck() {

        //최초 실행 여부 판단하는 구문
        SharedPreferences pref = getSharedPreferences("isInitial", Activity.MODE_PRIVATE);
        boolean initial = pref.getBoolean("isInitial", false);
        if (initial == false) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isInitial", true);
            editor.commit();

            loadDB();
            addShortcut(this);

            Log.d("@@@", "최초실행");

        } else {
            Log.d("@@@", "최초실행이 아님");
        }

    }

    private void addShortcut(Context context) {
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        shortcutIntent.setClassName(context, getClass().getName());
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                getResources().getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, R.mipmap.icon));
        intent.putExtra("duplicate", false);
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        sendBroadcast(intent);

    }

    private void loadRewardedVideoAd() {
        if (!mRewardedAd.isLoaded()) {
            //mRewardedAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
            mRewardedAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
        }
    }

    // Required to reward the user.
    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(this,
                String.format(" onRewarded! currency: %s amount: %d", reward.getType(),
                        reward.getAmount()),
                Toast.LENGTH_SHORT).show();
        // Reward the user.
    }

    // The following listener methods are optional.
    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        mBinding.adsButton.setEnabled(false);
        loadRewardedVideoAd();
        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
        public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad / errorCode = " + String.valueOf(errorCode), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        mBinding.adsButton.setEnabled(true);
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResume() {
        mRewardedAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedAd.destroy(this);
        super.onDestroy();
    }
}
