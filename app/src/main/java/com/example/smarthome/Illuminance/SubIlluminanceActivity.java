package com.example.smarthome.Illuminance;

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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smarthome.MainActivity;
import com.example.smarthome.Motion.SubMotionActivity;
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

public class SubIlluminanceActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button bedroom, bathroom,studyroom,liveroom1,liveroom2,kitchen;

    private static final String URL_GRAPH = "";
    String token,username;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_main);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Illuminance SubSensors");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        defineObjects();

        session = new SessionManager(getApplicationContext());
        token= session.getToken();
        username=session.getUsername();
       new IlluminanceClass().execute();

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
         Intent bath= new Intent(SubIlluminanceActivity.this,IlluminanceActivity.class);
         bath.putExtra("ID","bath");
         startActivity(bath);
         this.finish();
         break;

         case R.id.btn_bedroom:

             Intent bed= new Intent(SubIlluminanceActivity.this,IlluminanceActivity.class);
             bed.putExtra("ID","bed");
             startActivity(bed);
             this.finish();

                break;

         case R.id.btn_studyroom:

             Intent study= new Intent(SubIlluminanceActivity.this,IlluminanceActivity.class);
             study.putExtra("ID","study");
             startActivity(study);
             this.finish();

                break;

         case R.id.btn_liveroom_1:

             Intent live1= new Intent(SubIlluminanceActivity.this,IlluminanceActivity.class);
             live1.putExtra("ID","live1");
             startActivity(live1);
             this.finish();

         break;
         case R.id.btn_liveroom_2:

             Intent live2= new Intent(SubIlluminanceActivity.this,IlluminanceActivity.class);
             live2.putExtra("ID","live2");
             startActivity(live2);
             this.finish();

                break;
            case R.id.btn_kitchen:

                Intent kitchen= new Intent(SubIlluminanceActivity.this,IlluminanceActivity.class);
                kitchen.putExtra("ID","kitchen");
                startActivity(kitchen);
                this.finish();

                break;
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SubIlluminanceActivity.this, MainActivity.class));
        this.finish();
    }


    public  class IlluminanceClass extends AsyncTask<Void,Void,String> {

        String idResult = "";
        private ProgressDialog pD;
        String message="";
        String fun_id,value;
        int bathvalue,bedvalue,studyvalue,live1value,live2value,kitchenvalue;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pD = new ProgressDialog(SubIlluminanceActivity.this);
            pD.setMessage("Please wait");
            pD.setTitle("Illuminance data loading...");
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
                connection.setRequestProperty ("Authorization", basicAuth);
                connection.setRequestMethod("GET");
                /*connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);*/
                connection.setUseCaches(false);
                int response = connection.getResponseCode();
                Log.e("reponse",""+response);
                if (response == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));


                    String line = "";

                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        // Log.e("select branch",line);
                    }

                    idResult = sb.toString().replaceAll(" ", " ");
                    idResult = sb.toString();
                    Log.e("device id result", idResult);

                    JSONArray jsonArray=new JSONArray(idResult);


                    for(int i=0; i<jsonArray.length();i++){
                        //for (int i = jsonArray.length() - 1; i >= 0; i--) {
                        //m= new Model();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43222")) {

                                bathvalue++;

                            }



                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43237")) {
                                bedvalue++;

                        }


                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43207")) {
                               studyvalue++;

                            }


                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("41281")) {
                                live1value++;
                        }

                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("40756")) {
                                live2value++;
                        }

                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43192")) {
                                kitchenvalue++;
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

                Toast.makeText(SubIlluminanceActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();
                pD.dismiss();
            }
        }
    }



}
