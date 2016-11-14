package com.example.platinum_express.seniorprojectandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    ClientDb db = new ClientDb(this);
    EditText username;
    EditText password;
    Button login_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        login_submit = (Button) findViewById(R.id.login_submit);
        login_submit.setOnClickListener(this);
    }

    public void onClick(View view){
        //ClientDb database = new ClientDb(view.getContext());
        Log.d("login test", "in login");
        Log.d("login test", "username=" + username.getText().toString());
        Log.d("login test", "password=" + password.getText().toString());
        //Log.d("login test", db.getPass(username.getText().toString()));

        /*
        if(username.getText().toString().equals("admin") && password.getText().toString().equals("password") ||
                (password.getText().toString().equals(db.getPass(username.getText().toString())))||
                username.getText().toString().equals("a") && password.getText().toString().equals("a")){
        */
        if(username.getText().toString().equals("a") && password.getText().toString().equals("a")){
            Log.d("login test", "in login in if");
            Intent myIntent = new Intent(this, Timesheet.class);
            startActivity(myIntent);
        }
    }
}
