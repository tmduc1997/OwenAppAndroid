package com.example.owen2.app.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.owen2.R;
import com.example.owen2.app.Activities.fragment_Order_Detail;
import com.example.owen2.app.Models.Card;
import com.example.owen2.app.Models.Order;
import com.example.owen2.app.Models.OrderDetail_ID;
import com.example.owen2.app.Models.Order_Detail;
import com.example.owen2.app.Ultil.checkConnection;
import com.example.owen2.app.Ultil.server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Order_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Order> array_order;
    ArrayList<OrderDetail_ID> arrayList_id;


    public Order_Adapter(Context context, ArrayList<Order> array_order) {
        this.context = context;
        this.array_order = array_order;
    }

    @Override
    public int getCount() {
        return array_order.size();
    }

    @Override
    public Object getItem(int position) {
        return array_order.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public  class ViewHolder{
        public TextView txt_date,txt_number_code,txt_state,txt_shipped_date;
        public LottieAnimationView lottieAnimationView;
        public ImageView image_delete;
        Button btn_order_detial;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.record_order,null);
            viewHolder.txt_date= view.findViewById(R.id.txt_order_date);
            viewHolder.txt_shipped_date=view.findViewById(R.id.txt_order_shipped_date);
            viewHolder.txt_number_code = view.findViewById(R.id.txt_order_number_code);
            viewHolder.txt_state = view.findViewById(R.id.txt_order_state);
            viewHolder.lottieAnimationView=view.findViewById(R.id.order_img_status);
            viewHolder.btn_order_detial=view.findViewById(R.id.btn_order_detail);
            viewHolder.image_delete = view.findViewById(R.id.img_order_delete);
            view.setTag(viewHolder);
        }
        else {
            viewHolder= (ViewHolder) view.getTag();
        }

        final Order order = (Order) getItem(i);



        viewHolder.txt_date.setText(order.getCreateDate()+"");
        if (order.getShipping_status()<4){
            viewHolder.txt_shipped_date.setText("");
        }else {
            viewHolder.txt_shipped_date.setText(""+order.getShipped_Date());
        }
        viewHolder.txt_number_code.setText("#"+order.getOrder_ID());

        switch (order.getShipping_status()){
            case 0:
                viewHolder.lottieAnimationView.setAnimation(R.raw.waitting);
                viewHolder.txt_state.setText("Waiting for checked");
                break;
            case 1:
                viewHolder.lottieAnimationView.setAnimation(R.raw.recieved);
                viewHolder.txt_state.setText("The order has been received");
                break;
            case 2:
                viewHolder.lottieAnimationView.setAnimation(R.raw.checked);
                viewHolder.txt_state.setText("The package is being prepared");
                break;
            case 3:
                viewHolder.lottieAnimationView.setAnimation(R.raw.delivery);
                viewHolder.txt_state.setText("The package is being shipped");
                break;
            case 4:
                viewHolder.lottieAnimationView.setAnimation(R.raw.done);
                viewHolder.txt_state.setText("The package was shipped");
                break;

        }
//        if(order.getChecked()==0){
//            viewHolder.lottieAnimationView.setAnimation(R.raw.waitting);
//            viewHolder.txt_state.setText("Waiting for checking");
//        }
//        if(order.getChecked()==1 && order.getShipping()==0){
//            viewHolder.lottieAnimationView.setAnimation(R.raw.checked);
//            viewHolder.txt_state.setText("The package is available for shipping");
//        }
//        if(order.getChecked()==1 && order.getShipping()==1){
//            viewHolder.lottieAnimationView.setAnimation(R.raw.delivery);
//            viewHolder.txt_state.setText("The package is shipping");
//        }
//        if(order.getShipped()==1){
//            viewHolder.lottieAnimationView.setAnimation(R.raw.done);
//            viewHolder.txt_state.setText("The package was shipped");
//        }
        viewHolder.btn_order_detial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_Order_Detail fragment_order_detail = new fragment_Order_Detail();
                Bundle bundle = new Bundle();
                bundle.putInt("Order_ID", order.getOrder_ID());
                bundle.putInt("status", order.getShipping_status());
                bundle.putString("Date", order.getCreateDate());
                bundle.putString("Shipped_date", order.getShipped_Date());
                fragment_order_detail.setArguments(bundle);
               // Toast.makeText(context,"Orderid: "+order.getOrder_ID(),Toast.LENGTH_LONG).show();
                pushFragment(fragment_order_detail,context);
            }
        });
        viewHolder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrder_Detail(order.Order_ID);
                if(order.getShipping_status()>=2){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder((Activity) v.getContext());
                    alertDialog.setTitle("This package has covered");
                    alertDialog.setMessage("You can not cancel this order !");
                    alertDialog.setIcon(R.drawable.cancel);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
                else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder((Activity) v.getContext());
                    alertDialog.setTitle("Delete this order?");
                    alertDialog.setMessage("Are you sure you want to delete order?");
                    alertDialog.setIcon(R.drawable.question);
                    alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,order.Order_ID+"",Toast.LENGTH_SHORT).show();
                            cancellation_Order(order.Order_ID);
                        }
                    });
                    alertDialog.show();

                }

            }
        });
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_listview);
        view.startAnimation(animation);

        return view;
    }
    public void pushFragment(Fragment newFragment, Context context){
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragment, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void cancellation_Order(final int intOrder_ID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Cancellation_Order, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(context,response+"",Toast.LENGTH_SHORT).show();
                if(response.contains("1")){
                    Toast.makeText(context,"The Order has canceled already !",Toast.LENGTH_SHORT).show();

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
                params.put("Order_ID",String.valueOf(intOrder_ID));
                params.put("SL",String.valueOf(arrayList_id.size()));
                for (int i=0;i<arrayList_id.size();i++){
                    params.put("OrderDetail_ID"+i,String.valueOf(arrayList_id.get(i).getID()));
                    params.put("Quantity"+i,String.valueOf(arrayList_id.get(i).getQuantity()));
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void getOrder_Detail(final int intOrderID) {
        arrayList_id = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        String duongdan = server.Link_get_Order_Detail;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int Product_ID=0;
                int Quantity=0;
                int Available=0;
                int a=0;
                if(response!=null && response.length()!=2){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Product_ID=jsonObject.getInt("Product_ID");
                            Available=jsonObject.getInt("Available");
                            Quantity=jsonObject.getInt("Quantity");
                            a=Available+Quantity;
                            arrayList_id.add(new OrderDetail_ID(Product_ID,a));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                else {
                    checkConnection.showToast_short(context,"END LIST");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Order_ID",String.valueOf(intOrderID));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
