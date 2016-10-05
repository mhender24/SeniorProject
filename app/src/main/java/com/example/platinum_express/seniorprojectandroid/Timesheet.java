package com.example.platinum_express.seniorprojectandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static com.example.platinum_express.seniorprojectandroid.R.id.textView;

public class Timesheet extends AppCompatActivity {

    EditText batch;
    TableLayout history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet);

        batch = (EditText) findViewById(R.id.batch_text);
        history = (TableLayout) findViewById(R.id.history);

    }

    public void search(View view){
        TableRow tableRow;
        TextView textView;
        tableRow = new TableRow(this);
        for (int j = 0; j < 6; j++) {
            textView = new TextView(this);
            textView.setText("test");
            textView.setPadding(10, 10, 10, 10);
            tableRow.addView(textView);
        }
            history.addView(tableRow);
    }
}
