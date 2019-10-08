package a33y.jo.gazinotlar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import a33y.jo.gazinotlar.Helpers.Helper;
import a33y.jo.gazinotlar.LessonActivity;
import a33y.jo.gazinotlar.Models.Chat;
import a33y.jo.gazinotlar.Models.Note;
import a33y.jo.gazinotlar.Models.User;
import a33y.jo.gazinotlar.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context context;
    private List<Chat> chats ;
    private final static int MSG_TYPE_LEFT =0;
    private final static int MSG_TYPE_RIGHT =1;
    private User user;
    private Chat seenMessage;
    public ChatAdapter(Context context, List<Chat> chats, User user) {
        this.context = context;
        this.chats = chats;
        this.user = user;
        seenMessage = Helper.CheckIfSeen(chats,User.getCurrentUser());
    }

    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        if(itemType==MSG_TYPE_LEFT) {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_left, viewGroup, false);
            return new ChatViewHolder(v,context);
        }
        else{
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_right, viewGroup, false);
            return new ChatViewHolder(v,context);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder viewHolder, int i) {
           viewHolder.bind_data(chats.get(i));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
         if(chats.get(position).getSender().equals(fuser.getUid()))
             return MSG_TYPE_RIGHT;
         else
             return MSG_TYPE_LEFT;

    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

   ImageView profile;
   TextView message;
   TextView seen;
   Context c;
    public ChatViewHolder(@NonNull View itemView,Context c) {
        super(itemView);
        this.c= c;
        assign_views(itemView);

    }
    void assign_views(View v){
        profile = v.findViewById(R.id.profileimage);
        message = v.findViewById(R.id.showmessage);
        seen = v.findViewById(R.id.seen);
    }
    public  void bind_data(Chat chat){
          profile.setImageBitmap(user.getProfileimage());
          message.setText(chat.getMessage());
          if(seen !=null && seenMessage!=null)
          if(seenMessage.getId().equals(chat.getId())){
              seen.setVisibility(View.VISIBLE);
          }else {
              seen.setVisibility(View.GONE);
          }
    }


}
}