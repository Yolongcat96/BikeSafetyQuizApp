package com.example.android.bikesafetyquizapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DashboardActivity extends Activity {

    String cUserName = "";
    ImageView quizIconImageView;
    ImageView viewResultIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Receive data from Main Activity and set the data
        receiveDataFromMainActivity();

        // set user name on the textView
        setUserName();

        // set the listener to the quiz icon
        quizIconImageView = (ImageView)findViewById(R.id.quizIconView);
        quizIconImageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                callQuizActivity();
                Log.v("QuizApp", "Quiz icon is clicked.");
            }
        });

        // set the listener to the view result icon
        viewResultIconImageView = (ImageView)findViewById(R.id.viewResultView);
        viewResultIconImageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                callScoreQuizActivity();
                Log.v("QuizApp", "View Result icon is clicked.");
            }
        });
    }

    public void receiveDataFromMainActivity() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cUserName = extras.getString("user_name");
            Log.v("(QuizApp)","(receiveDataFromMainActivity) user name: " + cUserName);
        }

    }

    public void setUserName() {

        TextView userNameTextView = (TextView)findViewById(R.id.userNameView);
        // set the user name at the display
        userNameTextView.setText(cUserName);

    }

    // When clicking the quiz icon, this function is called.
    public void callQuizActivity() {

        // call quiz activity
        Intent QuizIntent = new Intent(this, QuizActivity.class);
        // pass user name from the edit text
        QuizIntent.putExtra("user_name", cUserName);
        startActivity(QuizIntent);
    }

    // When clicking the view result icon, this function is called.
    public void callScoreQuizActivity() {

        // call quiz activity
        Intent ScoreResultIntent = new Intent(this, ShowScoreBoard.class);
        // pass user name from the edit text
        ScoreResultIntent.putExtra("user_name", cUserName);
        startActivity(ScoreResultIntent);

    }

    // Go home (main screen)
    public void goHome (View v) {

        Log.d("QuizApp","go Home");

        // call quiz activity
        Intent MainIntent = new Intent(this, MainActivity.class);
        // pass user name from the edit text
        startActivity(MainIntent);

    }


}