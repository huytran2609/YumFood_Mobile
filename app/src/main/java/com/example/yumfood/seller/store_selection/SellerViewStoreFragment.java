package com.example.yumfood.seller.store_selection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.yumfood.R;
import com.example.yumfood.WelcomeActivity;
import com.example.yumfood.models.Store;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SellerViewStoreFragment extends Fragment {
    private Button btnAddNewStore;
    private List<Store> storeList;
    private StoreAdapter storeAdapter;
    private RecyclerView rcvStores;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_seller_view_store, container, false);
        rcvStores = view.findViewById(R.id.rcvStore);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvStores.setLayoutManager(linearLayoutManager);
        storeList = new ArrayList<>();
        storeAdapter = new StoreAdapter(getContext(), storeList);
        rcvStores.setAdapter(storeAdapter);
        getStoreListFromRealtimeDatabase();
        btnAddNewStore = view.findViewById(R.id.buttonAddNewStore);
        btnAddNewStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SellerViewStoreFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        // Inflate the layout for this fragment
        return view;

    }

    private void getStoreListFromRealtimeDatabase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("stores");
        SharedPreferences preferences = getContext().getSharedPreferences("Session", getContext().MODE_PRIVATE);
        String owner = preferences.getString("userId", "default value");
        Toast.makeText(getContext(), owner,
                Toast.LENGTH_SHORT).show();
        myRef.orderByChild("owner").equalTo(owner).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storeList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Store store = postSnapshot.getValue(Store.class);
                    if(store != null){
                        storeList.add(store);
                    }
                    storeAdapter.notifyDataSetChanged();
                }
                storeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Không lấy được danh sách", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
}