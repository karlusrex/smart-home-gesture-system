package com.smarthome.gesturecontrol;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class RemoveGesture extends AppCompatActivity {

    JSONObject gestureData;
    Button buttonCancel;
    Button buttonConfirm;
    String savedGestures = "";
    String selectedGesture = "";
    ArrayList<String> gestureList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_gesture);

        buttonCancel = (Button) findViewById(R.id.cancel);
        buttonConfirm = (Button) findViewById(R.id.confirm);

        new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... params) {
                //getting all the devices available, should be run as followed "python /location/to/python.py", currently for testing purposes python3 smart-home-gesture-system-main/Python/tellstickHandler.py
                savedGestures = RunSSH.run("python3 smart-home-gesture-system-main/Python/main.py --function getGestures");
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                try {
                    JSONObject jsonObject = new JSONObject(savedGestures);
                    System.out.println(jsonObject);
                    Iterator<String> stringIterator = jsonObject.keys();
                    for (Iterator<String> it = stringIterator; it.hasNext(); ) {
                        String string = it.next();
                        System.out.println(jsonObject.getJSONObject(string).getString("name"));
                        gestureList.add(jsonObject.getJSONObject(string).getString("name"));
                    }
                    gestureData = jsonObject;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }.execute(1);


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, gestureList);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(arrayAdapter);


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedGesture = (String) adapterView.getItemAtPosition(position);
            }
        });


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Integer, Void, Void>() {

                    @Override
                    protected Void doInBackground(Integer... params) {
                        RunSSH.run(String.format("python3 smart-home-gesture-system-main/Python/main.py --function removeGesture %s", selectedGesture));
                        return null;
                    }
                }.execute(1);
                openGestureActivity(MainActivity.class);
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGestureActivity(MainActivity.class);
            }
        });

    }

    public void openGestureActivity(Class gesture) {
        Intent intent = new Intent(this, gesture);
        startActivity(intent);
    }
}