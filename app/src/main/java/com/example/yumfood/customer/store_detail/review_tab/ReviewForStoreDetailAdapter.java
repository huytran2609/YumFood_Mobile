package com.example.yumfood.customer.store_detail.review_tab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfood.R;
import com.example.yumfood.models.Review;

import java.util.List;


public class ReviewForStoreDetailAdapter extends RecyclerView.Adapter<ReviewForStoreDetailAdapter.ReviewForStoreDetailViewHolder>{
    private final List<Review> reviewList;
    private Context context;

    public ReviewForStoreDetailAdapter(List<Review> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewForStoreDetailAdapter.ReviewForStoreDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewForStoreDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cus_shoppage_review_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewForStoreDetailAdapter.ReviewForStoreDetailViewHolder holder, int position) {
        Review review = reviewList.get(position);
        if(review == null)
            return ;
        holder.tvUserNameCmt.setText(review.getUser_cmt_name());
        holder.tvComment.setText(review.getCmt());
        holder.tvCommentDate.setText(review.getCmt_date());
        holder.reviewStoreRatingBar.setRating(review.getRating());
    }

    @Override
    public int getItemCount() {
        if(reviewList != null)
            return reviewList.size();
        return 0;
    }

    public class ReviewForStoreDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView tvComment, tvCommentDate, tvUserNameCmt;
        private RatingBar reviewStoreRatingBar;

        public ReviewForStoreDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserNameCmt = (TextView) itemView.findViewById(R.id.cus_shoppage_review_item_username);
            tvComment = (TextView) itemView.findViewById(R.id.cus_shoppage_review_item_comment);
            tvCommentDate = (TextView) itemView.findViewById(R.id.cus_shoppage_review_item_comment_time);
            reviewStoreRatingBar = (RatingBar) itemView.findViewById(R.id.fragement_cus_shoppage_review_item_rating_bar);
        }
    }
}
