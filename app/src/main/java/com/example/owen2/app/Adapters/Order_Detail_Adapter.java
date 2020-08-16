package com.example.owen2.app.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owen2.R;
import com.example.owen2.app.Models.Order_Detail;
import com.example.owen2.app.Models.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Order_Detail_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Order_Detail> arrayList;

    public Order_Detail_Adapter(Context context, ArrayList<Order_Detail> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public  class ViewHolder{
        public TextView Name,Price,Quantity;
        public ImageView Image;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.record_order_detail,null);
            viewHolder.Name= view.findViewById(R.id.txt_order_detail_name);
            viewHolder.Price = view.findViewById(R.id.txt_order_detail_price);
            viewHolder.Quantity = view.findViewById(R.id.txt_order_detail_quantity);
            viewHolder.Image = view.findViewById(R.id.img_order_detail);
            view.setTag(viewHolder);
        }
        else {
            viewHolder= (ViewHolder) view.getTag();
        }

        Order_Detail order_detail = (Order_Detail) getItem(i);


        viewHolder.Name.setMaxLines(1);
        viewHolder.Name.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.Name.setText(order_detail.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.Price.setText(decimalFormat.format(order_detail.getPrice())+" VND");
        viewHolder.Quantity.setText("X"+order_detail.getQuantity());
        Picasso.with(context).load(order_detail.getImage())
                .placeholder(R.drawable.loader)
                .error(R.drawable.photo)
                .into(viewHolder.Image);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_listview);
        view.startAnimation(animation);
        return view;
    }
}
