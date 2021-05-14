package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class SensorActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private ImageButton realtime,sensor;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Sensor Activity");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        session = new SessionManager(getApplicationContext());
        setObjects();


    }
    public void setObjects(){

        realtime=findViewById(R.id.btn_realtime);
        sensor=findViewById(R.id.btn_sensor);
        realtime.setOnClickListener(this);
        sensor.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

           case R.id.btn_realtime:

            startActivity(new Intent(SensorActivity.this,RealTimeSensors.class));
            finish();

            break;

           case R.id.btn_sensor:
            startActivity(new Intent(SensorActivity.this,SensorsTypeActivity.class));
            finish();
            break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.icon_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:  //other menu items if you have any
                session.setLogin(false);
                session.setToken(null);
                startActivity(new Intent(SensorActivity.this, LoginActivity.class));
                Toast.makeText(this, "logout Successfully", Toast.LENGTH_SHORT).show();
                finish();
                break;


        }
        return true;
    }
}
