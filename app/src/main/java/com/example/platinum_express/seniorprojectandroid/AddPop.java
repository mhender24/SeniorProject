package com.example.platinum_express.seniorprojectandroid;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by marcelhenderson on 10/7/16.
 */

    public class AddPop extends Dialog implements OnClickListener
    {


        public AddPop(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setTitle("Pop up Prototype");
            setContentView(R.layout.add_pop);

            Spinner process = (Spinner)findViewById(R.id.process_spin);
            EditText op = (EditText)findViewById(R.id.op);
            EditText date = (EditText)findViewById(R.id.date);
            EditText boards = (EditText)findViewById(R.id.board);
            EditText hours = (EditText)findViewById(R.id.hours);
            Spinner task = (Spinner)findViewById(R.id.task);
            Button b = (Button) findViewById(R.id.submit);
            b.setOnClickListener(this);

        }
        public void onClick(View v) {
            dismiss();
        }
    }
