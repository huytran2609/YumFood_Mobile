package com.example.yumfood.shipper.shipper_order.done;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.R;
import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.models.Order;
import com.example.yumfood.seller.order_detail.SellerOrderDetailActivity;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.DoneViewHolder>{

    private List<Order> orders;

    public DoneAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    private Context context;

    @NonNull
    @Override
    public DoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shipper_done_order, parent, false);
        return new DoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoneViewHolder holder, int position) {
        Order order = orders.get(position);
        if(order == null)
            return ;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String shipFee = currencyVN.format(order.getDeliveryFee()).replace("₫", "")+ " ₫";
        String total = currencyVN.format(order.getTotal()).replace("₫", "")+ " ₫";
        String payable = currencyVN.format(order.getTotal() - order.getDeliveryFee() - order.getApplyFee()).replace("₫", "")+ " ₫";
        holder.txt_doneShipFee.setText(shipFee);
        holder.txt_doneTotalReceived.setText(total);
        holder.txt_doneTotalPayable.setText(payable);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm dd-MM-yyyy");
        holder.txt_doneOrderDate.setText(dateFormat.format(order.getOrderDate()));
        holder.txt_doneDistance.setText(order.getDistance() + "km");
        holder.txt_doneCountProduct.setText(order.getOrderDetail().size()+"");
        holder.txt_doneOrderId.setText(order.getOrderId());

        YumFoodDatabase db = new YumFoodDatabase();
        db.loadUserFullname(order.getUserId(), holder.txt_doneFullName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(context, SellerOrderDetailActivity.class);
                switchActivityIntent.putExtra("order", order);
                context.startActivity(switchActivityIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(orders != null)
            return orders.size();
        return 0;
    }

    public class  DoneViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_doneOrderDate, txt_doneOrderStatus, txt_doneDistance, txt_doneFullName, txt_doneShipFee, txt_doneTotalPayable, txt_doneTotalReceived, txt_doneCountProduct, txt_doneOrderId, txt_doneOrder;
        private ConstraintLayout clParent;

        public  DoneViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_doneOrderDate = itemView.findViewById(R.id.txt_done_date);
            txt_doneOrderStatus = itemView.findViewById(R.id.txt_done_giaohangthanhcong);
            txt_doneFullName = itemView.findViewById(R.id.txt_done_full_name);
            txt_doneTotalPayable = itemView.findViewById(R.id.txt_done_total_payable);
            txt_doneTotalReceived = itemView.findViewById(R.id.txt_done_total_received);
            txt_doneCountProduct = itemView.findViewById(R.id.txt_done_count_product);
            txt_doneOrderId = itemView.findViewById(R.id.txt_done_order_id);
            txt_doneDistance = itemView.findViewById(R.id.txt_done_distance);
            clParent = itemView.findViewById(R.id.cl_shipper_done);
            txt_doneOrder = itemView.findViewById(R.id.txt_done_order);
            txt_doneShipFee = itemView.findViewById(R.id.txt_done_ship_fee);
        }
    }
}
