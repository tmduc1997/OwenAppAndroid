package com.example.owen2.app.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.owen2.R;
import com.example.owen2.app.Adapters.Card_Adapter;
import com.example.owen2.app.Adapters.Order_Adapter;
import com.example.owen2.app.Models.Card;
import com.example.owen2.app.Models.Order;
import com.example.owen2.app.Ultil.SessionManager;
import com.example.owen2.app.Ultil.checkConnection;
import com.example.owen2.app.Ultil.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fragment_Current_Orders extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout swipeRefreshLayout;
    GridView gridView;
    ArrayList<Order> arrayList;
    Order_Adapter order_adapter;
    int Customer_ID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_current_orders,container,false);
        gridView = view.findViewById(R.id.gridview_current_order);
        swipeRefreshLayout =view.findViewById(R.id.refresh_current_orders);
        swipeRefreshLayout.setOnRefreshListener(this);
        gridView.setAdapter(order_adapter);
        getOrder(Customer_ID);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"Here",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        sharedPreferences = getContext().getSharedPreferences("LOGIN",0);
        Customer_ID = sharedPreferences.getInt("CUSTOMER_ID",0);
        arrayList = new ArrayList<>();
        order_adapter = new Order_Adapter(getContext(),arrayList);

        super.onCreate(savedInstanceState);
    }
    private void getOrder(final int intCustomer_ID) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        String duongdan = server.Link_get_Order;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String CreateDate ="";
                String Shipped_Date="";
                int Order_ID=0;
                int status=0;

                int Discount=0;
                if(response!=null && response.length()!=2){
                    //lvsptheoloai.removeView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Order_ID=jsonObject.getInt("Order_ID");
                            CreateDate=jsonObject.getString("CreateDate");
                            status=jsonObject.getInt("Shipping_status");
                            Discount=jsonObject.getInt("Discount");
                            Shipped_Date=jsonObject.getString("Shipped_Date");
                            arrayList.add(new Order(Order_ID,Customer_ID,CreateDate,status,Discount,Shipped_Date));
                            order_adapter.notifyDataSetChanged();
                        }
                        Toast.makeText(getContext(),"iD"+Order_ID,Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                param.put("Customer_ID",String.valueOf(intCustomer_ID));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public void onRefresh() {
        arrayList.clear();
        getOrder(Customer_ID);
        order_adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

    }
}
