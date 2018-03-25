package com.example.android.bikesafetyquizapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by smseol on 3/18/18.
 */

public class ShowScoreBoard extends Activity {

    String cUserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d("QuizApp","Show score board");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cUserName = extras.getString("user_name");
            Log.v("(QuizApp)","(receiveDataFromMainActivity) user name: " + cUserName);
        }

        // Set content view
        setContentView(R.layout.show_score_board);

        // display all score information
        displayAllScores();

    }

    public void displayAllScores() {

        List<scoreUnit> scoreList = sortList(ScoreStorage.getInstance().getScoreList());
        //Collections.reverse(scoreList);

        Log.d("Score list", "size of " + scoreList.size() + " original: " + ScoreStorage.getInstance().getScoreList().size());
        // for test
        for (int i = 0; i < scoreList.size(); i++) {
            Log.d("Score list", "name:" + scoreList.get(i).userName);
        }

        if (scoreList.size() > 0) {

            TextView topScoreNameView = (TextView) findViewById(R.id.topscoreTextView);
            TextView topScoreView = (TextView) findViewById(R.id.scoreTextView);

            topScoreNameView.setText(scoreList.get(0).userName);
            String temp = scoreList.get(0).numCorrectAnswer + "/5";
            topScoreView.setText(temp);

            LinearLayout normalBoardLayout = (LinearLayout)findViewById(R.id.normalBoardLayout);

            for (int i = 1; i < scoreList.size(); i++) {

                scoreUnit ListUnit = scoreList.get(i);

                Log.d("Unit",ListUnit.userName);

                FrameLayout currLayout = new FrameLayout(this);
                FrameLayout.LayoutParams opLP1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                opLP1.setMargins(20, 30, 0, 0);
                currLayout.setLayoutParams(opLP1);

                FrameLayout.LayoutParams opLP2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                opLP2.setMargins(180, 30, 0, 0);

                FrameLayout.LayoutParams opLP3 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                opLP3.setMargins(560, 30, 0, 0);

                // background image
                ImageView bgView = new ImageView(this);
                bgView.setBackgroundResource(R.drawable.normal_bar);
                currLayout.addView(bgView);

                // name
                TextView nameView = new TextView(this);
                nameView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                nameView.setTextColor(getResources().getColor(R.color.userNameColor));
                nameView.setLayoutParams(opLP2);
                nameView.setText(ListUnit.userName);
                currLayout.addView(nameView);

                // score
                TextView scoreView = new TextView(this);
                scoreView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                scoreView.setTextColor(getResources().getColor(R.color.userNameColor));
                scoreView.setLayoutParams(opLP3);
                String score = ListUnit.numCorrectAnswer + "/5";
                scoreView.setText(score);
                currLayout.addView(scoreView);

                normalBoardLayout.addView(currLayout);
            }

        }
    }


    public List<scoreUnit> sortList (List<scoreUnit> list) {

        //List<scoreUnit> sortedList = new ArrayList<scoreUnit>();

        Collections.reverse(list);


        return list;


    }

    // Go home (main screen)
    public void goHome (View v) {

        Log.d("QuizApp","go Home");

        // call quiz activity
        Intent MainIntent = new Intent(this, DashboardActivity.class);

        MainIntent.putExtra("user_name", cUserName);
        // pass user name from the edit text
        startActivity(MainIntent);


    }

}
