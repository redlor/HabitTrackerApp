package com.example.android.habittrackerapp.data;


import android.provider.BaseColumns;

public class HabitContract {

    public static abstract class HabitEntry implements BaseColumns {
        // Defining the name of the table
        public static final String TABLE_NAME = "habits";

        // Table columns
        public static final String ID = "id";
        public static final String COLUMN_HABIT_NAME = "name";
        public static final String COLUMN_DATE = "last_date";
        public static final String COLUMN_HABIT_FREQUENCY = "frequency";

        // Possible values for frequency
        public static final int FREQUENCY_UNKNOWN = 0;
        public static final int FREQUENCY_HIGH = 1;
        public static final int FREQUENCY_LOW = 2;
    }
}
