package com.smarthome.gesturecontrol;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class RemoveGesture extends AppCompatActivity {

    Button buttonCancel;
    Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_gesture);

        buttonCancel = (Button) findViewById(R.id.cancel);
        buttonConfirm = (Button) findViewById(R.id.confirm);

        //AsyncTask
        //Få ut gestures
        //ersätt getResources().getStringArray(R.array.gestures) med de sparade gestures

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ny asynctask för att ta bort, genom att kalla main.py --function removeGesture
                openGestureActivity(MainActivity.class);
            }
        });

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, getResources().getStringArray(R.array.gestures));
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(arrayAdapter);

        //Få ut selected gesture i ArrayAdapter

    }

    public void openGestureActivity(Class gesture) {
        Intent intent = new Intent(this, gesture);
        startActivity(intent);
    }
}