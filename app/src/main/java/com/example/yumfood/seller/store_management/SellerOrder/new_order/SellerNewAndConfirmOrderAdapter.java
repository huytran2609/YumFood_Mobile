package com.example.yumfood.seller.store_management.SellerOrder.new_order;

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

public class SellerNewAndConfirmOrderAdapter extends RecyclerView.Adapter<SellerNewAndConfirmOrderAdapter.MerchantOrderViewHolder>{
    private final List<Order> orders;
    private Context context;

    public SellerNewAndConfirmOrderAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public MerchantOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_seller_new_and_confirm_order,parent,false);
        return new MerchantOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MerchantOrderViewHolder holder, int position) {
        Order order = orders.get(position);
        if(order == null)
            return ;
        holder.tvOrder.setText((position+1) + "");
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String total = currencyVN.format(order.getTotal()).replace("₫", "")+ " ₫";
        holder.tvTotal.setText(total);
        holder.tvOrderStatus.setText(order.getOrderStatus());

        holder.tvCountProduct.setText(order.getOrderDetail().size()+"");
        holder.tvOrderId.setText(order.getOrderId());

        DateFormat dateFormat = new SimpleDateFormat("hh:mm dd-MM-yyyy");
        holder.tvOrderDate.setText(dateFormat.format(order.getOrderDate()));

        YumFoodDatabase YumFoodDatabase = new YumFoodDatabase();
        YumFoodDatabase.loadUserFullnameToTextView(order.getUserId(), holder.tvFullName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToOrderDetail(order);
            }
        });
    }

    private void goToOrderDetail(Order order)
    {
        Intent switchActivityIntent = new Intent(this.context, SellerOrderDetailActivity.class);
        switchActivityIntent.putExtra("order", order);
        context.startActivity(switchActivityIntent);
    }

    @Override
    public int getItemCount() {
        if(orders != null)
            return orders.size();
        return 0;
    }

    public class MerchantOrderViewHolder extends RecyclerView.ViewHolder{
        private TextView tvOrderDate, tvOrderStatus, tvFullName, tvTotal, tvCountProduct, tvOrderId, tvOrder;
        private ConstraintLayout clParent;

        public MerchantOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderDate = itemView.findViewById(R.id.item_new_and_confirm_order_tv_order_date);
            tvOrderStatus = itemView.findViewById(R.id.item_new_and_confirm_order_tv_order_status);
            tvFullName = itemView.findViewById(R.id.item_new_and_confirm_order_tv_full_name);
            tvTotal = itemView.findViewById(R.id.item_new_and_confirm_order_tv_total);
            tvCountProduct = itemView.findViewById(R.id.item_new_and_confirm_order_tv_count_product);
            tvOrderId = itemView.findViewById(R.id.item_new_and_confirm_order_tv_order_id);
            clParent = itemView.findViewById(R.id.item_new_and_confirm_order_cl_parent);
            tvOrder = itemView.findViewById(R.id.item_new_and_confirm_order_tv_order);
        }
    }
}
