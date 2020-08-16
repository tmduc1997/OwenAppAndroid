package com.example.owen2.app.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.owen2.R;
import com.example.owen2.app.Adapters.Order_Detail_Adapter;
import com.example.owen2.app.Models.Card;
import com.example.owen2.app.Models.Order_Detail;
import com.example.owen2.app.Ultil.SessionManager;
import com.example.owen2.app.Ultil.checkConnection;
import com.example.owen2.app.Ultil.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fragment_Order_Detail extends Fragment {
    LinearLayout linearLayout;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    TextView txt_name,txt_phone,txt_address,txt_total,txt_date,txt_shipped_date,txt_code;

    GridView gridView;
    ArrayList<Order_Detail> arrayList;
    Order_Detail_Adapter order_detail_adapter;
    int Customer_ID;
    int Order_ID,shipped;
    String date, shipped_date;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_order_detail,container,false);
        linearLayout=view.findViewById(R.id.linear_detail);
        txt_name = view.findViewById(R.id.txt_order_detail_customer);
        txt_address = view.findViewById(R.id.txt_order_detail_address);
        txt_phone = view.findViewById(R.id.txt_order_detail_phone);
        txt_total = view.findViewById(R.id.txt_order_detail_total);
        txt_date = view.findViewById(R.id.txt_order_detail_date);
        txt_shipped_date = view.findViewById(R.id.txt_order_detail_shipped_date);
        txt_code = view.findViewById(R.id.txt_order_detail_code);
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.move_bottom_to_top);
        linearLayout.startAnimation(animation);
        gridView = view.findViewById(R.id.gridview_order_detail);
        gridView.setAdapter(order_detail_adapter);
        get_OrderID();
        getOrder_Detail(Order_ID);
        return view;
    }
    public void get_OrderID(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Order_ID = bundle.getInt("Order_ID", 0);
            date = bundle.getString("Date");
            shipped_date = bundle.getString("Shipped_date");
            shipped =bundle.getInt("status",0);
            txt_code.setText("#"+Order_ID);
            txt_date.setText(""+date);
            if (shipped==0){
                txt_shipped_date.setText("");
            }
            else {
                txt_shipped_date.setText(""+shipped_date);
            }
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getContext());
        sharedPreferences = getContext().getSharedPreferences("LOGIN",0);
        Get_ID();
        Get_Profile(Customer_ID);
        arrayList = new ArrayList<>();
        order_detail_adapter = new Order_Detail_Adapter(getContext(),arrayList);
    }
    private void Get_ID(){
        Customer_ID = sharedPreferences.getInt("CUSTOMER_ID",0);
    }
    private void Get_Profile(final int ID) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_get_Profile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String FullName ="";
                String Phone="";
                String Address="";

                if(response!=null && response.length()!=2){
                    //lvsptheoloai.removeView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            FullName=jsonObject.getString("FullName");
                            Phone=jsonObject.getString("Phone");
                            Address=jsonObject.getString("Address");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    txt_name.setText(FullName+"");
                    txt_phone.setText(Phone+"");
                    txt_address.setText(Address+"");
                }
                else {
                    checkConnection.showToast_short(getContext(),"END LIST");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("Customer_ID",String.valueOf(ID));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getOrder_Detail(final int intOrderID) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        String duongdan = server.Link_get_Order_Detail;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Name ="";
                String Image="";
                int Price=0;
                int Quantity=0;
                int Total=0;
                int Tong=0;
                String Classify="";
                if(response!=null && response.length()!=2){
                    //lvsptheoloai.removeView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Name=jsonObject.getString("Name");
                            Image=jsonObject.getString("Image");
                            Price=jsonObject.getInt("Price");
                            Quantity=jsonObject.getInt("Quantity");
                            Total=jsonObject.getInt("Total");
                            arrayList.add(new Order_Detail(Name,Price,Image,Quantity,Total));
                            order_detail_adapter.notifyDataSetChanged();
                            Tong+=Total;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    txt_total.setText(decimalFormat.format(Tong)+" VND");
                }
                else {
                    //lvsptheoloai.removeView(footerView);
                    checkConnection.showToast_short(getContext(),"END LIST");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("Order_ID",String.valueOf(intOrderID));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}
