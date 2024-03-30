package com.example.yumfood.seller.store_management.SellerOrder;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.yumfood.seller.store_management.SellerOrder.confirm_order.SellerConfirmOrderFragment;
import com.example.yumfood.seller.store_management.SellerOrder.history_order.SellerHistoryOrderFragment;
import com.example.yumfood.seller.store_management.SellerOrder.new_order.SellerNewOrderFragment;
public class SellerOrderViewPagerAdapter extends FragmentStateAdapter
{
    public SellerOrderViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new SellerConfirmOrderFragment();
            case 2:
                return new SellerHistoryOrderFragment();
            case 0:
                return new SellerNewOrderFragment();
        }
        return new SellerNewOrderFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
