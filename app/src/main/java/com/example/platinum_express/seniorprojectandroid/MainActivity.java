package com.example.platinum_express.seniorprojectandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.text.SimpleDateFormat;

import static java.lang.System.currentTimeMillis;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.test_btn);

        Spinner spinner = (Spinner) findViewById(R.id.process_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.process_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void addRow(View view){
        TableLayout table = (TableLayout) findViewById(R.id.process_table);
        TableRow row = new TableRow(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(params);

        TextView process = new TextView(this);
        process.setText("testing");
        row.addView(process);
        TextView operator = new TextView(this);
        operator.setText("Marcellus Wallice");
        row.addView(operator);
        TextView date = new TextView(this);
        long date_raw = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date_raw);
        date.setText(formattedDate);
        row.addView(date);
        TextView boards = new TextView(this);
        boards.setText("this many boards");
        row.addView(boards);
        TextView hours = new TextView(this);
        hours.setText("100 billion");
        row.addView(hours);
        TextView task = new TextView(this);
        task.setText("getting this shit done");
        row.addView(task);

        table.addView(row);

    }
}
