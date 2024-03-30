package com.example.yumfood.seller.store_management.SellerManageMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.yumfood.seller.store_management.SellerManageMenu.product.ProductFragment;
import com.example.yumfood.seller.store_management.SellerManageMenu.topping.ToppingFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new ToppingFragment();
            case 0:
                return new ProductFragment();
        }
        return new ProductFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
