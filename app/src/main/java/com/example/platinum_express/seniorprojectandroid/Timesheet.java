package com.example.platinum_express.seniorprojectandroid;

import android.app.Dialog;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static android.R.attr.entries;

public class Timesheet extends AppCompatActivity {

    private final int DATE_POSITION_IN_ARRAY = 2;
    HorizontalScrollView heading;
    HorizontalScrollView layout;
    EditText batch;
    TableLayout history;
    int tableWidths[] = {120, 120, 100, 135, 120, 300};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet);
        batch = (EditText) findViewById(R.id.batch_text);
        history = (TableLayout) findViewById(R.id.history);
        displayTimesheet();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }




    public void displayTimesheet() {
        clearHistory();
        GetTimesheetData timesheet = new GetTimesheetData();
        try {
            timesheet.execute().get();
            TableRow tableRow = null;
            for (int i = 0; i < timesheet.dataList.size(); i++) {
                HashMap<String, String> entry = timesheet.dataList.get(i);
                tableRow = createTableRow(entry);
                history.addView(tableRow);
            }
        } catch (InterruptedException e) {
            Log.d("Error", "You had an interruptedException:    " + e);
        } catch (ExecutionException e) {
            Log.d("Error", "You had an execution exception:    " + e);
        }
    }

    private TableRow createTableRow(HashMap<String, String> entry) {
        TableRow tableRow = new TableRow(this);
        for (int j = 0; j < 6; j++) {
            TextView tView = createTimesheetTextViewRecord(entry, j);
            tableRow.addView(tView);
        }
        return tableRow;
    }

    private TextView createTimesheetTextViewRecord(HashMap<String, String> entry, int j) {
        TextView textView = new TextView(this);
        String[] keys = {"Process", "Operator", "Date", "Boards", "Hours", "Task"};
        if (j == DATE_POSITION_IN_ARRAY)
            textView.setText(formatDate(entry.get("Date")));
        else
            textView.setText(entry.get(keys[j]));
        textView.setPadding(12, 5, 0, 0);
        textView.setWidth(tableWidths[j]);
        return textView;
    }

    private String formatDate(String date) {
        Log.d("Date", "date= " + date);
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedStr = "";
        try {
            formattedStr = myFormat.format(fromUser.parse(date));
        } catch (Exception e) {
            Log.d("Error", "Could not convert");
        }
        return formattedStr;
    }

    /*
    private Cursor getAllTimeSheetRecordsForUser(){
        ClientDb db = new ClientDb(this);
        return db.getTimeSheetRecordsForUser();
    }
    */

    public void search(View view) {
        displayTimesheet();
    }

    public void clearHistory() {
        if (history.getChildCount() > 0)
            history.removeAllViews();
    }

    public void add_entry(View view) {
        Dialog dlg = new AddPop(this, this);
        dlg.show();
    }





}
