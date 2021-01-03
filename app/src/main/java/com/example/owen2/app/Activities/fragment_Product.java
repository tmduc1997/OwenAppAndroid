package com.example.owen2.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.owen2.R;
import com.example.owen2.app.Adapters.Product2_Adapter;
import com.example.owen2.app.Adapters.Product_Adapter;
import com.example.owen2.app.Models.Product;
import com.example.owen2.app.Ultil.checkConnection;
import com.example.owen2.app.Ultil.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fragment_Product extends Fragment implements  SwipeRefreshLayout.OnRefreshListener {
    RecyclerView gridView_product;
    ArrayList<Product> arrayList_product;
    Product_Adapter product2_adapter;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;


    int ProCat_ID=0;
    private int totalItemCount;
    private int firstVisibleItem;
    private int visibleItemCount;
    private int previousTotal;
    private int page=1;
    private boolean load=false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_product,container,false);
        swipeRefreshLayout =view.findViewById(R.id.refresh_product);
        swipeRefreshLayout.setOnRefreshListener(this);
        gridView_product = view.findViewById(R.id.gridview_product);
        linearLayoutManager = new GridLayoutManager(getContext(),2);
        progressBar = view.findViewById(R.id.progressbar_product);

        gridView_product.setLayoutManager(linearLayoutManager);
        gridView_product.setAdapter(product2_adapter);
       // mHandler = new mHandler();
        Get_ProCat_ID();
        GetData();


        return  view;
    }

    private void pagination() {
        gridView_product.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount=linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                if(load){
                    if(totalItemCount>previousTotal){
                        previousTotal=totalItemCount;
                        page++;
                        load=false;
                    }
                }
                if(!load && (firstVisibleItem+visibleItemCount >=totalItemCount )){
                    getNext();
                    load=true;
                    //Toast.makeText(getContext(),"Page number: "+page,Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void getNext() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
                String duongdan = server.Link_get_ProductByCategory+String.valueOf(page);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int Product_ID=0;
                        String Name ="";
                        String Image="";
                        String Description="";
                        int Price=0;
                        int ProCat_ID=0;
                        int New=0;
                        int Top=0;
                        int Freeship=0;
                        int Available=0;
                        if(response!=null && response.length()!=2){
                            //lvsptheoloai.removeView(footerView);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i=0; i<jsonArray.length();i++){

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Product_ID = jsonObject.getInt("Product_ID");
                                    Name=jsonObject.getString("Name");
                                    Price=jsonObject.getInt("Price");
                                    Image=jsonObject.getString("Image");
                                    Description=jsonObject.getString("Description");
                                    ProCat_ID=jsonObject.getInt("ProCat_ID");
                                    New = jsonObject.getInt("New");
                                    Top =jsonObject.getInt("Top");
                                    Freeship =jsonObject.getInt("Freeship");
                                    Available = jsonObject.getInt("Available");

                                    arrayList_product.add(new Product(Product_ID,Name,Image,Description,Price,ProCat_ID,New, Top,Freeship,Available));
                                    product2_adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                        param.put("ProCat_ID",String.valueOf(ProCat_ID));
                        return param;
                    }
                };
                pagination();
                requestQueue.add(stringRequest);
                progressBar.setVisibility(View.GONE);
            }
        },1000);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayList_product = new ArrayList<>();
        product2_adapter = new Product_Adapter(getContext(),arrayList_product);
    }


    public void pushFragment(Fragment newFragment, Context context){
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragment, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void GetData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        String duongdan = server.Link_get_ProductByCategory+String.valueOf(page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int Product_ID=0;
                String Name ="";
                String Image="";
                String Description="";
                int Price=0;
                int ProCat_ID=0;
                int New=0;
                int Top=0;
                int Freeship=0;
                int Available=0;
                if(response!=null && response.length()!=2){
                    //lvsptheoloai.removeView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Product_ID = jsonObject.getInt("Product_ID");
                            Name=jsonObject.getString("Name");
                            Price=jsonObject.getInt("Price");
                            Image=jsonObject.getString("Image");
                            Description=jsonObject.getString("Description");
                            ProCat_ID=jsonObject.getInt("ProCat_ID");
                            New = jsonObject.getInt("New");
                            Top =jsonObject.getInt("Top");
                            Freeship =jsonObject.getInt("Freeship");
                            Available = jsonObject.getInt("Available");

                            arrayList_product.add(new Product(Product_ID,Name,Image,Description,Price,ProCat_ID,New, Top,Freeship,Available));
                            product2_adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                param.put("ProCat_ID",String.valueOf(ProCat_ID));
                return param;
            }
        };
        pagination();
        requestQueue.add(stringRequest);
    }

    private void Get_ProCat_ID(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ProCat_ID = bundle.getInt("ProCat_ID", 0);
            //Toast.makeText(getContext(),"ProCat: "+ProCat_ID,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        arrayList_product.clear();
        product2_adapter.notifyDataSetChanged();
        page=1;
        load=false;
        Get_ProCat_ID();
        GetData();

        swipeRefreshLayout.setRefreshing(false);
    }
}
