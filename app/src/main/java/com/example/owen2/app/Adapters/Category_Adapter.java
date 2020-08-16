package com.example.owen2.app.Adapters;

import android.content.Context;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.owen2.R;
import com.example.owen2.app.Activities.fragment_Main;
import com.example.owen2.app.Activities.fragment_Product;
import com.example.owen2.app.Models.Category;
import com.example.owen2.app.Models.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ItemHolder> {
    Context context;
    ArrayList<Category> array_category;

    public Category_Adapter(Context context, ArrayList<Category> array_category) {
        this.context = context;
        this.array_category = array_category;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_category,null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder( ItemHolder holder, int position) {
        Category category = array_category.get(position);
        holder.txt_category_name.setMaxLines(2);
        holder.txt_category_name.setEllipsize(TextUtils.TruncateAt.END);
        holder.txt_category_name.setText(category.getName());
        Picasso.with(context).load(category.getImage())
                .placeholder(R.drawable.loader)
                .error(R.drawable.photo)
                .into(holder.img_category);
    }

    @Override
    public int getItemCount() {
        return array_category.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView img_category;
        public TextView txt_category_name;

        public ItemHolder(View itemView) {
            super(itemView);
            img_category= itemView.findViewById(R.id.image_category);
            txt_category_name=itemView.findViewById(R.id.txt_category_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context,array_category.get(getPosition()).getName()+"",Toast.LENGTH_SHORT).show();
                    fragment_Product fragment_product = new fragment_Product();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ProCat_ID", array_category.get(getPosition()).getProCat_ID());
                    fragment_product.setArguments(bundle);
                    pushFragment(fragment_product,context);
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

