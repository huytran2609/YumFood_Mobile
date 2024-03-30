package com.example.yumfood.customer.store_detail.info_tab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.yumfood.R;
import com.example.yumfood.models.Store;

public class StoreInfoTabFragment extends Fragment {
    private Store storeInfo;
    public StoreInfoTabFragment(Store storeInfo) {
        this.storeInfo = storeInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_store_info_tab, container, false);
        return root;
    }
}