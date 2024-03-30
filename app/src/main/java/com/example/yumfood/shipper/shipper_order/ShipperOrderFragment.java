package com.example.yumfood.shipper.shipper_order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.yumfood.R;
import com.example.yumfood.shipper.ShipperActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ShipperOrderFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private View view;
    private ShipperActivity activity;
    private ShipperOrderViewPagerAdapter viewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shipper_order, container, false);
        tabLayout = view.findViewById(R.id.tablayout_shipper);
        viewPager2 = view.findViewById(R.id.view_pager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (ShipperActivity) getActivity();
        viewPagerAdapter = new ShipperOrderViewPagerAdapter(activity);
        viewPager2.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position){
                case 1:
                    tab.setText("Đang giao");
                    break;
                case 2:
                    tab.setText("Đã xong");
                    break;
                default:
                    tab.setText("Có đơn");
                    break;
            }
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
}