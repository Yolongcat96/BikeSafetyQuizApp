package com.example.android.bikesafetyquizapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.LocaleDisplayNames;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class QuizActivity extends Activity implements View.OnClickListener  {

    String questionTypeCollection[] = {"multi-option", "click", "open-end", "RadioButton", "checkbox"};
    String currUserName = "";
    List<QuestionObject> questionList;
    QuestionObject currentQuestionUnit;

    int currentProblemNumber = 0; // initial value
    int currentAnswerNumber[] = new int[2];
    String currentAnswer = "";

    int numOfProgressButton = 5;
    int totalNumOfProblems = 5;

    LinearLayout progressbarLayout;
    FrameLayout questionDisaplyLayout;
    LinearLayout optionViewLayout;

    boolean resultArray[] = new boolean[5]; // save the result. 0: incorrect, 1: correct
    float questionNumberSize = 20;
    boolean isCorrect = false;

    // Multiple option question
    boolean buttonSelectionArray[] = new boolean[4]; // for the multiple option question
    Button buttonArray[] = new Button[4]; // collect all option buttons
    int [][] imageResourceArray = new int[4][2];
    int myAnswerIndex = 0;

    // for the open-end question
    EditText editText;

    // for the checkbox question
    CheckBox cBoxArray[] = new CheckBox[4];

    // for the radio button question
    RadioButton rButtonArray[] = new RadioButton[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);

        // Receive the question set data from the assets (questionSets.json)
        getDataFromJsonFile();

        // Receive the user name from Main Activity
        receiveDataFromDashboardActivity();

        // Set user name at the TextView
        setUserName();

        // Start Quiz
        startQuiz();

    }

    // the initial part of starting the quiz. Show question -> answer the question -> check the answer and the last question
    public void startQuiz() {

        // display progress bar
        updateProgressbar();

        // display question
        displayQuestion();

    }

    public void updateProgressbar() {

        Log.d("QuizApp", "updateProgressbar");
        progressbarLayout = (LinearLayout)findViewById(R.id.progressbarLayout);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);


        for (int i = 0 ; i < numOfProgressButton; i++) {

            ImageView image = new ImageView(this);

            if (i < currentProblemNumber) {

                if (resultArray[i] == true) {
                    image.setBackgroundResource(R.drawable.progress_done);
                } else {
                    image.setBackgroundResource(R.drawable.progress_wrong);
                }

                image.setLayoutParams(lp);

            } else if (i == currentProblemNumber) {
                image.setBackgroundResource(R.drawable.progress_on);
            } else {
                image.setBackgroundResource(R.drawable.progress_empty);
            }

            progressbarLayout.addView(image);
        }

    }

    public void displayQuestion() {

        currentQuestionUnit = questionList.get(currentProblemNumber);

        questionDisaplyLayout = (FrameLayout) findViewById(R.id.quizShowLayout);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);

        // Choose the question type
        Log.d("QuizApp", "Found the type: " + currentQuestionUnit.problemType + "," + questionTypeCollection[0]);
        if (currentQuestionUnit.problemType.equals(questionTypeCollection[0])) {
            showMultipleOption(currentQuestionUnit);
        } else if (currentQuestionUnit.problemType.equals(questionTypeCollection[1])) {
            showClickQuestion(currentQuestionUnit);
        } else if (currentQuestionUnit.problemType.equals(questionTypeCollection[2])) {
            showOpenendQuestion(currentQuestionUnit);
        } else if (currentQuestionUnit.problemType.equals(questionTypeCollection[3])) {
            showRadioButtonQuestion(currentQuestionUnit);
        } else {
            showCheckBoxQuestion(currentQuestionUnit);
        }
    }

    //-----------------------------------
    // (1) Question Type 4
    // display radio button question
    //-----------------------------------
    public void showRadioButtonQuestion(QuestionObject obj) {

        for (int i = 0 ; i < 4 ; i ++) {
            buttonSelectionArray[i] = false;
        }

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);

        ImageView bgView = new ImageView(this);
        bgView.setBackgroundResource(R.drawable.question_bg1);
        bgView.setLayoutParams(lp);
        questionDisaplyLayout.addView(bgView);

        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(50, 10, 20, 10);

        String qeustionNunmberSting = "Question " + obj.problemNumber;
        TextView quesionNumberview = new TextView(this);
        quesionNumberview.setText(qeustionNunmberSting);
        quesionNumberview.setTextSize(questionNumberSize);
        quesionNumberview.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        quesionNumberview.setLayoutParams(lp1);
        questionDisaplyLayout.addView(quesionNumberview);

        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(50, 70, 20, 10);
        lp2.width = (int)(getScreenWidth()*0.8);

        TextView quesionview = new TextView(this);
        quesionview.setText(obj.problemQuestion);
        quesionview.setTextSize(questionNumberSize);
        quesionview.setTextColor(this.getResources().getColor(R.color.userNameColor));
        quesionview.setLayoutParams(lp2);
        questionDisaplyLayout.addView(quesionview);

        // add option view layout
        optionViewLayout = (LinearLayout) findViewById(R.id.optionViewLayout);
        optionViewLayout.setBackgroundResource(R.drawable.question_bg1);
        optionViewLayout.setOrientation(LinearLayout.VERTICAL);

        LayoutParams layPa = optionViewLayout.getLayoutParams();
        layPa.width = (int)(getScreenWidth()*0.95);
        layPa.height = 400;

        RadioGroup radiogroup = new RadioGroup(this);
        LinearLayout.LayoutParams radiogroupparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        radiogroupparams.setMargins(40, 0, 0, 60);
        radiogroup.setLayoutParams(radiogroupparams);
        radiogroup.setGravity(Gravity.CENTER_VERTICAL);

        for (int i = 0; i < 4; i++) {

            rButtonArray[i] = new RadioButton(this);
            rButtonArray[i].setText(obj.optionArray[i]);
            rButtonArray[i].setTextSize(22);
            rButtonArray[i].setId(i + 100);
            radiogroup.addView(rButtonArray[i]);

        }

        optionViewLayout.addView(radiogroup);

    }

    //-----------------------------------
    // (1) Question Type 3
    // display open-end question
    //-----------------------------------
    public void showOpenendQuestion(QuestionObject obj) {


        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 0, 0, 0);

        ImageView bgView = new ImageView(this);
        bgView.setBackgroundResource(R.drawable.question_bg);
        bgView.setLayoutParams(lp);
        questionDisaplyLayout.addView(bgView);

        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(40, 10, 20, 10);

        String qeustionNunmberSting = "Question " + obj.problemNumber;
        TextView quesionNumberview = new TextView(this);
        quesionNumberview.setText(qeustionNunmberSting);
        quesionNumberview.setTextSize(questionNumberSize);
        quesionNumberview.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        quesionNumberview.setLayoutParams(lp1);
        questionDisaplyLayout.addView(quesionNumberview);

        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(50, 70, 20, 10);
        lp2.width = (int)(getScreenWidth()*0.8);

        TextView quesionview = new TextView(this);
        quesionview.setText(obj.problemQuestion);
        quesionview.setTextSize(questionNumberSize);
        quesionview.setTextColor(this.getResources().getColor(R.color.userNameColor));
        quesionview.setLayoutParams(lp2);
        questionDisaplyLayout.addView(quesionview);

        // add option view layout
        optionViewLayout = (LinearLayout) findViewById(R.id.optionViewLayout);
        optionViewLayout.setBackgroundResource(R.drawable.answer_box);
        optionViewLayout.setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams answerL = optionViewLayout.getLayoutParams();
        answerL.width = (int)(getScreenWidth()*0.95);
        answerL.height = 200;

        // add EditText
        editText = new EditText(this);
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(20, 30, 0, 0);
        editText.setLayoutParams(editTextParams);

        editText.setTextSize(20);
        editText.setHint("Enter answer");
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editText.setEnabled(true);

        optionViewLayout.addView(editText, editTextParams);

    }

    //-----------------------------------
    // (1) Question Type 5
    // display check box question
    //-----------------------------------
    public void showCheckBoxQuestion(QuestionObject obj) {

        for (int i = 0 ; i < 2 ; i ++) {
            buttonSelectionArray[i] = false;
        }

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 0, 0, 0);

        ImageView bgView = new ImageView(this);
        bgView.setBackgroundResource(R.drawable.question_bg1);
        bgView.setLayoutParams(lp);
        questionDisaplyLayout.addView(bgView);

        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(50, 10, 20, 10);

        String qeustionNunmberSting = "Question " + obj.problemNumber;
        TextView quesionNumberview = new TextView(this);
        quesionNumberview.setText(qeustionNunmberSting);
        quesionNumberview.setTextSize(questionNumberSize);
        quesionNumberview.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        quesionNumberview.setLayoutParams(lp1);
        questionDisaplyLayout.addView(quesionNumberview);

        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp2.width = (int)(getScreenWidth()*0.85);
        lp2.setMargins(50, 70, 20, 10);

        TextView quesionview = new TextView(this);
        quesionview.setText(obj.problemQuestion);
        quesionview.setTextSize(questionNumberSize);
        quesionview.setTextColor(this.getResources().getColor(R.color.userNameColor));
        quesionview.setLayoutParams(lp2);
        questionDisaplyLayout.addView(quesionview);

        // add option view layout
        optionViewLayout = (LinearLayout) findViewById(R.id.optionViewLayout);
        optionViewLayout.setBackgroundResource(R.drawable.question_bg);
        optionViewLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        checkParams.setMargins(40, 20, 0, 0);
        checkParams.gravity = Gravity.LEFT;

        for (int i = 0 ; i < 4 ; i++) {

            CheckBox checkBox = new CheckBox(this);
            checkBox.setId(i+100);
            checkBox.setText(obj.optionArray[i]);
            checkBox.setTextSize(22);
            //checkBox.setButtonDrawable(R.drawable.custom_checkbox);
            checkBox.setLayoutParams(checkParams);
            cBoxArray[i] = checkBox;
            optionViewLayout.addView(checkBox);

        }


    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    //-----------------------------------
    // (1) Question Type 2
    // display Click question
    //-----------------------------------
    public void showClickQuestion(QuestionObject obj) {

        // set variables
        for (int i = 0 ; i < 2 ; i ++) {
            buttonSelectionArray[i] = false;
        }

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 0, 0, 0);

        ImageView bgView = new ImageView(this);
        bgView.setBackgroundResource(R.drawable.question_bg1);
        bgView.setLayoutParams(lp);
        questionDisaplyLayout.addView(bgView);

        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(60, 10, 20, 10);

        String qeustionNunmberSting = "Question " + obj.problemNumber;
        TextView quesionNumberview = new TextView(this);
        quesionNumberview.setText(qeustionNunmberSting);
        quesionNumberview.setTextSize(questionNumberSize);
        quesionNumberview.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        quesionNumberview.setLayoutParams(lp1);
        questionDisaplyLayout.addView(quesionNumberview);

        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp2.width = (int)(getScreenWidth()*0.8);
        lp2.setMargins(60, 70, 20, 10);

        TextView quesionview = new TextView(this);
        quesionview.setText(obj.problemQuestion);
        quesionview.setTextSize(questionNumberSize);
        quesionview.setTextColor(this.getResources().getColor(R.color.userNameColor));
        quesionview.setLayoutParams(lp2);
        questionDisaplyLayout.addView(quesionview);

        // set image file names
        imageResourceArray[0][0] = R.drawable.q2_option1;
        imageResourceArray[0][1] = R.drawable.checked_q2op1;
        imageResourceArray[1][0] = R.drawable.q2_option2;
        imageResourceArray[1][1] = R.drawable.checked_q2op2;

        // add option view layout
        optionViewLayout = (LinearLayout) findViewById(R.id.optionViewLayout);
        LinearLayout.LayoutParams opLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        opLP.setMargins(20, 0, 0, 0);

        LinearLayout optionViewLayout1 = (LinearLayout) findViewById(R.id.optionViewLayout1);
        LinearLayout.LayoutParams opLP1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        opLP1.setMargins(20, 10, 0, 0);


        LinearLayout optionViewLayout2 = (LinearLayout) findViewById(R.id.optionViewLayout2);
        LinearLayout.LayoutParams opLP2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        opLP2.setMargins(20, 10, 0, 0);

        for (int i = 0 ; i < 2 ; i++) {

            Button optionButton = new Button(this);
            optionButton.setBackgroundResource(imageResourceArray[i][0]);
            optionButton.setTag(i);


            if (i == 0) {

                optionButton.setLayoutParams(opLP1);
                optionViewLayout1.addView(optionButton);

            } else {
                optionButton.setLayoutParams(opLP2);
                optionViewLayout2.addView(optionButton);
            }

            optionButton.setOnClickListener(QuizActivity.this);
            buttonArray[i] = optionButton;

        }


    }

    //-----------------------------------
    // (1) Question Type 1
    // display multiple option question
    //-----------------------------------
    public void showMultipleOption(QuestionObject obj) {

        // set variables
        for (int i = 0 ; i < 4 ; i ++) {
            buttonSelectionArray[i] = false;
        }

        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(50, 30, 20, 10);

        String qeustionNunmberSting = "Question " + obj.problemNumber;
        TextView quesionNumberview = new TextView(this);
        quesionNumberview.setText(qeustionNunmberSting);
        quesionNumberview.setTextSize(questionNumberSize);
        quesionNumberview.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        quesionNumberview.setLayoutParams(lp1);
        questionDisaplyLayout.addView(quesionNumberview);

        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(50, 70, 20, 10);

        TextView quesionview = new TextView(this);
        quesionview.setText(obj.problemQuestion);
        quesionview.setTextSize(questionNumberSize);
        quesionview.setTextColor(this.getResources().getColor(R.color.userNameColor));
        quesionview.setLayoutParams(lp2);
        questionDisaplyLayout.addView(quesionview);


        FrameLayout.LayoutParams lp3 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp3.setMargins(90, 120, 20, 0);

        ImageView questionImageView = new ImageView(this);
        questionImageView.setBackgroundResource(R.drawable.question1_image);
        questionImageView.setLayoutParams(lp3);
        questionDisaplyLayout.addView(questionImageView);


        // set image file names
        imageResourceArray[0][0] = R.drawable.option1_image;
        imageResourceArray[0][1] = R.drawable.checked_option1;
        imageResourceArray[1][0] = R.drawable.option2_image;
        imageResourceArray[1][1] = R.drawable.checked_option2;
        imageResourceArray[2][0] = R.drawable.option3_image;
        imageResourceArray[2][1] = R.drawable.checked_option3;
        imageResourceArray[3][0] = R.drawable.option4_image;
        imageResourceArray[3][1] = R.drawable.checked_option4;

        // add option view layout
        optionViewLayout = (LinearLayout) findViewById(R.id.optionViewLayout);
        LinearLayout.LayoutParams opLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        opLP.setMargins(20, 0, 0, 0);

        LinearLayout optionViewLayout1 = (LinearLayout) findViewById(R.id.optionViewLayout1);
        LinearLayout.LayoutParams opLP1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        opLP1.setMargins(20, 20, 0, 0);

        LinearLayout optionViewLayout2 = (LinearLayout) findViewById(R.id.optionViewLayout2);
        LinearLayout.LayoutParams opLP2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        opLP2.setMargins(20, 20, 0, 0);

        for (int i = 0 ; i < 4 ; i++) {

            Button optionButton = new Button(this);
            optionButton.setBackgroundResource(imageResourceArray[i][0]);
            optionButton.setText(obj.optionArray[i]);
            optionButton.setTag(i);
            if (i < 2) {
                optionButton.setLayoutParams(opLP1);
                optionViewLayout1.addView(optionButton);
            } else {
                optionButton.setLayoutParams(opLP2);
                optionViewLayout2.addView(optionButton);
            }

            optionButton.setOnClickListener(QuizActivity.this);
            buttonArray[i] = optionButton;

        }

    }

    public void addRadioButton(Context ctx,int num){

        RadioGroup radioGroup= new RadioGroup(this); //(RadioGroup) alertInflatedView.findViewById(R.id.radiogroup);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);

        for(int i=0; i<num; i++){

            RadioButton radioButton  = new RadioButton(ctx);
            radioButton.setId(1+i);
            radioButton.setText("Radio"+i);
            radioButton.setTextSize(16);
            radioButton.setTextColor(getResources().getColor(R.color.userNameColor));
            radioButton.setButtonDrawable(R.drawable.radio_button_selector);
            radioButton.setPadding(80,0,0,0);
            radioButton.setGravity(Gravity.CENTER_VERTICAL);
            radioButton.setLayoutParams(layoutParams);
            radioGroup.addView(radioButton);

        }

    }


    public void allResetExceptWithIndex(int index, int maxNum) {

        // Reset all other buttons' selection info
        for (int i = 0 ; i < maxNum ; i++) {
            if(i != index) {
                Log.d("quizApp", "i = " + i + " current index: "+index);
                buttonSelectionArray[i] = false;
            }
        }

        // Reset all other buttons
        for (int i = 0 ; i < maxNum ; i++) {
            if (i != index) {

                Button tBtn = buttonArray[i];
                tBtn.setBackgroundResource(imageResourceArray[i][0]);
            }
        }

    }

    // When clicking option buttons
    @Override
    public void onClick(View v) {

        int tag = (int)v.getTag();

        Log.d("quizApp", "onClick: " + v.getTag() + " is pressed:"+v.isPressed() + " isSelcted:"+ buttonSelectionArray[tag]);

        if (buttonSelectionArray[tag]==false) {

            buttonSelectionArray[tag] = true;
            v.setBackgroundResource(imageResourceArray[tag][1]);
            if (currentProblemNumber == 0) {
                allResetExceptWithIndex(tag, 4);
            } else {
                allResetExceptWithIndex(tag, 2);
            }

        } else {

            buttonSelectionArray[tag] = false;
            v.setBackgroundResource(imageResourceArray[tag][0]);
        }


        currentAnswerNumber[myAnswerIndex] = tag + 1;

    }

    public void clickSubmitButton(View view) {

        isCorrect = checkCurrentAnswer();

        if (isCorrect == true) { // correct
            showImage(true);
        } else { // incorrect
            showImage(false);
        }

    }

    // This function is called when clicking the "SUBMIT" button
    public boolean checkCurrentAnswer() {

        boolean re_value = true;

        if (currentProblemNumber == 2) { // check currentAnswer (open-end question)

            currentAnswer = editText.getText().toString();
            Log.d("CheckAnswer(Open-end)", currentAnswer + "," + currentQuestionUnit.correctAnswer4Openend);
            if (currentAnswer.equalsIgnoreCase(currentQuestionUnit.correctAnswer4Openend)) {
                re_value = true;
            } else {
                re_value = false;
            }
        } else if (currentProblemNumber == 3) { // radio button

            re_value = checkRadioButtonAnswer();

        } else if (currentProblemNumber == 4) { // check currentAnswerNumber

            re_value = checkCheckBoxAnswer();

        } else {

            if (currentQuestionUnit.correctAnswerArray.length > 1) {
                re_value = checkMultipleAnswers();
            } else {
                Log.d("CheckAnswer",currentQuestionUnit.correctAnswerArray[myAnswerIndex] +","+currentAnswerNumber[myAnswerIndex]);
                if (currentQuestionUnit.correctAnswerArray[myAnswerIndex] == currentAnswerNumber[myAnswerIndex]) {
                    re_value = true;
                } else {
                    re_value = false;
                }
            }

        }

        resultArray[currentProblemNumber] = re_value;

        return re_value;
    }

    public boolean checkMultipleAnswers() {

        for (int i = 0 ; i < currentQuestionUnit.correctAnswerArray.length ; i++) {
            if (currentQuestionUnit.correctAnswerArray[i] != currentAnswerNumber[i]) return false;
        }

        return true;
    }

    public boolean checkCheckBoxAnswer() {

        // convert the checkbox into the current currentAnswerNumber
        int answerIndex = 0;
        for (int i = 0 ; i < cBoxArray.length; i++) {
            CheckBox currBox = cBoxArray[i];
            Log.d("Check the answer", "checked?"+currBox.isChecked());
            if (currBox.isChecked()) {
                currentAnswerNumber[answerIndex] = i+1;
                answerIndex++;
            }
        }

        for (int i = 0 ; i < currentQuestionUnit.correctAnswerArray.length ; i++) {
            if (currentQuestionUnit.correctAnswerArray[i] != currentAnswerNumber[i]) return false;
        }

        return true;
    }

    public boolean checkRadioButtonAnswer() {

        // convert the checkbox into the current currentAnswerNumber
        int answerIndex = 0;
        for (int i = 0 ; i < rButtonArray.length; i++) {
            RadioButton rButton = rButtonArray[i];
            Log.d("Check the answer", "checked?"+rButton.isChecked());
            if (rButton.isChecked()) {
                currentAnswerNumber[answerIndex] = i+1;
                answerIndex++;
            }
        }

        for (int i = 0 ; i < currentQuestionUnit.correctAnswerArray.length ; i++) {
            if (currentQuestionUnit.correctAnswerArray[i] != currentAnswerNumber[i]) return false;
        }

        return true;
    }

    public void showImage(boolean isCorrect) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setCancelable(false);
        if (isCorrect == true) {
            dialog.setContentView(R.layout.popup);
        } else {
            dialog.setContentView(R.layout.popup_wrong);
        }

        TextView title = (TextView) dialog.findViewById(R.id.popup_title);
        TextView message = (TextView) dialog.findViewById(R.id.popup_text);
        Button dialogButton = (Button) dialog.findViewById(R.id.ok_button);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clearAndGoToNext();
            }
        });

        dialog.show();

    }

    public void clearAndGoToNext() {

        Log.d("QuizApp","clear and go next");

        // check to go or end
        currentProblemNumber++;
        if (currentProblemNumber == totalNumOfProblems) {

            // show the current result page
            int number = getNumberOfCorrectAnswer();

            // check this user already did or not
            if (ScoreStorage.getInstance().isThisUserHere(currUserName)) {

                int savedScore = ScoreStorage.getInstance().getScore(currUserName);

                if (number > savedScore) {
                    ScoreStorage.getInstance().updateScoreWithName(currUserName, number);
                }

                Log.d("update the score: ", currUserName + ", score:" +number);

            } else {
                scoreUnit sUnit = new scoreUnit();
                sUnit.createScoreUnit(currUserName, number);
                ScoreStorage.getInstance().addNewScore(sUnit);
                Log.d("adding the new one",sUnit.userName);
            }

            ScoreStorage.getInstance().displayAllScores();
            showCurrentQuizResult();


        } else {

            // remove all problem-related view from the layout
            questionDisaplyLayout.removeAllViews();
            progressbarLayout.removeAllViews();

            LinearLayout l1 = (LinearLayout) findViewById(R.id.optionViewLayout1);
            l1.removeAllViewsInLayout();
            LinearLayout l2 = (LinearLayout) findViewById(R.id.optionViewLayout2);
            l2.removeAllViewsInLayout();

            // clear the array
            for (int i = 0; i < 4; i++) {
                buttonArray[i] = null;
            }

            if (currentProblemNumber == 3) {
                editText.setVisibility(EditText.GONE);
            } else if (currentProblemNumber == 4) {
                optionViewLayout.removeAllViewsInLayout();
            }

            //go to the next question
            startQuiz();

        }

    }

    public int getNumberOfCorrectAnswer() {
        int count = 0;

        for (int i = 0; i < resultArray.length ; i++) {
            if (resultArray[i] == true) {
                count++;
            }
        }

        return count;
    }

    public void showCurrentQuizResult() {

        currentProblemNumber = 0;

        Log.d("QuizApp", "show currentQuizResult");
        int totCorrectAnswer = getNumberOfCorrectAnswer();

        // call current quiz result activity
        Intent sResultIntent = new Intent(this, showCurrentResult.class);
        // pass user name from the edit text
        sResultIntent.putExtra("user_name", currUserName);
        sResultIntent.putExtra("numOfCorrectanswer", totCorrectAnswer);

        startActivity(sResultIntent);

    }

    public void getDataFromJsonFile() {

        // read json string
        String jsonString = loadJSONFromAsset("questionSets.json");

        // save the question set data
        saveQuestionSet(jsonString);

    }

    public void saveQuestionSet(String jsonString) {

        questionList = new ArrayList<QuestionObject>();
        Log.d("QuizApp", "saveQuestionSet");

        try {

            JSONObject obj = new JSONObject(jsonString);
            JSONArray proArray = obj.getJSONArray("problems");

            for (int i = 0 ; i < proArray.length(); i++) {

                JSONObject object = proArray.getJSONObject(i);
                int number = object.getInt("problemNumber");
                String type = object.getString("problemType");
                String question = object.getString("problemQuestion");
                String imgFileName = object.getString("imageFileName");
                String ca4Openend = object.getString("correctAnswer4Openend");


                JSONArray options = object.getJSONArray("options");
                String[] _options = new String[4];
                for (int j = 0 ; j < options.length(); j++) {

                    String op = options.getString(j);
                    _options[j] = op;
                }

                JSONArray answers = object.getJSONArray("correctAnswer");
                int[] _answers = new int[2];
                for (int j = 0 ; j < answers.length(); j++) {
                    int obj1 = answers.getInt(j);
                    _answers[j] = obj1;
                }

                QuestionObject questionUnit = new QuestionObject();
                questionUnit.createQuestionObject(number,type, question, imgFileName,ca4Openend, _options, _answers);
                questionList.add(questionUnit);

            }

        } catch (Throwable t) {

            Log.e("QuizApp", "Could not parse malformed JSON: \"" + jsonString + "\"");
        }

    }

    // Read Json file from Assets and save that with the format of string
    private String loadJSONFromAsset(String fileName) {

        String json = null;
        try {

            Log.d("QuizApp", "loadJSONFromAsset: "+fileName);

            InputStream is = getAssets().open(fileName);

            StringBuilder buf=new StringBuilder();
            BufferedReader in=
                    new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            json = buf.toString();

            Log.d("QuizApp", "loadJSONFromAsset: "+json);

        } catch (IOException ex) {

            Log.d("QuizApp", "loadJSONFromAsset: No file"+ex.toString());
            ex.printStackTrace();
            return null;
        }

        return json;
    }


    public void receiveDataFromDashboardActivity() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currUserName = extras.getString("user_name");
        }

    }

    public void setUserName() {

        TextView userNameTextView = (TextView)findViewById(R.id.userNameView2);
        // set the user name at the display
        userNameTextView.setText(currUserName);

    }


}