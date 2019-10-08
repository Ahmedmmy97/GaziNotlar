package a33y.jo.gazinotlar.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Listeners.FileListeners;
import a33y.jo.gazinotlar.Models.User;
import a33y.jo.gazinotlar.R;

/**
 * Created by ahmed on 23/8/2018.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserItemViewHolder> implements FileListeners{
    Context c;
    List<User> users;
    int type;
    public static final int CONV_TYPE =1;
    public static final int USER_TYPE =0;
    //DataListener dataListener;
   // CustomClickListener onClickListener;

    public UserListAdapter(Context c, List<User> users,int type) {
        this.c = c;
        this.users = users;
        this.type=type;
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==USER_TYPE) {
            View v = LayoutInflater.from(c).inflate(R.layout.user_item, parent, false);
            return new UserItemViewHolder(v, c);
        }else {
            View v = LayoutInflater.from(c).inflate(R.layout.conv_item, parent, false);
            return new UserItemViewHolder(v, c);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, final int position) {
          final User user = users.get(position);
        if(user.getProfileimage()==null){
            DataHelper.addFileListeners(this);
            if(user.isFacebook())
                new DataHelper.DownloadImageFromInternet(user,position).execute("https://graph.facebook.com/" + user.getFacebook_uid() + "/picture?height=400");
            else {
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        DataHelper.DownloadUsersProfile(c, user,position,true);
                    }
                }).start();

            }
        }

        holder.Assign_values(user);



    }

    @Override
    public int getItemCount() {

        return users!=null?users.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        if(type==USER_TYPE)
            return 0;
        else
            return 1;
    }

    @Override
    public void OnFileDownloaded() {

    }

    @Override
    public void OnFileDownloaded(int postion) {
        notifyItemChanged(postion);
    }

}
