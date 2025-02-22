package com.example.yumfood.customer.home.myorder.orderhistory;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yumfood.R;
import com.example.yumfood.customer.home.myorder.orderdetail.CustomerOrderDetailActivity;
import com.example.yumfood.models.Order;
import com.example.yumfood.models.Store;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MyOrderHistoryTabAdapter extends RecyclerView.Adapter<MyOrderHistoryTabAdapter.MyOrderHistoryTabViewHolder>{
    private List<Order> orders;
    private Context context;
    private DatabaseReference Database;

    public MyOrderHistoryTabAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public MyOrderHistoryTabAdapter.MyOrderHistoryTabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyOrderHistoryTabAdapter.MyOrderHistoryTabViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_myorder_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderHistoryTabAdapter.MyOrderHistoryTabViewHolder holder, int position) {
        Order order = orders.get(position);
        if(order == null)
            return;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String total = currencyVN.format(order.getTotal()).replace("₫", "")+ " ₫";
        //Log.d("total", total);
        //Log.d("numproduct", order.getOrderDetail().size()+" sản phẩm");
        holder.tvTotal.setText(total);
        holder.tvOrderStatus.setText(order.getOrderStatus());
        holder.tvNumProduct.setText("(" + order.getOrderDetail().size()+" sản phẩm)");
        holder.tvOrderId.setText(order.getOrderId());

        DateFormat dateFormat = new SimpleDateFormat("hh:mm dd-MM-yyyy");
        holder.tvOrderDate.setText(dateFormat.format(order.getOrderDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(context, CustomerOrderDetailActivity.class);
                switchActivityIntent.putExtra("order", order);
                context.startActivity(switchActivityIntent);
            }
        });

        String storeID = order.getStoreId();
        Database = FirebaseDatabase.getInstance().getReference();


        Database.child("stores").child(storeID).child("storeName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String storeName = snapshot.getValue(String.class);
                holder.tvStoreName.setText(storeName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Database.child("stores").child(storeID).child("avatar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String avatar = snapshot.getValue(String.class);
                Glide.with(context).load(avatar).into(holder.imgStore);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        holder.btnCheckReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(context, WriteStoreReview.class);
                switchActivityIntent.putExtra("order", order);
                context.startActivity(switchActivityIntent);
            }
        });
    }
    private void goToOrderDetail(Order order)
    {
        Intent switchActivityIntent = new Intent(this.context, CustomerOrderDetailActivity.class);
        switchActivityIntent.putExtra("order", order);
        context.startActivity(switchActivityIntent);
    }

    @Override
    public int getItemCount() {
        if(orders != null)
            return orders.size();
        return 0;
    }

    public class MyOrderHistoryTabViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderId, tvOrderDate, tvStoreName, tvStoreAddress, tvTotal, tvNumProduct, tvOrderStatus;
        private ImageView imgStore;
        private Button btnReorder, btnCheckReview;

        public MyOrderHistoryTabViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = (TextView) itemView.findViewById(R.id.tv_myorder_history_orderid);
            tvOrderDate = (TextView) itemView.findViewById(R.id.tv_myorder_history_orderdate);
            tvStoreName = (TextView) itemView.findViewById(R.id.myorder_history_storename);
            tvStoreAddress = (TextView) itemView.findViewById(R.id.myorder_history_storeaddress);
            tvTotal = (TextView) itemView.findViewById(R.id.myorder_history_total);
            tvNumProduct = (TextView) itemView.findViewById(R.id.myorder_history_numproduct);
            tvOrderStatus = (TextView) itemView.findViewById(R.id.myorder_history_orderstt);
            imgStore = (ImageView) itemView.findViewById(R.id.img_myorder_history_storeimg);
            btnReorder = itemView.findViewById(R.id.myorder_history_reorder_btn);
            btnCheckReview = itemView.findViewById(R.id.myorder_history_checkstorereview_btn);
        }
    }
}
