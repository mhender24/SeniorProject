package com.example.platinum_express.seniorprojectandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.text.SimpleDateFormat;

import static java.lang.System.currentTimeMillis;

public class MainActivity extends AppCompatActivity {
    ClientDb db = new ClientDb(this);
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);

    }

    public void login(View view){
        //ClientDb database = new ClientDb(getContext());

        Log.d("login test", "in login");
        Log.d("login test", "username=" + username.getText().toString());
        Log.d("login test", "password=" + password.getText().toString());
        Log.d("login test", db.getPass(username.getText().toString()));


        if(username.getText().toString().equals("admin") && password.getText().toString().equals("password") ||
                (password.getText().toString().equals(db.getPass(username.getText().toString())))||
                username.getText().toString().equals("a") && password.getText().toString().equals("a")){



            Log.d("login test", "in login in if");
            Intent myIntent = new Intent(this, Timesheet.class);
            startActivity(myIntent);
        }
    }
}
