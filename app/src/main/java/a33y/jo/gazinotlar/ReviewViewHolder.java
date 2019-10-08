package a33y.jo.gazinotlar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Date;

import a33y.jo.gazinotlar.Helpers.Helper;
import a33y.jo.gazinotlar.Models.Review;

/**
 * Created by ahmed on 23/8/2018.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    TextView time;
    TextView publisher;
    TextView section;
    TextView comment;
    ImageView profile;
    RatingBar ratingBar;
    Context c;

    public ReviewViewHolder(View itemView, Context c) {
        super(itemView);
        Bind_Views(itemView);
        this.c=c;

    }


    private void Bind_Views(View view){
        time = view.findViewById(R.id.time);
        publisher = view.findViewById(R.id.publisher);
        section = view.findViewById(R.id.section);
        comment = view.findViewById(R.id.comment);
        profile = view.findViewById(R.id.profile);
        ratingBar = view.findViewById(R.id.ratingbar);
    }
    public void Assign_values(Review review){
       publisher.setText(review.getPublisher());
       section.setText(review.getSection());
       comment.setText(review.getComment());
       ratingBar.setRating(review.getRating());
       time.setText(getTime(new Date(),review.getDate()));
    }
    private String getTime(Date date1, Date date2){
        String time="";
        if(Helper.getYears(date1,date2)!=0){
             time += Helper.getYears(date1,date2) + "Y";
        }else if(Helper.getMonths(date1,date2)!=0){
            time += Helper.getMonths(date1,date2) + "M";
        }else if(Helper.getDays(date1,date2)!=0){
            time += Helper.getDays(date1,date2) + " Days";
        }else if(Helper.getHours(date1,date2)!=0){
            time += Helper.getHours(date1,date2) + "hr";
        }else if(Helper.getMinutes(date1,date2)!=0){
            time += Helper.getMinutes(date1,date2) + "min";
        }else if(Helper.getSeconds(date1,date2)!=0){
            time += Helper.getSeconds(date1,date2) + "sec";
        }

        time+=" ago";
        return  time;
    }


}
