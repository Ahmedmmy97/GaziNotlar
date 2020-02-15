package a33y.jo.gazinotlar;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Helpers.Helper;
import a33y.jo.gazinotlar.Listeners.UserListener;
import a33y.jo.gazinotlar.Models.Category;
import a33y.jo.gazinotlar.Models.User;

public class Login extends AppCompatActivity implements UserListener{
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private static final String TAG = "FacebookLogin";
    private CallbackManager mCallbackManager;
    LoginButton loginButton;
    Button customloginButton;
    Button emaillogin;
    String imagename;
    LinearLayout layout;
    private static boolean isFacebook=false;
    private static final int RC_SIGN_IN = 123;
    private static final int Email_SIGN_UP = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printKeyHash();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(null);
        }

        layout = findViewById(R.id.signupLayout);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        customloginButton = findViewById(R.id.buttonFacebookLoginvisible);
        customloginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

         loginButton = findViewById(R.id.buttonFacebookLogin);
        loginButton.setReadPermissions("email", "public_profile");
        emaillogin = findViewById(R.id.emaillogin);
        Category.categories.clear();
        //User.users.clear();
        emaillogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.PhoneBuilder().build()
                );

// Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);
            }
        });
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                AccessTokenTracker tokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        if (currentAccessToken == null) {
                            Log.d("FB", "User Logged Out.");
                            //Do your task here after logout
                            FirebaseAuth.getInstance().signOut();
                            updateUI(null);
                        }
                    }
                };
                tokenTracker.startTracking();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
      layout.animate().alpha(1.0f).setDuration(1500).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  super.onActivityResult(requestCode, resultCode, data);
              if(requestCode==RC_SIGN_IN){
                  IdpResponse response = IdpResponse.fromResultIntent(data);

                  if (resultCode == RESULT_OK) {

                      // Successfully signed in
                     final FirebaseUser user = mAuth.getCurrentUser();
                     if(user.getEmail()==null || user.getEmail().equals("") || user.getEmail() == "null") {

                         startActivityForResult(new Intent(this,EmailLogin.class),Email_SIGN_UP);
                     }else {
                         isFacebook = false;
                         DataHelper.addUserListener(Login.this);
                         DataHelper.readUser(getApplicationContext(),user.getUid());
                     }

                  }
              }else if(requestCode == Email_SIGN_UP) {
                  if (resultCode == RESULT_OK){
                      if(data.getBooleanExtra("state",false))
                          Snackbar.make(findViewById(R.id.maincontainer),"Failed to Register. Please Try Again Later",Snackbar.LENGTH_LONG).show();
                      else {
                          isFacebook = false;
                          FirebaseUser user = mAuth.getCurrentUser();
                          DataHelper.addUserListener(Login.this);
                          DataHelper.readUser(getApplicationContext(),user.getUid());
                          imagename = data.getStringExtra("imagename");
                      }

                  }else {
                      Snackbar.make(findViewById(R.id.maincontainer), "Failed to Register. Please Try Again Later", Snackbar.LENGTH_LONG).show();
                  }
                  }else {

                  // Pass the activity result back to the Facebook SDK
                  mCallbackManager.onActivityResult(requestCode, resultCode, data);
              }
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            isFacebook =true;
                            FirebaseUser user = mAuth.getCurrentUser();
                            DataHelper.addUserListener(Login.this);
                            DataHelper.readUser(getApplicationContext(),user.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                    }
                });
    }
    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("a33y.jo.gazinotlar", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }
    private void updateUI(FirebaseUser user) {

         if (user != null) {
             if(user.getEmail()==null || user.getEmail().equals("") || user.getEmail() == "null"){

             }else {

                 Snackbar.make(findViewById(R.id.maincontainer), "Welcome : " + user.getDisplayName(), Snackbar.LENGTH_LONG).show();
                 Intent intent = new Intent(this, MainActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(intent);
                 finish();
             }
        } else  {
             Snackbar.make(findViewById(R.id.maincontainer),"Signed out",Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
         //   updateUI(currentUser);
    }

    @Override
    public void OnUsersAdded() {

    }

    @Override
    public void OnSearchFinished(List<User> users) {

    }

    @Override
    public void OnUserSent() {
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    @Override
    public void OnUserFound(User user) {
        FirebaseUser fuser = mAuth.getCurrentUser();
        if(user==null){
            User Localuser;
            if(isFacebook) {
                Localuser = new User(fuser.getUid(), fuser.getDisplayName(), fuser.getEmail(), true, Helper.getFacebookProviderId(fuser));
                User.setCurrentUser(Localuser);
                DataHelper.sendUser(getApplicationContext(), Localuser);

            }else{
                Localuser = new User(fuser.getUid(), fuser.getDisplayName(), fuser.getEmail(), false,null);
                Localuser.setImagename(imagename);
                User.setCurrentUser(Localuser);
                DataHelper.sendUser(getApplicationContext(), Localuser);
            }

        }else {
            User.setCurrentUser(user);
            updateUI(fuser);
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }

}
