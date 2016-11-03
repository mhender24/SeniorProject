package com.example.platinum_express.seniorprojectandroid;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import java.util.Date;

import static com.example.platinum_express.seniorprojectandroid.R.id.op;

/**
 * Created by marcelhenderson on 10/7/16.
 */

    public class AddPop extends Dialog implements OnClickListener
    {
        Timesheet timesheet;
        Spinner process;
        EditText operator;
        EditText date;
        EditText boards;
        EditText hours;
        Spinner task;

        public AddPop(Context context, Timesheet timesheet) {
            super(context);
            this.timesheet = timesheet;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setTitle("Pop up Prototype");
            setContentView(R.layout.add_pop);

            process = (Spinner)findViewById(R.id.process_spin);
            operator = (EditText)findViewById(op);
            date = (EditText)findViewById(R.id.date);
            boards = (EditText)findViewById(R.id.board);
            hours = (EditText)findViewById(R.id.hours);
            task = (Spinner)findViewById(R.id.task);
            Button b = (Button) findViewById(R.id.submit);
            b.setOnClickListener(this);
            ArrayAdapter process_adapter = ArrayAdapter.createFromResource(
                    this.getContext(), R.array.process_array, android.R.layout.simple_spinner_item);
            process.setAdapter(process_adapter);
            ArrayAdapter task_adapter = ArrayAdapter.createFromResource(
                    this.getContext(), R.array.task_array, android.R.layout.simple_spinner_item);
            task.setAdapter(task_adapter);
        }

        public void onClick(View v) {
            //add to database
            ClientDb db = new ClientDb(getContext());
            int parsedBoards = Integer.parseInt(boards.getText().toString().trim());
            int parsedHours = Integer.parseInt(hours.getText().toString().trim());
            db.testInsertIntoTimesheet(process.getSelectedItem().toString(), parsedBoards, parsedHours, task.getSelectedItem().toString());
            timesheet.displayTimesheet();
            Log.d("Mine", "Exiting dialog box");
            dismiss();
        }
    }
