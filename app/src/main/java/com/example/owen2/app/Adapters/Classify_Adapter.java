package com.example.owen2.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.owen2.R;
import com.example.owen2.app.Models.Classify;

import java.util.ArrayList;

public class Classify_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Classify> arrayList_classify;

    public Classify_Adapter(Context context, ArrayList<Classify> arrayList_classify) {
        this.context = context;
        this.arrayList_classify = arrayList_classify;
    }

    @Override
    public int getCount() {
        return arrayList_classify.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList_classify.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.record_classify,null);
        TextView textViewSize =convertView.findViewById(R.id.txt_classify);
        textViewSize.setText(arrayList_classify.get(position).getName());
        return convertView;
    }
}
