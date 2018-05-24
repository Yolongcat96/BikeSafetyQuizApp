package com.example.android.bikesafetyquizapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by smseol on 3/18/18.
 */

public class ScoreStorage {

    public static ScoreStorage instance = null;
    public Context context;

    List<scoreUnit> scoreList = new ArrayList<scoreUnit>();

    private ScoreStorage() {
    }

    ;

    public static ScoreStorage getInstance() {
        if (instance == null) {
            instance = new ScoreStorage();
        }
        return (instance);
    }

    public void addNewScore(scoreUnit score) {
        Log.d("addNewScore", "Name: " + score.userName + scoreList.size());
        //scoreList.add(score);
        if (scoreList.size() == 0) {
            scoreList.add(score);
        } else {
            boolean addedToLast = true;
            for (int i = 0; i < scoreList.size(); i++) {
                if (score.numCorrectAnswer >= scoreList.get(i).numCorrectAnswer) {
                    scoreList.add(score);
                    Collections.swap(scoreList, i, i + 1);
                    addedToLast = false;
                    break;
                }
            }
            if (addedToLast) scoreList.add(score);
        }
    }

    public List<scoreUnit> getScoreList() {
        return scoreList;
    }

    public void displayAllScores() {
        for (int i = 0; i < scoreList.size(); i++) {
            scoreUnit unit = scoreList.get(i);
            Log.d("Score", "Name: " + unit.userName);
        }
    }

    public String getName() {
        int lastIndex = scoreList.size() - 1;
        return scoreList.get(lastIndex).userName;
    }

    public void updateScoreWithName(String name, int score) {
        for (int i = 0; i < scoreList.size(); i++) {
            scoreUnit unit = scoreList.get(i);
            if (name.compareToIgnoreCase(unit.userName) == 0) {
                unit.numCorrectAnswer = score;
            }

        }
    }

    public int getScore(String name) {
        for (int i = 0; i < scoreList.size(); i++) {
            scoreUnit unit = scoreList.get(i);
            if (name.compareToIgnoreCase(unit.userName) == 0) {
                return unit.numCorrectAnswer;
            }

        }
        return 0;
    }

    public boolean isThisUserHere(String name) {
        for (int i = 0; i < scoreList.size(); i++) {
            scoreUnit unit = scoreList.get(i);
            if (name.compareToIgnoreCase(unit.userName) == 0) {
                return true;
            }
        }
        return false;
    }
}