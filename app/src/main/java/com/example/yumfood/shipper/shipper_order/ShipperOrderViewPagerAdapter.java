package com.example.yumfood.shipper.shipper_order;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.yumfood.shipper.shipper_order.delivering.DeliveringFragment;
import com.example.yumfood.shipper.shipper_order.done.DoneFragment;
import com.example.yumfood.shipper.shipper_order.received_order.ReceivedOrderFragment;

public class ShipperOrderViewPagerAdapter extends FragmentStateAdapter {

    public ShipperOrderViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new DeliveringFragment();
            case 2:
                return new DoneFragment();
            default:
                return new ReceivedOrderFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
