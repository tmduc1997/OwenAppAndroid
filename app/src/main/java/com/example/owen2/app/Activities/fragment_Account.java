package com.example.owen2.app.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.owen2.app.Ultil.SessionManager;
import com.example.owen2.app.Ultil.checkConnection;
import com.example.owen2.app.Ultil.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class fragment_Account extends Fragment {

    Dialog dialog;
    TextView txt_image,txt_fullname,txt_email,txt_id,txt_score,txt_phone,txt_pass,txt_address;
    ImageView img_change_name,img_change_phone,img_change_address,img_change_password;
    ImageView img_logout,img_home;
    int Customer_ID=0;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;

    String FullName ="";
    String Email="";
    String Phone="";
    String PassWord="";
    String Gender="";
    int Score=0;
    String Avatar="";
    String Address="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_account2,container,false);
       // dialog = new Dialog(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        //txt_image = view.findViewById(R.id.profile_image);
        txt_fullname = view.findViewById(R.id.profile_txt_fullname);
        txt_email = view.findViewById(R.id.profile_txt_email);
        txt_id = view.findViewById(R.id.profile_txt_id_cusromer);
        txt_score = view.findViewById(R.id.profile_txt_scores);
        txt_phone = view.findViewById(R.id.profile_txt_phone);
        txt_pass = view.findViewById(R.id.profile_txt_password);
        txt_address = view.findViewById(R.id.profile_txt_address);
        img_logout = view.findViewById(R.id.img_logout);
        img_home =view.findViewById(R.id.account_home);
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_Main fragment_main = new fragment_Main();
                pushFragment(fragment_main,getContext());
            }
        });
        img_change_name = view.findViewById(R.id.img_change_name);
        img_change_phone =view.findViewById(R.id.img_change_phone);
        img_change_address=view.findViewById(R.id.img_change_address);
        img_change_password=view.findViewById(R.id.img_change_password);
        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                FragmentTransaction transaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.full_screen, new fragment_Pre_Login());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();
            }
        });
        img_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_Change_Name();
            }
        });
        img_change_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_Change_Phone();
            }
        });

        img_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_Change_Address();
            }
        });

        img_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_Confirm();
            }
        });
        return  view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getContext());
        sharedPreferences = getContext().getSharedPreferences("LOGIN",0);
        Get_ID();
        Get_Profile(Customer_ID);
    }

    private void Get_ID(){
        Customer_ID = sharedPreferences.getInt("CUSTOMER_ID",0);
        Toast.makeText(getContext(),"ID: "+Customer_ID,Toast.LENGTH_SHORT).show();
    }
    private void Get_Profile(final int ID) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        //String duongdan = server.Link_get_ProductByCategory+String.valueOf(page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_get_Profile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


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
                    txt_fullname.setText(FullName+"");
                    txt_email.setText(Email+"");
                    txt_id.setText(Customer_ID+"");
                    txt_score.setText(Score+"");
                    txt_phone.setText(Phone+"");
                    txt_pass.setText(PassWord+"");
                    txt_address.setText(Address+"");
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

    public void showDialog_Change_Name(){
        final EditText editText;
        TextView txt_cancel,txt_save;
        dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        View view  = getActivity().getLayoutInflater().inflate(R.layout.change_name, null);
        txt_cancel = view.findViewById(R.id.txt_change_name_close);
        txt_save =view.findViewById(R.id.txt_change_name_save);
        editText = view.findViewById(R.id.edit_change_name);
        editText.setText(""+txt_fullname.getText());
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =editText.getText().toString().trim();
                if(name.length()<=0){
                    Toast.makeText(getContext(),"Please, Enter your name !",Toast.LENGTH_LONG).show();
                }else {
                    update_Name(name,Customer_ID);
                    txt_fullname.setText(name);
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
//        FragmentManager manager = getFragmentManager();
//        Dialog_name dialog_name = new Dialog_name();
//        dialog_name.show(manager,"My dialog");


    }

    public void showDialog_Change_Phone(){
        final EditText editText;
        TextView txt_cancel,txt_save;
        dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        View view  = getActivity().getLayoutInflater().inflate(R.layout.change_phone, null);
        txt_cancel = view.findViewById(R.id.txt_change_phone_close);
        txt_save =view.findViewById(R.id.txt_change_phone_save);
        editText = view.findViewById(R.id.edit_change_phone);
        editText.setText(""+txt_phone.getText());
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone =editText.getText().toString().trim();
                if(phone.length()<=0){
                    Toast.makeText(getContext(),"Please, Enter your name !",Toast.LENGTH_LONG).show();
                }else {
                    update_Phone(phone,Customer_ID);
                    txt_phone.setText(phone);
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
//        FragmentManager manager = getFragmentManager();
//        Dialog_name dialog_name = new Dialog_name();
//        dialog_name.show(manager,"My dialog");


    }

    public void showDialog_Change_Address(){
        final EditText editText;
        TextView txt_cancel,txt_save;
        dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        View view  = getActivity().getLayoutInflater().inflate(R.layout.change_address, null);
        txt_cancel = view.findViewById(R.id.txt_change_address_close);
        txt_save =view.findViewById(R.id.txt_change_address_save);
        editText = view.findViewById(R.id.edit_change_address);
        editText.setText(""+txt_address.getText());
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =editText.getText().toString().trim();
                if(name.length()<=0){
                    Toast.makeText(getContext(),"Please, Enter your address !",Toast.LENGTH_LONG).show();
                }else {
                    update_Address(name,Customer_ID);
                    txt_address.setText(name);
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
//        FragmentManager manager = getFragmentManager();
//        Dialog_name dialog_name = new Dialog_name();
//        dialog_name.show(manager,"My dialog");


    }

    public void showDialog_Change_Password(){
        final EditText editText;
        TextView txt_cancel,txt_save;
        dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        View view  = getActivity().getLayoutInflater().inflate(R.layout.change_password, null);
        txt_cancel = view.findViewById(R.id.txt_change_password_close);
        txt_save =view.findViewById(R.id.txt_change_password_save);
        editText = view.findViewById(R.id.edit_change_password);
        editText.setText(""+txt_pass.getText());
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =editText.getText().toString().trim();
                if(name.length()<=0){
                    Toast.makeText(getContext(),"Please, Enter your password !",Toast.LENGTH_LONG).show();
                }else {
                    update_Password(name,Customer_ID);
                    txt_address.setText(name);
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
//        FragmentManager manager = getFragmentManager();
//        Dialog_name dialog_name = new Dialog_name();
//        dialog_name.show(manager,"My dialog");


    }

    public void showDialog_Confirm(){
        final EditText editText;
        TextView txt_cancel,txt_save;
        dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        View view  = getActivity().getLayoutInflater().inflate(R.layout.confirm_change_password, null);
        txt_cancel = view.findViewById(R.id.txt_confirm_change_close);
        txt_save =view.findViewById(R.id.txt_confim_change_password_OK);
        editText = view.findViewById(R.id.edit_current_password);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login_process(txt_email.getText().toString().trim(),editText.getText().toString().trim());
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
//        FragmentManager manager = getFragmentManager();
//        Dialog_name dialog_name = new Dialog_name();
//        dialog_name.show(manager,"My dialog");


    }

    public void update_Name(final String strName, final int intID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Update_Name, new Response.Listener<String>() {
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
                params.put("Name",String.valueOf(strName));
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

    public void update_Password(final String strPass, final int intID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Update_Password, new Response.Listener<String>() {
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
                params.put("PassWord",String.valueOf(strPass));
                params.put("Customer_ID",String.valueOf(intID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void Login_process(final String Email, final String PassWord){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.Link_Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    Toast.makeText(getContext(),"Password is not correct",Toast.LENGTH_SHORT).show();
                }else {
                    showDialog_Change_Password();
                    //Toast.makeText(getContext(),"Login is success",Toast.LENGTH_SHORT).show();
                    String Email;
                    String Avatar;
                    int Customer_ID;
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length();i++){
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

    public void pushFragment(Fragment newFragment, Context context){
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.replace(R.id.container_fragment, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }


}
