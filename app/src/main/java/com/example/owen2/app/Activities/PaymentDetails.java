package com.example.owen2.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.owen2.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {

    TextView txt_id,txt_amount,txt_status;
    ImageView home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        mapping();
        Intent intent = getIntent();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });

    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            txt_id.setText("Transaction ID: "+response.getString("id"));
            txt_status.setText("Transaction status : "+response.getString("state"));
            txt_amount.setText("Payment amount : "+"$"+response.getString(paymentAmount));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mapping() {
        txt_id =findViewById(R.id.txt_id);
        txt_amount =findViewById(R.id.txt_amount);
        txt_status =findViewById(R.id.txt_status);
        home=findViewById(R.id.home);
    }

    public void pushFragment(Fragment newFragment, Context context){
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragment, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }
}
