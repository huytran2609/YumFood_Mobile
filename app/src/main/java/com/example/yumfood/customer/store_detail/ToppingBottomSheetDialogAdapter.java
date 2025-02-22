package com.example.yumfood.customer.store_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.R;
import com.example.yumfood.models.Topping;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ToppingBottomSheetDialogAdapter extends RecyclerView.Adapter<ToppingBottomSheetDialogAdapter.ToppingBottomSheetDialogViewHolder>{
    public final List<Topping> list;
    private Context context;

    public ToppingBottomSheetDialogAdapter(List<Topping> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ToppingBottomSheetDialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topping_for_topping_bottom_dialog,parent,false);
        return new  ToppingBottomSheetDialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingBottomSheetDialogViewHolder holder, int position) {
        Topping topping = list.get(position);
        if(topping == null)
            return ;
        holder.tvToppingName.setText(topping.getToppingName());
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String price = currencyVN.format(topping.getToppingPrice()).replace("₫", "")+ " ₫";
        holder.tvPrice.setText(price);
        if (context instanceof StoreDetailActivity) {
            ((StoreDetailActivity)context).getToppingBottomSheetDialog().visibleTvAddTopping();
        }
        holder.ckb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof StoreDetailActivity) {
                    if(holder.ckb.isChecked())
                    {
                        ((StoreDetailActivity)context).getToppingBottomSheetDialog().updatePriceWhenAddTopping(topping.getToppingPrice());
                        ((StoreDetailActivity)context).getToppingBottomSheetDialog().addToppingString(topping.getToppingName());

                    }
                    else
                    {
                        ((StoreDetailActivity)context).getToppingBottomSheetDialog().updatePriceWhenAddTopping(topping.getToppingPrice() * -1);
                        ((StoreDetailActivity)context).getToppingBottomSheetDialog().removeToppingString(topping.getToppingName());
                    }
                }
            }
        });
    }
//    public void addToppingString(String toppingName)
//    {
//        SharedPreferences prefs = context.getSharedPreferences("Session", MODE_PRIVATE);
//        SharedPreferences.Editor editor = context.getSharedPreferences("Session", MODE_PRIVATE).edit();
//        String topping = prefs.getString("topping", "No name defined");
//        if(topping.isEmpty() || topping.equals("No name defined"))
//        {
//            Log.e("1", "emty" + toppingName);
//            editor.putString("topping", toppingName);
//        }
//        else
//        {
//            Log.e("2", "notemty" + toppingName);
//            editor.putString("topping", topping + ", " + toppingName);
//        }
//        editor.apply();
//    }
//
//    public void removeToppingString(String toppingName)
//    {
//        SharedPreferences prefs = context.getSharedPreferences("Session", MODE_PRIVATE);
//        SharedPreferences.Editor editor = context.getSharedPreferences("Session", MODE_PRIVATE).edit();
//        String topping = prefs.getString("topping", "No name defined");
//        String newString = topping.replace(", " + toppingName, "").replace(toppingName, "");
//        editor.putString("topping", newString);
//        editor.apply();
//    }
    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }

    public class ToppingBottomSheetDialogViewHolder extends RecyclerView.ViewHolder{
        private TextView tvToppingName, tvPrice;
        private CheckBox ckb;

        public ToppingBottomSheetDialogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvToppingName = itemView.findViewById(R.id.item_topping_for_topping_bottom_dialog_tv_topping_name);
            tvPrice = itemView.findViewById(R.id.item_topping_for_topping_bottom_dialog_tv_price);
            ckb = itemView.findViewById(R.id.item_topping_for_topping_bottom_dialog_ckb_is_include);
        }
    }
}
