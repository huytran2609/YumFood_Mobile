package com.example.yumfood.customer.home.myorder.orderconfirmation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yumfood.YumFoodDatabase;
import com.example.yumfood.R;
import com.example.yumfood.models.CartItem;
import com.example.yumfood.models.CartSession;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartItemForOrderConfirmationAdapter extends RecyclerView.Adapter<CartItemForOrderConfirmationAdapter.CartItemForOrderConfirmationViewHolder>{

    private final List<CartItem> cart;
    private CartSession cartSession;
    private Context context;
    private YumFoodDatabase YumFoodDB = new YumFoodDatabase();;

    public CartItemForOrderConfirmationAdapter(List<CartItem> cart, Context context) {
        this.cart = cart;
        this.context = context;
        cartSession = new CartSession(context);
    }

    @NonNull
    @Override
    public CartItemForOrderConfirmationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_item_for_order_confirmation, parent,false);
        return new CartItemForOrderConfirmationAdapter.CartItemForOrderConfirmationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemForOrderConfirmationViewHolder holder, int position) {
        CartItem cartItem = cart.get(position);
        if(cartItem == null)
            return ;
        holder.tvProductName.setText(cartItem.product.getProductName());
        holder.tvQuantity.setText("SL: x "+ cartItem.quantity);
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String priceAfterFormat = currencyVN.format(cartItem.product.getPrice()).replace("₫", "")+ " ₫";
        holder.tvPrice.setText(priceAfterFormat);
        String topping = cartItem.getTopping();
        if(topping.equals(""))
        {
            holder.tvTopping.setVisibility(View.GONE);
        }
        else
        {
            holder.tvTopping.setText(topping);
        }
        if(!cartItem.product.getProductImage().isEmpty())
        {
            Glide.with(context).load(cartItem.product.getProductImage()).into(holder.ivProductImage);
//            goFoodDatabase.loadImageToImageView(holder.ivProductImage, "product_image" , cartItem.product.getProductImage());
        }
    }

    @Override
    public int getItemCount() {
        if(cart != null)
            return cart.size();
        return 0;
    }

    public class CartItemForOrderConfirmationViewHolder  extends RecyclerView.ViewHolder{
        private TextView tvProductName, tvPrice, tvQuantity, tvTopping;
        private ImageView ivProductImage;
        private ConstraintLayout clProductItem;

        public CartItemForOrderConfirmationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.item_cart_item_for_order_confirmation_tv_product_name);
            tvPrice = itemView.findViewById(R.id.item_cart_item_for_order_confirmation_tv_unit_price);
            ivProductImage = itemView.findViewById(R.id.item_cart_item_for_order_confirmation_iv_product_image);
            clProductItem = itemView.findViewById(R.id.item_cart_item_for_order_confirmation_cl_product_item);
            tvQuantity = itemView.findViewById(R.id.item_cart_item_for_order_confirmation_tv_quantity);
            tvTopping =  itemView.findViewById(R.id.item_cart_item_for_order_confirmation_tv_topping);
        }
    }
}
