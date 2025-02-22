
package com.example.yumfood.seller.store_management.SellerManageMenu.product_grouping;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SellerAddProductGroupingActivity extends AppCompatActivity {
    private Button btnAddNewProductGrouping;
    private ImageView ivBtnBack;
    private RecyclerView rcvProductGroupingList;
    private EditText etEnterNewProductGroup;
    private List<String> productGroupingList;
    private Seller_ProductGroupingAdapter adapter;

    private void initUi()
    {
        ivBtnBack = (ImageView) findViewById(R.id.activity_product_grouping_ib_back);
        btnAddNewProductGrouping = (Button) findViewById(R.id.activity_product_grouping_btn_add_product_grouping);
        etEnterNewProductGroup = (EditText)  findViewById(R.id.activity_product_grouping_et_product_grouping);
        rcvProductGroupingList = (RecyclerView) findViewById(R.id.activity_product_grouping_rcv_product_grouping_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvProductGroupingList.setLayoutManager(linearLayoutManager);

        productGroupingList = new ArrayList<>();
        adapter = new Seller_ProductGroupingAdapter( productGroupingList, this);
        rcvProductGroupingList.setAdapter(adapter);
    }
    private void getProductListFromRealtimeDatabase()
    {
        SharedPreferences prefs = this.getSharedPreferences("Session", MODE_PRIVATE);
        String storeId = prefs.getString("storeId", "No name defined");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("stores").child(storeId).child("productGrouping");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productGroupingList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String productGrouping = postSnapshot.getValue(String.class);
                    productGroupingList.add(productGrouping);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SellerAddProductGroupingActivity.this, "Không lấy được danh sách nhóm món", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product_grouping);
        initUi();
        getProductListFromRealtimeDatabase();
        ivBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddNewProductGrouping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newProductGrouping = etEnterNewProductGroup.getText().toString();
                if(!newProductGrouping.isEmpty())
                {
                    SharedPreferences prefs = SellerAddProductGroupingActivity.this.getSharedPreferences("Session", MODE_PRIVATE);
                    String storeId = prefs.getString("storeId", "No name defined");

                    productGroupingList.add(newProductGrouping);
                    YumFoodDatabase database = new YumFoodDatabase();
                    database.updateProductGroupingForStore(storeId, productGroupingList);

                    etEnterNewProductGroup.getText().clear();
                    Toast.makeText(SellerAddProductGroupingActivity.this, "Thêm nhóm món thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}