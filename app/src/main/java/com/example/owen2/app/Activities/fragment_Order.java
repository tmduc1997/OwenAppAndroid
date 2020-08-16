package com.example.owen2.app.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.owen2.R;
import com.example.owen2.app.Adapters.Pagerr_Adapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class fragment_Order extends Fragment {
    TabLayout tablayout;
    ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_order,container,false);
        tablayout = view.findViewById(R.id.TabBar);
        viewPager =view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new Pagerr_Adapter(getFragmentManager()));
        tablayout.setupWithViewPager(viewPager);
        return view;
    }


}
