package com.example.yumfood.customer.home.myorder;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.yumfood.customer.home.myorder.orderhistory.MyOrderHistoryTabFragment;
import com.example.yumfood.customer.home.myorder.orderdetail.MyOrderOngoingTabFragment;

public class MyOrderTabViewPagerAdapter extends FragmentStateAdapter {

    public MyOrderTabViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyOrderOngoingTabFragment();
            case 1:
                return new MyOrderHistoryTabFragment();
            default:
                return new MyOrderOngoingTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
