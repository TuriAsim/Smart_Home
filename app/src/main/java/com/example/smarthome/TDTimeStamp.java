package com.example.smarthome;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TDTimeStamp {

   static Context _context;
    public static int mYear, mMonth, mDay;
    public static int hour,minute,second;
    Date myDate;
    static Calendar calendar=Calendar.getInstance();
    static String date=null;
    static String time=null;

    public TDTimeStamp(Context context){

        this._context=context;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertTime(String time){
        String formattedDtm=null;
        Log.e("time here",time);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //final long unixTime = Long.parseLong(time);
        try {
            final long unixTime = (long) Double.parseDouble(time);
            formattedDtm = Instant.ofEpochSecond(unixTime).atZone(ZoneId.of("GMT+1")).format(formatter);
            Log.e("timconvert",formattedDtm);
        }
        catch (Exception e){

            e.printStackTrace();
        }

        return formattedDtm;

    }

    public static long timeConversion(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm", Locale.ENGLISH); //Specify your locale
        long unixTime = 0;
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+1")); //Specify your timezone
        try {
            unixTime = dateFormat.parse(time).getTime();
            unixTime = unixTime / 1000;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return unixTime;
    }



    public static String setTimeDate() {
         Calendar calendar;
         Date myDate;
        String dateAsString;
        calendar = Calendar.getInstance();
        myDate = new Date();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(myDate);
        Date time = calendar.getTime(); //dd-M-yyyy hh:mm:ss
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyy-MM-dd");
        dateAsString = outputFmt.format(time);
        //String time=year+month+formattedDayOfMonth;
//        Log.e("time is",dateAsString);
        long unixTime = timeConversion(dateAsString);
        Log.e("Unix time",""+unixTime);
        Log.e("current date", dateAsString);

       return dateAsString;
    }



    /*public static String datePicker(){
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        String m,y,d;

        DatePickerDialog datePickerDialog = new DatePickerDialog(_context, new DatePickerDialog.OnDateSetListener() {

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


                date= year + "-" +formattedMonth + "-" + formattedDayOfMonth;


            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

        return date;
    }*/


    /*public static String timePicker(){
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        ////start of time picker
        final TimePickerDialog timePickerDialog = new TimePickerDialog(c, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //edfdate.setText(date+" "+hourOfDay + ":" + minute);
                        time=hourOfDay + ":" + minute;
                        StringBuilder sb = new StringBuilder()
                                .append(hourOfDay).append(":").append(minute);
                    }
                }, hour, minute,true);
        timePickerDialog.show();
        // end time picker


        return time;
    }*/
}
