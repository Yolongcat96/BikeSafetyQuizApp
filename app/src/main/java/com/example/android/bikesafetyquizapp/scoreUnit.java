package com.example.android.bikesafetyquizapp;

/**
 * Created by smseol on 3/18/18.
 */

public class scoreUnit {

    public String userName;
    public int numCorrectAnswer;


    public void createScoreUnit (String userName, int numCorrectAnswer) {
        this.userName = userName;
        this.numCorrectAnswer = numCorrectAnswer;
    }
}
