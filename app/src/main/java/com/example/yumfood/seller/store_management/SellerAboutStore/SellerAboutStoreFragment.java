package com.example.yumfood.seller.store_management.SellerAboutStore;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.yumfood.LoginActivity;
import com.example.yumfood.R;
import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.customer.home.HomeActivity;
import com.example.yumfood.models.Store;


public class SellerAboutStoreFragment extends Fragment {
    private Button btnLogout;
    private ConstraintLayout clCustomer;
    private Switch swStoreStatus;
    private TextView txtStoreName, txtRating, txtLikeAmount, txtTimeActi;
    private ConstraintLayout clCustomerAddress,clMerchant;
    private YumFoodDatabase goFoodDatabase;
    private String storeId;
    private Store storeInfo;
    private Integer status;
    private ImageView imgStoreAvt;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadInfoToForm()
    {
        goFoodDatabase = new YumFoodDatabase();

        goFoodDatabase.loadStoreInfoToTextView(storeId, txtStoreName,swStoreStatus,imgStoreAvt, getContext());


    }
    private void getStoreInfo()
    {
        SharedPreferences preferences = getContext().getSharedPreferences("Session", getContext().MODE_PRIVATE);
        storeId = preferences.getString("storeId", "default value");
        status = preferences.getInt("storeStatus", 1);
    }
    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_about_store, container, false);
        btnLogout = (Button) view.findViewById(R.id.fragment_mertchant_me_btn_logout) ;
        clCustomer = (ConstraintLayout) view.findViewById(R.id.fragment_mertchant_me_cl_customer) ;
        swStoreStatus = (Switch) view.findViewById(R.id.fragment_mertchant_me_sw_store_status) ;
        txtStoreName = (TextView) view.findViewById(R.id.txtStoreName);
        txtRating = (TextView) view.findViewById(R.id.txtRating);
        txtTimeActi = (TextView) view.findViewById(R.id.txtTimeActi);
        imgStoreAvt = (ImageView)  view.findViewById(R.id.imgStoreAvt);

        getStoreInfo();
        loadInfoToForm();
        if(status.equals("1")) swStoreStatus.setChecked(true);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(switchActivityIntent);
            }
        });
        clCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(switchActivityIntent);
            }
        });
        swStoreStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getSharedPreferences("Session", MODE_PRIVATE);
                String storeId = prefs.getString("storeId", "No name defined");

                YumFoodDatabase goFoodDatabase = new YumFoodDatabase();
                if(swStoreStatus.isChecked())
                    goFoodDatabase.updateStoreStatus(storeId, 1);
                else
                    goFoodDatabase.updateStoreStatus(storeId, 0);
            }
        });
        return view;
    }
}