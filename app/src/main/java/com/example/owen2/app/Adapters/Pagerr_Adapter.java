package com.example.owen2.app.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.owen2.app.Activities.fragment_Current_Orders;
import com.example.owen2.app.Activities.fragment_History;

public class Pagerr_Adapter extends FragmentStatePagerAdapter {

    private  String listTab[]={"Current_orders","History"};
    private  fragment_Current_Orders fragment_current_orders;
    private fragment_History fragment_history;
    public Pagerr_Adapter(FragmentManager fm) {
        super(fm);
        fragment_current_orders = new fragment_Current_Orders();
        fragment_history = new fragment_History();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return fragment_current_orders;
            case 1:
                return fragment_history;
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return listTab.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTab[position];
    }
}
