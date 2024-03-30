package com.example.yumfood.seller.store_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.yumfood.R;
import com.example.yumfood.customer.home.homepage.HomeFragment;
import com.example.yumfood.customer.home.personal.CustomerPersonal;
import com.example.yumfood.models.Store;
import com.example.yumfood.seller.store_management.SellerAboutStore.SellerAboutStoreFragment;
import com.example.yumfood.seller.store_management.SellerReport.SellerReportFragment;
import com.example.yumfood.seller.store_management.SellerManageMenu.SellerManageMenuFragment;
import com.example.yumfood.seller.store_management.SellerOrder.SellerOrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SellerStoreManagementActivity extends AppCompatActivity {
    private Store store;
    BottomNavigationView btnBottom_Nav_Menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_store_management);
        btnBottom_Nav_Menu = findViewById(R.id.nav_view);
        setFragment(new SellerOrderFragment() );
        btnBottom_Nav_Menu.setSelectedItemId(R.id.nav_order);
        btnBottom_Nav_Menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_order:
                        setFragment(new SellerOrderFragment());
                        return true;
                    case R.id.nav_menu:
                        setFragment(new SellerManageMenuFragment());
                        return true;
                    case R.id.nav_dashboard:
                        setFragment(new SellerReportFragment());
                        return true;
                    case R.id.nav_setting:
                        setFragment(new SellerAboutStoreFragment());
                        return true;
                }
                return false;
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_store_detail, fragment);
        fragmentTransaction.commit();
    }


}