package com.magnetic.pokemonsafari.model;

import java.util.Date;

/**
 *
 */
public class ImageStats {

    private int score;
    private Date date;

    public ImageStats(int score, Date date) {
        this.score = score;
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public Date getDate() {
        return date;
    }
}
