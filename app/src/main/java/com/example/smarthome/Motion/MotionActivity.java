package com.example.smarthome.Motion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.smarthome.Illuminance.IlluminanceActivity;
import com.example.smarthome.Illuminance.IlluminanceGraphActivity;
import com.example.smarthome.MainActivity;
import com.example.smarthome.Model;
import com.example.smarthome.MotionAdapter;
import com.example.smarthome.NetworkCheck;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MotionActivity extends AppCompatActivity implements View.OnClickListener{

    ListView mlistview;
    private Toolbar toolbar;
    public static List<Model> motionlist;
    private SessionManager session;
    Model m;
    String token,username;

    EditText edfdate, edtodate,edftime,edtotime;
    Button search,graph;
    private int mYear, mMonth, mDay;
    Date myDate;
    Calendar calendar;
    String dateAsString;
    String CurrentDate,FinalDate;
    private int hour,minute,second;
    String date;
    String intent;
    private static final String URL_GRAPH = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Motion Sensor");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        motionlist=new ArrayList();
        mlistview=findViewById(R.id.motionlistview);
        myDate = new Date();
        setValue();
        calendar = Calendar.getInstance();
        date= TDTimeStamp.setTimeDate();
        intent=getIntent().getStringExtra("ID");
        session = new SessionManager(getApplicationContext());
        token= session.getToken();
        username=session.getUsername();
        Log.e("motion token",token);
        Log.e("motion name",username);
        if (NetworkCheck.getInstance(this).isOnline()) {

            new MotionClass().execute();

        }
        else{
            Toast.makeText(getApplicationContext(),"Network Connection Error",Toast.LENGTH_LONG).show();

        }
        Log.e("after exe","after ex");

    }

    public void setValue() {

        edfdate = (EditText) findViewById(R.id.txt_fromdate);
        edtodate = (EditText) findViewById(R.id.txt_todate);
        edftime=findViewById(R.id.txt_fromtime);
        edtotime=findViewById(R.id.txt_totime);
        search = (Button) findViewById(R.id.btn_search);
        graph=findViewById(R.id.btn_graph);
        edfdate.setOnClickListener(this);
        edtodate.setOnClickListener(this);
        edftime.setOnClickListener(this);
        edtotime.setOnClickListener(this);
        search.setOnClickListener(this);
        graph.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.txt_fromdate:

                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                String m,y,d;

                DatePickerDialog datePickerDialog = new DatePickerDialog(MotionActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        int month = monthOfYear + 1;
                        String formattedMonth = "" + month;
                        String formattedDayOfMonth = "" + dayOfMonth;

                        if(month < 10){

                            formattedMonth = "0" + month;
                        }
                        if(dayOfMonth < 10){

                            formattedDayOfMonth = "0" + dayOfMonth;
                        }


                        edfdate.setText(year + "-" +formattedMonth + "-" + formattedDayOfMonth);


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();

                break;

            case R.id.txt_todate:

                // Get Current Date

                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog2 = new DatePickerDialog(MotionActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        int month = monthOfYear + 1;
                        String formattedMonth = "" + month;
                        String formattedDayOfMonth = "" + dayOfMonth;

                        if(month < 10){

                            formattedMonth = "0" + month;
                        }
                        if(dayOfMonth < 10){

                            formattedDayOfMonth = "0" + dayOfMonth;
                        }


                        edtodate.setText(year + "-" +formattedMonth + "-" + formattedDayOfMonth);


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog2.setTitle("Select Date");
                datePickerDialog2.show();

                break;
            case R.id.txt_fromtime:
                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(MotionActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                edftime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();

                break;

            case R.id.txt_totime:

                final Calendar to = Calendar.getInstance();
                hour = to.get(Calendar.HOUR_OF_DAY);
                minute = to.get(Calendar.MINUTE);
                TimePickerDialog totime = new TimePickerDialog(MotionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        edtotime.setText(hourOfDay + ":" + minute);

                    }
                },hour, minute, false);
                totime.setTitle("Select Time");
                totime.show();

                break;

            case R.id.btn_search:
                Log.e("time is",edfdate.getText().toString()+" "+edftime.getText().toString().toString());
                CurrentDate = String.valueOf(TDTimeStamp.timeConversion(edfdate.getText().toString()+" "+edftime.getText().toString()));
                FinalDate = String.valueOf(TDTimeStamp.timeConversion(edtodate.getText().toString()+" "+edtotime.getText().toString()));
                Log.e("time is",CurrentDate+" :"+ FinalDate);
                motionlist.clear();

                if (edfdate.getText().toString().equals("")) {

                    edfdate.setError("Enter Date");

                }
                else if(edftime.getText().toString().equals(""))
                {
                    edftime.setError("Enter Time");
                }
                else if (edtodate.getText().toString().equals(""))
                {
                    edtodate.setError("Enter Date");

                }
                else if (edtotime.getText().toString().equals("")){

                    edtotime.setError("Enter Time");
                }
                else{
                    new MotionClass().execute();

                }
                break;

            case R.id.btn_graph:

                Intent intentg=new Intent(MotionActivity.this, MotionGraphActivity.class);
                intentg.putExtra("ID",intent);
                startActivity(intentg);
                this.finish();

                break;

        }

    }


    //// default date and time set to edittext




    public  class MotionClass extends AsyncTask<Void,Void,String>{

        String idResult = "";
        private ProgressDialog pD;
        String message="";
        String fun_id,value,name,fun_type,timestamp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pD = new ProgressDialog(MotionActivity.this);
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
                     Log.e("current date",""+CurrentDate);
                 if(CurrentDate == null && FinalDate == null) {
                     url = new URL(URL_GRAPH);
                     Log.e("if url",""+url);
                 }
                 else{
                     url = new URL(URL_GRAPH+"?"+"from="+CurrentDate+"&"+"to="+FinalDate);
                     Log.e("else url",""+url);

                 }
                Log.e("url",""+url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                String userCredentials = username+":"+token;
                //String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
               String basicAuth = "Basic " +Base64.getEncoder().encodeToString((username+":"+token).getBytes(StandardCharsets.UTF_8));
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
                        if (intent.equals("bath")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43216")) {
                                value = jsonObject.getString("value");
                                if (!value.equals("0")) {
                                    name = jsonObject.getString("name");
                                    fun_type = jsonObject.getString("function_type");
                                    timestamp = TDTimeStamp.convertTime(jsonObject.getString("timestamp"));
                                    addValue(fun_id, name, value, fun_type, timestamp);
                                }
                            }
                        }

                        if (intent.equals("bed")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43231")) {
                                value = jsonObject.getString("value");
                                if (!value.equals("0")) {
                                name = jsonObject.getString("name");
                                fun_type = jsonObject.getString("function_type");
                                timestamp = TDTimeStamp.convertTime(jsonObject.getString("timestamp"));
                                addValue(fun_id, name, value, fun_type, timestamp);
                            }
                        }
                        }

                        if (intent.equals("study")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43201")) {
                                value = jsonObject.getString("value");
                                if (!value.equals("0")) {
                                    name = jsonObject.getString("name");
                                    fun_type = jsonObject.getString("function_type");
                                    timestamp = TDTimeStamp.convertTime(jsonObject.getString("timestamp"));
                                    addValue(fun_id, name, value, fun_type, timestamp);
                                }
                            }
                        }
                        if (intent.equals("live1")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("41275")) {
                                value = jsonObject.getString("value");
                                if (!value.equals("0")) {
                                    name = jsonObject.getString("name");
                                    fun_type = jsonObject.getString("function_type");
                                    timestamp = TDTimeStamp.convertTime(jsonObject.getString("timestamp"));
                                    addValue(fun_id, name, value, fun_type, timestamp);
                                }
                            }
                        }
                        if (intent.equals("live2")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("40750")) {
                                value = jsonObject.getString("value");
                                if (!value.equals("0")) {
                                    name = jsonObject.getString("name");
                                    fun_type = jsonObject.getString("function_type");
                                    timestamp = TDTimeStamp.convertTime(jsonObject.getString("timestamp"));
                                    addValue(fun_id, name, value, fun_type, timestamp);
                                }
                            }
                        }
                        if (intent.equals("kitchen")) {
                            fun_id = jsonObject.getString("function_id");
                            if (fun_id.equals("43186")) {
                                value = jsonObject.getString("value");
                                if (!value.equals("0")) {
                                    name = jsonObject.getString("name");
                                    fun_type = jsonObject.getString("function_type");
                                    timestamp = TDTimeStamp.convertTime(jsonObject.getString("timestamp"));
                                    addValue(fun_id, name, value, fun_type, timestamp);
                                }
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
                MotionAdapter mAdapter = new MotionAdapter(MotionActivity.this, motionlist);
                mlistview.setAdapter(mAdapter);
                pD.dismiss();
            }
            else if (s.equals("2")){

                Toast.makeText(MotionActivity.this,"From and TO datetime is not correct",Toast.LENGTH_SHORT).show();
                pD.dismiss();
            }
            else if (motionlist.size()<1){

                Toast.makeText(MotionActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();
                pD.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MotionActivity.this, SubMotionActivity.class));
        this.finish();
    }
    public void addValue(String id, String name, String value, String type, String timestamp) {

        Model m =new Model();
        m.setFunctionid(id);
        m.setName(name);
        m.setValue(value);
        m.setFunctiontype(type);
        m.setTimestamp(timestamp);
        motionlist.add(m);
    }

}
