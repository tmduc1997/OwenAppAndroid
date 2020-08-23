package com.example.owen2.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.owen2.R;
import com.example.owen2.app.Adapters.Order_Adapter;
import com.example.owen2.app.Adapters.Order_Detail_Adapter;
import com.example.owen2.app.Models.OrderDetail_ID;
import com.example.owen2.app.Models.Order_Detail;
import com.example.owen2.app.Ultil.SessionManager;
import com.example.owen2.app.Ultil.checkConnection;
import com.example.owen2.app.Ultil.server;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class fragment_Pre_CheckOut extends Fragment {
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    ImageView edit_address,edit_phone;
    CheckBox checkBox;
    TextView txt_customer_name,txt_date,txt_total,txt_coins,txt_discount,txt_tong;
    TextInputEditText txt_customer_address,txt_customer_phone;
    GridView gridView;
    ArrayList<Order_Detail> arrayList;
    Order_Detail_Adapter order_detail_adapter;
    Button btn_checkout,btn_check_promt,btn_checkout_paypal;
    int Customer_ID;
    String CurrentDateTime;
    ArrayList<OrderDetail_ID> arrayList_id;
    int Order_ID;
    int Score;
    int Tong;
    double Discount;
    String strstart;
    String strend_a;
    int intCondition;
    double intValue;
    double promt;
    int intcore;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.pre_checkout,container,false);
        edit_address =view.findViewById(R.id.pre_checkout_edit_address);
        edit_phone=view.findViewById(R.id.pre_checkout_edit_phone);
        btn_checkout =view.findViewById(R.id.btn_check_out);
        btn_checkout_paypal=view.findViewById(R.id.btn_checkout_paypal);
        btn_check_promt=view.findViewById(R.id.btn_check_promotion);
        checkBox = view.findViewById(R.id.checkout_checkbox);
        txt_customer_name = view.findViewById(R.id.txt_pre_checkout_customer_name);
        txt_customer_address = view.findViewById(R.id.txt_pre_checkout_customer_address);
        txt_total = view.findViewById(R.id.txt_pre_checkout_total);
        txt_tong = view.findViewById(R.id.pre_checkout_txt_tong);
        txt_customer_phone = view.findViewById(R.id.txt_pre_checkout_customer_phone);
        txt_coins = view.findViewById(R.id.checkout_coins);
        txt_date = view.findViewById(R.id.txt_pre_checkout_date);
        txt_discount=view.findViewById(R.id.txt_pre_checkout_discount);
        txt_discount.setText("0 VND");
        gridView = view.findViewById(R.id.gridview_pre_checkout);
        gridView.setAdapter(order_detail_adapter);
        Get_curent_Datetime();
        txt_date.setText(""+CurrentDateTime);
        Get_Profile(Customer_ID);
        get_Promotion();
        getCard(Customer_ID);
        check_Promotion();



        process_Edit_Infor();
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_Out();
            }
        });
        btn_checkout_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Activity_Paypal.class);
                startActivity(intent);
            }
        });
        return  view;
    }
    private void process_Edit_Infor() {
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txt_customer_address.isEnabled()){
                   txt_customer_address.setEnabled(true);
                   edit_address.setImageResource(R.drawable.save);
                }else {
                    txt_customer_address.setEnabled(false);
                    edit_address.setImageResource(R.drawable.edit);
                    update_Address(txt_customer_address.getText().toString(),Customer_ID);
                    //Toast.makeText(getContext(),txt_customer_address.getText().toString(),Toast.LENGTH_SHORT).show();
                }

            }
        });

        edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txt_customer_phone.isEnabled()){
                    txt_customer_phone.setEnabled(true);
                    edit_phone.setImageResource(R.drawable.save);
                }else {
                    txt_customer_phone.setEnabled(false);
                    edit_phone.setImageResource(R.drawable.edit);
                    update_Phone(txt_customer_phone.getText().toString(),Customer_ID);
                    //Toast.makeText(getContext(),txt_customer_phone.getText().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void check_Out() {
//        fragment_CheckOut  fragment_checkOut = new fragment_CheckOut();
//        pushFragment(fragment_checkOut,getContext());
        process_Checkout();
    }
    private void process_Checkout(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Check_Out, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(getContext(),""+response,Toast.LENGTH_SHORT).show();
                if(response.contains("1")){
                    Toast.makeText(getContext(),"Thanks for your order !",Toast.LENGTH_SHORT).show();
                    fragment_Main fragment_main = new fragment_Main();
                    pushFragment(fragment_main,getContext());

                }else {
                    Toast.makeText(getContext(),"Fail",Toast.LENGTH_SHORT).show();
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
                params.put("Customer_ID",String.valueOf(Customer_ID));
                params.put("Score",String.valueOf(intcore));
                params.put("Discount",String.valueOf(Discount));
                params.put("CreateDate",String.valueOf(CurrentDateTime));
                params.put("Order_ID",String.valueOf(Order_ID));
                params.put("SL",String.valueOf(arrayList_id.size()));
                for (int i=0;i<arrayList_id.size();i++){
                    params.put("OrderDetail_ID"+i,String.valueOf(arrayList_id.get(i).getID()));
                    params.put("Quantity"+i,String.valueOf(arrayList_id.get(i).getQuantity()));
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        sharedPreferences = getContext().getSharedPreferences("LOGIN",0);
        Customer_ID = sharedPreferences.getInt("CUSTOMER_ID",0);
        arrayList = new ArrayList<>();
        arrayList_id = new ArrayList<>();

        order_detail_adapter = new Order_Detail_Adapter(getContext(),arrayList);
        super.onCreate(savedInstanceState);
    }

    private void Get_Profile(final int ID) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        //String duongdan = server.Link_get_ProductByCategory+String.valueOf(page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_get_Profile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String FullName ="";
                String Email="";
                String Phone="";
                String PassWord="";
                String Gender="";

                String Avatar="";
                String Address="";

                if(response!=null && response.length()!=2){
                    //lvsptheoloai.removeView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            FullName=jsonObject.getString("FullName");
                            Email=jsonObject.getString("Email");
                            Phone=jsonObject.getString("Phone");
                            PassWord=jsonObject.getString("PassWord");
                            Gender=jsonObject.getString("Gender");
                            Address=jsonObject.getString("Address");
                            Score=jsonObject.getInt("Score");
                            Avatar=jsonObject.getString("Avatar");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    txt_customer_name.setText(FullName+"");
                    txt_customer_phone.setText(""+Phone);
                    txt_customer_address.setText(Address+"");
                    txt_coins.setText("Use "+decimalFormat.format(Score)+" coins");
                    intcore = Score;
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

    private void getCard(final int intCustomer_ID) {

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
                int Total=0;
                int OrderDetail_ID;
                String Classify="";
                int Available=0;
                if(response!=null && response.length()!=2){
                    //lvsptheoloai.removeView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Name=jsonObject.getString("Name");
                            Product_ID=jsonObject.getInt("Product_ID");
                            Image=jsonObject.getString("Image");
                            Price=jsonObject.getInt("Price");
                            Available=jsonObject.getInt("Available");
                            Quantity=jsonObject.getInt("Quantity");
                            OrderDetail_ID=jsonObject.getInt("OrderDetail_ID");
                            Order_ID=jsonObject.getInt("Order_ID");
                            Total=jsonObject.getInt("Total");

                            Tong+=Total;

                            //for checkout
                            int a=0;
                            if(Quantity<=Available){
                                a=Available-Quantity;
                            }else {
                                a=Available;
                                update_Card(a,a*Price,OrderDetail_ID);
                            }
                            arrayList_id.add(new OrderDetail_ID(Product_ID,a));
                            arrayList.add(new Order_Detail(Name,Price,Image,Quantity,Total));
                            order_detail_adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    txt_total.setText(decimalFormat.format(Tong)+" VND");
                    txt_tong.setText(decimalFormat.format(Tong)+" VND");

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

    public void  Get_curent_Datetime( ){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss a");
        CurrentDateTime=simpleDateFormat.format(calendar.getTime());
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

    public void update_Address(final String strAddress, final int intID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Update_Address, new Response.Listener<String>() {
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
                params.put("Address",String.valueOf(strAddress));
                params.put("Customer_ID",String.valueOf(intID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void update_Phone(final String strPhone, final int intID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Update_Phone, new Response.Listener<String>() {
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
                params.put("Phone",String.valueOf(strPhone));
                params.put("Customer_ID",String.valueOf(intID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void pushFragment(Fragment newFragment, Context context){
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragment, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    private void get_Promotion() {
       // Toast.makeText(getContext(),"Tong ngoai ham: "+Tong,Toast.LENGTH_LONG).show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_get_Promotion, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int Promotion_ID =0;
                String Name="";
                String Image="";
                String Description="";
                String Start="";
                String End="";
                int  Condition=0;
                double  Value=0;

                if(response!=null && response.length()!=2){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
//                        Promotion_ID=jsonObject.getInt("Promotion_ID");
//                        Name=jsonObject.getString("Name");
//                        Image=jsonObject.getString("Image");
//                        Description=jsonObject.getString("Description");
                        Start=jsonObject.getString("Start");
                        End=jsonObject.getString("End");
                        Condition=jsonObject.getInt("Condition_a");
                        Value=jsonObject.getDouble("Value_a");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    strstart=Start;
                    strend_a=End;
                    intCondition=Condition;
                    intValue=Value;
                   // Toast.makeText(getContext(),"Start: "+strstart+"\nEnd: "+strend_a,Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void check_Promotion(){

        btn_check_promt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"Tong: "+Tong+"\nStart: "+strstart+"\nEnd: "+strend_a+"\nCondition: "+intCondition+"\nValue: "+intValue,Toast.LENGTH_LONG).show();
                if(Tong>=intCondition){
                    if(isBetween(strstart,strend_a)){
                      promt=Tong*intValue;
                    }
                    else {
                        promt=0;
                        Toast.makeText(getContext(),"Conditions are not valid",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    promt=0;
                    Toast.makeText(getContext(),"Conditions are not valid",Toast.LENGTH_SHORT).show();
                }
                Discount=promt;
                txt_discount.setText(decimalFormat.format(Discount)+ " VND");
                txt_total.setText(decimalFormat.format(Tong-Discount)+" VND");
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked()){
                    Discount=promt+Score;
                    intcore=0;
                    txt_coins.setText("0 Coins");
                }
                else {
                    Discount=promt;
                    intcore=Score;
                    txt_coins.setText("Use "+decimalFormat.format(Score)+" coins");
                }
                txt_discount.setText(decimalFormat.format(Discount)+ " VND");
                txt_total.setText(decimalFormat.format(Tong-Discount)+" VND");
            }
        });




    }

    public boolean isBetween(String strStart, String strEnd){
        boolean between=false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start;
        Date end;
        Date now;
        try {
            start = format.parse(strStart);
            end = format.parse(strEnd);
            now= new Date();
            if (now.after(start) && now.before(end) || (now.equals(start) || (now.equals(end)))) {
                between = true;

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return between;
    }
}
