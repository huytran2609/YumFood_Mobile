package com.example.yumfood.seller.store_management.SellerManageMenu.topping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.R;
import com.example.yumfood.models.Product;

import java.util.List;

public class ProductForAddToppingAdapter extends RecyclerView.Adapter<ProductForAddToppingAdapter.ProductForAddToppingViewHolder>{
    private final List<Product> list;
    private Context context;

    public ProductForAddToppingAdapter(List<Product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductForAddToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller_product_add_topping,parent,false);
        return new ProductForAddToppingAdapter.ProductForAddToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductForAddToppingViewHolder holder, int position) {
        Product product = list.get(position);
        if(product == null)
            return ;
        holder.tvName.setText(product.getProductName());
        holder.ckbIsApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof SellerAddToppingActivity) {
                    if(holder.ckbIsApply.isChecked())
                        ((SellerAddToppingActivity)context).addItemToApplyProductToList(product.getProductName());
                    else
                        ((SellerAddToppingActivity)context).removeItemToApplyProductToList(product.getProductName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }

    public class ProductForAddToppingViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private CheckBox ckbIsApply;

        public ProductForAddToppingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_product_for_add_topping_tv_product_name);
            ckbIsApply = itemView.findViewById(R.id.item_product_for_add_topping_ckb_is_include);
        }
    }
}

