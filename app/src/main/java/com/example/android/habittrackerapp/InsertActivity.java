package com.example.android.habittrackerapp;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.habittrackerapp.data.HabitContract;
import com.example.android.habittrackerapp.data.HabitDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InsertActivity extends AppCompatActivity {

    EditText mNameEditText;
    TextView mDateTextView;
    Spinner mFrequencySpinner;

    private String mCurrentDate;
    private int mFrequency = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        mNameEditText = (EditText) findViewById(R.id.edit_habit_name);
        mDateTextView = (TextView) findViewById(R.id.text_date);
        mFrequencySpinner = (Spinner) findViewById(R.id.spinner_frequency);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = sdf.format(c.getTime());
        mDateTextView.setText(mCurrentDate);

        setupSpinner();
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter frequencySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_frequecy_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        frequencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mFrequencySpinner.setAdapter(frequencySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.frequency_high))) {
                        mFrequency = HabitContract.HabitEntry.FREQUENCY_HIGH;
                    } else if (selection.equals(getString(R.string.frequency_low))) {
                        mFrequency = HabitContract.HabitEntry.FREQUENCY_LOW;
                    } else {
                        mFrequency = HabitContract.HabitEntry.FREQUENCY_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFrequency = HabitContract.HabitEntry.FREQUENCY_UNKNOWN;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insert, menu);
        return true;
    }

    private void insertHabit() {
        String nameString = mNameEditText.getText().toString().trim();

        // Create database helper
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //Create a ContentValues object
        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitContract.HabitEntry.COLUMN_HABIT_NAME, nameString);
        contentValues.put(HabitContract.HabitEntry.COLUMN_DATE, mCurrentDate);
        contentValues.put(HabitContract.HabitEntry.COLUMN_HABIT_FREQUENCY, mFrequency);

        // Insert a new row for habit in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, contentValues);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving habit", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertHabit();
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
