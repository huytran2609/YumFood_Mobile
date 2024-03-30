package com.example.yumfood.customer.store_detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.yumfood.R;
import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.customer.cart.CartActivity;
import com.example.yumfood.customer.home.HomeActivity;
import com.example.yumfood.customer.home.myorder.orderconfirmation.OrderConfirmationActivity;
import com.example.yumfood.models.CartSession;
import com.example.yumfood.models.Review;
import com.example.yumfood.models.Store;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StoreDetailActivity extends AppCompatActivity {

    private ImageView ivStoreAvatar, ivShowCart, ivButtonBack, ivStoreWishList;
    private TextView tvStoreName, tvTotal, tvRating, tvDeliveryTime;
    private Button btnDelivery;
    private TabLayout tablayout;
    private ViewPager2 viewPager;
    private ToppingBottomSheetDialog toppingBottomSheetDialog;
    private RatingBar storeRatingBar;


    private Store storeInfo;
    private YumFoodDatabase YumFoodDB;
    private TextView tvStoreStatus;
   private CartSession cartSession;
    DatabaseReference realtimedbRef;


    private void initUi()
    {
        ivStoreAvatar = (ImageView) findViewById(R.id.StoreImage);
        ivShowCart = (ImageView) findViewById(R.id.StoreCart);
        ivStoreWishList = (ImageView) findViewById(R.id.StoreWishList);
        tvStoreName = (TextView) findViewById(R.id.StoreName);
        tvTotal = (TextView) findViewById(R.id.store_total);
        btnDelivery = (Button) findViewById(R.id.store_btn_delivery);
        tvRating = (TextView) findViewById(R.id.StoreRatingNumber);
        tvDeliveryTime = (TextView) findViewById(R.id.StoreDeliveryTime);
        tablayout = findViewById(R.id.StoreTab);
        viewPager = findViewById(R.id.StoreViewPager);
        ivButtonBack = (ImageView) findViewById(R.id.btnBack) ;
        tvStoreStatus = (TextView)  findViewById(R.id.StoreDeliveryTime);
        storeRatingBar = (RatingBar) findViewById(R.id.StoreRatingBar);

        cartSession = new CartSession(StoreDetailActivity.this);

    }

    private void receiveStoreInfo()
    {
        Intent intent = getIntent();
        storeInfo = (Store) intent.getSerializableExtra("store");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateTotalPrice()
    {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String total = currencyVN.format(cartSession.getTotal()).replace("₫", "")+ " ₫";
        tvTotal.setText(total);
    }

    public void setToppingBottomSheetDialog(ToppingBottomSheetDialog toppingBottomSheetDialog) {
        this.toppingBottomSheetDialog = toppingBottomSheetDialog;
    }

    public ToppingBottomSheetDialog getToppingBottomSheetDialog() {
        return toppingBottomSheetDialog;
    }

    public void getRatingTotal(){
        realtimedbRef = FirebaseDatabase.getInstance().getReference();
        realtimedbRef.child("stores").child(storeInfo.getStoreId()).child("reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float total = 0;
                int num_review = 0;
                for (DataSnapshot querysnapshot: snapshot.getChildren()) {
                    Review review = querysnapshot.getValue(Review.class);
                    total += review.getRating();
                    num_review += 1;
                }
                storeRatingBar.setRating(total / num_review);
                tvRating.setText(String.valueOf(total / num_review));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        receiveStoreInfo();
        initUi();
        getRatingTotal();

        final StoreDetailTabViewPagerAdapter adapter = new StoreDetailTabViewPagerAdapter(this, storeInfo);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tablayout, viewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Đặt đơn");
                    break;
                case 1:
                    tab.setText("Đánh giá");
                    break;
                case 2:
                    tab.setText("Thông tin");
                    break;
            }
        }).attach();
        tablayout.setTranslationY(0);
        tablayout.setAlpha(1);

        updateTotalPrice();

        YumFoodDB = new YumFoodDatabase();
        Glide.with(StoreDetailActivity.this).load(storeInfo.getAvatar()).into(ivStoreAvatar);
        tvStoreName.setText(storeInfo.getStoreName());
        tvRating.setText(String.valueOf(storeInfo.getRating()));
        tvDeliveryTime.setText(storeInfo.getDeliveryTime());
        ivShowCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(StoreDetailActivity.this, CartActivity.class);
                switchActivityIntent.putExtra("store", storeInfo);
                startActivity(switchActivityIntent);
            }
        });
        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartSession.count() > 0)
                {
                    Intent switchActivityIntent = new Intent(StoreDetailActivity.this, OrderConfirmationActivity.class);
                    switchActivityIntent.putExtra("store", storeInfo);
                    startActivity(switchActivityIntent);
                }
                else
                {
                    new SweetAlertDialog(StoreDetailActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            .setContentText("Chưa có sản phẩm nào trong giỏ hàng")
                            .setCustomImage(R.drawable.empty_cart)
                            .show();
                }
            }
        });

        ivStoreWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("Session", MODE_PRIVATE);
                String userId = prefs.getString("userId", "No name defined");

                YumFoodDB.insertLoveStore(storeInfo, userId);
                // ivStoreWishList.setColorFilter(getApplicationContext().getResources().getColor(R.color.red));
                ivStoreWishList.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_heart_fill_48));
            }
        });

        ivButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(StoreDetailActivity.this, HomeActivity.class);
                switchActivityIntent.putExtra("store", String.valueOf(storeInfo));
                startActivity(switchActivityIntent);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        updateTotalPrice();
    }

}