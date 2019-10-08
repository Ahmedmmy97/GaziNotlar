package a33y.jo.gazinotlar.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import a33y.jo.gazinotlar.Models.Category;
import a33y.jo.gazinotlar.R;

public class Adapter extends BaseAdapter {
    Context context;
    ViewHolder holder;
    public Adapter(Context context) {
        this.context = context;
    }

   /* @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_layout,viewGroup,false);
        return new ReviewViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder viewHolder, int i) {
           viewHolder.bind_data(Category.categories.get(i));
    }

    @Override
    public int getItemCount() {
        return Category.categories.size();
    }*/

    @Override
    public int getCount() {
        return Category.categories.size();
    }

    @Override
    public Object getItem(int i) {
        return Category.categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_layout, viewGroup, false);
            holder = new ViewHolder(view, context);
            //holder.bind_data(Category.categories.get(i));
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        holder.bind_data(Category.categories.get(i));
        return view;
    }
}
class ViewHolder extends RecyclerView.ViewHolder{

   ImageView icon;
   TextView title;
   LinearLayout container;
   Context c;
    public ViewHolder(@NonNull View itemView,Context c) {
        super(itemView);
        this.c= c;
        assign_views(itemView);
    }
    void assign_views(View v){
        icon = v.findViewById(R.id.thumbcat);
        title = v.findViewById(R.id.ctitle);
        container = v.findViewById(R.id.container);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)c).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        container.getLayoutParams().width =(int) (width/2);
        if(dm.densityDpi<240){
            container.getLayoutParams().width =(int) (width/3);
        }

    }
    public  void bind_data(Category category){
        icon.setImageResource(category.getIcon());
        title.setText(category.getTitle());
    }
}