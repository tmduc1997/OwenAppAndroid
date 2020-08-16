package com.example.owen2.app.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.owen2.R;
import com.example.owen2.app.Ultil.server;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class fragmentSignUP extends Fragment {
    TextInputLayout edit_email,edit_pass,edit_confirm;
    String email,pass,confirm;
    Button btn_signup;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_signup,container,false);
        edit_email =view.findViewById(R.id.signup_edt_email);
        edit_pass=view.findViewById(R.id.signup_edt_pass);
        edit_confirm=view.findViewById(R.id.signup_edt_confirm_pass);
        btn_signup=view.findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate();
            }
        });


        return view;
    }

    private void Validate(){
        email=edit_email.getEditText().getText().toString().trim();
        pass=edit_pass.getEditText().getText().toString().trim();
        confirm=edit_confirm.getEditText().getText().toString().trim();
        //Toast.makeText(getContext(),""+email+pass+confirm,Toast.LENGTH_LONG).show();
        if(email.isEmpty()|| pass.isEmpty() || confirm.isEmpty()){
            Toast.makeText(getContext(),"Please enter all fields",Toast.LENGTH_LONG).show();

        }else {
            if(pass.equals(confirm)){
              Login_process2(email,pass);
            }
            else {
                Toast.makeText(getContext(),"Password confirm is not match",Toast.LENGTH_LONG).show();
            }

        }

    }

    private void Login_process2(final String Email, final String PassWord){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    signUp(email,pass);
                }else {
                    Toast.makeText(getContext(),"exit",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error"+error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();
                param.put("Email",Email);
                param.put("PassWord",PassWord);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void signUp(final String Email, final String PassWord){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_SignUp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    Toast.makeText(getContext(),"SignUp fail",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"SignUp success",Toast.LENGTH_SHORT).show();
                    fragmentManager = ((FragmentActivity)getContext()).getSupportFragmentManager();
                    fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.full_screen,new fragment_Account());
                    fragmentTransaction.commit();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error"+error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();
                param.put("Email",Email);
                param.put("PassWord",PassWord);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
