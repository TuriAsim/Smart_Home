package com.example.smarthome;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import android.support.v7.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;


public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText username,password;
    private Button loginButton;
    private SessionManager session;
    private String uname,upass;

    private static final String URL_GRAPH = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Login Activity");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        session = new SessionManager(getApplicationContext());
        widgetObj();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uname=username.getText().toString();
                upass=password.getText().toString();
                if (username.getText().toString().equals("") && password.getText().toString().equals(""))
                {
                    Toast.makeText(LoginActivity.this,"Username or Password is Null",Toast.LENGTH_SHORT).show();
                }

                new LoginAPI().execute();

                }

        });

    }

    public void widgetObj(){

        username=findViewById(R.id.userName);
        password=findViewById(R.id.password);
        loginButton=findViewById(R.id.sign_in_button);

    }

    public class LoginAPI extends AsyncTask<Void, Void, String>{

        String idResult = "";
        private ProgressDialog pD;
        String message="";
        String Token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pD = new ProgressDialog(LoginActivity.this);
            pD.setMessage("Please wait");
            pD.setTitle("Check Credentials ...");
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
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                String userCredentials = uname+":"+upass;
                Log.e("cridential",userCredentials);
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
                connection.setRequestProperty ("Authorization", basicAuth);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                //myURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                JSONObject logjson=new JSONObject();
                logjson.put("device_id", "AAA");
                logjson.put("app_version", "1");

                Log.e("json object", logjson.toString());
                OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
                wr.write(logjson.toString());
                Log.e("value send", logjson.toString());
                wr.flush();
                wr.close();

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

                JSONObject jsonObject=new JSONObject(idResult);
                Token=jsonObject.getString("token");

                message="1";


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

                if (s.equals("1"))
                {
                    Log.e("message",s+":  "+Token);
                    session.setLogin(true);
                    session.setToken(Token);
                    session.setUserName(uname);

                    pD.dismiss();
                    startActivity(new Intent(LoginActivity.this, SensorActivity.class));
                    finish();

                }
                else{
                    pD.dismiss();
                    Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();

                }
        }
    }


}
