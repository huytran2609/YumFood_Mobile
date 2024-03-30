package com.example.yumfood.shipper.shipper_order.delivering;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.R;
import com.example.yumfood.models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveringFragment extends Fragment {

    private RecyclerView rcvDeliveringOrder;
    private List<Order> orders;
    private DeliveringAdapter adapter;

    private void initUi(ViewGroup view){
        rcvDeliveringOrder = view.findViewById(R.id.fragment_shipper_rcv_delivering_order);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvDeliveringOrder.setLayoutManager(linearLayoutManager);
        orders = new ArrayList<>();
        adapter = new DeliveringAdapter(orders, getContext());
        rcvDeliveringOrder.setAdapter(adapter);
    }

    private void getOrderFromDatabase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("orders");
        SharedPreferences prefs = getActivity().getSharedPreferences("Session", MODE_PRIVATE);
        String userId = prefs.getString("userId", "No name defined");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orders.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Order order = postSnapshot.getValue(Order.class);
                    if(order != null  && !order.getOrderStatus().equals("Giao hàng thành công") && !order.getOrderStatus().contains("Đã hủy")){
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_delivering, container, false);
        initUi(view);
        getOrderFromDatabase();
        return view;
    }
}