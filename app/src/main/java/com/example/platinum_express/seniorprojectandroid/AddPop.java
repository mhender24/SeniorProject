package com.example.platinum_express.seniorprojectandroid;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v7.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



/**
 * Created by marcelhenderson on 10/7/16.
 */

    public class AddPop extends AppCompatActivity implements OnClickListener
    {
        Timesheet timesheet;


        Spinner process;
        EditText operator;
        EditText date;
        EditText boards;
        EditText hours;
        Spinner task;
        EditText batch;


        String processStr;
        String operatorStr;
        String dateStr;
        String boardsStr;
        String hoursStr;
        String taskStr;
        String batchStr;

        private static final String TAG_SUCCESS = "success";
        private static String url_create_product = "http://www.bgmeng.com/TrackBGMphp/create_timesheet_record.php";
        JSONParser jsonParser = new JSONParser();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_pop);
            setTitle("Pop up Prototype");
            operatorStr = getIntent().getStringExtra("username");
            batchStr = getIntent().getStringExtra("batch");
            setViewData();
            Button b = (Button) findViewById(R.id.submit);
            b.setOnClickListener(this);

            setupAdapter(R.array.process_array, process);
            setupAdapter(R.array.task_array, task);


        }

        private void setViewData(){
            process = (Spinner)findViewById(R.id.process_spin);
            operator = (EditText) findViewById(R.id.op);
            operator.setText(operatorStr.toString());
            date = (EditText)findViewById(R.id.date);
            date.setText(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
            boards = (EditText)findViewById(R.id.board);
            hours = (EditText)findViewById(R.id.hours);
            task = (Spinner)findViewById(R.id.task);
            batch = (EditText) findViewById(R.id.batch);
            batch.setText(batchStr.toString());
        }

        private void setStrData(){
             processStr = process.getSelectedItem().toString();
             operatorStr = operator.getText().toString();
             dateStr = date.getText().toString();
             boardsStr = boards.getText().toString();
             hoursStr = hours.getText().toString();
             taskStr = task.getSelectedItem().toString();
             batchStr = batch.getText().toString();
        }

        private void setupAdapter(int arrayId, Spinner spinner){
            ArrayAdapter process_adapter = ArrayAdapter.createFromResource(
                    this, arrayId, android.R.layout.simple_spinner_item);
            spinner.setAdapter(process_adapter);
        }

        public void onClick(View v) {
            //add to database
            setStrData();
            DateFormat dateFormat = DateFormat.getDateInstance();
            String parsedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //ClientDb db = new ClientDb(getContext());
            //int parsedBoards = Integer.parseInt(boards.getText().toString().trim());
            //int parsedHours = Integer.parseInt(hours.getText().toString().trim());
            //db.testInsertIntoTimesheet(process.getSelectedItem().toString(), parsedDate,
             //       parsedBoards, parsedHours, task.getSelectedItem().toString());
            InsertTimesheetData insert = new InsertTimesheetData();
            try {
                insert.execute().get();
            } catch(InterruptedException e){
                Log.d("Error", "You had an interruptedException:    " + e );
            } catch(ExecutionException e){
                Log.d("Error", "You had an execution exception:    " + e );
            }
            //timesheet.displayTimesheet();
            Log.d("Mine", "Exiting dialog box");

            //TODO Create a new intent to switch back to Timesheet
            Intent intent = new Intent(this, Timesheet.class);
            intent.putExtra("username", operator.getText().toString());
            intent.putExtra("batch", batch.getText().toString());
            startActivity(intent);
        }

        class InsertTimesheetData extends AsyncTask<String, String, String> {


            protected String doInBackground(String... args) {
                DateFormat dateFormat = DateFormat.getDateInstance();
                String parsedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("batch", batchStr));
                params.add(new BasicNameValuePair("process", processStr));
                params.add(new BasicNameValuePair("operator", operatorStr));
                params.add(new BasicNameValuePair("date", parsedDate));
                params.add(new BasicNameValuePair("boards", boardsStr));
                params.add(new BasicNameValuePair("hours", hoursStr));
                params.add(new BasicNameValuePair("task", taskStr));


                JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                        "POST", params);

                Log.d("Create Response", json.toString());

                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("Error", "Insert Successful");
                    } else {
                        Log.d("Error", "Insert Failed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }
    }