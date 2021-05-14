package com.example.smarthome;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.Motion.MotionActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class RealTimeSensors extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout realtime;
    private Toolbar toolbar;
    private TextView bedroom, bathroom,studyroom,liveroom1,liveroom2,kitchen;
    private TextView dbedroom,dbathroom,dstudyroom,dliveroom1,dliveroom2,dkitchen;

    public static List<Model> motionlist;
    private SessionManager session;
    String token,username;

    private static final String URL_GRAPH = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_sensors);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("RealTime Sensors");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        session = new SessionManager(getApplicationContext());
        token= session.getToken();
        username=session.getUsername();
        components();
        realtime=findViewById(R.id.swip_realtime);
        realtime.setColorSchemeResources(R.color.colorAccent);
        realtime.setOnRefreshListener(this);
        new MotionClass().execute();

    }

    public void components(){

        bedroom=findViewById(R.id.btn_bedroom);
        bathroom=findViewById(R.id.btn_bathroom);
        studyroom=findViewById(R.id.btn_studyroom);
        liveroom1=findViewById(R.id.btn_liveroom1);
        liveroom2=findViewById(R.id.btn_liveroom1);
        kitchen=findViewById(R.id.btn_kitchen);
        dbathroom=findViewById(R.id.txt_bathdate);
        dbedroom=findViewById(R.id.txt_bedroom);
        dstudyroom=findViewById(R.id.txt_studydate);
        dliveroom1=findViewById(R.id.txt_liveroom_1);
        dliveroom2=findViewById(R.id.txt_liveroom_2);
        dkitchen=findViewById(R.id.txt_kitchendate);

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                clearComponent();
                Log.e("refreshing","refreshing");
                new MotionClass().execute();

                realtime.setRefreshing(false);

            }
        }, 2000);

    }




    //////////////

    public  class MotionClass extends AsyncTask<Void,Void,String> {

        String idResult = "";
        private ProgressDialog pD;
        String message="";
        String fun_id,value,name,fun_type,timestamp;
        String sensorResult;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pD = new ProgressDialog(RealTimeSensors.this);
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
                      //  if (intent.equals("bath")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43216")) {
                                value = jsonObject.getString("value");
                                if (value.equals("255")){

                                    timestamp = TDTimeStamp.convertTime(jsonObject.getString("timestamp"));
                                    sensorResult="43216";
                                    break;

                                }


                            }
                       // }

                       // if (intent.equals("bed")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43231")) {
                                value = jsonObject.getString("value");
                                if (value.equals("255")){
                                    timestamp = TDTimeStamp.convertTime(jsonObject.getString("timestamp"));
                                    sensorResult="43231";
                                    break;

                                }


                            }
                       // }

                       // if (intent.equals("study")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43201")) {
                                value = jsonObject.getString("value");
                                if (value.equals("255")) {
                                    timestamp = TDTimeStamp.convertTime(jsonObject.getString("timestamp"));
                                    sensorResult="43201";
                                    break;
                                }

                            }
                      //  }
                       // if (intent.equals("live1")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("41275")) {
                                value = jsonObject.getString("value");
                                if (value.equals("255")){

                                    timestamp = TDTimeStamp.convertTime(jsonObject.getString("timestamp"));
                                    sensorResult="41275";
                                    break;

                                }


                            }
                      //  }
                     //   if (intent.equals("live2")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("40750")) {
                                value = jsonObject.getString("value");
                                if (value.equals("255")) {
                                    timestamp = TDTimeStamp.convertTime(jsonObject.getString("timestamp"));
                                    sensorResult="40750";
                                    break;
                                }
                            }
                      //  }
                     //   if (intent.equals("kitchen")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43186")) {
                                value = jsonObject.getString("value");
                                if (value.equals("255")){

                                    timestamp = TDTimeStamp.convertTime(jsonObject.getString("timestamp"));
                                    sensorResult="43186";
                                    break;
                                }

                                //name = jsonObject.getString("name");
                                //fun_type = jsonObject.getString("function_type");

                               // addValue(fun_id, name, value, fun_type, timestamp);
                            }
                      //  }

                        Log.e("sensor result",""+sensorResult);
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
                Log.e("sensor on result",""+sensorResult);
                if ( sensorResult != null) {

                    if (sensorResult.equals("43216")) {

                        bathroom.setBackgroundColor(Color.GREEN);
                        dbathroom.setText(timestamp);

                    } else if (sensorResult.equals("43231")) {

                        bedroom.setBackgroundColor(Color.GREEN);
                        dbedroom.setText(timestamp);

                    } else if (sensorResult.equals("43201")) {

                        studyroom.setBackgroundColor(Color.GREEN);
                        dstudyroom.setText(timestamp);

                    } else if (sensorResult.equals("41275")) {

                        liveroom1.setBackgroundColor(Color.GREEN);
                        dliveroom1.setText(timestamp);

                    } else if (sensorResult.equals("40750")) {

                        liveroom2.setBackgroundColor(Color.GREEN);
                        dliveroom2.setText(timestamp);

                    } else if (sensorResult.equals("43186")) {

                        kitchen.setBackgroundColor(Color.GREEN);
                        dkitchen.setText(timestamp);

                    }
                    pD.dismiss();
                }

                else {

                    Toast.makeText(RealTimeSensors.this,"All sensor is OFF",Toast.LENGTH_SHORT).show();
                    pD.dismiss();
                }
            }
            else if (s.equals("2")){

                Toast.makeText(RealTimeSensors.this,"All sensor is OFF",Toast.LENGTH_SHORT).show();
                pD.dismiss();
            }
            else {

                Toast.makeText(RealTimeSensors.this,"All sensor is OFF",Toast.LENGTH_SHORT).show();
                pD.dismiss();
            }
        }
    }


    public void clearComponent() {

        bedroom.setBackgroundColor(Color.parseColor("#808080"));
        bathroom.setBackgroundColor(Color.parseColor("#808080"));
        studyroom.setBackgroundColor(Color.parseColor("#808080"));
        liveroom1.setBackgroundColor(Color.parseColor("#808080"));
        liveroom2.setBackgroundColor(Color.parseColor("#808080"));
        kitchen.setBackgroundColor(Color.parseColor("#808080"));
        dbedroom.setText("");
        dbathroom.setText("");
        dstudyroom.setText("");
        dliveroom1.setText("");
        dliveroom2.setText("");
        dkitchen.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RealTimeSensors.this, SensorActivity.class));
        this.finish();
    }
}
