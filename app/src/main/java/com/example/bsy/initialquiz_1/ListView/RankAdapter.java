package com.example.bsy.initialquiz_1.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bsy.initialquiz_1.Item.Rank;
import com.example.bsy.initialquiz_1.R;

import java.util.ArrayList;

/**
 * Created by bsy on 2017-09-10.
 */

public class RankAdapter extends BaseAdapter{
    ArrayList<Rank> ranks = new ArrayList<>();

    @Override
    public int getCount() {
        return ranks.size();
    }

    @Override
    public Rank getItem(int position) {
        return ranks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        TextView rankTextView;
        TextView nameTextView;
        TextView cntTextView;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_result,parent, false);

            rankTextView = (TextView) convertView.findViewById(R.id.rank_TextView);
            nameTextView = (TextView) convertView.findViewById(R.id.name_TextView);
            cntTextView = (TextView) convertView.findViewById(R.id.cnt_TextView);

            rankTextView.setText(String.valueOf(getItem(position).getRank()));
            nameTextView.setText(getItem(position).getName());
            cntTextView.setText(String.valueOf(getItem(position).getCnt())+"개");

        }

        return convertView;
    }

    //이미지 추가시
    public void addItem(int i_rank, String name, int cnt ){
        Rank rank = new Rank();

        rank.setRank(i_rank);
        rank.setName(name);
        rank.setCnt(cnt);

        ranks.add(rank);
        notifyDataSetChanged();

    }
}
