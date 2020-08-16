package com.example.owen2.app.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.owen2.app.Ultil.checkConnection;
import com.example.owen2.app.Ultil.server;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class fragment_About  extends Fragment {
    SliderLayout sliderLayout;
    TextView txt_name,txt_phone,txt_address,txt_email,txt_description;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_about,container,false);
        txt_name =view.findViewById(R.id.About_txt_Name);
        txt_email =view.findViewById(R.id.About_txt_Email);
        txt_phone=view.findViewById(R.id.About_txt_Phone);
        txt_address=view.findViewById(R.id.About_txt_Address);
        txt_description=view.findViewById(R.id.About_txt_Description);
        sliderLayout = view.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(1); //set scroll delay in seconds :
        setSliderViews();
        get_About();
        return view;
    }

    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(getContext());

            switch (i) {
                case 0:
                    sliderView.setImageUrl("https://image.plo.vn/w800/Uploaded/2020/dbemzreyxq/2019_03_15/dhct-13_mtlw.jpg");
                    break;
                case 1:
                    sliderView.setImageUrl("https://www.ctu.edu.vn/images/upload/userfiles/image/NDH.jpg");
                    break;
                case 2:
                    sliderView.setImageUrl("https://images.foody.vn/res/g27/267740/s800/foody-truong-dai-hoc-can-tho-809-636607855427006453.jpg");
                break;
                case 3:
                    sliderView.setImageUrl("https://mientaycogi.com/wp-content/uploads/2019/10/h%E1%BB%99i-tr%C6%B0%E1%BB%9Dng-r%C3%B9a-%C4%91%E1%BA%A1i-h%E1%BB%8Dc-c%E1%BA%A7n-th%C6%A1-flycam.jpg");
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

    private void get_About() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        //String duongdan = server.Link_get_ProductByCategory+String.valueOf(page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_get_About, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Name="";
                String Phone="";
                String Address="";
                String Email="";
                String Description="";

                if(response!=null && response.length()!=2){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Name=jsonObject.getString("Name");
                        Email=jsonObject.getString("Email");
                        Phone=jsonObject.getString("Phone");
                        Address=jsonObject.getString("Address");
                        Description=jsonObject.getString("Description");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    txt_name.setText(" "+Name);
                    txt_email.setText(" "+Email);
                    txt_phone.setText(" "+Phone);
                    txt_address.setText(" "+Address);
                    txt_description.setText(" "+Description);
                }
                else {
                    checkConnection.showToast_short(getContext(),"END LIST");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
}
