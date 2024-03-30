package com.example.yumfood.seller.store_management.SellerManageMenu.topping;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.R;
import com.example.yumfood.models.Topping;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class ToppingFragment extends Fragment {
    private FloatingActionButton fabAddNewTopping;
    private RecyclerView rcvTopping;
    private ToppingAdapter adapter;
    private List<Topping> toppingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_topping, container, false);

        fabAddNewTopping = root.findViewById(R.id.fragment_topping_fab_add_new_topping);
        rcvTopping = root.findViewById(R.id.fragment_topping_rcv_topping);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvTopping.setLayoutManager(linearLayoutManager);

        toppingList = new ArrayList<>();
        adapter = new ToppingAdapter(toppingList, getContext());
        rcvTopping.setAdapter(adapter);

        getProductListFromRealtimeDatabase();

        return root;
    }

    private void getProductListFromRealtimeDatabase() {
        // Lấy mã cửa hàng
        SharedPreferences prefs = getContext().getSharedPreferences("Session", MODE_PRIVATE);
        String storeId = prefs.getString("storeId", "No name defined");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("stores").child(storeId).child("menu").child("topping");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toppingList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Topping topping = postSnapshot.getValue(Topping.class);
                    toppingList.add(topping);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabAddNewTopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getContext(), SellerAddToppingActivity.class);
                startActivity(switchActivityIntent);
            }
        });
    }
}

