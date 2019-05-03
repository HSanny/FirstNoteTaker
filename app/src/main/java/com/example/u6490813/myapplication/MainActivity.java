package com.example.u6490813.myapplication;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.u6490813.myapplication.R;

/*
 * MainActivity.java - a simple class for demonstrating the main activity for a note application
 * @ author - Sanny,Hong Jiajia
 * @ uniID - u6490813
 */

public class MainActivity extends AppCompatActivity {

    /*
     * call super class onCreate to recover state information
     * set Use Interface layout
     * @param savedInstanceState a Bundle of state information
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
     * obtain references to Button widget and set up listener
     * @param view the basic building block of GUI */
    public void buttonPress(View view){
        TextView tv = (TextView) findViewById(R.id.textView);
        EditText et = (EditText) findViewById(R.id.editText);
        String text = et.getText().toString().trim();
        if(!TextUtils.isEmpty(text)){
            // if input text is not empty, set text into textView and clear editText
            tv.setText(text);
            //et.getText().clear();
        }
        else{
            // else set textView to empty and pop-up feedback bar to user
            //tv.setText(null);
            Snackbar.make(view,"Fill in empty field",Snackbar.LENGTH_SHORT).show();
        }
    }
}
