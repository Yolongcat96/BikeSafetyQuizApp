package com.example.android.bikesafetyquizapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    String userName = "Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Set content view
        setContentView(R.layout.activity_main);

    }

    public void getUserName() {

        // get user name
        final EditText nameText = (EditText)findViewById(R.id.enterName);
        if (nameText != null) {
            userName = nameText.getText().toString();
        }
    }

    public void startButton(View view) {

        // get user name
        getUserName();

        // call dashboard activity
        Intent dashBoardIntent = new Intent(this, DashboardActivity.class);
        // pass user name from the edit text
        Log.v("(QuizApp)","(Click Start Button) user name: " + userName);
        dashBoardIntent.putExtra("user_name", userName);
        startActivity(dashBoardIntent);

    }
}
