package com.example.bsy.initialquiz_1.Item;

/**
 * Created by bsy on 2017-09-10.
 */

public class Rank {

    private int id;
    private int cnt;
    private int rank;
    private String name;


    public Rank(){}

    public Rank(int cnt, String name) {
        this.cnt = cnt;
        this.name = name;
    }

    public Rank(int id, int cnt, String name) {
        this.id = id;
        this.cnt = cnt;
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
