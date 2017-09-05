package com.example.bsy.initialquiz_1.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bsy.initialquiz_1.Item.Quiz;

import java.util.ArrayList;

/**
 * Created by bsy on 2017-08-31.
 */

public class QuizBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "quiz.db";

    private static  final String TABLE = "quizs";
    private static final String ID = "id";
    private static final String ANSWER = "answer";
    private static final String HINT_1 = "hint_1";
    private static final String HINT_2 = "hint_2";

    public QuizBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);

    }

    //db만들때 한번만 호출
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE+"("+
                ID+" integer primary key autoincrement, " +
                ANSWER+","+
                HINT_1+","+
                HINT_2+")");

        Log.d("@@@", "DB onCreate");
    }

    //버전이 업데이트 되는 경우
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE);
        onCreate(db);
    }

    //새로운 quiz 추가
    public void addQuiz(Quiz quiz){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ANSWER, quiz.getAnswer());
        values.put(HINT_1, quiz.getHint_1());
        values.put(HINT_2, quiz.getHint_2());

        db.insert(TABLE, null, values);
        db.close();
    }

    //id에 해당하는 quiz객체 가져오기
    public Quiz getQuiz(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE, new String[] { ID, ANSWER, HINT_1, HINT_2 },
                ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Quiz quiz = new Quiz(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));

        return quiz;

    }

    //모든 quiz 정보 가져오기
    public ArrayList<Quiz> getAllQuiz(){
        ArrayList <Quiz> quizs = new ArrayList<Quiz>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Quiz quiz= new Quiz();
                quiz.setId(Integer.parseInt(cursor.getString(0)));
                quiz.setAnswer(cursor.getString(1));
                quiz.setHint_1(cursor.getString(2));
                quiz.setHint_2(cursor.getString(3));
                // Adding contact to list
                quizs.add(quiz);
            } while (cursor.moveToNext());
        }

        return quizs;

    }

    //quiz 정보 업데이트
    public int updateQuiz(Quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ANSWER, quiz.getAnswer());
        values.put(HINT_1, quiz.getHint_1());
        values.put(HINT_2, quiz.getHint_2());

        // updating row
        return db.update(TABLE, values, ID + " = ?",
                new String[] { String.valueOf(quiz.getId())});
    }

    // Contact 정보 삭제하기
    public void deleteQuiz(Quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, ID + " = ?",
                new String[] { String.valueOf(quiz.getId()) });
        db.close();
    }

    // Contact 정보 숫자
    public int getQuizCount() {
        String countQuery = "SELECT  * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
