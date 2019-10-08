package a33y.jo.gazinotlar;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Helpers.FileHelper;
import a33y.jo.gazinotlar.Helpers.Helper;
import a33y.jo.gazinotlar.Listeners.FileListeners;
import a33y.jo.gazinotlar.Models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFrag extends Fragment implements FileListeners{


    private File imageFile;

    public MoreFrag() {
        // Required empty public constructor
    }

    ImageView image;
    ProgressBar bar;
    ImageView set_image;
    private static final int RESULT_LOAD_IMAGE = 1;
    private final int RESULT_CROP = 400;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.more,container,false);
        CardView signout = v.findViewById(R.id.signout);
        TextView username = v.findViewById(R.id.username);
        set_image = v.findViewById(R.id.set_image);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        username.setText(user.getDisplayName());

        signout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DataHelper.updateStatus("offline");
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                User.getUsers().clear();
                User.setCurrentUser(null);
                Intent intent = new Intent(getActivity(),Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        image = v.findViewById(R.id.image);
        bar = v.findViewById(R.id.loading);
           if( User.getCurrentUser().getProfileimage()==null){
               DataHelper.addFileListeners(this);
                if(User.getCurrentUser().isFacebook())
                  new DataHelper.DownloadImageFromInternet(image,bar).execute("https://graph.facebook.com/" + User.getCurrentUser().getFacebook_uid() + "/picture?height=400");
                else {

                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                DataHelper.DownloadUsersProfile(getContext(), User.getCurrentUser(), -1,false);
                            }
                        }).start();

                }
            }else {
                image.setImageBitmap(User.getCurrentUser().getProfileimage());
                bar.setVisibility(View.GONE);
            }
           v.findViewById(R.id.editprofile).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Toast.makeText(getContext(),"Option #1",Toast.LENGTH_SHORT).show();
               }
           });
           set_image.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   requestPermissions(
                           new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                           100);
               }
           });
        return v;
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
            if(resultCode == getActivity().RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                image.setImageBitmap(selectedBitmap);

            }
        }else if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null !=data){
            if(data.getClipData()!=null) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    imageFile = new File(FileHelper.getPath(getContext(), uri));

                }
            }else{
                Uri uri = data.getData();
                imageFile  = new File(FileHelper.getPath(getContext(), uri));

            }


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            dialogBuilder.setTitle("Are You sure?!");
            dialogBuilder.setMessage("Do you want to change profile picture?");
            dialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (imageFile.exists()){
                        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        try {
                            imageBitmap = Helper.modifyOrientation(imageBitmap, imageFile.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageBitmap = Helper.getResizedBitmap(imageBitmap,700);
                        image.setImageBitmap(imageBitmap);
                        OutputStream outStream = null;
                        imageFile = new File(getContext().getFilesDir(),imageFile.getName());
                        try {
                            outStream = new FileOutputStream(imageFile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        DataHelper.sendUserImage(getContext(),imageFile,FirebaseAuth.getInstance().getCurrentUser().getUid());
                        User.getCurrentUser().setImagename(imageFile.getName());
                        DataHelper.updateUserImage(getContext(), User.getCurrentUser());
                    }

                }
            });
            dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialogBuilder.create().show();

        } else {
            Toast.makeText(getContext(), "You have not selected an image", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void OnFileDownloaded() {

        image.setImageBitmap( User.getCurrentUser().getProfileimage());
        bar.setVisibility(View.GONE);


    }

    @Override
    public void OnFileDownloaded(int postion) {

    }


}
