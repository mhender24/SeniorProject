package com.example.platinum_express.seniorprojectandroid;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;



/**
 * Created by marcelhenderson on 10/7/16.
 */

    public class AddPop extends AppCompatActivity implements OnClickListener
    {

        private static String get_tasks_url = "http://www.bgmeng.com/TrackBGMphp/get_tasks.php";
        private static String get_process_url = "http://www.bgmeng.com/TrackBGMphp/get_process.php";

        Timesheet timesheet;


        Spinner process;
        EditText operator;
        EditText date;
        EditText boards;
        EditText hours;
        Spinner task;
        EditText batch;
        TextView error;


        String processStr;
        String operatorStr;
        String dateStr;
        String boardsStr;
        String hoursStr;
        String taskStr;
        String batchStr;

        CountDownTimer timeout;
        boolean inBackground;

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
            Button b = (Button) findViewById(R.id.buttonSave);
            b.setOnClickListener(this);

            GetTaskData taskClass = new GetTaskData();
            GetProcessData processClass = new GetProcessData();

            try {
                ArrayList<String> taskArr = (ArrayList)taskClass.execute().get();
                ArrayAdapter<String> taskArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, taskArr);
                taskArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view

                ArrayList<String> processArr = (ArrayList)processClass.execute().get();
                ArrayAdapter<String> processArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, processArr);
                processArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view

                task.setAdapter(taskArrayAdapter);
                process.setAdapter(processArrayAdapter);

            } catch(Exception e){
                Log.d("Error", "Interrupted Exception occurred in retrieving Spinner data");
            }

            inBackground = false;
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

        @Override
        public void onBackPressed(){
            Intent intent = new Intent(this, Timesheet.class);
            intent.putExtra("username", operator.getText().toString());
            intent.putExtra("batch", batch.getText().toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finishAffinity();

            startActivity(intent);
            finish();

        }

        public void onClick(View v) {
            //add to database
            setStrData();
            DateFormat dateFormat = DateFormat.getDateInstance();
            String parsedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            if (Float.parseFloat(hours.getText().toString())>12) {
                //TODO Throw error message to screen
                error = (TextView) findViewById(R.id.error);
                error.setText("Number of hours must be less than 12");
            } else if(Float.parseFloat(hours.getText().toString())<0){
                //TODO Throw error message to screen
                error = (TextView) findViewById(R.id.error);
                error.setText("Number of hours can't be less than 0");

            } else if (Integer.parseInt(boards.getText().toString())<0) {
                //TODO Throw error message to screen
                error = (TextView) findViewById(R.id.error);
                error.setText("Number of boards can't be less than 0");

            } else{
                InsertTimesheetData insert = new InsertTimesheetData();
                try {
                    insert.execute().get();
                } catch (InterruptedException e) {
                    Log.d("Error", "You had an interruptedException:    " + e);
                } catch (ExecutionException e) {
                    Log.d("Error", "You had an execution exception:    " + e);
                }

                Log.d("Mine", "Exiting dialog box");
                Intent intent = new Intent(this, Timesheet.class);
                intent.putExtra("username", operator.getText().toString());
                intent.putExtra("batch", batch.getText().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                inBackground = false;

                finishAffinity();
                startActivity(intent);
                finish();
            }
        }

//        @Override
//        public void onResume()
//        {
//            super.onResume();
//            if (inBackground) {
//                timeout.cancel();
//            }
//            inBackground = false;
//        }
//
//        public void onDestroy(){
//            super.onStop();
//            inBackground = false;
//        }
//
//        @Override
//        protected void onStop() {
//            super.onStop();
//            finish();
//        }
//
//        @Override
//        public void onPause()
//        {
//            super.onPause();
//            inBackground = true;
//            timeout = new CountDownTimer( 1 * 30 * 1000 , 1000 )
//            {
//
//                public void onTick(long millisUntilFinished) {}
//
//                public void onFinish()
//                {
//                    if ( inBackground )
//                    {
//
//                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        finishAffinity();
//                        timeout.cancel();
//
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        timeout.cancel();
//                    }
//                }
//            }.start();
//        }

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

        private class GetTaskData extends AsyncTask<String, Void, List<String> >  {


            protected ArrayList<String> doInBackground(String... args) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                ArrayList<String> tasksArr = null;
                JSONObject task_json = jsonParser.makeHttpRequest(get_tasks_url,
                        "POST", params);



                try {
                    int task_success = task_json.getInt(TAG_SUCCESS);
                    if (task_success == 1) {
                        JSONArray tasks = task_json.getJSONArray("data");
                        tasksArr = new ArrayList<String>();
                        for(int i=0; i<tasks.length(); i++){
                            JSONObject c = tasks.getJSONObject(i);
                            tasksArr.add(c.getString("Task"));
                        }

                    } else {
                        Log.d("Error", "Insert Failed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return tasksArr;
            }
        }

        private class GetProcessData extends AsyncTask<String, Void, List<String> >  {

            protected ArrayList<String> doInBackground(String... args) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                ArrayList<String> processArr = null;
                JSONObject process_json = jsonParser.makeHttpRequest(get_process_url,
                        "POST", params);



                try {
                      int process_success = process_json.getInt(TAG_SUCCESS);
                    if (process_success == 1) {
                        JSONArray process = process_json.getJSONArray("data");
                        processArr = new ArrayList<String>();
                        for(int i=0; i<process.length(); i++){
                            JSONObject c = process.getJSONObject(i);
                            processArr.add(c.getString("Process"));
                        }
                    } else {
                        Log.d("Error", "Insert Failed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return processArr;
            }
        }

    }