package com.example.jason.midyear;

import android.provider.BaseColumns;

public final class QuizContract {

    private QuizContract() {
    }

    public static class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_SHORTCUT = "shortcut";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_ANSWER = "answer";
    }
}