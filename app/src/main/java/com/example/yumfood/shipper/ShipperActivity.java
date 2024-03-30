package com.example.yumfood.shipper;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yumfood.R;
import com.example.yumfood.customer.favourite.CustomerFavourite;
import com.example.yumfood.customer.home.homepage.HomeFragment;
import com.example.yumfood.customer.home.myorder.MyOrderFragment;
import com.example.yumfood.customer.home.notification.CustomerNotification;
import com.example.yumfood.customer.home.personal.CustomerPersonal;
import com.example.yumfood.shipper.shipper_order.ShipperOrderFragment;
import com.example.yumfood.shipper.shipper_order.ShipperPersonalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ShipperActivity extends AppCompatActivity {


    BottomNavigationView btnBottom_Nav_Shipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper);
        setFragment(new ShipperOrderFragment());
        btnBottom_Nav_Shipper = findViewById(R.id.nav_shipper);
        btnBottom_Nav_Shipper.setSelectedItemId(R.id.nav_homeshipper);

        btnBottom_Nav_Shipper.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_homeshipper:
                        setFragment(new ShipperOrderFragment());
                        return true;
                    case R.id.nav_shipper:
                        setFragment(new ShipperPersonalFragment());
                        return true;
                }
                return false;
            }
        });

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_shipper, fragment);
        fragmentTransaction.commit();
    }
}