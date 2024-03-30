package com.example.yumfood.seller.order_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.R;
import com.example.yumfood.R;
import com.example.yumfood.models.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SellerOrderDetailAdapter extends RecyclerView.Adapter<SellerOrderDetailAdapter.ProductForMerchantOrderDetailViewHolder>{
    private final List<CartItem> cart;
    private Context context;

    public SellerOrderDetailAdapter(List<CartItem> cart, Context context) {
        this.cart = cart;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductForMerchantOrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_for_order_detail,parent,false);
        return new SellerOrderDetailAdapter.ProductForMerchantOrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductForMerchantOrderDetailViewHolder holder, int position) {
        CartItem item= cart.get(position);
        if(item == null)
            return ;
        holder.tvProductName.setText(item.product.getProductName());
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String price = currencyVN.format(item.product.getPrice()).replace("₫", "")+ " ₫";
        holder.tvPrice.setText(price);
        holder.tvQuantity.setText("x"+item.quantity);
        String topping = item.getTopping();
        if(topping.equals(""))
        {
            holder.tvTopping.setVisibility(View.GONE);
        }
        else
        {
            holder.tvTopping.setText("+Topping: " + topping);
        }
    }

    @Override
    public int getItemCount() {
        if(cart != null)
            return cart.size();
        return 0;
    }

    public class ProductForMerchantOrderDetailViewHolder extends RecyclerView.ViewHolder{
        private TextView tvProductName, tvQuantity, tvPrice, tvTopping;

        public ProductForMerchantOrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.item_product_for_merchant_order_detail_tv_product_name);
            tvQuantity = itemView.findViewById(R.id.item_product_for_merchant_order_detail_tv_quantity);
            tvPrice = itemView.findViewById(R.id.item_product_for_merchant_order_detail_tv_unit_price);
            tvTopping = itemView.findViewById(R.id.item_product_for_merchant_order_detail_tv_topping);
        }
    }
}
