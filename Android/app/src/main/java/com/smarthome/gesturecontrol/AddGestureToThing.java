package com.smarthome.gesturecontrol;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class AddGestureToThing extends AppCompatActivity {

    Button buttonCancel;
    Button buttonConfirm;
    Button buttonUpdateGesture;
    String gestureBoolArray;
    String selectedAction;
    TextInputEditText textInputEditText;
    JSONObject thingJSONObject;
    String thingObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gesture_to_thing);

        buttonCancel = (Button) findViewById(R.id.cancel);
        buttonConfirm = (Button) findViewById(R.id.confirm);
        buttonUpdateGesture = (Button) findViewById(R.id.buttonUpdateGesture);
        textInputEditText = (TextInputEditText) findViewById(R.id.textInputText);
        TextView textView = (TextView) findViewById(R.id.textViewGestures);
        Intent intent = getIntent();
        String thingId = intent.getStringExtra("thingId");

        try {
            thingJSONObject = new JSONObject(thingId);
            Iterator<String> stringIterator = thingJSONObject.keys();
            for (Iterator<String> it = stringIterator; it.hasNext(); ) {
                thingObjectId= it.next();
                textInputEditText.setText(thingJSONObject.getJSONObject(thingObjectId).toString());
            }


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String deviceType = intent.getStringExtra("thingType");
        String[] actions;

        if (deviceType.equals("device")){
            actions = getResources().getStringArray(R.array.deviceActions);
        } else {
            actions = getResources().getStringArray(R.array.sensorActions);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, actions);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedAction = (String) adapterView.getItemAtPosition(position);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGestureActivity(AddGesture.class);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Integer, Void, Void>(){

                    @Override
                    protected Void doInBackground(Integer... params) {
                        //getting all the sensors available, should be run as followed "python /location/to/python.py"
                        RunSSH.run(String.format("python3 smart-home-gesture-system-main/Python/main.py --function saveGesture %s", gestureBoolArray, textInputEditText.getText().toString(), selectedAction, thingObjectId));
                        return null;
                    }
                }.execute(1);
                openGestureActivity(MainActivity.class);
            }
        });

        buttonUpdateGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Integer, Void, Void>(){

                    @Override
                    protected Void doInBackground(Integer... params) {
                        //getting all the sensors available, should be run as followed "python /location/to/python.py"
                        gestureBoolArray = RunSSH.run(String.format("python3 smart-home-gesture-system-main/Python/main.py --function getGestureBoolArray"));
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void v) {
                        String gestureIndex = "";
                        String[] gestureStrings = {"Index Finger", " Middle Finger", " Ring Finger", " Pinky Finger"};
                        String[] boolStrings = gestureBoolArray.replace("[", "").replace("]", "").split(",");
                        for (int i = 0; i < boolStrings.length; i++) {
                            if (Integer.valueOf(boolStrings[i]) == 1) {
                                gestureIndex = gestureIndex + gestureStrings[i] + " Up";
                            } else {
                                gestureIndex = gestureIndex + gestureStrings[i] + " Down";
                            }
                        }
                        textView.setText(gestureIndex);
                    }
                }.execute(1);
            }
        });
    }
    public void openGestureActivity(Class gesture) {
        Intent intent = new Intent(this, gesture);
        startActivity(intent);
    }
}