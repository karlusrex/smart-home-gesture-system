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

public class EditGesture extends AppCompatActivity {

    JSONObject gestureData;
    JSONObject gestureDataExtra;
    Button buttonCancel;
    Button buttonConfirm;
    String savedGestures;
    String selectedGesture = "";
    ArrayList<String> gestureList = new ArrayList<String>();
    String thingValue;
    String gestureBoolArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gesture);

        buttonCancel = (Button) findViewById(R.id.cancel);
        buttonConfirm = (Button) findViewById(R.id.confirm);

        new AsyncTask<Integer, Void, Void>(){

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


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, getResources().getStringArray(R.array.gestures));
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String selectedItem = (String) adapterView.getItemAtPosition(position);

                Iterator<String> stringIterator = gestureData.keys();
                for (Iterator<String> it = stringIterator; it.hasNext(); ) {
                    String string = it.next();
                    try {
                        if (gestureData.getJSONObject(string).getString("name").equals(selectedItem)) {
                            if (gestureData.getJSONObject(string).getString("action").equals("device/turnOn") || gestureData.getJSONObject(string).getString("action").equals("device/turnOff")) {
                                thingValue = "device";
                            } else {
                                thingValue = "sensor";
                            }
                            gestureBoolArray = gestureData.getJSONObject(string).getString("array");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                for (Iterator<String> it = stringIterator; it.hasNext(); ) {
                    String string = it.next();
                    if (string.equals(selectedItem)) {
                        try {
                            gestureDataExtra = gestureData.getJSONObject(string);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });






        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGestureActivity(MainActivity.class, gestureDataExtra.toString());
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGestureActivity(EditGestureToThing.class, gestureDataExtra.toString());
            }
        });



    }

    public void openGestureActivity(Class gesture, String stringExtra) {
        Intent intent = new Intent(this, gesture);
        intent.putExtra("thingId", stringExtra);
        intent.putExtra("thingValue", thingValue);
        intent.putExtra("oldName", selectedGesture);
        intent.putExtra("gestureBoolArray", gestureBoolArray);
        startActivity(intent);
    }
}