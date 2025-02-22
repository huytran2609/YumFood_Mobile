package com.example.yumfood.customer.home.homepage;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.LoginActivity;
import com.example.yumfood.R;
import com.example.yumfood.customer.home.HomeActivity;
import com.example.yumfood.customer.home.MapActivity;
import com.example.yumfood.models.Home_CategoriesModel;
import com.example.yumfood.models.Home_RecommendedModel;
import com.example.yumfood.models.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
//    private TextView tvLocation;
//    DatabaseReference realtimedbRef;
    ArrayAdapter adapterSearch;
    ArrayList<String> listSearch;
    AutoCompleteTextView autoCompleteTextViewSearch;
    FirebaseFirestore fire_store = FirebaseFirestore.getInstance();
    RecyclerView categories_order_RecyclerView;
    CategoriesItemAdapter categories_order_Adapter;
    List<Home_CategoriesModel> categories_order_ModelList;

    RecyclerView recommended_order_RecyclerView;
    RecommendedAdapter recommended_order_Adapter;
    List<Home_RecommendedModel> recommended_order_ModelList;
    private TextView viTri;
    private RecyclerView rcvStoreListByCategory;
    private List<Store> storeListByCategory;
    private ListStoreAdapter listStoreAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        initUi(root);
//        SharedPreferences preferences = getContext().getSharedPreferences("Session", getContext().MODE_PRIVATE);
//        String owner = preferences.getString("userId", "Default");
//        Toast.makeText(getContext(), owner,
//                Toast.LENGTH_SHORT).show();
        autoCompleteTextViewSearch = (AutoCompleteTextView) root.findViewById(R.id.autoSearch);
        listSearch = new ArrayList<>();
        listSearch.add("Trà sữa");
        listSearch.add("Tra sua");
        listSearch.add("Bánh Ngọt");
        listSearch.add("Banh Ngot");
        listSearch.add("Bánh");
        listSearch.add("Trà");
        listSearch.add("Tra");
        listSearch.add("Do an");
        listSearch.add("Đồ ăn");
        listSearch.add("Banh man");
        listSearch.add("Bánh mặn");
        listSearch.add("Ca phe");
        listSearch.add("Cà phê");
        listSearch.add("Nuoc ngot");
        listSearch.add("Nước ngọt");
        adapterSearch = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listSearch);
        autoCompleteTextViewSearch.setAdapter(adapterSearch);
        autoCompleteTextViewSearch.setThreshold(1);
        autoCompleteTextViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy item đã chọn
                String selectedItem = (String) parent.getItemAtPosition(position);
//                Toast.makeText(getContext(), selectedItem, Toast.LENGTH_SHORT).show();

                // Xử lý sự kiện khi người dùng chọn item
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("stores");

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        storeListByCategory.clear();
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            Store store = postSnapshot.getValue(Store.class);
                            if(store.getStoreCategory().contains(selectedItem))
                            {
                                storeListByCategory.add(store);
                            }
                        }
                        listStoreAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Không lấy được danh sách món", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        // categories order
        getRecommendedOrder(root);
        getCategoriesOrder(root);
        getStoreListByCategoryFromRealtimeDatabase();
        return root;
    }

    private void initUi(ViewGroup root)
    {

        viTri = (TextView) root.findViewById(R.id.txt_vitri);
//        CartSession cartSession = new CartSession(getActivity());
//        cartSession.removeAllItem();
//        tvLocation = (TextView) root.findViewById(R.id.tvLocation);
        viTri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });
        rcvStoreListByCategory = (RecyclerView) root.findViewById(R.id.rec_storelist_by_category);
        storeListByCategory = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rcvStoreListByCategory.setLayoutManager(linearLayoutManager);
        listStoreAdapter = new ListStoreAdapter(storeListByCategory, getContext());
        rcvStoreListByCategory.setAdapter(listStoreAdapter);
    }

    public void getStoreListByCategoryFromRealtimeDatabase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("stores");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Store store = postSnapshot.getValue(Store.class);
                    storeListByCategory.add(store);
                }
                listStoreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Không lấy được danh sách món", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getStoreListByCategoryFromRealtimeDatabase(String category)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("stores");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storeListByCategory.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Store store = postSnapshot.getValue(Store.class);
                    if(store.getStoreCategory().contains(category))
                    {
                        storeListByCategory.add(store);
                    }
                }
                listStoreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Không lấy được danh sách món", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCategoriesOrder(ViewGroup view){
        categories_order_RecyclerView = view.findViewById(R.id.rec_categories_order);
        categories_order_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categories_order_ModelList = new ArrayList<>();
        categories_order_Adapter = new CategoriesItemAdapter(getActivity(), categories_order_ModelList, this);
        categories_order_RecyclerView.setAdapter(categories_order_Adapter);

        fire_store.collection("Categories_Order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                Home_CategoriesModel categories_order = document.toObject(Home_CategoriesModel.class);
                                categories_order_ModelList.add(categories_order);
                                categories_order_Adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void getRecommendedOrder(ViewGroup view){
        recommended_order_RecyclerView = view.findViewById(R.id.rec_sale);
        recommended_order_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recommended_order_ModelList = new ArrayList<>();
        recommended_order_Adapter = new RecommendedAdapter(getActivity(), recommended_order_ModelList);
        recommended_order_RecyclerView.setAdapter(recommended_order_Adapter);

        fire_store.collection("Sales_Order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                Home_RecommendedModel recommended_order = document.toObject(Home_RecommendedModel.class);
                                recommended_order_ModelList.add(recommended_order);
                                recommended_order_Adapter.notifyDataSetChanged();
                            }
                        }
                    }

                });
    }


//    private void getLocation(){
//        realtimedbRef = FirebaseDatabase.getInstance().getReference("Users");
//        realtimedbRef.child(LoginTabFragment.UID).child("cur_location").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String cur_location = snapshot.getValue(String.class);
//                tvLocation.setText(cur_location);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });
//    }
}
