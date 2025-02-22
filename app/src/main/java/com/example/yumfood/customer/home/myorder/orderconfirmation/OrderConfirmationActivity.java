package com.example.yumfood.customer.home.myorder.orderconfirmation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yumfood.R;
import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.customer.home.HomeActivity;
import com.example.yumfood.customer.home.myorder.orderconfirmation.address.CustomerOrderAddressActivity;
import com.example.yumfood.customer.store_detail.StoreDetailActivity;
import com.example.yumfood.models.CartItem;
import com.example.yumfood.models.CartSession;
import com.example.yumfood.models.Notification;
import com.example.yumfood.models.OrdAddress;
import com.example.yumfood.models.Order;
import com.example.yumfood.models.ShippingAddress;
import com.example.yumfood.models.Store;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderConfirmationActivity extends AppCompatActivity {
    private RecyclerView rcvProduct;
    private CartSession cartSession;
    private List<CartItem> cart;
    private CartItemForOrderConfirmationAdapter adapter;
    private TextView tvDeliveryTime, tvTotal, tvApplyFee, tvDeliveryFee, tvSum, tvDistance, tvShippingAddress, tvCustomerPhone, tvCustomerName;
    private SwitchCompat scDoorDelivery, scTakeEatingUtensils;
    private Button btnCofirm;
    private RadioGroup rgPaymentMethod;
    private RadioButton rbCash, rbOnlinePayment;
    private int sum = 0, deliveryFee = 0, applyFee = 0, total = 0;
    private float distance = 0;
    private ImageView  ivBtnBack;
    private ConstraintLayout clAddress;
    private YumFoodDatabase YumFoodDB;
    private String userId;
    private Store storeInfo;
    private OrdAddress ordAddressInfo;

    private void receiveStoreInfo()
    {
        Intent intent = getIntent();
        storeInfo = (Store) intent.getSerializableExtra("store");
    }
    private void receiveOrdAddressInfo(){
        Intent intent = getIntent();
        ordAddressInfo = (OrdAddress) intent.getSerializableExtra("address");
    }

    private void getUserInfo()
    {
        SharedPreferences prefs = this.getSharedPreferences("Session", MODE_PRIVATE);
        userId = prefs.getString("userId", "No name defined");
    }

    private void initUi()
    {
        rcvProduct = (RecyclerView) findViewById(R.id.activity_order_confirmation_rcv_cart);
        tvSum = (TextView)findViewById(R.id.activity_order_confirmation_tv_sum) ;
        tvTotal = (TextView)findViewById(R.id.activity_order_confirmation_tv_total) ;
        tvApplyFee = (TextView)findViewById(R.id.activity_order_confirmation_tv_apply_fee) ;
        tvDeliveryFee = (TextView)findViewById(R.id.activity_order_confirmation_tv_delivery_fee);
        scDoorDelivery = (SwitchCompat)findViewById(R.id.activity_order_confirmation_sc_door_delivery);
        scTakeEatingUtensils= (SwitchCompat) findViewById(R.id.activity_order_confirmation_sc_take_eating_utensils);
        btnCofirm =(Button) findViewById(R.id.activity_order_confirmation_btn_confirm);
        rgPaymentMethod = (RadioGroup)findViewById(R.id.activity_order_confirmation_rg_payment_method);
        rbCash = (RadioButton)  findViewById(R.id.activity_order_confirmation_rb_cash);
        rbOnlinePayment = (RadioButton)findViewById(R.id.activity_order_confirmation_rb_online_payment);
        ivBtnBack = (ImageView) findViewById(R.id.activity_order_confirmation_ib_back);
        clAddress = (ConstraintLayout)  findViewById(R.id.activity_order_confirmation_cl_address);
        tvShippingAddress = (TextView) findViewById(R.id.activity_order_confirmation_shipping_address);
        tvCustomerPhone = (TextView) findViewById(R.id.activity_order_confirmation_customer_phone);
        tvCustomerName = (TextView) findViewById(R.id.activity_order_confirmation_customer_name);
        tvDistance = (TextView) findViewById(R.id.activity_order_confirmation_tv_distance);
        tvDeliveryTime = (TextView) findViewById(R.id.DeliveryTime);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvProduct.setLayoutManager(linearLayoutManager);

        cartSession = new CartSession(OrderConfirmationActivity.this);
        cart = cartSession.getCart();
        adapter = new CartItemForOrderConfirmationAdapter( cart, this);
        rcvProduct.setAdapter(adapter);
        YumFoodDB = new YumFoodDatabase();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadInfoToForm()
    {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

        tvDeliveryTime.setText("Đơn hàng sẽ được giao đến bạn trong khoảng " + storeInfo.getDeliveryTime() + " nữa.");

        // Load sum cart
        sum = cartSession.getTotal();
        String sumString = currencyVN.format(sum).replace("₫", "")+ " ₫";
        tvSum.setText(sumString);

        // Load apply fee
        applyFee = 2000;
        String applyFeeString = currencyVN.format(applyFee).replace("₫", "")+ " ₫";
        tvApplyFee.setText(applyFeeString);

        // Load delivery fee

        distance = (float) ((storeInfo.getStoreAddress().length() + tvShippingAddress.getText().toString().length() + tvCustomerName.getText().toString().length()) / 32 + 0.7);
        tvDistance.setText("(" + distance + " km)");
        if(distance <= 3)
            deliveryFee = 10000 + (int)distance * 1000 ;
        else
            deliveryFee = 10000 + (int)distance * 3000;
        String deliveryFeeString = currencyVN.format(deliveryFee).replace("₫", "")+ " ₫";
        tvDeliveryFee.setText(deliveryFeeString);

        // Load total fee
        total = sum + applyFee + deliveryFee;
        String totalString = currencyVN.format(total).replace("₫", "")+ " ₫";
        tvTotal.setText(totalString);

        btnCofirm.setText("Đặt đơn - " + totalString);

        // Load Shipping Address
        if(ordAddressInfo != null) {
            Log.d("addressinfo", ordAddressInfo.getAddress());
            tvCustomerName.setText(ordAddressInfo.getName());
            tvShippingAddress.setText(ordAddressInfo.getAddress().replace(", Vietnam", "").replace(", Việt Nam", ""));
            tvCustomerPhone.setText(ordAddressInfo.getPhone_number());
        }else {
            YumFoodDB.loadCustomerShippingAddressToTextView(userId, tvCustomerName, tvCustomerPhone, tvShippingAddress);
        }
    }

    private Order getOrderInfoFromForm()
    {
        Order order = new Order();
        order.setTotal(total);
        order.setDeliveryFee(deliveryFee);
        order.setApplyFee(applyFee);
        if(rbOnlinePayment.isChecked())
            order.setPaymentMethod("Thanh toán online");
        else
            order.setPaymentMethod("Tiền mặt");
        order.setOrderDetail(cart);
        order.setStoreId(cart.get(0).product.getStoreId());
        order.setUserId(userId);

        if(scDoorDelivery.isChecked())
            order.setDoorDelivery(1);
        else
            order.setDoorDelivery(0);
        if(scTakeEatingUtensils.isChecked())
            order.setTakeEatingUtensils(1);
        else
            order.setTakeEatingUtensils(0);
        order.setOrderStatus("Đặt hàng thành công");
        order.setOrderDate(new Date());

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress(tvShippingAddress.getText().toString());
        shippingAddress.setPhone(tvCustomerPhone.getText().toString());
        shippingAddress.setReceiver(tvCustomerName.getText().toString());
        order.setShippingAddress(shippingAddress);

        order.setDistance(distance);
        order.setShipperId(" ");
        return order;
    }

    private void createNotification(Order order)
    {
        Notification notification = new Notification();
        notification.setUserId(order.getUserId());
        notification.setTitle("Thông báo đặt hàng thành công");
        notification.setContent("Cảm ơn quý khách đã đặt hàng. Đơn hàng của quý khách đang được xử lý. Mã đơn hàng của quý khách là: " + order.getOrderId());
        notification.setNotificationTime(new Date());
        YumFoodDB.insertNotification(notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        initUi();
        receiveStoreInfo();
        receiveOrdAddressInfo();
        getUserInfo();
        loadInfoToForm();
        btnCofirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = getOrderInfoFromForm();
                YumFoodDB.insertOrder(order);
                createNotification(order);
                cartSession.removeAllItem();

                Toast.makeText(OrderConfirmationActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                Intent switchActivityIntent = new Intent(OrderConfirmationActivity.this, HomeActivity.class);
                startActivity(switchActivityIntent);
            }
        });
        ivBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(OrderConfirmationActivity.this, StoreDetailActivity.class);
                switchActivityIntent.putExtra("store", storeInfo);
                startActivity(switchActivityIntent);
            }
        });
        clAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(OrderConfirmationActivity.this, CustomerOrderAddressActivity.class);
                switchActivityIntent.putExtra("store", storeInfo);
                startActivity(switchActivityIntent);
            }
        });
        scDoorDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scDoorDelivery.isChecked())
                {
                    deliveryFee += 5000;
                }
                else
                {
                    deliveryFee -= 5000;
                }
                Locale localeVN = new Locale("vi", "VN");
                NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
                String deliveryFeeString = currencyVN.format(deliveryFee).replace("₫", "")+ " ₫";
                tvDeliveryFee.setText(deliveryFeeString);
                total = sum + applyFee + deliveryFee;
                String totalString = currencyVN.format(total).replace("₫", "")+ " ₫";
                tvTotal.setText(totalString);
                btnCofirm.setText("Đặt đơn - " + totalString);
            }
        });
    }
}