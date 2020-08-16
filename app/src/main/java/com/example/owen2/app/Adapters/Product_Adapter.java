package com.example.owen2.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.owen2.R;
import com.example.owen2.app.Activities.fragment_Detail;
import com.example.owen2.app.Activities.fragment_Product;
import com.example.owen2.app.Models.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.ItemHolder> {
    Context context;
    ArrayList<Product> arrayProduct;

    public Product_Adapter(Context context, ArrayList<Product> arrayProduct) {
        this.context = context;
        this.arrayProduct = arrayProduct;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_product,null);
        ItemHolder itemHolder = new ItemHolder(v);

        return itemHolder;
    }

    @Override
    public void onBindViewHolder( ItemHolder holder, int position) {
        Product product = arrayProduct.get(position);
        holder.txt_product_name.setMaxLines(1);
        holder.txt_product_name.setEllipsize(TextUtils.TruncateAt.END);
        holder.txt_product_name.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txt_product_price.setText(decimalFormat.format(product.getPrice())+" VND");
        Picasso.with(context).load(product.getImage())
                .placeholder(R.drawable.loader)
                .error(R.drawable.photo)
                .into(holder.img_product);
    }

    @Override
    public int getItemCount() {
        return arrayProduct.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView img_product;
        public TextView txt_product_name,txt_product_price;

        public ItemHolder(View itemView) {
            super(itemView);
            img_product= itemView.findViewById(R.id.image_product);
            txt_product_price=itemView.findViewById(R.id.txt_product_price);
            txt_product_name=itemView.findViewById(R.id.txt_product_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment_Detail fragmen_detail = new fragment_Detail();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Product_ID", arrayProduct.get(getPosition()).getProduct_ID());
                    bundle.putInt("ProCat_ID", arrayProduct.get(getPosition()).getProCat_ID());
                    bundle.putInt("Price", arrayProduct.get(getPosition()).getPrice());
                    bundle.putInt("Available", arrayProduct.get(getPosition()).getAvailable());
                    fragmen_detail.setArguments(bundle);
                    pushFragment(fragmen_detail,context);
                }
            });
        }
    }
    public void pushFragment(Fragment newFragment, Context context){
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragment, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

