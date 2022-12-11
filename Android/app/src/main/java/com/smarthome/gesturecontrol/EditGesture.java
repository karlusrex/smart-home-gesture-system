package com.smarthome.gesturecontrol;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class EditGesture extends AppCompatActivity {

    Button buttonCancel;
    Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gesture);

        buttonCancel = (Button) findViewById(R.id.cancel);
        buttonConfirm = (Button) findViewById(R.id.confirm);

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