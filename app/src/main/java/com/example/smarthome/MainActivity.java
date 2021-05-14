package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.smarthome.Illuminance.SubIlluminanceActivity;
import com.example.smarthome.Motion.SubMotionActivity;
import com.example.smarthome.Power.SubPowerActivity;
import com.example.smarthome.Temperature.SubTemperatureActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SessionManager session;
    private Toolbar toolbar;
    private ImageButton b1,b2,b3,b4,b5,b6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Main Activity");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        b1=this.findViewById(R.id.sensor1);
        b2=this.findViewById(R.id.sensor2);
        b3=this.findViewById(R.id.sensor3);
        b4=this.findViewById(R.id.sensor4);
        b5=this.findViewById(R.id.sensor5);
        b6=this.findViewById(R.id.sensor6);
        session = new SessionManager(getApplicationContext());


        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
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
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Toast.makeText(this, "logout Successfully", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }


        return true;
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();
        Log.e("id is",""+id);
        switch (v.getId()){

            case R.id.sensor1:
                Log.e("click","you have clicked");
                 ///startActivity(new Intent(MainActivity.this, MotionActivity.class));
                startActivity(new Intent(MainActivity.this, SubMotionActivity.class));
                 this.finish();
            break;
            case R.id.sensor2:

                //startActivity(new Intent(MainActivity.this, TemperatureActivity.class));
                startActivity(new Intent(MainActivity.this, SubTemperatureActivity.class));
                this.finish();

                break;
            case R.id.sensor3:

                //startActivity(new Intent(MainActivity.this, IlluminanceActivity.class));
                startActivity(new Intent(MainActivity.this, SubIlluminanceActivity.class));
                this.finish();

                break;
            case R.id.sensor4:

                //startActivity(new Intent(MainActivity.this, PowerActivity.class));
                startActivity(new Intent(MainActivity.this, SubPowerActivity.class));
                this.finish();

                break;
            case R.id.sensor5:

                /*startActivity(new Intent(MainActivity.this, SensorActivity.class));
                this.finish();*/

                break;
            case R.id.sensor6:

                /*startActivity(new Intent(MainActivity.this, SensorActivity.class));
                this.finish();*/

                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(MainActivity.this, SensorsTypeActivity.class));
    }
}
