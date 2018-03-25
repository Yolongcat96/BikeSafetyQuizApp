package com.example.android.bikesafetyquizapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smseol on 3/18/18.
 */

public class ScoreStorage {

    private static ScoreStorage instance = null;
    private Context context;

    List<scoreUnit> scoreList = new ArrayList<scoreUnit>();

    private ScoreStorage() {};

    public static ScoreStorage getInstance() {

        if (instance == null) {
            instance = new ScoreStorage();
        }
        return(instance);
    }

    public void addNewScore (scoreUnit score) {
        scoreList.add(score);
    }

    public List<scoreUnit> getScoreList() {
        return scoreList;
    }

    public void displayAllScores() {

        for (int i = 0 ; i < scoreList.size(); i++) {

            scoreUnit unit = scoreList.get(i);
            Log.d("Score", "Name: "+ unit.userName);
        }
    }

    public String getName() {

        int lastIndex = scoreList.size() -1;

        return scoreList.get(lastIndex).userName;
    }

    public void updateScoreWithName (String name, int score) {

        for (int i = 0 ; i < scoreList.size(); i++) {
            scoreUnit unit = scoreList.get(i);
            if (name.compareToIgnoreCase(unit.userName) == 0) {
                unit.numCorrectAnswer = score;
            }

        }
    }

    public int getScore(String name) {

        for (int i = 0 ; i < scoreList.size(); i++) {
            scoreUnit unit = scoreList.get(i);
            if (name.compareToIgnoreCase(unit.userName) == 0) {
                return unit.numCorrectAnswer;
            }

        }

        return 0;
    }

    public boolean isThisUserHere (String name) {

        for (int i = 0 ; i < scoreList.size(); i++) {
            scoreUnit unit = scoreList.get(i);

            if (name.compareToIgnoreCase(unit.userName) == 0) {
                return true;
            }
        }

        return false;
    }
}
