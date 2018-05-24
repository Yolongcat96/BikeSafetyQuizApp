package com.example.android.bikesafetyquizapp;

/**
 * Created by smseol on 3/12/18.
 */
// QuestionObject Unit
public class QuestionObject {

    public int problemNumber;
    public String problemType;
    public String problemQuestion;
    public String imageFileName;
    public String correctAnswer4Openend; // for the special case
    public String[] optionArray;
    public int[] correctAnswerArray;

    public void createQuestionObject(int number, String type, String question, String imageFileName, String correctAnswer4Openend, String[] opArray, int[] caArray) {
        this.problemNumber = number;
        this.problemType = type;
        this.problemQuestion = question;
        this.imageFileName = imageFileName;
        this.correctAnswer4Openend = correctAnswer4Openend;
        this.optionArray = opArray;
        this.correctAnswerArray = caArray;

    }

}
