package com.example.smarthome.Motion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smarthome.Illuminance.IlluminanceActivity;
import com.example.smarthome.Illuminance.SubIlluminanceActivity;
import com.example.smarthome.MainActivity;
import com.example.smarthome.MotionAdapter;
import com.example.smarthome.R;
import com.example.smarthome.SessionManager;
import com.example.smarthome.TDTimeStamp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class SubMotionActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private Button bedroom, bathroom,studyroom,liveroom1,liveroom2,kitchen;

    private static final String URL_GRAPH = "";

    String token,username;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_motion);
        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Motion SubSensors");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        defineObjects();

        session = new SessionManager(getApplicationContext());
        token= session.getToken();
        username=session.getUsername();
        new MotionClass().execute();
    }

    private void defineObjects() {
        bedroom=findViewById(R.id.btn_bedroom);
        bathroom=findViewById(R.id.btn_bathroom);
        studyroom=findViewById(R.id.btn_studyroom);
        liveroom1=findViewById(R.id.btn_liveroom_1);
        liveroom2=findViewById(R.id.btn_liveroom_2);
        kitchen=findViewById(R.id.btn_kitchen);
        bedroom.setOnClickListener(this);
        bathroom.setOnClickListener(this);
        studyroom.setOnClickListener(this);
        liveroom1.setOnClickListener(this);
        liveroom2.setOnClickListener(this);
        kitchen.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_bathroom:
                Intent bath= new Intent(SubMotionActivity.this, MotionActivity.class);
                bath.putExtra("ID","bath");
                startActivity(bath);
                this.finish();
                break;

            case R.id.btn_bedroom:

                Intent bed= new Intent(SubMotionActivity.this,MotionActivity.class);
                bed.putExtra("ID","bed");
                startActivity(bed);
                this.finish();

                break;

            case R.id.btn_studyroom:

                Intent study= new Intent(SubMotionActivity.this,MotionActivity.class);
                study.putExtra("ID","study");
                startActivity(study);
                this.finish();

                break;

            case R.id.btn_liveroom_1:

                Intent live1= new Intent(SubMotionActivity.this,MotionActivity.class);
                live1.putExtra("ID","live1");
                startActivity(live1);
                this.finish();

                break;
            case R.id.btn_liveroom_2:

                Intent live2= new Intent(SubMotionActivity.this,MotionActivity.class);
                live2.putExtra("ID","live2");
                startActivity(live2);
                this.finish();

                break;
            case R.id.btn_kitchen:

                Intent kitchen= new Intent(SubMotionActivity.this,MotionActivity.class);
                kitchen.putExtra("ID","kitchen");
                startActivity(kitchen);
                this.finish();

                break;
        }
    }


    public  class MotionClass extends AsyncTask<Void,Void,String> {

        String idResult = "";
        private ProgressDialog pD;
        String message="";
        String fun_id,value;
        int bathvalue,bedvalue,studyvalue,live1value,live2value,kitchenvalue;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pD = new ProgressDialog(SubMotionActivity.this);
            pD.setMessage("Please wait");
            pD.setTitle("Motion data loading...");
            pD.setIndeterminate(false);
            pD.setCancelable(false);
            pD.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            try {

                    url = new URL(URL_GRAPH);
                    Log.e("if url",""+url);

                Log.e("url",""+url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                String userCredentials = username+":"+token;
                //String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
                String basicAuth = "Basic " + Base64.getEncoder().encodeToString((username+":"+token).getBytes(StandardCharsets.UTF_8));
                Log.e("basicauth rep",basicAuth);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                connection.setRequestProperty ("Authorization", basicAuth);
                connection.setRequestMethod("GET");
                connection.setUseCaches(false);
                int response = connection.getResponseCode();
                Log.e("motion reponse",""+response);

                if (response==200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    String line = "";
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        // Log.e("select branch",line);
                    }

                    idResult = sb.toString().replaceAll(" ", " ");
                    idResult = sb.toString();
                    Log.e("motion result", idResult);

                    JSONArray jsonArray = new JSONArray(idResult);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //for (int i = jsonArray.length() - 1; i >= 0; i--){
                        //m= new Model();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                       // if (intent.equals("bath")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43216")) {
                                value = jsonObject.getString("value");
                                if (!value.equals("0")) {
                                    bathvalue++;
                                }
                        }

                       // if (intent.equals("bed")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43231")) {
                                value = jsonObject.getString("value");
                                if (!value.equals("0")) {
                                    bedvalue++;
                                }
                        }

                       // if (intent.equals("study")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43201")) {
                                value = jsonObject.getString("value");
                                if (!value.equals("0")) {
                                    studyvalue++;

                                }
                            }
                        //if (intent.equals("live1")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("41275")) {
                                value = jsonObject.getString("value");
                                if (!value.equals("0")) {
                                    live1value++;
                                }
                        }
                        //if (intent.equals("live2")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("40750")) {
                                value = jsonObject.getString("value");
                                if (!value.equals("0")) {
                                    live2value++;
                                }
                        }
                        //if (intent.equals("kitchen")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43186")) {
                                value = jsonObject.getString("value");
                                if (!value.equals("0")) {
                                    kitchenvalue++;
                                }
                            }

                    }

                    message = "1";
                }

                else {

                    message="2";
                }


            }
            catch (Exception e) {
                Log.e("exception", e.getMessage());
                message=e.getMessage();
                e.printStackTrace();

            }
            return message;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("1")) {

                bedroom.setText("Bed room\t"+"("+bedvalue+")");
                bathroom.setText("Bath room\t"+"("+bathvalue+")");
                studyroom.setText("Study Room\t"+"("+studyvalue+")");
                liveroom1.setText("Living Room-1\t"+"("+live1value+")");
                liveroom2.setText("Living Room-2\t"+"("+live2value+")");
                kitchen.setText("Kitchen\t"+"("+kitchenvalue+")");

                pD.dismiss();
            }

            else {

                Toast.makeText(SubMotionActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();
                pD.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SubMotionActivity.this, MainActivity.class));
        this.finish();
    }
}
