package com.example.android.bikesafetyquizapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DashboardActivity extends Activity {

    private String currentUserName = "";
    private ImageView quizIconImageView;
    private ImageView viewResultIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        // Receive data from Main Activity and set the data
        receiveDataFromMainActivity();
        // set user name on the textView
        setUserName();
        // set the touch event listener to the quiz icon
        quizIconImageView = findViewById(R.id.quizIconView);
        quizIconImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callQuizActivity();
                Log.v("QuizApp", "Quiz icon is clicked.");
            }
        });
        // set the touch event listener to the view result icon
        viewResultIconImageView = findViewById(R.id.viewResultView);
        viewResultIconImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callScoreQuizActivity();
                Log.v("QuizApp", "View Result icon is clicked.");
            }
        });
    }

    private void receiveDataFromMainActivity() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUserName = extras.getString("user_name");
            Log.v("(QuizApp)", "(receiveDataFromMainActivity) user name: " + currentUserName);
        }

    }

    private void setUserName() {
        TextView userNameTextView = (TextView) findViewById(R.id.userNameView);
        // set the user name at the display
        userNameTextView.setText(currentUserName);

    }

    // When clicking the quiz icon, this function is called.
    public void callQuizActivity() {
        // call quiz activity
        Intent QuizIntent = new Intent(this, QuizActivity.class);
        // pass user name from the edit text
        QuizIntent.putExtra("user_name", currentUserName);
        startActivity(QuizIntent);
    }

    // When clicking the view result icon, this function is called.
    private void callScoreQuizActivity() {
        // call quiz activity
        Intent ScoreResultIntent = new Intent(this, ShowScoreBoard.class);
        // pass user name from the edit text
        ScoreResultIntent.putExtra("user_name", currentUserName);
        startActivity(ScoreResultIntent);

    }

    // Go home (main screen)
    public void goHome(View v) {
        Log.d("QuizApp", "go Home");
        // call quiz activity
        Intent MainIntent = new Intent(this, MainActivity.class);
        // pass user name from the edit text
        startActivity(MainIntent);

    }

}