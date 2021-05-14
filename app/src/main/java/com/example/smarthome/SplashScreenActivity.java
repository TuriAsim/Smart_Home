package com.example.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;


import androidx.appcompat.app.AppCompatActivity;


public class SplashScreenActivity extends AppCompatActivity {

    private Handler splashHandler = null;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.and_splash);

        session = new SessionManager(getApplicationContext());
        splashHandler = new Handler();
        splashHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session.isLoggedIn()) {
                    Log.e("spalsh",""+session.isLoggedIn());
                    final Intent mainIntent = new Intent(SplashScreenActivity.this, SensorActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    finish();
                }
                else {
                    final Intent mainIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    finish();
                }
            }
        }, 3000);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashHandler.removeCallbacksAndMessages(null);
    }
}
