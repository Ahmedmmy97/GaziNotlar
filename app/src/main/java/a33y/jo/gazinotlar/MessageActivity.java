package a33y.jo.gazinotlar;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import a33y.jo.gazinotlar.Adapters.ChatAdapter;
import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Helpers.Helper;
import a33y.jo.gazinotlar.Listeners.ChatListeners;
import a33y.jo.gazinotlar.Models.Chat;
import a33y.jo.gazinotlar.Models.User;

public class MessageActivity extends AppCompatActivity implements ChatListeners{
     ImageView profileImage;
     TextView username;
     FirebaseUser fuser;
     User user;
     ImageButton send;
     EditText message;
     RecyclerView recyclerView;
     LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        getViews();
        assignValues();

    }

    private void getViews(){
        profileImage = findViewById(R.id.profileimage);
        username =findViewById(R.id.username);
        send =  findViewById(R.id.sendbtn);
        message = findViewById(R.id.text);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void assignValues(){
        user = User.getUser(getIntent().getStringExtra("uid"));
        DataHelper.readMessages(user,this);
        username.setText(user.getUsername());
        profileImage.setImageBitmap(user.getProfileimage());
        send.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               String msg = message.getText().toString();
               if(!msg.equals("")){
                   Chat chat = new Chat(fuser.getUid(),user.getUid(),msg);

                   DataHelper.sendMessage(chat,MessageActivity.this);
               }else {
                   Snackbar.make(findViewById(android.R.id.content),"Cannot send empty message",Snackbar.LENGTH_SHORT).show();
               }
           }
       });
    }

    @Override
    public void OnMessageSent(boolean isSuccessful) {
          message.setText("");
          hideKeyboard();
          message.clearFocus();
    }

    @Override
    protected void onDestroy() {
        DataHelper.removeChatListener();
        super.onDestroy();
    }

    @Override
    public void OnMessagesReceived(boolean isSuccessful) {
        if(isSuccessful) {
            ChatAdapter adapter = new ChatAdapter(this, Chat.getChats(), user);
            recyclerView.setAdapter(adapter);
            Helper.makeMessageSeen(Chat.getChats(),user);
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    protected void onResume() {
        super.onResume();
        DataHelper.updateStatus("online");
    }


}
