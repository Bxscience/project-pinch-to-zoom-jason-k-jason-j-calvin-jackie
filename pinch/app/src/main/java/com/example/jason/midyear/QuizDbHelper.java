package com.example.jason.midyear;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jason.midyear.QuizContract.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "csquiz.db";
    private static final int DATABASE_VERSION = 15;
    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_CATEGORY + " TEXT, " +
                QuestionsTable.COLUMN_SHORTCUT + " TEXT, " +
                QuestionsTable.COLUMN_QUESTION + " INTEGER, " +
                QuestionsTable.COLUMN_ANSWER + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        List<String> algorithmsAnswers= Arrays.asList("a","a","c","d","b","b","a","a","a","e");
        for(int i=0;i<10;i++){
            addQuestion(new Question("Algorithms and Big O","algorithms",i+1,algorithmsAnswers.get(i)));
        }
        List<String> arithmeticAnswers= Arrays.asList("c","b","c","d","c","e","d","d","d","e");
        for(int i=0;i<10;i++){
            addQuestion(new Question("Arithmetic and Boolean Expressions","a_b",i+1,arithmeticAnswers.get(i)));
        }
        List<String> listAnswers= Arrays.asList("e","c","d","c","d","c","e","a","b","d");
        for(int i=0;i<10;i++){
            addQuestion(new Question("ArrayList","list",i+1,listAnswers.get(i)));
        }
        List<String> arraysAnswers= Arrays.asList("b","c","b","a","e","e","a","b","e","e");
        for(int i=0;i<10;i++){
            addQuestion(new Question("Arrays","arrays",i+1,arraysAnswers.get(i)));
        }
        List<String> classesAnswers= Arrays.asList("c","e","e","a","e","d","c","c","c","e");
        for(int i=0;i<10;i++){
            addQuestion(new Question("Classes, Interfaces, Inheritance, and Polymorphism","classes",i+1,classesAnswers.get(i)));
        }
        List<String> fieldsAnswers= Arrays.asList("d","e","e","d","b","c","e","d","b","c");
        for(int i=0;i<10;i++){
            addQuestion(new Question("Fields, Methods, and Parameter Passing","fmp",i+1,fieldsAnswers.get(i)));
        }
        List<String> loopsAnswers= Arrays.asList("e","d","e","a","c","a","a","c","a","b");
        for(int i=0;i<10;i++){
            addQuestion(new Question("Loops","loops",i+1,loopsAnswers.get(i)));
        }
        List<String> recursionAnswers= Arrays.asList("c","d","d","e","d","a","e","c","a","b");
        for(int i=0;i<10;i++){
            addQuestion(new Question("Recursion","recur",i+1,recursionAnswers.get(i)));
        }
        List<String> sortingAnswers= Arrays.asList("c","a","b","b","c","e","a","c","d","c");
        for(int i=0;i<10;i++){
            addQuestion(new Question("Sorting","sorting",i+1,sortingAnswers.get(i)));
        }
        List<String> stringsAnswers= Arrays.asList("a","b","e","d","b","c","c","a","a","a");
        for(int i=0;i<10;i++){
            addQuestion(new Question("Strings","strings",i+1,stringsAnswers.get(i)));
        }
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_CATEGORY, question.getCategory());
        cv.put(QuestionsTable.COLUMN_SHORTCUT,question.getShortcut());
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_ANSWER, question.getAnswer());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public List<Question> getQuestions(String category) {
        List<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT "+QuestionsTable.COLUMN_CATEGORY+", "+QuestionsTable.COLUMN_SHORTCUT+", "+ QuestionsTable.COLUMN_QUESTION+", "+ QuestionsTable.COLUMN_ANSWER+" FROM " + QuestionsTable.TABLE_NAME+
                " WHERE "+QuestionsTable.COLUMN_CATEGORY+" = '"+category+"'", null);
        int i=0;
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setCategory(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY)));
                question.setShortcut(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_SHORTCUT)));
                question.setQuestion(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setAnswer(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER)));
                questionList.add(question);
                i++;
            } while (c.moveToNext() && i<5);
        }

        c.close();
        return questionList;
    }
    public List<String> getAllCategories(){
        List<String> categoryList = new ArrayList<>();
        db=getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT "+QuestionsTable.COLUMN_CATEGORY+" FROM "+QuestionsTable.TABLE_NAME, null);
        if(c.moveToFirst()){
            do{
                categoryList.add(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY)));
            }while (c.moveToNext());
        }
        return categoryList;
    }
}
