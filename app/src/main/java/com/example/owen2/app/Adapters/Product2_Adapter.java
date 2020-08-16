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
import com.example.owen2.app.Models.Product;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Product2_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Product> array_product2;

    public Product2_Adapter(Context context, ArrayList<Product> array_product2) {
        this.context = context;
        this.array_product2 = array_product2;
    }
    @Override
    public int getCount() {
        return array_product2.size();
    }

    @Override
    public Object getItem(int position) {
        return array_product2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public  class ViewHolder{
        public TextView Name,Price;
        public ImageView Image;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.record_product2,null);
            viewHolder.Name= view.findViewById(R.id.txt_product_name);
            viewHolder.Price = view.findViewById(R.id.txt_product_price);
            viewHolder.Image = view.findViewById(R.id.image_product);
            view.setTag(viewHolder);
        }
        else {
            viewHolder= (ViewHolder) view.getTag();
        }

        Product product = (Product) getItem(i);


        viewHolder.Name.setMaxLines(2);
        viewHolder.Name.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.Name.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.Price.setText(decimalFormat.format(product.getPrice())+" VND");
        Picasso.with(context).load(product.getImage())
                .placeholder(R.drawable.loader)
                .error(R.drawable.photo)
                .into(viewHolder.Image);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_listview);
        view.startAnimation(animation);
        return view;
    }
}
