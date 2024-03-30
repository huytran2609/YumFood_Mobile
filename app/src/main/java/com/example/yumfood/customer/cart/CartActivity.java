package com.example.yumfood.customer.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.R;
import com.example.yumfood.customer.home.myorder.orderconfirmation.OrderConfirmationActivity;
import com.example.yumfood.models.CartItem;
import com.example.yumfood.models.CartSession;
import com.example.yumfood.models.Store;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rcvCart;
    private CartItemAdapter cartItemAdapter;
    private List<CartItem> cart;
    private CartSession cartSession;
    private TextView tvDeleteAllItem, tvTotal;
    private ImageView ivEmptyCart, ivBtnBack;
    private Button btnDelivery;
    private Store storeInfo;

    private void receiveStoreInfo()
    {
        Intent intent = getIntent();
        storeInfo = (Store) intent.getSerializableExtra("store");
    }

    private void initUi()
    {
        rcvCart = (RecyclerView) findViewById(R.id.ProductInCart);
        tvDeleteAllItem = (TextView) findViewById(R.id.TextViewDeleteAllCart);
        ivEmptyCart = (ImageView) findViewById(R.id.ImageEmptyCart);
        tvTotal = (TextView) findViewById(R.id.TotalMoney) ;
        btnDelivery = (Button) findViewById(R.id.btnDelivery) ;
        ivBtnBack = (ImageView) findViewById(R.id.btnBack);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this);
        rcvCart.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(CartActivity.this,DividerItemDecoration.VERTICAL);
        rcvCart.addItemDecoration(dividerItemDecoration);

        cartSession = new CartSession(CartActivity.this);
        cart = cartSession.getCart();
        cartItemAdapter  = new CartItemAdapter(cart, CartActivity.this);
        rcvCart.setAdapter(cartItemAdapter);
        checkEmptyCartImageView();
    }

    public void checkEmptyCartImageView()
    {
        cart = cartSession.getCart();
        if(cart.size() == 0)
        {
            ivEmptyCart.setVisibility(View.VISIBLE);
            rcvCart.setVisibility(View.GONE);
        }
        else
        {
            ivEmptyCart.setVisibility(View.GONE);
            rcvCart.setVisibility(View.VISIBLE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateTotalPrice()
    {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String total = currencyVN.format(cartSession.getTotal()).replace("₫", "")+ " ₫";
        tvTotal.setText(total);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initUi();
        receiveStoreInfo();
        updateTotalPrice();
        tvDeleteAllItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(CartActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Xóa tất cả món")
                        .setContentText("Bạn có muốn xóa tất cả món trong giỏ hàng?")
                        .setConfirmText("Xóa tất cả")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                cartSession.removeAllItem();
                                checkEmptyCartImageView();
                                updateTotalPrice();
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
        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Demo bao cao giua ki
                if(cartSession.count() > 0)
                {
                    Intent switchActivityIntent = new Intent(CartActivity.this, OrderConfirmationActivity.class);
                    switchActivityIntent.putExtra("store", storeInfo);
                    startActivity(switchActivityIntent);
                }
                else
                {
                    new SweetAlertDialog(CartActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            .setContentText("Chưa có sản phẩm nào trong giỏ hàng")
                            .setCustomImage(R.drawable.empty_cart)
                            .show();
                }
            }
        });
        ivBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}