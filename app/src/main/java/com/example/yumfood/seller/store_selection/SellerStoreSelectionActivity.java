package com.example.yumfood.seller.store_selection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yumfood.LoginActivity;
import com.example.yumfood.R;
import com.example.yumfood.WelcomeActivity;
import com.example.yumfood.customer.home.homepage.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SellerStoreSelectionActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextView logOutTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_store_selection);
        backButton = (ImageView) findViewById(R.id.btnBack);
        logOutTextView = (TextView) findViewById(R.id.txtViewLogOut);
        //setFragment(new SellerViewStoreFragment());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sw = new Intent(SellerStoreSelectionActivity.this, WelcomeActivity.class);
                startActivity(sw);
            }
        });

        logOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(SellerStoreSelectionActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Đăng xuất")
                        .setContentText("Bạn có chắc muốn đăng xuất?")
                        .setConfirmText("Đồng ý")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                FirebaseAuth.getInstance().signOut();
                                Intent sw = new Intent(SellerStoreSelectionActivity.this, LoginActivity.class);
                                startActivity(sw);
                                finish();
                            }
                        })
                        .setCancelButton("Hủy", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });



    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.seller_content_selection_store, fragment);
        fragmentTransaction.commit();
    }
}