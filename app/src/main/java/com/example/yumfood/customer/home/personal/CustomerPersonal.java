package com.example.yumfood.customer.home.personal;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.yumfood.LoginActivity;
import com.example.yumfood.R;
import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.customer.home.myorder.orderconfirmation.address.CustomerOrderAddressActivity;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CustomerPersonal extends Fragment{
    private Button btnLogout;
    private ConstraintLayout clCustomerAddress;
    YumFoodDatabase db;
    private TextView txtFullName;
    private String userId;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadInfoToForm()
    {
        db = new YumFoodDatabase();

        db.loadUserFullnameToTextView(userId, txtFullName);
    }
    private void getUserInfo()
    {
        SharedPreferences preferences = getContext().getSharedPreferences("Session", getContext().MODE_PRIVATE);
        userId = preferences.getString("userId", "default value");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_personal, container, false);
        btnLogout = (Button) root.findViewById(R.id.fragment_cus_me_logout_btn);
        clCustomerAddress = (ConstraintLayout)  root.findViewById(R.id.fragment_customer_me_cl_customer_address);
        txtFullName = root.findViewById(R.id.txtFullName);
        getUserInfo();
        loadInfoToForm();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Đăng xuất")
                        .setContentText("Bạn có chắc muốn đăng xuất?")
                        .setConfirmText("Đồng ý")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
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
        clCustomerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getActivity(), CustomerOrderAddressActivity.class);
                startActivity(switchActivityIntent);
            }
        });
        return root;
    }
}