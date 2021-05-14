package com.example.smarthome.Illuminance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.Model;
import com.example.smarthome.Motion.MotionActivity;
import com.example.smarthome.Power.PowerActivity;
import com.example.smarthome.Power.SubPowerActivity;
import com.example.smarthome.R;
import com.example.smarthome.Temperature.TemperatureActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IlluminanceGraphActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LineChart mChart;
    private static List<Model> graphList;
    private Button ok;
    String intent;
    TextView txtname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Illuminance Sensors Graph");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        txtname=findViewById(R.id.title);

        mChart = (LineChart) findViewById(R.id.sensor_linechart);
        ok=findViewById(R.id.OkBtn);
        graphList= IlluminanceActivity.illlist;
        intent=getIntent().getStringExtra("ID");
        Collections.reverse(graphList);

        if (intent.equals("bath"))
        {
            //graphList= IlluminanceActivity.illlist;
            txtname.setText("Illuminance bathroom Sensor Graph");

        }
        else if (intent.equals("bed"))
        {
            //graphList=  IlluminanceActivity.illlist;
            txtname.setText("Illuminance bedroom Sensor Graph");

        }
        else if (intent.equals("study"))
        {
           /// graphList=  IlluminanceActivity.illlist;
            txtname.setText("Illuminance study room Sensor Graph");

        }

        else if (intent.equals("live1"))
        {
            //graphList=  IlluminanceActivity.illlist;
            txtname.setText("Illuminance Living room-1 Sensor Graph");

        }

        else if (intent.equals("live2"))
        {
          //  graphList= TemperatureActivity.templist;
            txtname.setText("Illuminance Living room-2 Sensor Graph");

        }

        else if (intent.equals("kitchen"))
        {
           // graphList= TemperatureActivity.templist;
            txtname.setText("Illuminance Kitchen Sensor Graph");

        }


           // Log.e("graph listsize",""+graphList.size());
        if (graphList.size() != 0) {
            showGraph();
        }
        else {

            Toast.makeText(IlluminanceGraphActivity.this,"No data found",Toast.LENGTH_SHORT).show();
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("graph intent",intent);

                startActivity(new Intent(IlluminanceGraphActivity.this, SubIlluminanceActivity.class));
                finish();

            }
        });

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = e.getX();
                float y = e.getY();

                Toast toast = Toast.makeText(getApplicationContext(), "Your factor value is : "+y, Toast.LENGTH_SHORT);
                toast.setGravity(0,Math.round(x), Math.round(y));
                toast.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    //// grapsh show method

    public void showGraph(){

        ArrayList<Entry> sysValues = new ArrayList<>();

        try {
            for (int t = 0; t < graphList.size();t++) {

                Log.e("value is", graphList.get(t).getValue());
                sysValues.add(new Entry(t, Float.parseFloat(graphList.get(t).getValue().toString())));

            }

        }

        catch (NegativeArraySizeException e){
            e.printStackTrace();
        }


        LineDataSet sysSet = new LineDataSet(sysValues, "" );


            sysSet.setFillAlpha(110);
            //sysSet.setColor(Color.GREEN);
            sysSet.setCircleColor(Color.RED);
            sysSet.setCircleRadius(5f);
            sysSet.setLineWidth(2.5f);




        YAxis barLeftAxis = mChart.getAxisLeft();
        barLeftAxis.setAxisMaximum(100);
        barLeftAxis.setAxisMinimum(0);
        barLeftAxis.setGranularity(10f);
        barLeftAxis.setLabelCount(20);




        YAxis rightYAxis = mChart.getAxisRight();
        rightYAxis.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(1000);
        xAxis.setLabelRotationAngle(-70);
        //barLeftAxis.setGranularity(.01f);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getAreaCount()));



        Description description=new Description();
        description.setText(" ");
        description.setTextColor(Color.BLUE);
        description.setTextSize(25);
        mChart.setDescription(description);


        mChart.setNoDataText("No Data");
        mChart.setNoDataTextColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(sysSet);


        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.animateX(5000);
        mChart.invalidate();

        Toast.makeText(IlluminanceGraphActivity.this, "Graph Data", Toast.LENGTH_SHORT).show();





    }

    public ArrayList<String> getAreaCount() {

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        String dateAsString;
        //String[] xAxisLabel=new String[2000];
        String date;

        for (int t = 0; t < graphList.size();t++) {
           try {

               Log.e("time stamp is", graphList.get(t).getTimestamp().toString());
               date = graphList.get(t).getTimestamp().toString();
               /*String[] times = date.substring(11, 16).split(":");

               dateAsString=times[0]+":"+times[1];

               Log.e("date print",""+dateAsString);*/
               xAxisLabel.add(date);

           }
           catch (Exception e){

               e.printStackTrace();
           }

        }
        Log.e("xsize",""+xAxisLabel.size());
        return xAxisLabel ;

    }


}
