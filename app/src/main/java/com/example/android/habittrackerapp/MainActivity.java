package com.example.android.habittrackerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.habittrackerapp.data.HabitContract;
import com.example.android.habittrackerapp.data.HabitDbHelper;

;

public class MainActivity extends AppCompatActivity {

    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new HabitDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                HabitContract.HabitEntry.ID,
                HabitContract.HabitEntry.COLUMN_HABIT_NAME,
                HabitContract.HabitEntry.COLUMN_DATE,
                HabitContract.HabitEntry.COLUMN_HABIT_FREQUENCY
        };

        // Create the Cursor
        Cursor cursor = db.query(HabitContract.HabitEntry.TABLE_NAME, projection, null, null,
                null, null, null);

        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {
            displayView.append(
                    HabitContract.HabitEntry.ID + " -" +
                            HabitContract.HabitEntry.COLUMN_HABIT_NAME + " -" +
                            HabitContract.HabitEntry.COLUMN_DATE + " -" +
                            HabitContract.HabitEntry.COLUMN_HABIT_FREQUENCY + "\n");

            // Get the index of the columns
            int idColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_HABIT_NAME);
            int dateColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DATE);
            int frequencyColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_HABIT_FREQUENCY);

            // While cycle to move through the rows
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                int currentFrequency = cursor.getInt(frequencyColumnIndex);

                displayView.append(("\n" + currentId + " -" +
                        currentName + " -" +
                        currentDate + " -" +
                        currentFrequency));
            }
        } finally {
            cursor.close();
        }
    }
}
