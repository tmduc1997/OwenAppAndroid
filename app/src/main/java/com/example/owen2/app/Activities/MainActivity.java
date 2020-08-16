package com.example.owen2.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.owen2.R;
import com.example.owen2.app.Activities.fragment_Account;
import com.example.owen2.app.Activities.fragment_Main;
import com.example.owen2.app.Ultil.SessionManager;
import com.google.android.material.navigation.NavigationView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle  actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;

    EditText editText_search;
    Button btn_search,btn_card;
    int Customer_ID=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.dashboard);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout =findViewById(R.id.drawer);
        navigationView =findViewById(R.id.navigationView);
        editText_search=findViewById(R.id.main_edit_search);
        btn_search=findViewById(R.id.main_btn_search);
        btn_card=findViewById(R.id.main_btn_card);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_Search fragment_search = new fragment_Search();
                Bundle bundle = new Bundle();
                bundle.putString("txtSearch", String.valueOf(editText_search.getText()));
                fragment_search.setArguments(bundle);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,fragment_search);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sessionManager.islogin()){
                    fragment_Card fragment_card = new fragment_Card();
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_fragment,fragment_card);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else {
                    fragment_Pre_Login fragment_pre_login = new fragment_Pre_Login();
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.full_screen,fragment_pre_login);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(this);

        sessionManager = new SessionManager(this);
        sharedPreferences = getSharedPreferences("LOGIN",0);
        //Customer_ID=sharedPreferences.getInt("CUSTOMER_ID",0);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment,new fragment_Main());
        fragmentTransaction.commit();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.home){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new fragment_Main());
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.my_Account){
            if(sessionManager.islogin()){
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.full_screen,new fragment_Account());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }else {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.full_screen,new fragment_Pre_Login());
                fragmentTransaction.commit();
            }

        }
        if(item.getItemId() == R.id.my_Orders){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new fragment_Order());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.about){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new fragment_About());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.setting){

        }
        if(item.getItemId() == R.id.Promotion){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new fragment_Promotion());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        return true;
    }

    public void pushFragment(Fragment newFragment, Context context){
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragment, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public boolean isBetween2Dates(){
        boolean between=false;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date start = null;
        Date end= null;
        Date now=null;
        try {
            String a="06/09/2020";
            String b="06/13/2020";
            start = format.parse(a);
            end = format.parse(b);
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
