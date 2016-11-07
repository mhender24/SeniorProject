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
import static com.example.platinum_express.seniorprojectandroid.R.id.date;
import static com.example.platinum_express.seniorprojectandroid.R.id.horizontalView;
import static com.example.platinum_express.seniorprojectandroid.R.id.textView;
import java.text.SimpleDateFormat;
import java.util.Date;


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
        Cursor entries = getAllTimeSheetRecordsForUser();
        Log.d("Mine", "Number = " + entries.getCount());
        TableRow tableRow = null;
        while(entries.moveToNext()) {
            tableRow = createTableRow(entries);
            history.addView(tableRow);
        }
        entries.close();
    }

    private TableRow createTableRow(Cursor entry){
        TableRow tableRow = new TableRow(this);
        for (int j = 0; j < 6; j++) {
            TextView tView = createTimesheetTextViewRecord(entry, j);
            tableRow.addView(tView);
        }
        return tableRow;
    }

    private TextView createTimesheetTextViewRecord(Cursor entry, int j){
        int tableWidths[] = {120, 120, 100, 135, 120, 300};
        TextView textView = new TextView(this);

        if(j==2) {
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
               // Log.d("Data", "returned: " + entry.getString(j+1));
                String reformattedStr = myFormat.format(fromUser.parse(entry.getString(j + 1)));
                Log.d("Data", "formatted: " + reformattedStr);
                textView.setText(reformattedStr);
            } catch(Exception e){
                Log.d("Error", "Could not convert");
            }
        }
        else
            textView.setText(entry.getString(j+1));
        
        textView.setPadding(12, 5, 0, 0);
        textView.setWidth(tableWidths[j]);
        return textView;
    }

    private Cursor getAllTimeSheetRecordsForUser(){
        ClientDb db = new ClientDb(this);
        return db.getTimeSheetRecordsForUser();
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
}
