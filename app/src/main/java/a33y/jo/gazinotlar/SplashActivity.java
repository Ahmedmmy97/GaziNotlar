package a33y.jo.gazinotlar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Listeners.UserListener;
import a33y.jo.gazinotlar.Models.User;


public class SplashActivity extends AppCompatActivity implements UserListener{
    private static int  splashTime = 5000;
    private SharedPreferences firstTime;
    public static Context c;
    ImageView logo ;
    ProgressBar loadingbar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth =FirebaseAuth.getInstance();
        setContentView(R.layout.splash_frag);
        c = SplashActivity.this;
        logo = findViewById(R.id.logo);
        loadingbar = findViewById(R.id.loading);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                try {
                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                } catch (Exception e) {

                }
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser!=null) {
                    DataHelper.addUserListener(SplashActivity.this);
                    DataHelper.readUser(getApplicationContext(), currentUser.getUid());
                }else {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }


            }
        },2000);

    }

           // finish();

    @Override
    public void OnUsersAdded() {
        // intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);


        // startActivity(intent);
    }

    @Override
    public void OnSearchFinished(List<User> users) {

    }

    @Override
    public void OnUserSent() {

    }

    @Override
    public void OnUserFound(User user) {

        if(user!=null){
            User.setCurrentUser(user);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }


}


