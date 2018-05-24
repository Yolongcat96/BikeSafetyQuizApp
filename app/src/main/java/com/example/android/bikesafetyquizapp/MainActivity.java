package com.example.android.bikesafetyquizapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;

public class MainActivity extends Activity {

    private String userName = "";
    private float densityScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view
        setContentView(R.layout.activity_main);
    }

    // receive the user input from the edit text view at the main view
    private String getUserName() {
        // get user name
        final EditText nameText = (EditText) findViewById(R.id.enterName);
        if (nameText != null) {
            userName = nameText.getText().toString();
        }
        return userName;
    }

    // when click this button, it moves the user to the dashboard
    public void startButton(View view) {
        // check the user name is typed
        if (getUserName().equals("")) {
            showWarning();
        } else {
            // call dashboard activity
            Intent dashBoardIntent = new Intent(this, DashboardActivity.class);
            // pass user name from the edit text
            Log.v("(QuizApp)", "(Click Start Button) user name: " + userName);
            dashBoardIntent.putExtra("user_name", userName);
            startActivity(dashBoardIntent);
        }
    }

    private void showWarning() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Warnning!")
                .setMessage("Please enter user name.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}