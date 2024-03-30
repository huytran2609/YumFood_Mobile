package com.example.yumfood.seller.order_detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.R;
import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.models.Notification;
import com.example.yumfood.models.Order;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SellerOrderDetailActivity extends AppCompatActivity {
    private Order order;
    private TextView tvCustomerName, tvTotal, tvCountProduct, tvOrderId, tvChangeOrderStatus;
    private ImageView ivBtnBack;
    private Button btnAccept, btnReject;
    private TextView tvOrderDate, tvOrderStatus, tvSubTotal, tvShipper, tv16, tvShipperName, tvDistance, tvShipperPhone, tvReceiver, tvApplyFee, tvShipFee, tvReceiverPhone, tvShippingAddress, tvStoreName, tvStoreAddress;
    private RecyclerView rcvProductList;
    private SellerOrderDetailAdapter adapter;
    private CheckBox ckbDoorDelivery, ckbTakeEatingUtensils;
    private boolean[] selectedReason;
    private List<Integer> selectedList = new ArrayList<>();
    private String[] cancelReason = {"Quán hết món", "Quán tạm nghỉ, không nhận đơn hàng này", "Tài xế từ chối vận chuyển đơn hàng"};
    private String[] orderStatus ={"Đã tiếp nhận đơn hàng", "Đang vận chuyển", "Giao hàng thành công"};
    private YumFoodDatabase yumFoodDatabase;
    private void initUi()
    {
        tvCustomerName = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_customer_name);
        rcvProductList = (RecyclerView) findViewById(R.id.activity_merchant_order_detail_rcvOrder) ;
        tvTotal = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_total);
        tvCountProduct = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_count_product);
        tvOrderId = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_order_id);
        ivBtnBack = (ImageView) findViewById(R.id.activity_merchant_order_detail_iv_btn_back);
        btnAccept = (Button)findViewById(R.id.activity_merchant_order_detail_btn_accept) ;
        btnReject  = (Button) findViewById(R.id.activity_merchant_order_detail_btn_reject);

        tvOrderDate =  (TextView) findViewById(R.id.activity_merchant_order_detail_tv_order_date);
        tvOrderStatus = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_oder_status);
        tvReceiver = (TextView) findViewById(R.id.activity_merchant_order_detail_receiver);
        tvReceiverPhone = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_phone);
        tvShippingAddress = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_shipping_address);
        tvStoreName = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_store_name);
        tvStoreAddress = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_store_address);
        ckbDoorDelivery = (CheckBox) findViewById(R.id.activity_merchant_order_detail_ckb_door_delivery);
        ckbTakeEatingUtensils = (CheckBox) findViewById(R.id.activity_merchant_order_detail_ckb_take_eating_utensils);
        tvShipFee =  (TextView) findViewById(R.id.activity_merchant_order_detail_tv_shipping_fee);
        tvShipperName = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_shipper_name);
        tvShipperPhone = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_shipper_phone);
        tvApplyFee = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_apply_fee);
        tvShipper  = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_shipper);
        tv16 =  (TextView) findViewById(R.id.activity_merchant_order_detail_tv_16);
        tvSubTotal =  (TextView) findViewById(R.id.activity_merchant_order_detail_tv_sub_total);
        tvDistance = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_tv_distance);
//        tvChangeOrderStatus = (TextView) findViewById(R.id.activity_merchant_order_detail_tv_change_order_status);




        if(!order.getOrderStatus().equals("Đặt hàng thành công"))
        {
            btnAccept.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
        }



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SellerOrderDetailActivity.this);
        rcvProductList.setLayoutManager(linearLayoutManager);


        adapter = new SellerOrderDetailAdapter( order.getOrderDetail(), SellerOrderDetailActivity.this);
        rcvProductList.setAdapter(adapter);
        rcvProductList.setNestedScrollingEnabled(false);
    }

    private void receiveOrderInfo()
    {
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");
    }

    private void loadDataToForm()
    {
        YumFoodDatabase goFoodDatabase = new YumFoodDatabase();
        goFoodDatabase.loadUserFullnameToTextView(order.getUserId(), tvCustomerName);
        if(order.getShipperId().equals(" "))
        {

            tvShipperName.setVisibility(View.GONE);
            tvShipperPhone.setVisibility(View.GONE);
            tvShipper.setVisibility(View.GONE);
            tv16.setVisibility(View.GONE);
        }
        else
            goFoodDatabase.loadUserFullNameAndPhoneToTextView(order.getShipperId(), tvShipperName, tvShipperPhone);
        tvOrderStatus.setText(order.getOrderStatus());
        tvDistance.setText("(" + order.getDistance() +"km)");
        if(order.getOrderStatus().contains("Đã hủy"))
        {
            tvOrderStatus.setText("Đã hủy");
            tvOrderStatus.setTextColor(Color.RED);
        }
        else
        {
            tvOrderStatus.setText(order.getOrderStatus());
        }
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String total = currencyVN.format(order.getTotal()).replace("₫", "")+ " ₫";
        tvTotal.setText(total);
        tvCountProduct.setText("(" + order.getOrderDetail().size() + " món)");
        tvOrderId.setText(order.getOrderId());
        int subTotal = 0;
        if(order.getDoorDelivery() == 1)
        {
            ckbDoorDelivery.setChecked(true);
            subTotal += 5000;
        }
        subTotal = subTotal + order.getTotal() - order.getApplyFee() - order.getDeliveryFee();
        if(order.getTakeEatingUtensils() == 1)
            ckbTakeEatingUtensils.setChecked(true);
        ckbDoorDelivery.setEnabled(false);
        ckbTakeEatingUtensils.setEnabled(false);
        String shipFee = currencyVN.format(order.getDeliveryFee()).replace("₫", "")+ " ₫";
        String applyFee = currencyVN.format(order.getApplyFee()).replace("₫", "")+ " ₫";
        String subTotalString = currencyVN.format(subTotal).replace("₫", "")+ " ₫";
        tvApplyFee.setText(applyFee);
        tvShipFee.setText(shipFee);
        tvSubTotal.setText(subTotalString);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm dd-MM-yyyy");
        tvOrderDate.setText(dateFormat.format(order.getOrderDate()));
        goFoodDatabase.loadShippingAddressToTextViewByOrderId(order.getOrderId(), tvReceiver, tvShippingAddress);
        goFoodDatabase.loadStoreNameAndAddressToTextView(order.getStoreId(), tvStoreName, tvStoreAddress);

    }

    private void createAcceptNotification(Order order)
    {
        Notification notification = new Notification();
        notification.setUserId(order.getUserId());
        notification.setTitle("Thay đổi trạng thái đơn hàng");
        notification.setContent("Đơn hàng " +  order.getOrderId() + " của quý khách đã được cửa hàng " + tvStoreName.getText() + " tiếp nhận và đang được xử lý");
        notification.setNotificationTime(new Date());
        yumFoodDatabase.insertNotification(notification);
    }

    private void createCancelNotification(Order order)
    {
        Notification notification = new Notification();
        notification.setUserId(order.getUserId());
        notification.setTitle("Đơn hàng của bạn đã bị hủy");
        notification.setContent("Đơn hàng "+ order.getOrderId() + " của quý khách tại cửa hàng " + tvStoreName.getText() + " đã bị hủy. Vui lòng đặt hàng lại hoặc liên hệ với cửa hàng để biết thêm thông tin chi tiết.");
        notification.setNotificationTime(new Date());
        yumFoodDatabase.insertNotification(notification);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_detail);
        yumFoodDatabase = new YumFoodDatabase();
        receiveOrderInfo();
        initUi();
        loadDataToForm();
        ivBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.setOrderStatus("Đã tiếp nhận đơn hàng");
                yumFoodDatabase.updateOrder(order);
                new SweetAlertDialog(SellerOrderDetailActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Thành công")
                        .setContentText("Đã tiếp nhận đơn hàng!")
                        .show();
                btnAccept.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
                createAcceptNotification(order);
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SellerOrderDetailActivity.this);
                builder.setTitle("Chọn nguyên nhân từ chối đơn hàng");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(cancelReason, selectedReason, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // Check condition
                        if(b){
                            // When checkbox selected
                            // Add position in day list
                            selectedList.add(i);
                            Collections.sort(selectedList);
                        }
                        else{
                            selectedList.remove(i);
                        }
                    }
                });
                builder.setPositiveButton("Xong", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        order.setOrderStatus("Đã hủy bởi quán");
                        order.setFinishTime(new Date());
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int j = 0; j < selectedList.size(); j++)
                        {
                            // Concat array value
                            stringBuilder.append(cancelReason[selectedList.get(j)]);
                            if(j != selectedList.size() -1)
                            {
                                stringBuilder.append(", ");
                            }
                        }
                        order.setCancelReason(stringBuilder.toString());
                        yumFoodDatabase.updateOrder(order);
                        createCancelNotification(order);
                        finish();
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
}