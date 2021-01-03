package com.example.owen2.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.owen2.R;
import com.example.owen2.app.Config.Config;
import com.example.owen2.app.Ultil.server;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Activity_Paypal extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE=7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    Button btn_checkout;
    String amount="";
    TextView txt_amount;
    // define for checkout

    String Customer_ID,intcore, Discount,CurrentDateTime,Order_ID,SL;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        mapping();
        getAmount();

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }



    private void processPayment() {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD","Donate for Owen",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(paymentConfirmation!=null){
                    try {
                        String paymentDetail = paymentConfirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this,PaymentDetails.class)
                                .putExtra("PaymentDetails",paymentDetail)
                                .putExtra("PaymentAmount",amount)
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                checkOutPaypal();
            }
            else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_SHORT).show();
        }
        else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(getApplicationContext(),"invalid",Toast.LENGTH_SHORT).show();
    }

    private void mapping() {
        btn_checkout=findViewById(R.id.btn_checkout);
        txt_amount=findViewById(R.id.txt_amount);
    }

    private void getAmount(){
        Intent intent = getIntent();
        amount=intent.getStringExtra("amount");
        txt_amount.setText(amount);

    }

    private void checkOutPaypal() {
        Intent intent = getIntent();
        Customer_ID=intent.getStringExtra("Customer_ID");
        intcore=intent.getStringExtra("Score");
        Discount=intent.getStringExtra("Discount");
        CurrentDateTime=intent.getStringExtra("CreateDate");
        Order_ID=intent.getStringExtra("Order_ID");
        SL =intent.getStringExtra("SL");

        final int count =Integer.parseInt(SL);
        final String[] ID = new String [count];
        final String[] Quantity = new String [count];


        for(int i=0;i<count;i++){
            String id,quantity;
            id=intent.getStringExtra("OrderDetail_ID"+i);
            quantity=intent.getStringExtra("Quantity"+i);
            ID[i]=id;
            Quantity[i]=quantity;
        }

        finish();
//        Toast.makeText(getApplicationContext(),amount+"\n"+Customer_ID+"\n"+intcore+"\n"+Discount+"\n"+CurrentDateTime+"\n"+Order_ID+"\n"+count,Toast.LENGTH_LONG).show();
//
//        for(int i=0;i<count;i++){
//            Toast.makeText(getApplicationContext(),ID[i]+": "+Quantity[i],Toast.LENGTH_LONG).show();
//        }












        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Check_Out, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getContext(),""+response,Toast.LENGTH_SHORT).show();
                if(response.contains("1")){
                    Toast.makeText(getApplicationContext(),"Thanks for your order !",Toast.LENGTH_SHORT).show();
//                    fragment_Main fragment_main = new fragment_Main();
//                    pushFragment(fragment_main,getContext());

                }else {
                    Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error"+error,Toast.LENGTH_SHORT).show();
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
                params.put("SL",String.valueOf(count));
                for (int i=0;i<count;i++){
                    params.put("OrderDetail_ID"+i,String.valueOf(ID[i]));
                    params.put("Quantity"+i,String.valueOf(Quantity[i]));
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}
