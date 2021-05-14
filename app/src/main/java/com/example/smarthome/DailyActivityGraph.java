package com.example.smarthome;

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

import com.example.smarthome.Illuminance.IlluminanceActivity;
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

public class DailyActivityGraph extends AppCompatActivity {

    private Toolbar toolbar;
    private LineChart mChart;
    private static List<Model> graphList;
    private Button ok;
    String intent;
    TextView txtname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_graph);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Illuminance Sensors Graph");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        txtname = findViewById(R.id.title);

        mChart = (LineChart) findViewById(R.id.sensor_linechart);
        ok = findViewById(R.id.OkBtn);
        graphList = DailyActivity.illlist;
        intent = getIntent().getStringExtra("ID");
        Collections.reverse(graphList);
        showGraph();


        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                float x = e.getX();
                float y = e.getY();

                Toast toast = Toast.makeText(getApplicationContext(), "Your factor value is : " + y, Toast.LENGTH_SHORT);
                toast.setGravity(0, Math.round(x), Math.round(y));
                toast.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DailyActivityGraph.this, DailyActivity.class));
                finish();
            }
        });

    }


    public void showGraph(){


        ArrayList<Entry> L1=new ArrayList<>();

        String labelarray[] = {"0","Living-1", "Living-2","Kitchen","Study-Room","Bed-Room","Bathroom"};


        ArrayList<Integer> colors = new ArrayList<Integer>();

        try {
            for (int t = 0; t < graphList.size();t++) {

                //Log.e("value is", graphList.get(t).getValue());
                if (graphList.get(t).getFunctionid().equals("41275"))
                {
                   // L1.add(new Entry(t, Float.parseFloat(graphList.get(t).getValue().toString())));
                    L1.add(new Entry(t, Float.parseFloat("1")));
                    colors.add( this.getResources().getColor( R.color.green ) );

                }
                else if (graphList.get(t).getFunctionid().equals("40750"))
                {
                    //L2.add(new Entry(t, Float.parseFloat(graphList.get(t).getValue().toString())));
                    L1.add(new Entry(t, Float.parseFloat("2")));
                    colors.add( this.getResources().getColor( R.color.blue ) );
                }
                else if (graphList.get(t).getFunctionid().equals("43186")){
                   // K3.add(new Entry(t, Float.parseFloat(graphList.get(t).getValue().toString())));
                    L1.add(new Entry(t, Float.parseFloat("3")));
                    colors.add( this.getResources().getColor( R.color.black ) );
                }
                else if (graphList.get(t).getFunctionid().equals("43201")){
                   // S4.add(new Entry(t, Float.parseFloat(graphList.get(t).getValue().toString())));
                    L1.add(new Entry(t, Float.parseFloat("4")));
                    colors.add( this.getResources().getColor( R.color.colorPrimary ) );
                }
                else if (graphList.get(t).getFunctionid().equals("43231")){
                   // B5.add(new Entry(t, Float.parseFloat(graphList.get(t).getValue().toString())));
                    L1.add(new Entry(t, Float.parseFloat("5")));
                    colors.add( this.getResources().getColor( R.color.colorAccent ) );
                }

                else if (graphList.get(t).getFunctionid().equals("43216"))
                {
                    //B6.add(new Entry(t, Float.parseFloat(graphList.get(t).getValue().toString())));
                    L1.add(new Entry(t, Float.parseFloat("6")));
                    colors.add( this.getResources().getColor( R.color.pink ) );
                }


            }

        }

        catch (NegativeArraySizeException e){
            e.printStackTrace();
        }

        LineDataSet setL1 = new LineDataSet(L1, "Patient Motion" );

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        if (L1.size()>0) {
            setL1.setFillAlpha(110);
            setL1.setCircleColors( colors );
            setL1.setColors(colors);
            setL1.setCircleRadius(5f);
            setL1.setLineWidth(2.5f);
           dataSets.add(setL1);
        }



        YAxis barLeftAxis = mChart.getAxisLeft();
        barLeftAxis.setGranularityEnabled(true);
        //barLeftAxis.setLabelCount(60);
        barLeftAxis.setAxisMaximum(6);
        barLeftAxis.setAxisMinimum(0);
        barLeftAxis.setGranularity(1f);
        barLeftAxis.setValueFormatter(new IndexAxisValueFormatter(labelarray));


        //barLeftAxis.setGranularity(10f);


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
        mChart.setDescription(description);
        mChart.setNoDataText("No Data");
        mChart.setNoDataTextColor(Color.RED);
        mChart.getLegend().setEnabled(false);






        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.animateX(5000);
        mChart.invalidate();

        Toast.makeText(DailyActivityGraph.this, "Graph Data", Toast.LENGTH_SHORT).show();

    }


    public ArrayList<String> getAreaCount() {

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        String dateAsString;
        //String[] xAxisLabel=new String[2000];
        String date;

        for (int t = 0; t < graphList.size();t++) {
            try {

                //Log.e("time stamp is", graphList.get(t).getTimestamp().toString());
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

    public ArrayList<String> getYAreaCount() {

        String labelarray[] = {"Living-1", "Living-2","Kitchen","Study-Room","Bed-Room","Bath-Room"};
        final ArrayList<String> yAxisLabel = new ArrayList<>();
        String label;

        for (int t = 0; t < labelarray.length;t++) {
            try {

               // Log.e("label is", labelarray[t]);
                label = labelarray[t];
               yAxisLabel.add(label);

            }
            catch (Exception e){

                e.printStackTrace();
            }

        }
    return yAxisLabel;
    }

}


