package com.example.platinum_express.seniorprojectandroid;

import android.app.Application;
import android.content.Intent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG_SUCCESS = "success";
    private static final String JSON_DATA = "data";
    private static final String JSON_PASSWORD = "User_Password_PBKDF2";
    JSONParser jsonParser = new JSONParser();

    ClientDb db = new ClientDb(this);
    EditText username;
    EditText password;
    TextView error;
    Button login_submit;
    boolean isConnected = false;

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
    @Override
    public void onBackPressed(){
        System.exit(0);
    }

    public void onClick(View view){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting());
        if (isConnected){
            Log.d("netowrk: ", "true");
            Log.d("login test", "in login");
            Log.d("login test", "username=" + username.getText().toString());
            Log.d("login test", "password=" + password.getText().toString());
            username.setText(username.getText().toString().trim());
            if (username.getText().toString().equals("")||password.getText().toString().equals("")){
                error.setText("Invalid Username/Password");
            } else {
                try {
                    AuthenticateUser auth = new AuthenticateUser(username.getText().toString());
                    auth.execute().get();
                    Log.d("compare", "enter pass = " + password.getText().toString() + "encrypted= " + auth.encryptedPassword);
                    if (Encryption.decrypt(password.getText().toString(), auth.encryptedPassword)) {
                        Intent intent = new Intent(this, Timesheet.class);
                        intent.putExtra("username", username.getText().toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else
                        error.setText("Invalid Username/Password");
                } catch (Exception e) {
                    Log.d("Error", "Occurred during decryption");
                    error.setText("Invalid Username/Password");
                }
            }
        } else {
            Log.d("netowrk: ", "false");
            error.setText("No network found");

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
