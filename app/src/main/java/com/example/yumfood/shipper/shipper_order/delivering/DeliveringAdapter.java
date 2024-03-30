package com.example.yumfood.shipper.shipper_order.delivering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.R;
import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.models.Order;
import com.example.yumfood.seller.order_detail.SellerOrderDetailAdapter;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DeliveringAdapter extends RecyclerView.Adapter<DeliveringAdapter.DeliveringViewHolder>{

    private List<Order> orders;

    public DeliveringAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    private Context context;
    @NonNull
    @Override
    public DeliveringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shipper_delivering_order, parent, false);
        return new DeliveringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveringViewHolder holder, int position) {
        Order order = orders.get(position);
        if(order == null)
            return ;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String shipFee = currencyVN.format(order.getDeliveryFee()).replace("₫", "")+ " ₫";
        String total = currencyVN.format(order.getTotal()).replace("₫", "")+ " ₫";
        String payable = currencyVN.format(order.getTotal() - order.getDeliveryFee() - order.getApplyFee()).replace("₫", "")+ " ₫";
        holder.txtShipFee.setText(shipFee);
        holder.txtTotalReceived.setText(total);
        holder.txtTotalPayable.setText(payable);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm dd-MM-yyyy");
        holder.txtOrderDate.setText(dateFormat.format(order.getOrderDate()));
        YumFoodDatabase db = new YumFoodDatabase();
        db.loadShippingAddress(order.getOrderId(), holder.txtCustomerName, holder.txtCustomerAddress);
        db.loadStoreNameAndAddress(order.getStoreId(), holder.txtStoreName, holder.txtStoreAddress);
        holder.txtDistance.setText(order.getDistance() + "km");
        loadProductList(holder, order);
        if(order.getDoorDelivery() == 1)
            holder.ckbDoorDelivery.setChecked(true);
        if(order.getTakeEatingUtensils() == 1)
            holder.ckbTakeEatingUtensils.setChecked(true);
        holder.ckbDoorDelivery.setEnabled(false);
        holder.ckbTakeEatingUtensils.setEnabled(false);
        holder.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Xác nhận")
                        .setContentText("Bạn đã giao hàng thành công?").setCustomImage(R.drawable.shipper_icon)
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                order.setOrderStatus("Giao hàng thành công");
                                order.setFinishTime(new Date());
                                db.updateOrder(order);
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
    private void loadProductList(@NonNull DeliveringViewHolder holder, Order order)
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.rcvProducts.setLayoutManager(linearLayoutManager);
        SellerOrderDetailAdapter adapter = new SellerOrderDetailAdapter(order.getOrderDetail(), context);
        holder.rcvProducts.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        if(orders != null)
            return orders.size();
        return 0;
    }

    public class DeliveringViewHolder extends RecyclerView.ViewHolder{
        private TextView txtShipFee, txtTotalPayable, txtTotalReceived, txtStoreName, txtCustomerName, txtOrderDate, txtStoreAddress, txtCustomerAddress, txtDistance;
        private Button btnDone;
        private RecyclerView rcvProducts;
        private CheckBox ckbDoorDelivery, ckbTakeEatingUtensils;

        public DeliveringViewHolder(@NonNull View itemView) {
            super(itemView);
            txtShipFee = itemView.findViewById(R.id.txt_delivering_fee);
            txtTotalPayable = itemView.findViewById(R.id.txt_delivering_pay);
            txtTotalReceived = itemView.findViewById(R.id.txt_delivering_received);
            txtStoreName = itemView.findViewById(R.id.txt_delivering_store_name);
            txtCustomerName  = itemView.findViewById(R.id.txt_delivering_cus_name);
            btnDone = itemView.findViewById(R.id.btn_delivering_accept);
            txtOrderDate = itemView.findViewById(R.id.txt_delivering_date);
            txtStoreAddress = itemView.findViewById(R.id.txt_delivering_start_address);
            txtCustomerAddress = itemView.findViewById(R.id.txt_delivering_end_address);
            txtDistance = itemView.findViewById(R.id.txt_delivering_distance);
            rcvProducts = itemView.findViewById(R.id.rcv_delivering_products);
            ckbDoorDelivery = itemView.findViewById(R.id.ckb_delivering_door);
            ckbTakeEatingUtensils = itemView.findViewById(R.id.ckb_delivering_utensil);
        }
    }
}
