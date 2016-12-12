package com.example.platinum_express.seniorprojectandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by BGM Engineering on 12/9/2016.
 */

public class EditPop extends AppCompatActivity implements View.OnClickListener
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

    String indexStr;
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
    JSONParser jsonParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_pop);
        setTitle("Pop up Prototype");
        indexStr = getIntent().getStringExtra("Index");
        operatorStr = getIntent().getStringExtra("username");
        batchStr = getIntent().getStringExtra("batch");
        setViewData();
        Button b = (Button) findViewById(R.id.buttonSave);
        b.setOnClickListener(this);

        EditPop.GetTaskData taskClass = new EditPop.GetTaskData();
        EditPop.GetProcessData processClass = new EditPop.GetProcessData();

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

        try
        {
            new GetRowInfo().execute();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.e("Error", "Error occurred while loading row info ");
        }

        //setupAdapter(R.array.process_array, process);
        //setupAdapter(R.array.task_array, task);
        inBackground = false;
    }

    private void setViewData(){
        process = (Spinner)findViewById(R.id.process_spin);
        operator = (EditText) findViewById(R.id.op);
        date = (EditText)findViewById(R.id.date);
        boards = (EditText)findViewById(R.id.board);
        hours = (EditText)findViewById(R.id.hours);
        task = (Spinner)findViewById(R.id.task);
        batch = (EditText) findViewById(R.id.batch);
    }

    private void setStrData()
    {
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
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        timeout.cancel();
        finishAffinity();
        inBackground = false;

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

        }
        else
        {
            new UpdateRow().execute();
            Log.d("Mine", "Exiting dialog box");
            Intent intent = new Intent(this, Timesheet.class);
            intent.putExtra("username", operator.getText().toString());
            intent.putExtra("batch", batch.getText().toString());
            //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);


            finishAffinity();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            inBackground = false;
            startActivity(intent);
            finish();
        }
    }

    private class UpdateRow extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                URL url = new URL("http://www.bgmeng.com/TrackBGMphp/Time_Update_Row.php?" +
                                    "Index=" + indexStr + "&" +
                                    "Process=" + processStr + "&" +
                                    "Operator=" + operatorStr + "&" +
                                    "Date=" + dateStr + "&" +
                                    "Boards=" + boardsStr + "&" +
                                    "Hours=" + hoursStr + "&" +
                                    "Task=" + taskStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                return null;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (inBackground) {
            timeout.cancel();
        }
        inBackground = false;
    }

    public void onDestroy(){
        super.onStop();
        inBackground = false;
    }


    @Override
    public void onPause()
    {
        super.onPause();
        inBackground = true;
        timeout = new CountDownTimer( 1 * 30 * 1000 , 1000 )
        {

            public void onTick(long millisUntilFinished) {}

            public void onFinish()
            {
                if ( inBackground )
                {

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finishAffinity();
                    timeout.cancel();
                    inBackground = false;
                    startActivity(intent);
                    finish();
                } else {
                    timeout.cancel();
                }
            }
        }.start();
    }

    private class GetTaskData extends AsyncTask<String, Void, List<String> >  {


        protected ArrayList<String> doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            ArrayList<String> tasksArr = null;
            JSONObject task_json = jsonParser.makeHttpRequest(get_tasks_url,
                    "POST", params);

            // JSONObject process_json = jsonParser.makeHttpRequest(get_process_url,
            //        "POST", params);

            //Log.d("Create Response", json.toString());

            try {
                int task_success = task_json.getInt(TAG_SUCCESS);
                //  int process_success = process_json.getInt(TAG_SUCCESS);
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

            // JSONObject process_json = jsonParser.makeHttpRequest(get_process_url,
            //        "POST", params);

            //Log.d("Create Response", json.toString());

            try {
                //int task_success = task_json.getInt(TAG_SUCCESS);
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

    private class GetRowInfo extends AsyncTask<Void, Void, Void>
    {
        private String strBatchLotCode;
        private String strProcess;
        private String strOperator;
        private String strDate;
        private String strBoards;
        private String strHours;
        private String strTask;

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                URL url = new URL("http://www.bgmengineering.net/TrackBGMphp/Get_Time_Row.php?Index=" + indexStr);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                strBatchLotCode = doc.getElementsByTagName("Batch").item(0).getTextContent();
                strProcess = doc.getElementsByTagName("Process").item(0).getTextContent();
                strOperator = doc.getElementsByTagName("Operator").item(0).getTextContent();
                strDate = doc.getElementsByTagName("Date").item(0).getTextContent();
                strBoards = doc.getElementsByTagName("Boards").item(0).getTextContent();
                strHours = doc.getElementsByTagName("Hours").item(0).getTextContent();
                strTask = doc.getElementsByTagName("Task").item(0).getTextContent();

                return null;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            batch.setText(strBatchLotCode);
            operator.setText(strOperator);
            date.setText(strDate);
            boards.setText(strBoards);
            hours.setText(strHours);
            int processIndex = 0;
            for(int i = 0; i < process.getCount(); i++)
            {
                if(process.getItemAtPosition(i).toString().equals(strProcess))
                {
                    processIndex = i;
                    break;
                }
            }
            process.setSelection(processIndex);
            int taskIndex = 0;
            for(int i = 0; i < task.getCount(); i++)
            {
                if(task.getItemAtPosition(i).toString().equals(strTask))
                {
                    taskIndex = i;
                    break;
                }
            }
            task.setSelection(taskIndex);


        }
    }

}
