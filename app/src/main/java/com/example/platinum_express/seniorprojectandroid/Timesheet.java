package com.example.platinum_express.seniorprojectandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Timesheet extends AppCompatActivity {

    EditText batch;
    ListView history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet);

        batch = (EditText) findViewById(R.id.batch_text);
        history = (ListView) findViewById(R.id.history);

    }

    public void search(View view){
        TextView t = new TextView(this);
        t.setText("Hello");
        history.addView(t);
    }
}
