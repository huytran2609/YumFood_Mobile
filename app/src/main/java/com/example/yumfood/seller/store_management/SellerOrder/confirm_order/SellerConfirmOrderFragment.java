package com.example.yumfood.seller.store_management.SellerOrder.confirm_order;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yumfood.R;
import com.example.yumfood.models.Order;
import com.example.yumfood.seller.store_management.SellerOrder.new_order.SellerNewAndConfirmOrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SellerConfirmOrderFragment extends Fragment {
    private RecyclerView rcvOrder;

    private SellerNewAndConfirmOrderAdapter adapter;
    private List<Order> orders;

    private View view;
    public SellerConfirmOrderFragment() {
        // Required empty public constructor
    }
    private void getOrderFromRealtimeDatabase()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences("Session", MODE_PRIVATE);
        String storeId = prefs.getString("storeId", "No name defined");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("orders");

        myRef.orderByChild("storeId").equalTo(storeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orders.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Order order = postSnapshot.getValue(Order.class);
                    if(order != null && (order.getOrderStatus().equals("Đã tiếp nhận đơn hàng") || order.getOrderStatus().equals("Đang vận chuyển"))){
                        orders.add(order);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Không lấy được danh sách món", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_seller_confirm_order, container, false);
        // Inflate the layout for this fragment
        rcvOrder = view.findViewById(R.id.rcvSellerConfirmOrder);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvOrder.setLayoutManager(linearLayoutManager);
        orders = new ArrayList<>();
        adapter = new SellerNewAndConfirmOrderAdapter(orders, getContext());
        rcvOrder.setAdapter(adapter);
        getOrderFromRealtimeDatabase();
        return view;
    }
}