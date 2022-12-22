package com.smarthome.gesturecontrol;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class AddGesture extends AppCompatActivity {

    Button buttonCancel;
    Button buttonConfirm;

    JSONObject devicesData;
    JSONObject sensorsData;
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
                //getting all the devices available, should be run as followed "python /location/to/python.py", currently for testing purposes python3 smart-home-gesture-system-main/Python/tellstickHandler.py
                returnValueDevices = RunSSH.run("python3 smart-home-gesture-system-main/Python/tellstickHandler.py");
                return null;
            }
            @Override
            protected void onPostExecute(Void v) {
                try {
                    JSONObject jsonObject = new JSONObject(returnValueDevices);
                    System.out.println(jsonObject);
                    Iterator<String> stringIterator = jsonObject.keys();
                    for (Iterator<String> it = stringIterator; it.hasNext(); ) {
                        String string = it.next();
                        System.out.println(jsonObject.getJSONObject(string).getString("name"));
                        deviceList.add(jsonObject.getJSONObject(string).getString("name"));
                    }
                    devicesData = jsonObject;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }.execute(1);

        new AsyncTask<Integer, Void, Void>(){

            @Override
            protected Void doInBackground(Integer... params) {
                //getting all the sensors available, should be run as followed "python /location/to/python.py"
                returnValueSensors = RunSSH.run("python3 smart-home-gesture-system-main/Python/tellstickHandler.py");
                return null;
            }
            @Override
            protected void onPostExecute(Void v) {
                try {
                    JSONObject jsonObject = new JSONObject(returnValueSensors);
                    System.out.println(jsonObject);
                    Iterator<String> stringIterator = jsonObject.keys();
                    for (Iterator<String> it = stringIterator; it.hasNext(); ) {
                        String string = it.next();
                        System.out.println(jsonObject.getJSONObject(string).getString("name"));
                        sensorList.add(jsonObject.getJSONObject(string).getString("name"));
                    }
                    sensorsData = jsonObject;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }.execute(1);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, deviceList);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(arrayAdapter);

        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, R.layout.dropdown_item, sensorList);
        AutoCompleteTextView autoCompleteTextView2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        autoCompleteTextView2.setAdapter(arrayAdapter2);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                autoCompleteTextView2.setEnabled(false);
            }
        });

        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                autoCompleteTextView.setEnabled(false);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGestureActivity(MainActivity.class, sensorsData.toString());
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGestureActivity(AddGestureToThing.class, devicesData.toString());
            }
        });

    }

    public void openGestureActivity(Class gesture, String stringExtra) {
        Intent intent = new Intent(this, gesture);
        intent.putExtra("devices", stringExtra);
        startActivity(intent);
    }
}