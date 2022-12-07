package com.smarthome.gesturecontrol;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Button buttonEditGesture;
    private Button buttonAddGesture;
    private Button buttonRemoveGesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAddGesture = (Button) findViewById(R.id.addNewGesture);
        buttonEditGesture = (Button) findViewById(R.id.editGesture);
        buttonRemoveGesture = (Button) findViewById(R.id.removeGesture);

        buttonAddGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGestureActivity(AddGesture.class);
            }
        });

        buttonEditGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGestureActivity(EditGesture.class);
            }
        });

        buttonRemoveGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGestureActivity(RemoveGesture.class);
            }
        });
    }
    public void openGestureActivity(Class gesture) {
        Intent intent = new Intent(this, gesture);
        startActivity(intent);
    }
}