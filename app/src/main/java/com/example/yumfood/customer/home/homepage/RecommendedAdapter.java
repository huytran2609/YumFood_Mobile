package com.example.yumfood.customer.home.homepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yumfood.LoginActivity;
import com.example.yumfood.R;
import com.example.yumfood.customer.product_detail.ProductDetailActivity;
import com.example.yumfood.models.Home_RecommendedModel;
import com.example.yumfood.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder>{
    private Context context;
    private List<Home_RecommendedModel> recommendedList;

    public RecommendedAdapter(Context context, List<Home_RecommendedModel> recommendedList){
        this.context = context;
        this.recommendedList = recommendedList;
    }

    @NonNull
    @Override
    public RecommendedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecommendedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_catesale_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedViewHolder holder, int position) {
        Home_RecommendedModel recommended = recommendedList.get(position);
        Glide.with(context).load(recommended.getImg_url()).into(holder.recommendedImage);
        holder.recommendedName.setText(recommended.getName());
        holder.recommendedDeliveryTime.setText(recommended.getDeliveryTime());
        holder.recommendedPrice.setText(recommended.getPrice());
        int newPrice = Integer.valueOf(recommended.getPrice().replace(".","").replace(" ₫",""));
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String priceAfterFormat = currencyVN.format(newPrice * 1.5).replace("₫", "")+ " ₫";
        holder.recommendedPrice2.setText(priceAfterFormat);
        holder.recommendedPrice2.setPaintFlags(holder.recommendedPrice2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.clSaleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String store_ID = recommended.getStore_ID();
                String product_ID = recommended.getProduct_ID();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("stores").child(store_ID).child("menu").child("products").child(product_ID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Product product_info = dataSnapshot.getValue(Product.class);
                        Intent switchActivityIntent = new Intent(view.getContext(), ProductDetailActivity.class);
                        switchActivityIntent.putExtra("product", product_info);
                        view.getContext().startActivity(switchActivityIntent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(view.getContext(), "Không lấy được thông tin sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendedList.size();
    }

    public class RecommendedViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout clSaleOrder;
        ImageView recommendedImage;

        TextView recommendedName, recommendedDeliveryTime, recommendedPrice, recommendedPrice2;

        public RecommendedViewHolder(@NonNull View itemView) {
            super(itemView);
            recommendedImage = itemView.findViewById(R.id.sale_image);
            recommendedName = itemView.findViewById(R.id.sale_name);
            recommendedDeliveryTime = itemView.findViewById(R.id.sale_delivery);
            recommendedPrice = itemView.findViewById(R.id.sale_price);
            recommendedPrice2 = itemView.findViewById(R.id.sale_price2);
            clSaleOrder = itemView.findViewById(R.id.cl_sale_order);
        }
    }
}
