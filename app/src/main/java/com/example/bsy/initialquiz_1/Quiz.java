package com.example.bsy.initialquiz_1;

import java.util.ArrayList;

/**
 * Created by bsy on 2017-08-29.
 */

public class Quiz {

    private ArrayList<Quiz> quizs = new ArrayList<Quiz>() ;

    String s_answer;
    String s_hint;

    public ArrayList<Quiz> getQuizs() {
        return quizs;
    }

    public void setQuizs(ArrayList<Quiz> quizs) {
        this.quizs = quizs;
    }

    public String getS_answer() {
        return s_answer;
    }

    public void setS_answer(String s_answer) {
        this.s_answer = s_answer;
    }

    public String getS_hint() {
        return s_hint;
    }

    public void setS_hint(String s_hint) {
        this.s_hint = s_hint;
    }

    //리스트 추가시
    public void addItem(String answer, String hint){
       Quiz quiz = new Quiz();

        quiz.setS_answer(answer);
        quiz.setS_hint(hint);
        quizs.add(quiz);

    }

    public void setItem(){
        addItem("선풍기", "ㅅㅍㄱ");
        addItem("컴퓨터", "ㅋㅍㅌ");
        addItem("키보드","ㅋㅂㄷ");
        addItem("모니터", "ㅁㄴㅌ");
    }

}
