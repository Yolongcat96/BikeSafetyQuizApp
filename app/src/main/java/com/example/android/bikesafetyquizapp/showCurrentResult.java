package com.example.android.bikesafetyquizapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by smseol on 3/18/18.
 */

public class showCurrentResult extends Activity {

    int numberOfCorrectAnswer = 0;
    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("QuizApp", "Show current result");
        // Set content view
        setContentView(R.layout.show_result);
        // get the data and set the data
        setUserName();
    }

    public void setUserName() {
        Log.d("QuizApp", "setUserName");
        // get the data from the previous screen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("user_name");
            numberOfCorrectAnswer = extras.getInt("numOfCorrectanswer");
            TextView name_score = (TextView) findViewById(R.id.userNameResultView);
            String display_string = userName + "'s score is\n" + numberOfCorrectAnswer + "/5";
            name_score.setText(display_string);
        }

    }

    // Try to solve the questions again from the number 1
    public void solveAgain(View v) {
        Log.d("QuizApp", "solve the questions again");
        // call quiz activity
        Intent QuizIntent = new Intent(this, QuizActivity.class);
        // pass user name from the edit text
        QuizIntent.putExtra("user_name", userName);
        startActivity(QuizIntent);

    }

    // Go home (main screen)
    public void goHome(View v) {
        Log.d("QuizApp", "go Home");
        // call quiz activity
        Intent MainIntent = new Intent(this, DashboardActivity.class);
        // pass user name from the edit text
        MainIntent.putExtra("user_name", userName);
        startActivity(MainIntent);


    }

}
