package com.example.owen2.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.owen2.R;
import com.example.owen2.app.Adapters.Category_Adapter;
import com.example.owen2.app.Adapters.Product2_Adapter;
import com.example.owen2.app.Adapters.Product_Adapter;
import com.example.owen2.app.Adapters.Product_Adapter;
import com.example.owen2.app.Models.Category;
import com.example.owen2.app.Models.Product;
import com.example.owen2.app.Ultil.checkConnection;
import com.example.owen2.app.Ultil.server;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class fragment_Main extends Fragment {
    SliderLayout sliderLayout;
    RecyclerView recyclerView_new,recyclerView_top,recyclerView_category;


    ArrayList<Product> arrayListProduct_new, arrayListProduct_top;
    ArrayList<Category> arrayList_category;
    ArrayList<Product> arrayList_suggest;

    Product_Adapter product_adapter_new, product_adapter_top;
    Category_Adapter category_adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_main,container,false);
        sliderLayout = view.findViewById(R.id.imageSlider_main);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(1); //set scroll delay in seconds :
        setSliderViews();

        recyclerView_new = view.findViewById(R.id.recyclerview_New);
        recyclerView_top = view.findViewById(R.id.recyclerview_Top);
        recyclerView_category = view.findViewById(R.id.recyclerview_category);


        recyclerView_new.setHasFixedSize(true);
        recyclerView_top.setHasFixedSize(true);
        recyclerView_category.setHasFixedSize(true);

        LinearLayoutManager layoutNEW
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager layoutTOP
                = new GridLayoutManager(getContext(),2);
        LinearLayoutManager layoutCATE
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView_new.setLayoutManager(layoutNEW);
        recyclerView_top.setLayoutManager(layoutTOP);
        recyclerView_category.setLayoutManager(layoutCATE);

        recyclerView_new.setAdapter(product_adapter_new);
        recyclerView_top.setAdapter(product_adapter_top);
        recyclerView_category.setAdapter(category_adapter);
        return  view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayListProduct_new = new ArrayList<>();
        arrayListProduct_top = new ArrayList<>();
        arrayList_category = new ArrayList<>();
        arrayList_suggest = new ArrayList<>();

        product_adapter_new = new Product_Adapter(getContext(),arrayListProduct_new);
        product_adapter_top = new Product_Adapter(getContext(),arrayListProduct_top);
        category_adapter = new Category_Adapter(getContext(),arrayList_category);

        GetData_Category();
        GetData_Products(server.Link_get_10_NewProducts,arrayListProduct_new,product_adapter_new);
        GetData_Products(server.Link_get_10_TopProducts,arrayListProduct_top,product_adapter_top);

       // LoadMoreData();
    }

    public void GetData_Products(String link, final ArrayList<Product> arrayList, final Product_Adapter adapter){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(link, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response!=null){
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
                    for (int i=0; i<response.length();i++){
                        try {

                            JSONObject jsonObject= response.getJSONObject(i);
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

                            arrayList.add(new Product(Product_ID,Name,Image,Description,Price,ProCat_ID,New, Top,Freeship,Available));
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"NOT RESPONDING",Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void GetData_Category(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.Link_get_ProductCategory, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response!=null){
                    int ProCat_ID=0;
                    String Name ="";
                    String Image="";
                    String Description="";

                    for (int i=0; i<response.length();i++){
                        try {

                            JSONObject jsonObject= response.getJSONObject(i);
                            ProCat_ID = jsonObject.getInt("ProCat_ID");
                            Name=jsonObject.getString("Name");
                            Image=jsonObject.getString("Image");
                            Description=jsonObject.getString("Description");

                            arrayList_category.add(new Category(ProCat_ID,Name,Image,Description));
                            category_adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"NOT RESPONDING",Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(getContext());

            switch (i) {
                case 0:
                    sliderView.setImageUrl("https://vn-test-11.slatic.net/shop/60e097cc7bf00b8af26309d549f1d162.png");
                    break;
                case 1:
                    sliderView.setImageUrl("https://sudospaces.com/chanhtuoi-com/uploads/2015/11/%C3%A2fasf.jpg");
                    break;
                case 2:
                    sliderView.setImageUrl("https://tmluxury.vn/wp-content/uploads/banner-thoi-trang-nam-dep-tm-luxury.jpg");
                    break;
                case 3:
                    sliderView.setImageUrl("https://www.rostail.com//assets/uploads/products/MENS_FASHION.png");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription("" + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    //Toast.makeText(MainActivity.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }




}
