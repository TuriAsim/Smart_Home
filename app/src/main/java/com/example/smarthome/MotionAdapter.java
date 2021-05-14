package com.example.smarthome;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MotionAdapter extends ArrayAdapter<Model> {

    Context context;
    List<Model> motionList;

    public MotionAdapter(@NonNull Context context, @NonNull List<Model> motionList) {
        super(context, R.layout.motion_adapter, motionList);

        this.context=context;
        this.motionList=motionList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        holder=new ViewHolder();
        if (convertView==null) {
            convertView = layoutInflater.inflate(R.layout.motion_adapter, null);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txt_value = (TextView) convertView.findViewById(R.id.txt_value);
            holder.txt_fun_type= convertView.findViewById(R.id.txt_function_type);
            holder.txt_time=convertView.findViewById(R.id.text_time);
            convertView.setTag(holder);

        }

        else  {

            holder= (ViewHolder) convertView.getTag();

        }
        Log.e("pidadp",motionList.get(position).getName());
        holder.txt_name.setText(motionList.get(position).getName());
        holder.txt_value.setText(motionList.get(position).getValue());
        holder.txt_fun_type.setText(motionList.get(position).getFunctiontype());
        holder.txt_time.setText(motionList.get(position).getTimestamp());
        return  convertView;
    }

    private class ViewHolder{

        private TextView txt_name,txt_value,txt_fun_type,txt_time;
    }
}
