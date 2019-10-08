package a33y.jo.gazinotlar.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Helpers.Helper;
import a33y.jo.gazinotlar.MessageActivity;
import a33y.jo.gazinotlar.Models.User;
import a33y.jo.gazinotlar.R;

/**
 * Created by ahmed on 23/8/2018.
 */

public class UserItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    TextView username;
    ImageView profile;
    ProgressBar loading;
    Context c;
    User user;
    TextView lastmessage;
    TextView newmessages;
    ImageView delete;
    ImageView status;
    public UserItemViewHolder(View itemView, Context c) {
        super(itemView);
        Bind_Views(itemView);
        this.c=c;

    }


    private void Bind_Views(View view){
        username = view.findViewById(R.id.username);
        profile = view.findViewById(R.id.profileimage);
        loading =view.findViewById(R.id.loading);
        lastmessage= view.findViewById(R.id.message);
        newmessages = view.findViewById(R.id.newmessages);
        delete = view.findViewById(R.id.deleteconv);
        status = view.findViewById(R.id.status);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
    }

    public void Assign_values(final User user){
      username.setText(user.getUsername());
      profile.setImageBitmap(user.getProfileimage());
      this.user =user;
      if(user.getProfileimage()!=null)
          loading.setVisibility(View.GONE);
     if(lastmessage!=null) {
         lastmessage.setText(Helper.getLastMessage(user));
         int newmsg = Helper.getNewMessages(user);
         if(newmsg>0) {
             newmessages.setText(String.valueOf(newmsg));
             newmessages.setVisibility(View.VISIBLE);
         }else {
             newmessages.setVisibility(View.GONE);
         }
         delete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
                 dialogBuilder.setTitle("Are You sure?!");
                 dialogBuilder.setMessage("Do you want to delete this conversation?");
                 dialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                         DataHelper.deleteChat(Helper.fetchChatList(user));
                     }
                 });
                 dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                     }
                 });
                 dialogBuilder.create().show();

             }
         });
         if(user.getStatus()!=null && user.getStatus().equals("online"))
             status.setImageResource(R.drawable.greenicon);
         else
             status.setImageResource(R.drawable.redicon);
     }
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(c, MessageActivity.class);
        intent.putExtra("uid",user.getUid());
        c.startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {

        return true;
    }
}
