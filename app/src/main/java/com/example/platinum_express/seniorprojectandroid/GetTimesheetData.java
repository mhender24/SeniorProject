package com.example.platinum_express.seniorprojectandroid;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.name;

/**
 * Created by marcelhenderson on 11/13/16.
 */
public class GetTimesheetData extends AsyncTask<String, String, String> {

    // JSON Node names
    private static final String JSON_SUCCESS = "success";
    private static final String JSON_TIMESHEET = "data";
    private static final String JSON_PK = "Index";
    private static final String JSON_BATCH = "Batch_Lot_Code";
    private static final String JSON_PROCESS = "Process";
    private static final String JSON_OPERATOR = "Operator";
    private static final String JSON_DATE = "Date";
    private static final String JSON_BOARDS = "Boards";
    private static final String JSON_HOURS = "Hours";
    private static final String JSON_TASK = "Task";

    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> dataList;
    JSONArray timesheet = null;

    // url to get all products list
    private static String url_user_timesheet = "http://www.bgmeng.com/TrackBGMphp/get_timesheet_record.php";


    protected String doInBackground(String... args) {
        Log.d("Check", "In doInBackground");
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        JSONObject json = jParser.makeHttpRequest(url_user_timesheet, "GET", params);
        dataList = new ArrayList<HashMap<String, String>>();

        Log.d("All Products: ", json.toString());

        try {
            int success = json.getInt(JSON_SUCCESS);

            if (success == 1) {
                Log.d("Check", "In success if statement");

                timesheet = json.getJSONArray(JSON_TIMESHEET);
                Log.d("length", " " + timesheet.length());
                for (int i = 0; i < timesheet.length(); i++) {
                    JSONObject c = timesheet.getJSONObject(i);

                    String id = c.getString(JSON_PK);
                    String batch = c.getString(JSON_BATCH);
                    String process = c.getString(JSON_PROCESS);
                    String operator = c.getString(JSON_OPERATOR);
                    String date = c.getString(JSON_DATE);
                    String boards = c.getString(JSON_BOARDS);
                    String hours = c.getString(JSON_HOURS);
                    String task = c.getString(JSON_TASK);

                    Log.d("Data", "ID = " + id);
                    Log.d("Data", "Batch = " + batch);
                    Log.d("Data", "process = " + process);
                    Log.d("Data", "operator = " + operator);
                    Log.d("Data", "date = " + date);
                    Log.d("Data", "boards = " + boards);
                    Log.d("Data", "hours = " + hours);
                    Log.d("Data", "task = " + task);



                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(JSON_PK, id);
                    map.put(JSON_BATCH, batch);
                    map.put(JSON_PROCESS, process);
                    map.put(JSON_OPERATOR, operator);
                    map.put(JSON_DATE, date);
                    map.put(JSON_BOARDS, boards);
                    map.put(JSON_HOURS, hours);
                    map.put(JSON_TASK, task);

                    dataList.add(map);
                }
            } else {
                Log.d("Error", "No entries found");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
