package com.fave.bsy.initialquiz_1.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fave.bsy.initialquiz_1.Item.Rank;

import java.util.ArrayList;

/**
 * Created by bsy on 2017-09-10.
 */

public class RankBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "rank.db";

    private static final String TABLE = "ranks";
    private static final String ID = "id";
    private static final String CNT = "cnt";
    private static final String NAME = "name";

    public RankBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE+"("+
                ID+" integer primary key autoincrement, " +
                CNT+","+
                NAME+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE);
        onCreate(db);
    }

    //새로운 rank 추가
    public void addRank(Rank rank){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CNT, rank.getCnt());
        values.put(NAME, rank.getName());
        db.insert(TABLE, null, values);
        db.close();
    }

    //모든 rank 정보 가져오기
    public ArrayList<Rank> getAllRank(){
        ArrayList <Rank> ranks = new ArrayList<Rank>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Rank rank = new Rank();
                rank.setId(Integer.parseInt(cursor.getString(0)));
                rank.setCnt(Integer.parseInt(cursor.getString(1)));
                rank.setName(cursor.getString(2));
                // Adding contact to list
                ranks.add(rank);
            } while (cursor.moveToNext());
        }

        return ranks;

    }

    //모든 rank 정보 상위 3개 가져오기
    public ArrayList<Rank> getThreeRank(){
        ArrayList <Rank> ranks = new ArrayList<Rank>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE + " order by cnt desc limit 0,2";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Rank rank = new Rank();
                rank.setId(Integer.parseInt(cursor.getString(0)));
                rank.setCnt(Integer.parseInt(cursor.getString(1)));
                rank.setName(cursor.getString(2));
                // Adding contact to list
                ranks.add(rank);
            } while (cursor.moveToNext());
        }

        return ranks;
    }

    //rank 정보 업데이트
    public int updateRank(Rank rank) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CNT, rank.getCnt());
        values.put(NAME, rank.getName());

        // updating row
        return db.update(TABLE, values, ID + " = ?",
                new String[] { String.valueOf(rank.getId())});
    }

    // rank 정보 삭제하기
    public void deleteRank(Rank rank) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, ID + " = ?",
                new String[] { String.valueOf(rank.getId()) });
        db.close();
    }

    // rank 정보 숫자
    public int getRankCount() {
        String countQuery = "SELECT  * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
