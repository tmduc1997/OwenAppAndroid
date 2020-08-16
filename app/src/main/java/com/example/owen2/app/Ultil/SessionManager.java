package com.example.owen2.app.Ultil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;


public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE=0;

    private static final String PREF_NAME="LOGIN";
    private static final String LOGIN="IS_LOGIN";
    private static final String EMAIL="EMAIL";
    private static final String CUSTOMER_ID="CUSTOMER_ID";
    private static final String AVATAR="AVATAR";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=sharedPreferences.edit();

    }
    public void createSession(String Email,int Customer_ID,String Avatar){
        editor.putBoolean(LOGIN,true);
        editor.putString(EMAIL,Email);
        editor.putInt(CUSTOMER_ID,Customer_ID);
        editor.putString(AVATAR,Avatar);
        editor.apply();
    }

    public boolean islogin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    public void checkLogin(){
        if(!this.islogin()){

        }
    }
    public void logout(){
        editor.clear();
        editor.commit();
        Toast.makeText(context,"Account is logout",Toast.LENGTH_SHORT).show();
    }
}
