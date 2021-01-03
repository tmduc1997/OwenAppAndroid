package com.example.owen2.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.owen2.R;
import com.example.owen2.app.Models.Product;
import com.example.owen2.app.Ultil.SessionManager;
import com.example.owen2.app.Ultil.server;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class fragment_login extends Fragment {
    Button btn_login;
    TextInputLayout edit_password,edit_email;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String text_email,text_password;
    SessionManager sessionManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        edit_email = view.findViewById(R.id.edit_email);
        edit_password= view.findViewById(R.id.edit_password);
        btn_login=view.findViewById(R.id.btn_login);
        sessionManager = new SessionManager(getContext());
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Process_Login();
            }
        });
        return view;
    }

    private boolean ValidateEmail(){
        text_email=edit_email.getEditText().getText().toString().trim();
        if(text_email.isEmpty()){
            edit_email.setError("This field can not empty !");
            return false;
        }else {
            edit_email.setError(null);
            return true;
        }
    }

    private boolean ValidatePassword(){
        text_password=edit_password.getEditText().getText().toString().trim();
        if(text_password.isEmpty()){
            edit_password.setError("This field can not empty !");
            return false;
        }else {
            edit_password.setError(null);
            return true;
        }
    }

    private void Process_Login(){
        if(!ValidateEmail() | !ValidatePassword()){
            Toast.makeText(getContext(),"Vui lòng nhập đủ các trường !",Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(getContext(),"YES",Toast.LENGTH_SHORT).show();
            Login_process2(text_email,text_password);
        }
    }
    private void Login_process2(final String Email, final String PassWord){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    Toast.makeText(getContext(),"Password or email is not correct",Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getContext(),"Login is success",Toast.LENGTH_SHORT).show();
                    String Email;
                    String Avatar;
                    int Customer_ID;
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Email = jsonObject.getString("Email");
                            Customer_ID=jsonObject.getInt("Customer_ID");
                            Avatar=jsonObject.getString("Avatar");
                            sessionManager.createSession(Email,Customer_ID,Avatar);


                            //Toast.makeText(getContext(),"Email: "+Email+"\nCustomerid: "+Customer_ID+"\nAvatar: "+Avatar,Toast.LENGTH_SHORT).show();
                            FragmentTransaction transaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.full_screen, new fragment_Account());
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction.commit();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
