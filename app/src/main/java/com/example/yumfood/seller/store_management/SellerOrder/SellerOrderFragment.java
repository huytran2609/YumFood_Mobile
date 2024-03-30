package com.example.yumfood.seller.store_management.SellerOrder;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yumfood.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SellerOrderFragment extends Fragment {
    private TabLayout tabLayout;
    private View view;
    private ViewPager2 viewPager;
    private SellerOrderViewPagerAdapter viewPagerAdapter;
    private ImageView btnBack;

    public SellerOrderFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_seller_order, container, false);
        tabLayout = view.findViewById(R.id.tabLayoutSellerOrder);
        viewPager = view.findViewById(R.id.viewPagerSellerOrder);
        btnBack = view.findViewById(R.id.buttonBackPageOrder);
        viewPagerAdapter = new SellerOrderViewPagerAdapter(this.getActivity());
        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position)
            {
                case 1:
                    tab.setText("Đã nhận");
                    break;
                case 2:
                    tab.setText("Lịch sử");
                    break;
                default:
                    tab.setText("Mới");
                    break;
            }
        }).attach();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}