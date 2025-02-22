package com.example.yumfood.customer.home.myorder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yumfood.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyOrderFragment extends Fragment {
    private TabLayout tablayout;
    private ViewPager2 viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_my_order, container, false);
        tablayout = (TabLayout) root.findViewById(R.id.MyOrderTab);
        viewPager = root.findViewById(R.id.MyOrderViewPager);
        MyOrderTabViewPagerAdapter adapter = new MyOrderTabViewPagerAdapter(this.getActivity());
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tablayout, viewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Đang đến");
                    break;
                case 1:
                    tab.setText("Lịch sử");
                    break;
            }
        }).attach();
        tablayout.setTranslationY(0);
        tablayout.setAlpha(1);

        return root;
    }
}