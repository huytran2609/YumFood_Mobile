package com.example.yumfood.customer.favourite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yumfood.R;
import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.models.Store;

import java.util.List;


public class CustomerFavouriteAdapter extends RecyclerView.Adapter<CustomerFavouriteAdapter.CustomerFavouriteViewHolder>{
    private final List<Store> storeList;
    private Context context;
    private YumFoodDatabase yumFoodDatabase = new YumFoodDatabase();

    public CustomerFavouriteAdapter(List<Store> storeList, Context context) {
        this.storeList = storeList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerFavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_customer_favourite, parent,false);
        return new CustomerFavouriteAdapter.CustomerFavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerFavouriteViewHolder holder, int position) {
        Store store = storeList.get(position);
        if(store == null)
            return ;
        holder.tvStoreName.setText(store.getStoreName());
        if(!store.getAvatar().isEmpty())
        {
            Glide.with(context).load(store.getAvatar()).into(holder.ivStoreAvatar);
        }

    }

    @Override
    public int getItemCount() {
        if(storeList != null)
            return storeList.size();
        return 0;
    }


    public class CustomerFavouriteViewHolder  extends RecyclerView.ViewHolder{
        private TextView tvStoreName;
        private ImageView ivStoreAvatar;

        public CustomerFavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStoreName = (TextView) itemView.findViewById(R.id.layout_store_for_customer_favourite_tv_store_name);
            ivStoreAvatar = (ImageView) itemView.findViewById(R.id.layout_store_for_customer_favourite_iv_store_avatar);
        }
    }
}
