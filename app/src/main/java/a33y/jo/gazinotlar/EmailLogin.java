package a33y.jo.gazinotlar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Helpers.FileHelper;
import a33y.jo.gazinotlar.Helpers.Helper;

public class EmailLogin extends AppCompatActivity{
    private static final int RESULT_LOAD_IMAGE = 1;
    private final int RESULT_CROP = 400;
    EditText email;
    EditText username;
    EditText password;
    Button signup;
    boolean failed;
    ImageView imageView;
    File imageFile;
    Intent data = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        get_views();
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DataHelper.sendUserImage(getApplicationContext(),imageFile,FirebaseAuth.getInstance().getCurrentUser().getUid());
                update_Email(FirebaseAuth.getInstance().getCurrentUser());
            }
        });
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(EmailLogin.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);

            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length >0&&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CROP ) {
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                imageView.setImageBitmap(selectedBitmap);

            }
        }else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null !=data){
            if(data.getClipData()!=null) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    imageFile = new File(FileHelper.getPath(this, uri));

            }
            }else{
                Uri uri = data.getData();
                imageFile  = new File(FileHelper.getPath(this, uri));

            }

            if (imageFile.exists()){
                Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                try {
                    imageBitmap = Helper.modifyOrientation(imageBitmap, imageFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageBitmap = Helper.getResizedBitmap(imageBitmap,500,500);
                imageView.setImageBitmap(imageBitmap);
                OutputStream outStream = null;
                imageFile = new File(getFilesDir(),imageFile.getName());
                try {
                    outStream = new FileOutputStream(imageFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);

            }

        } else {
            Toast.makeText(getApplicationContext(), "You have not selected and image", Toast.LENGTH_SHORT).show();
        }
    }
    private void get_views(){
        email = findViewById(R.id.email);
        username = findViewById(R.id.user);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        imageView = findViewById(R.id.image);
    }

    private void update_Email(final FirebaseUser user){

        user.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    update_password(user);
                }else{
                    failed =true;
                    data.putExtra("state",failed);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

    }

    private void update_password(final FirebaseUser user) {
        user.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    update_username(user);
                }else{
                    failed =true;
                    data.putExtra("state",failed);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }

    private void update_username(FirebaseUser user) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username.getText().toString())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("UpdateProfile", "User profile updated.");
                            failed =false;
                            data.putExtra("state",failed);
                            data.putExtra("imagename",imageFile.getName());
                            setResult(RESULT_OK, data);
                            finish();
                        }else {
                            failed =true;
                            data.putExtra("state",failed);
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    }
                });
    }
}
