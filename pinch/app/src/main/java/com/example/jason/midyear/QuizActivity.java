package com.example.jason.midyear;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    private TextView selectedAnswer;
    private Button submit;
    private ImageView question;
    private Button back;
    private Button next;
    private ImageView check;
    private TextView category;

    private List<Question> questionList;
    private List<String> choicesList;
    private List<RadioButton> radioButtonsList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private String correctChoice;
    private int score;
    private boolean answered;
    private boolean resubmitted;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mImageView=(ImageView)findViewById(R.id.question);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        rb5 = findViewById(R.id.radio_button5);
        check=findViewById(R.id.check);
        selectedAnswer=findViewById(R.id.selectedAnswer);
        submit = findViewById(R.id.submitButton);
        question= findViewById(R.id.question);
        back= findViewById(R.id.backButton);
        next = findViewById(R.id.nextButton);
        category=findViewById(R.id.text_view_category);

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        Intent intent=getIntent();
        String categoryName=intent.getStringExtra(StartingScreenActivity.EXTRA_CATEGORY_NAME);
        category.setText(categoryName);
        questionList = dbHelper.getQuestions(categoryName);
        choicesList= new ArrayList<String>();
        radioButtonsList=new ArrayList<RadioButton>(Arrays.asList(rb1,rb2,rb3,rb4,rb5));
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);
        rbGroup.clearCheck();
        selectedAnswer.setText("Selected Answer: ");
        score=0;

        showNextQuestion();

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked() || rb5.isChecked()) {
                    clearText();
                    switch (rbGroup.getCheckedRadioButtonId()) {
                        case R.id.radio_button1:
                            rb1.setText("***(A)");
                            break;
                        case R.id.radio_button2:
                            rb2.setText("***(B)");
                            break;
                        case R.id.radio_button3:
                            rb3.setText("***(C)");
                            break;
                        case R.id.radio_button4:
                            rb4.setText("***(D)");
                            break;
                        case R.id.radio_button5:
                            rb5.setText("***(E)");
                            break;
                    }
                    selectedAnswer.setText("Selected Answer: " + findViewById(rbGroup.getCheckedRadioButtonId()).getTag());
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered && !resubmitted) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()|| rb4.isChecked() || rb5.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(answered && !resubmitted)
                    recheckAnswers();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answered)
                    showNextQuestion();
                else {
                    Toast.makeText(QuizActivity.this, "Please submit an answer first before moving on to the next question", Toast.LENGTH_SHORT).show();
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showNextQuestion() {
        clearText();
        selectedAnswer.setText("Selected Answer: ");
        rbGroup.clearCheck();
        check.setImageDrawable(null);
        submit.setEnabled(true);
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            int questionId= getResources().getIdentifier(currentQuestion.getShortcut()+"_"+currentQuestion.getQuestion(),"drawable",getPackageName());
            question.setImageResource(questionId);
            choicesList=new ArrayList<String>();
            String aName=currentQuestion.getShortcut()+"_"+currentQuestion.getQuestion()+"a";
            String bName=currentQuestion.getShortcut()+"_"+currentQuestion.getQuestion()+"b";
            String cName=currentQuestion.getShortcut()+"_"+currentQuestion.getQuestion()+"c";
            String dName=currentQuestion.getShortcut()+"_"+currentQuestion.getQuestion()+"d";
            String eName=currentQuestion.getShortcut()+"_"+currentQuestion.getQuestion()+"e";

            choicesList.add(aName);
            choicesList.add(bName);
            choicesList.add(cName);
            choicesList.add(dName);
            choicesList.add(eName);

            correctChoice=currentQuestion.getShortcut()+"_"+currentQuestion.getQuestion()+currentQuestion.getAnswer();
            int aId =getResources().getIdentifier(aName,"drawable",getPackageName());
            rb1.setButtonDrawable(aId);
            int bId =getResources().getIdentifier(bName,"drawable",getPackageName());
            rb2.setButtonDrawable(bId);
            int cId =getResources().getIdentifier(cName,"drawable",getPackageName());
            rb3.setButtonDrawable(cId);
            int dId =getResources().getIdentifier(dName,"drawable",getPackageName());
            rb4.setButtonDrawable(dId);
            int eId =getResources().getIdentifier(eName,"drawable",getPackageName());
            rb5.setButtonDrawable(eId);

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            resubmitted=false;
            submit.setText("Submit");
        } else {
            finishQuiz();
        }
    }

    private void checkAnswer() {
        answered = true;
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected);
        selectedAnswer.setText("Selected Answer: "+rbSelected.getTag());
        if (choicesList.get(answerNr).equals(correctChoice)) {
            score++;
            textViewScore.setText("Score: " + score);
            Drawable drawable=getResources().getDrawable(R.drawable.correct);
            check.setImageDrawable(drawable);
            submit.setEnabled(false);
        }
        else {
            Drawable drawable=getResources().getDrawable(R.drawable.incorrect);
            check.setImageDrawable(drawable);
            shuffleAnswers();
        }
        if (questionCounter==questionCountTotal)
            next.setText("Finish");
    }

    private void shuffleAnswers(){
        clearText();
        Collections.shuffle(choicesList);
        for(int i=0;i<radioButtonsList.size();i++){
            int picid=getResources().getIdentifier(choicesList.get(i),"drawable",getPackageName());
            radioButtonsList.get(i).setButtonDrawable(picid);
        }
        submit.setText("Resubmit");
}
    private void recheckAnswers(){
        resubmitted=true;
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected);
        selectedAnswer.setText("Selected Answer: "+rbSelected.getTag());
        if (choicesList.get(answerNr).equals(correctChoice)){
            score++;
            textViewScore.setText("Score: " + score);
            Drawable drawable=getResources().getDrawable(R.drawable.correct);
            check.setImageDrawable(drawable);
        }
        submit.setEnabled(false);
    }
    private void finishQuiz() {
        finish();
    }
    private void clearText(){
        rb1.setText("(A)");
        rb2.setText("(B)");
        rb3.setText("(C)");
        rb4.setText("(D)");
        rb5.setText("(E)");
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            question.setScaleX(mScaleFactor);
            question.setScaleY(mScaleFactor);
            return true;
        }
    }
    public boolean isWithinQuestionBounds(int xPoint, int yPoint) {
               int[] l = new int[2];
               question.getLocationOnScreen(l);
               int x = l[0];
               int y = l[1];
               int w = question.getWidth();
               int h = question.getHeight();

               if (xPoint< x || xPoint> x + w || yPoint< y || yPoint> y + h) {
                   return false;
               }
               return true;
           }
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
              if(isWithinQuestionBounds(Math.round(motionEvent.getRawX()), Math.round(motionEvent.getRawY())))
              {
                  mScaleGestureDetector.onTouchEvent(motionEvent);

                  return true;
              } else {

                  return super.dispatchTouchEvent(motionEvent);
              }



    }
}
