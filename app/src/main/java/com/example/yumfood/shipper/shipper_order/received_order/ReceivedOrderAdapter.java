package com.example.yumfood.shipper.shipper_order.received_order;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.R;
import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.models.Order;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReceivedOrderAdapter extends RecyclerView.Adapter<ReceivedOrderAdapter.ReceiveOrderViewHolder>{


    private List<Order> orders;
    private Context context;

    public ReceivedOrderAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public ReceiveOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shipper_received_order, parent, false);
        return new ReceiveOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveOrderViewHolder holder, int position) {
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
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Xác nhận")
                        .setContentText("Bạn có chắc muốn nhận đơn hàng này?").setCustomImage(R.drawable.shipper_icon)
                        .setConfirmText("Đồng ý")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                SharedPreferences prefs = context.getSharedPreferences("Session", MODE_PRIVATE);
                                String userId = prefs.getString("userId", "No name defined");
                                order.setShipperId(userId);
                                order.setOrderStatus("Đang vận chuyển");
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
        holder.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orders.remove(order);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(orders != null)
            return orders.size();
        return 0;
    }

    public class ReceiveOrderViewHolder extends RecyclerView.ViewHolder{
        private TextView txtShipFee, txtTotalPayable, txtTotalReceived, txtStoreName, txtCustomerName, txtOrderDate, txtStoreAddress, txtCustomerAddress, txtDistance;
        private Button btnAccept, btnSkip;
        private ConstraintLayout clParent;

        public ReceiveOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtShipFee = itemView.findViewById(R.id.txt_received_order_fee);
            txtTotalPayable = itemView.findViewById(R.id.txt_received_order_pay);
            txtTotalReceived = itemView.findViewById(R.id.txt_received_order_shipper_cash);
            txtStoreName = itemView.findViewById(R.id.txt_received_order_store_name);
            txtCustomerName  = itemView.findViewById(R.id.txt_received_order_cus_name);
            btnAccept = itemView.findViewById(R.id.btn_received_order_accpet);
            btnSkip = itemView.findViewById(R.id.btn_received_order_skip);
            txtOrderDate = itemView.findViewById(R.id.txt_received_order_date);
            txtStoreAddress = itemView.findViewById(R.id.txt_received_order_start_address);
            txtCustomerAddress = itemView.findViewById(R.id.txt_received_order_end_address);
            txtDistance = itemView.findViewById(R.id.txt_received_order_distance);
            clParent = itemView.findViewById(R.id.cl_shipper_received_order);
        }
    }
}
