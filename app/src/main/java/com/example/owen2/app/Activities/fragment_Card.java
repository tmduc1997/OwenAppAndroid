package com.example.owen2.app.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.owen2.app.Models.Card;
import com.example.owen2.app.Models.Product;
import com.example.owen2.app.Ultil.SessionManager;
import com.example.owen2.app.Ultil.checkConnection;
import com.example.owen2.app.Ultil.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fragment_Card extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout swipeRefreshLayout;

    GridView listView;
    ArrayList<Card> arrayList;
    Card_Adapter card_adapter;
    Button btn_buynow;
    int Customer_ID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_card,container,false);
        swipeRefreshLayout =view.findViewById(R.id.refresh_card);
        swipeRefreshLayout.setOnRefreshListener(this);
        listView = view.findViewById(R.id.listview_card);
        listView.setAdapter(card_adapter);
        btn_buynow = view.findViewById(R.id.btn_card_buynow);
        btn_buynow.setVisibility(View.INVISIBLE);
        btn_buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_Pre_CheckOut fragment_pre_checkOut = new fragment_Pre_CheckOut();
                pushFragment(fragment_pre_checkOut,getContext());
            }
        });
        getCard(Customer_ID);
        return view;
    }
    public void pushFragment(Fragment newFragment, Context context){
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragment, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        sharedPreferences = getContext().getSharedPreferences("LOGIN",0);
        Customer_ID = sharedPreferences.getInt("CUSTOMER_ID",0);

        arrayList = new ArrayList<>();
        card_adapter = new Card_Adapter(getContext(),arrayList);
        super.onCreate(savedInstanceState);
    }

    private void getCard(final int intCustomer_ID) {
        arrayList.clear();
        card_adapter.notifyDataSetChanged();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        String duongdan = server.Link_get_Card;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Name ="";
                String Image="";
                int Product_ID=0;
                int Price=0;
                int Quantity=0;
                int Available=0;
                int OrderDetail_ID=0;
                String Classify="";
                if(response!=null && response.length()!=2){
                    btn_buynow.setVisibility(View.VISIBLE);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Product_ID=jsonObject.getInt("Product_ID");
                            Name=jsonObject.getString("Name");
                            Image=jsonObject.getString("Image");
                            Price=jsonObject.getInt("Price");
                            Available = jsonObject.getInt("Available");
                            Quantity=jsonObject.getInt("Quantity");
                            OrderDetail_ID=jsonObject.getInt("OrderDetail_ID");
                            Classify=jsonObject.getString("Classify");

                            int a=0;
                            if(Quantity<=Available){
                                a=Quantity;
                            }else {
                                a=Available;
                                update_Card(a,a*Price,OrderDetail_ID);
                            }

                            arrayList.add(new Card(Product_ID,Name,Image,Price,a,OrderDetail_ID,Classify,Available));
                            card_adapter.notifyDataSetChanged();
                        }
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

    public void update_Card(final int intQuantity, final int intPrice, final int intOrderDetail_ID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Update_Shoppingcard, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")){
                    Toast.makeText(getContext(),"Update success",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getContext(),"Update fail",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error"+error,Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        arrayList.clear();
        getCard(Customer_ID);
        card_adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

    }
}
