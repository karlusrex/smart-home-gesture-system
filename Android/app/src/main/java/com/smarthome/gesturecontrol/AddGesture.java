package com.smarthome.gesturecontrol;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class AddGesture extends AppCompatActivity {

    Button buttonCancel;
    Button buttonConfirm;

    String[] devices={};
    String[] sensors={};
    ArrayList<String> deviceList = new ArrayList<String>();
    ArrayList<String> sensorList = new ArrayList<String>();
    String returnValueDevices = "";
    String returnValueSensors = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gesture);

        buttonCancel = (Button) findViewById(R.id.cancel);
        buttonConfirm = (Button) findViewById(R.id.confirm);
        new AsyncTask<Integer, Void, Void>(){

            @Override
            protected Void doInBackground(Integer... params) {
                //getting all the devices available, should be run as followed "python /location/to/python.py"
                returnValueDevices = RunSSH.run("");
                return null;
            }
            @Override
            protected void onPostExecute(Void v) {
                devices = returnValueDevices.split(" ");
                for (String s : devices) {deviceList.add(s) ;}
            }
        }.execute(1);

        new AsyncTask<Integer, Void, Void>(){

            @Override
            protected Void doInBackground(Integer... params) {
                //getting all the sensors available, should be run as followed "python /location/to/python.py"
                returnValueSensors = RunSSH.run("");
                return null;
            }
            @Override
            protected void onPostExecute(Void v) {
                sensors = returnValueSensors.split(" ");
                for (String s : sensors) {sensorList.add(s) ;}
            }
        }.execute(1);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGestureActivity(MainActivity.class);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGestureActivity(AddGestureToThing.class);
            }
        });

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, deviceList);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(arrayAdapter);

        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, R.layout.dropdown_item, sensorList);
        AutoCompleteTextView autoCompleteTextView2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        autoCompleteTextView2.setAdapter(arrayAdapter2);

    }

    public void openGestureActivity(Class gesture) {
        Intent intent = new Intent(this, gesture);
        startActivity(intent);
    }
}