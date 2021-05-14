package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SensorsTypeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button physical, physiological;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors_type);
        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Sensor Activity");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        setObjects();
    }

    private void setObjects() {

        physical=findViewById(R.id.btn_physical);
        physiological=findViewById(R.id.btn_physiological);
        physical.setOnClickListener(this);
        physiological.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_physical:

                startActivity(new Intent(SensorsTypeActivity.this, MainActivity.class));
                finish();

                break;

            case R.id.btn_physiological:
                startActivity(new Intent(SensorsTypeActivity.this, DailyActivity.class));
                finish();

                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(SensorsTypeActivity.this, SensorActivity.class));
    }
}
