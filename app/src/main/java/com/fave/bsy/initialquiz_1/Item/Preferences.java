package com.fave.bsy.initialquiz_1.Item;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by seungyeop on 2017-10-31.
 */

public class Preferences {

    private static final String PREF_INITIAL = "isInitial";
    private static final String PREF_COIN = "coin";

    public static Boolean getPrefInitial(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_INITIAL, true);
    }

    public static void setPrefInitial (Context context, boolean isInitial){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_INITIAL, isInitial)
                .apply();
    }

    public static int getPreCoin(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_COIN, 100);
    }

    public static void setPreCoin (Context context, int coin){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_COIN, coin)
                .apply();
    }
}
