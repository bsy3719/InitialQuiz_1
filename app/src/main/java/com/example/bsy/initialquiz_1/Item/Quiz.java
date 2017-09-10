package com.example.bsy.initialquiz_1.Item;

import java.util.ArrayList;

/**
 * Created by bsy on 2017-08-29.
 */

public class Quiz {

    private int id;
    private int type;
    private String answer;
    private String hint_1;
    private String hint_2;

    public Quiz() {}

    public Quiz(String answer) {
        this.answer = answer;
    }

    public Quiz(int type, String answer, String hint_1, String hint_2) {
        this.type = type;
        this.answer = answer;
        this.hint_1 = hint_1;
        this.hint_2 = hint_2;
    }

    public Quiz(int id, int type, String answer, String hint_1, String hint_2) {
        this.id = id;
        this.type = type;
        this.answer = answer;
        this.hint_1 = hint_1;
        this.hint_2 = hint_2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHint_1() {
        return hint_1;
    }

    public void setHint_1(String hint_1) {
        this.hint_1 = hint_1;
    }

    public String getHint_2() {
        return hint_2;
    }

    public void setHint_2(String hint_2) {
        this.hint_2 = hint_2;
    }
}
