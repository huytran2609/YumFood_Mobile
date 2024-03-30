package com.example.yumfood.customer.home.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yumfood.R;
import com.example.yumfood.models.Home_CategoriesModel;

import java.util.List;

public class CategoriesItemAdapter extends RecyclerView.Adapter<CategoriesItemAdapter.HomeCategoriesViewHolder>{

    private Context context;
    private List<Home_CategoriesModel> li_popular_prods;
    private HomeFragment homeFragment;
    public CategoriesItemAdapter(Context context, List<Home_CategoriesModel> li_popular_prods, HomeFragment homeFragment){
        this.context = context;
        this.li_popular_prods = li_popular_prods;
        this.homeFragment = homeFragment;
    }


    @NonNull
    @Override
    public HomeCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeCategoriesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categories_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCategoriesViewHolder holder, int position) {
        Glide.with(context).load(li_popular_prods.get(position).getImg_url()).into(holder.catImg);
        String categoryName = li_popular_prods.get(position).getName();
        holder.catName.setText(categoryName);
        holder.clParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categoryName.equals("Tất cả"))
                {
                    homeFragment.getStoreListByCategoryFromRealtimeDatabase();
                }
                else
                {
                    homeFragment.getStoreListByCategoryFromRealtimeDatabase(categoryName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return li_popular_prods.size();
    }

    public class HomeCategoriesViewHolder extends RecyclerView.ViewHolder {
        ImageView catImg;
        TextView catName;
        CardView clParent;

        public HomeCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg = itemView.findViewById(R.id.img_cate);
            catName = itemView.findViewById(R.id.txt_cate_name);
            clParent = itemView.findViewById(R.id.cv_categories_order);
        }
    }
}
