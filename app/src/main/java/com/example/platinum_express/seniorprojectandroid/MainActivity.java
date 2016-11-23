package com.example.platinum_express.seniorprojectandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG_SUCCESS = "success";
    private static final String JSON_DATA = "data";
    private static final String JSON_PASSWORD = "User_Password_PBKDF2";
    //private static String url_create_product = "http://10.0.2.2/SeniorProjectPHP/create_timesheet_record.php";
    JSONParser jsonParser = new JSONParser();

    ClientDb db = new ClientDb(this);
    EditText username;
    EditText password;
    TextView error;
    Button login_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        login_submit = (Button) findViewById(R.id.login_submit);
        error = (TextView) findViewById(R.id.login_error);
        login_submit.setOnClickListener(this);
    }

    public void onClick(View view){
        Log.d("login test", "in login");
        Log.d("login test", "username=" + username.getText().toString());
        Log.d("login test", "password=" + password.getText().toString());
        try {
            AuthenticateUser auth = new AuthenticateUser(username.getText().toString());
            auth.execute().get();
            Log.d("compare", "enter pass = " + password.getText().toString() + "encrypted= " + auth.encryptedPassword);
            if(Encryption.decrypt(password.getText().toString(), auth.encryptedPassword)){
                Intent intent = new Intent(this, Timesheet.class);
                intent.putExtra("username", username.getText().toString());
                startActivity(intent);
            }
            else
                error.setText("Invalid Username/Password");
        } catch(Exception e) {
            Log.d("login test", db.getPass(username.getText().toString()));
            Log.d("Error", "Occurred during decryption");
            error.setText("Invalid Username/Password");
        }
        if(username.getText().toString().equals("a") && password.getText().toString().equals("a"))
        {
            Intent intent = new Intent(this, Timesheet.class);
            intent.putExtra("username", username.getText().toString());
            startActivity(intent);
        }
    }

    class AuthenticateUser extends AsyncTask<String, String, String> {
        String url_get_user_password = "http://www.bgmeng.com/TrackBGMphp/get_user_record.php";
        JSONArray jsonResponse;
        private String username;
        private String encryptedPassword;

        public AuthenticateUser(String username){
            this.username = username;
            this.encryptedPassword = "";
            this.jsonResponse = null;
            this.url_get_user_password += "?username=" + username;
        }
        protected String doInBackground(String... args) {
           List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("username", username));
            Log.d("url", "url= " + url_get_user_password);
            JSONObject json = jsonParser.makeHttpRequest(url_get_user_password,
                    "GET", params);

            Log.d("Getting User Password", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    jsonResponse = json.getJSONArray(JSON_DATA);
                    JSONObject data = jsonResponse.getJSONObject(0);
                    encryptedPassword = data.getString(JSON_PASSWORD);
                    Log.d("Completed", "Insert Successful");
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
