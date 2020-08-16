package com.example.owen2.app.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.owen2.R;
import com.example.owen2.app.Models.Card;
import com.example.owen2.app.Models.Product;
import com.example.owen2.app.Ultil.server;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Card_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Card> array_card;
    int n_quantity;

    public Card_Adapter(Context context, ArrayList<Card> array_card) {
        this.context = context;
        this.array_card = array_card;
    }

    @Override
    public int getCount() {
        return array_card.size();
    }

    @Override
    public Object getItem(int position) {
        return array_card.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public  class ViewHolder{
        public TextView Name,Price,Quantiy,Classify,Total;
        public ImageView Image;
        public ImageView img_minus,img_plus,img_delete;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.record_card,null);
            viewHolder.Name= view.findViewById(R.id.txt_card_name);
            viewHolder.Price = view.findViewById(R.id.txt_card_price);
            viewHolder.Image = view.findViewById(R.id.img_card);
            viewHolder.Quantiy=view.findViewById(R.id.txt_card_quantity);
            viewHolder.Classify=view.findViewById(R.id.txt_card_classify);
            viewHolder.Total=view.findViewById(R.id.txt_card_total);
            viewHolder.img_minus=view.findViewById(R.id.img_card_minus);
            viewHolder.img_plus=view.findViewById(R.id.img_card_plus);
            viewHolder.img_delete=view.findViewById(R.id.img_card_delete);
            view.setTag(viewHolder);
        }
        else {
            viewHolder= (ViewHolder) view.getTag();
        }

        final Card card = (Card) getItem(i);


        viewHolder.Name.setMaxLines(1);
        viewHolder.Name.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.Name.setText(card.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.Price.setText(decimalFormat.format(card.getPrice())+" VND");
        int tol=card.getPrice()*card.getQuantity();
        viewHolder.Total.setText(decimalFormat.format(tol)+" VND");
        viewHolder.Quantiy.setText(""+card.getQuantity());
        viewHolder.Classify.setText(""+card.getClassify());
        Picasso.with(context).load(card.getImage())
                .placeholder(R.drawable.loader)
                .error(R.drawable.photo)
                .into(viewHolder.Image);
        final int intQuantity = Integer.parseInt(viewHolder.Quantiy.getText().toString());

        Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_listview);
        view.startAnimation(animation);
        setStatusButton(intQuantity,card,viewHolder);

        final ViewHolder finalViewHolder = viewHolder;

        finalViewHolder.img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int intQuantity = Integer.parseInt(finalViewHolder.Quantiy.getText().toString());
                final int intNew_Quantity= intQuantity+1;
                //Toast.makeText(context,""+n_quantity,Toast.LENGTH_LONG).show();
                int n_Price= card.getPrice()*intNew_Quantity;
                int orderdetail_id=card.getOrderDetail_ID();
                try {
                    update_Card(intNew_Quantity,n_Price,orderdetail_id);
                    finalViewHolder.Quantiy.setText(""+intNew_Quantity);
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    finalViewHolder.Total.setText(decimalFormat.format(n_Price)+" VND");

                }catch (Exception e){
                }
                setStatusButton(intNew_Quantity,card, finalViewHolder);
            }
        });
        viewHolder.img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int intQuantity = Integer.parseInt(finalViewHolder.Quantiy.getText().toString());
                final int intNew_Quantity= intQuantity-1;
                int n_Price= card.getPrice()*intNew_Quantity;
                int orderdetail_id=card.getOrderDetail_ID();
                try {
                    update_Card(intNew_Quantity,n_Price,orderdetail_id);
                    finalViewHolder.Quantiy.setText(""+intNew_Quantity);
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    finalViewHolder.Total.setText(decimalFormat.format(n_Price)+" VND");

                }catch (Exception e){
                }
                setStatusButton(intNew_Quantity,card, finalViewHolder);
            }
        });
        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder((Activity) v.getContext());
                alertDialog.setTitle("Delete this item?");
                alertDialog.setMessage("Are you sure you want to delete this?");
                alertDialog.setIcon(R.drawable.question);
                alertDialog.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete_Card(card.getOrderDetail_ID());
                    }
                });
                alertDialog.show();
            }
        });
        return view;
    }

    public  static void setStatusButton(int intQuantity, Card card,ViewHolder viewHolder){
        if(intQuantity == card.getAvailable()){
            viewHolder.img_minus.setVisibility(View.VISIBLE);
            viewHolder.img_plus.setVisibility(View.INVISIBLE);
        }
        if(intQuantity == 1){
            viewHolder.img_minus.setVisibility(View.INVISIBLE);
            viewHolder.img_plus.setVisibility(View.VISIBLE);
        }
        if(intQuantity==1 && intQuantity == card.getAvailable()){
            viewHolder.img_minus.setVisibility(View.INVISIBLE);
            viewHolder.img_plus.setVisibility(View.INVISIBLE);
        }
        if(intQuantity>1 && intQuantity < card.getAvailable()){
            viewHolder.img_minus.setVisibility(View.VISIBLE);
            viewHolder.img_plus.setVisibility(View.VISIBLE);
        }
    }

    public void update_Card(final int intQuantity, final int intPrice, final int intOrderDetail_ID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Update_Shoppingcard, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")){
                    Toast.makeText(context,"Update success",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(context,"Update fail",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"error"+error,Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("OrderDetail_ID",String.valueOf(intOrderDetail_ID));
                params.put("Quantity",String.valueOf(intQuantity));
                params.put("Price",String.valueOf(intPrice));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void delete_Card(final int intOrderDetail_ID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Delete_card, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")){
                    Toast.makeText(context,"Delete success",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(context,"Delete fail",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"error"+error,Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("OrderDetail_ID",String.valueOf(intOrderDetail_ID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
