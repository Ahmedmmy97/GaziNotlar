package a33y.jo.gazinotlar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import a33y.jo.gazinotlar.Models.Review;

/**
 * Created by ahmed on 23/8/2018.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewViewHolder>  {
    Context c;
    List<Review> reviews;
    //DataListener dataListener;
   // CustomClickListener onClickListener;

    public ReviewListAdapter(Context c, List<Review> reviews) {
        this.c = c;
        this.reviews = reviews;

    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.review_item_layout,parent,false);
        return new ReviewViewHolder(v,c);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
             holder.Assign_values(reviews.get(position));

    }

    @Override
    public int getItemCount() {
        return reviews!=null?reviews.size():0;
    }


}
