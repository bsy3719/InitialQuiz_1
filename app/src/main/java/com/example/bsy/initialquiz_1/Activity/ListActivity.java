package com.example.bsy.initialquiz_1.Activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bsy.initialquiz_1.Database.RankBaseHelper;
import com.example.bsy.initialquiz_1.Font.FontBaseActivity;
import com.example.bsy.initialquiz_1.Item.Rank;
import com.example.bsy.initialquiz_1.ListView.RankAdapter;
import com.example.bsy.initialquiz_1.R;
import com.example.bsy.initialquiz_1.databinding.ActivityListBinding;
import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ActivityListBinding mBinding;

    private ArrayList<Rank> ranks = new ArrayList<>();
    private RankAdapter mRankAdapter = new RankAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list);

        //배너 광고
        AdRequest adRequest = new AdRequest.Builder().build();
        mBinding.adView.loadAd(adRequest);

        //리스트 어댑터 등록
        mBinding.resultListView.setDivider(null);
        mBinding.resultListView.setAdapter(mRankAdapter);

        RankBaseHelper rankBaseHelper = new RankBaseHelper(this);

        ranks = rankBaseHelper.getThreeRank();

        for (int i = 0; i < ranks.size(); i++){
            mRankAdapter.addItem(i+1, ranks.get(i).getName(), ranks.get(i).getCnt());
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
