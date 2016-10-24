package com.example.platinum_express.seniorprojectandroid;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static android.R.attr.entries;
import static android.graphics.Canvas.EdgeType.AA;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.view.ViewGroup.*;
import static com.example.platinum_express.seniorprojectandroid.R.id.horizontalView;
import static com.example.platinum_express.seniorprojectandroid.R.id.textView;

public class Timesheet extends AppCompatActivity{

    EditText batch;
    TableLayout history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet);

        batch = (EditText) findViewById(R.id.batch_text);
        history = (TableLayout) findViewById(R.id.history);

        displayTimesheet();
    }

    public void displayTimesheet(){
        clearHistory();
        ClientDb db = new ClientDb(this);
        Cursor entries = db.getTimeSheetRecordsForUser();
        Log.d("Mine", "Entires = " + Integer.toString(entries.getColumnCount()));
        int tableWidths[] = {120, 120, 100, 135, 120, 300};
        TextView textView;
        TableRow tableRow = null;
        while(entries.moveToNext()) {
            tableRow = new TableRow(this);
            for (int j = 0; j < 6; j++) {
                Log.d("Mine", "Number " + j + " = " + entries.getString(j));
                textView = new TextView(this);
                textView.setText(entries.getString(j+1));
                textView.setPadding(12, 5, 0, 0);
                textView.setWidth(tableWidths[j]);
                tableRow.addView(textView);
            }
            history.addView(tableRow);
        }
        entries.close();
    }

    public void search(View view){
        displayTimesheet();
    }

    public void clearHistory(){
        if(history.getChildCount() > 0)
            history.removeAllViews();
    }

    public void add_entry(View view){
        Dialog dlg = new AddPop(this, this);
        dlg.show();
    }

    public void createRow(){
        TableRow tableRow = new TableRow(this);
        EditText editText;

        Spinner process = new Spinner(this);
        ArrayAdapter process_adapter = ArrayAdapter.createFromResource(
                this, R.array.process_array, android.R.layout.simple_spinner_item);
        process.setAdapter(process_adapter);
        tableRow.addView(process);

        for(int i =0; i< 4; i++){
            editText = new EditText(this);
            editText.setPadding(10, 10, 10, 10);
            tableRow.addView(editText);
        }

        Spinner task = new Spinner(this);
        ArrayAdapter task_adapter = ArrayAdapter.createFromResource(
                this, R.array.task_array, android.R.layout.simple_spinner_item);
        task.setAdapter(task_adapter);

        tableRow.addView(task);
        history.addView(tableRow);
    }
}
