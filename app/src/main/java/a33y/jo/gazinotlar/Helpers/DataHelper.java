package a33y.jo.gazinotlar.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import a33y.jo.gazinotlar.Listeners.ChatListeners;
import a33y.jo.gazinotlar.Listeners.DataListeners;
import a33y.jo.gazinotlar.Listeners.FileListeners;
import a33y.jo.gazinotlar.Listeners.ReviewListeners;
import a33y.jo.gazinotlar.Listeners.UserListener;
import a33y.jo.gazinotlar.Models.Category;
import a33y.jo.gazinotlar.Models.Chat;
import a33y.jo.gazinotlar.Models.Note;
import a33y.jo.gazinotlar.Models.User;

public class DataHelper{
    private static final String TAG = "DataHelper" ;
    public static DataListeners dataListeners;
    public static List<UserListener> userListeners = new ArrayList<>();
    public static List<FileListeners> fileListeners = new ArrayList<>();
    public static ReviewListeners reviewListeners;
    private static StorageReference mStorageRef;
    public static boolean first = true;
    private static ValueEventListener chatListener;
    public static void addUserListener(UserListener userListener) {
        DataHelper.userListeners.add(userListener);
    }

    public static void setDataListeners(DataListeners dataListeners) {
        DataHelper.dataListeners = dataListeners;
    }



    public static void addFileListeners(FileListeners fileListeners) {
        DataHelper.fileListeners.add(fileListeners);
    }

    public static ReviewListeners getReviewListeners() {
        return reviewListeners;
    }

    public static void setReviewListeners(ReviewListeners reviewListeners) {
        DataHelper.reviewListeners = reviewListeners;
    }

    public static void create(final Context c){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref =  database.getReference("Cat");
        for(Category cat: Category.categories) {
            myref.push().setValue(cat).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(c,"Success",Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(c,"Failed to create",Toast.LENGTH_LONG).show();
                    }
                }

            });
        }

    }

    public static void getData( final  Context c){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref =  database.getReference("Cat");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                 Category cat = dataSnapshot.getValue(Category.class);
                 cat.setKey(dataSnapshot.getKey());
                 int intCurrImageResourceID = c.getResources().getIdentifier(cat.getTitle().toLowerCase(), "drawable", c.getPackageName());
                 cat.setIcon(intCurrImageResourceID);
                 Category.categories.add(cat);
                // DownloadIcon(c,cat);
                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Category cat = dataSnapshot.getValue(Category.class);
                cat.setKey(dataSnapshot.getKey());
                int intCurrImageResourceID = c.getResources().getIdentifier(cat.getTitle().toLowerCase(), "drawable", c.getPackageName());
                cat.setIcon(intCurrImageResourceID);
                for(Category c : Category.categories)
                    if(c.getKey().equals(cat.getKey())){
                        Category.categories.get(Category.categories.indexOf(c)).set(cat);

                        break;
                    }
                if(dataListeners!=null)
                    dataListeners.OnChildChanged(0);

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                Category cat = dataSnapshot.getValue(Category.class);
                cat.setKey(dataSnapshot.getKey());
                for(Category c : Category.categories)
                    if(c.getKey().equals(cat.getKey())){
                        Category.categories.remove(Category.categories.indexOf(c));
                        break;
                    }
                if(dataListeners!=null)
                    dataListeners.OnChildChanged(0);
                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Category cat = dataSnapshot.getValue(Category.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(c, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        myref.addChildEventListener(childEventListener);
        myref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataListeners!=null)
                    dataListeners.OnDataAdded();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static  void sendMessage(Chat chat,final ChatListeners chatListeners){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref =  database.getReference("Users").child(chat.getSender()).child("chats");
        DatabaseReference newref = myref.push();
        newref.setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                  if(chatListeners!=null)
                      chatListeners.OnMessageSent(true);
                }else {
                    if(chatListeners!=null)
                    chatListeners.OnMessageSent(false);
                }
            }
        });
        String key = newref.getKey();
        myref =  database.getReference("Users").child(chat.getReceiver()).child("chats");
        myref.child(key).setValue(chat);
    }

    public static  void updateMessage(final Chat chat,final ChatListeners chatListeners){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myref =  database.getReference("Users").child(chat.getSender()).child("chats").child(chat.getId());
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    myref.updateChildren(new HashMap<String, Object>(){
                        {
                            put("seen",chat.isSeen());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference ref2 =  database.getReference("Users").child(chat.getReceiver()).child("chats").child(chat.getId());
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    ref2.updateChildren(new HashMap<String, Object>() {
                        {
                            put("seen", chat.isSeen());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public static void updateStatus(final String status){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myref =  database.getReference("Users").child(User.getCurrentUser().getUid());
        myref.updateChildren(new HashMap<String, Object>(){
            {
                put("status",status);
            }
        });
    }
    public static  void deleteChat(final List<Chat> chats){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        for(Chat chat :chats) {
            DatabaseReference myref = database.getReference("Users").child(User.getCurrentUser().getUid()).child("chats").child(chat.getId());
            myref.removeValue();
        }
    }

    public static void readMessages(final User user , final ChatListeners chatListeners){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myref =  database.getReference("Users").child(fuser.getUid()).child("chats");

        chatListener =  new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Chat.getChats().clear();

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Chat message = dataSnapshot1.getValue(Chat.class);
                    message.setId(dataSnapshot1.getKey());
                    assert  message !=null;
                    assert fuser !=null;

                    if((message.getSender().equals(fuser.getUid())&&message.getReceiver().equals(user.getUid()))
                            || (message.getSender().equals(user.getUid())&&message.getReceiver().equals(fuser.getUid())))
                    {
                        Chat.getChats().add(message);
                    }

                }

                if(chatListeners!=null){
                    chatListeners.OnMessagesReceived(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(chatListeners!=null){
                    chatListeners.OnMessagesReceived(false);
                }
            }
        };
        myref.addValueEventListener(chatListener);
    }
    public static void removeChatListener(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myref =  database.getReference("Users").child(fuser.getUid()).child("chats");
        myref.removeEventListener(chatListener);
    }
    public static void readMessages(final ChatListeners chatListeners){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myref =  database.getReference("Users").child(fuser.getUid()).child("chats");

        myref.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Chat.getAllchats().clear();

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Chat message = dataSnapshot1.getValue(Chat.class);
                    message.setId(dataSnapshot1.getKey());
                    assert  message !=null;
                    assert fuser !=null;

                   Chat.getAllchats().add(message);

                }
                if(chatListeners!=null){
                    chatListeners.OnMessagesReceived(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(chatListeners!=null){
                    chatListeners.OnMessagesReceived(false);
                }
            }
        });
    }
    public static void searchUsers(String s) {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("Users").orderByChild("search").startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    User user = dataSnapshot1.getValue(User.class);

                    assert  user !=null;
                    assert fuser !=null;

                    if(!user.getUid().equals(fuser.getUid()) && User.getUser(user.getUid())==null){
                        users.add(user);
                    }
                    if(User.getUser(user.getUid())!=null){
                        user.set(User.getUser(user.getUid()));
                        users.add(user);
                    }

                }
                for(UserListener userListener : userListeners)
                    if(userListener!=null)
                        userListener.OnSearchFinished(users);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static void readUsers(){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref =  database.getReference("Users");
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        myref.orderByChild("username").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //User.getUsers().clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    User user = dataSnapshot1.getValue(User.class);

                    assert  user !=null;
                    assert fuser !=null;

                    if(!user.getUid().equals(fuser.getUid()) && User.getUser(user.getUid())==null){
                        User.getUsers().add(user);
                    }
                    if(User.getUser(user.getUid())!=null){
                             User.getUser(user.getUid()).update(user);
                    }

                }
                for(UserListener userListener : userListeners)
                if(userListener!=null)
                    userListener.OnUsersAdded();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void readUser(final  Context c, String uid){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref =  database.getReference("Users").child(uid);
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        myref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user;
                if(dataSnapshot.exists()){
                   user = dataSnapshot.getValue(User.class);
                }else {
                   user=null;
                }
                for(UserListener userListener : userListeners)
               if(userListener!=null)
                   userListener.OnUserFound(user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void sendReview(Context c, Category t) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("Cat").child(t.getKey());
        myref.setValue(t).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if(reviewListeners!=null)
                         reviewListeners.OnReviewUploaded();

                } else {


                }
            }

        });
    }


    public static void sendUser(Context c, User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("Users").child(user.getUid());
        myref.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    for(UserListener userListener : userListeners)
                    if(userListener!=null)
                        userListener.OnUserSent();

                } else {


                }
            }

        });
    }
    public static void updateUserImage(Context c, final User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("Users").child(user.getUid());
        myref.updateChildren(new HashMap<String, Object>(){
            {
                put("imagename",user.getImagename());
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    for(UserListener userListener : userListeners)
                        if(userListener!=null)
                            userListener.OnUserSent();

                } else {


                }
            }

        });
    }
    public static  boolean exists(final Note note, Context context){
        final File file = new File(context.getFilesDir(), note.getTitle());
        if(file.exists()) {
            for (Category c : Category.categories)
                for (Note n : c.getNotes())
                    if (note.getId().equals(n.getId())) {
                        Category.categories.get(Category.categories.indexOf(c)).getNotes().get(Category.categories.get(Category.categories.indexOf(c)).getNotes().indexOf(n)).setFile(file);
                        break;
                    }
        }
        return file.exists();
    }


    public static void sendUserImage(Context c,  final File f, String uid) {
                    mStorageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference storageReference = mStorageRef.child(uid).child(f.getName());

                       storageReference.putFile(Uri.fromFile(f))
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    // Get a URL to the uploaded content
                                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener(){
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                }
                            });

    }




    public static void DownloadRemFile(final Note note,Context context){

        final File file = new File(context.getFilesDir(), note.getTitle());
        FirebaseStorage.getInstance().setMaxDownloadRetryTimeMillis(2000);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        if(!file.exists()) {
            String s = note.getTitle();
            StorageReference sRef = mStorageRef.child(s);
            sRef.getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            // ...
                            for(Category c : Category.categories)
                                for(Note n : c.getNotes())
                                if(note.getId().equals(n.getId())){
                                    Category.categories.get(Category.categories.indexOf(c)).getNotes().get(  Category.categories.get(Category.categories.indexOf(c)).getNotes().indexOf(n)).setFile(file);
                                    break;
                                }
                            for(FileListeners fileListener :fileListeners)
                                if(fileListener!=null)
                                   fileListener.OnFileDownloaded();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    // ...

                }
            });

        }else{
            for(Category c : Category.categories)
                for(Note n : c.getNotes())
                    if(note.getId().equals(n.getId())){
                        Category.categories.get(Category.categories.indexOf(c)).getNotes().get(  Category.categories.get(Category.categories.indexOf(c)).getNotes().indexOf(n)).setFile(file);
                        break;
                    }

        }
    }

    private static Bitmap correctImage(File file, boolean isSmall){
        Bitmap imageBitmap = BitmapFactory.decodeFile(file.getPath());
        try {
            imageBitmap = Helper.modifyOrientation(imageBitmap, file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
         if(isSmall)
             return Helper.getResizedBitmap(imageBitmap,200);
         else
             return imageBitmap;
    }
    public static void DownloadUsersProfile(Context context, final User user, final int pos, final boolean isSmall){

        final File file = new File(context.getFilesDir(), user.getUid());
        FirebaseStorage.getInstance().setMaxDownloadRetryTimeMillis(2000);
        mStorageRef = FirebaseStorage.getInstance().getReference();


        StorageReference sRef = mStorageRef.child(user.getUid()).child(user.getImagename());
        sRef.getFile(file)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...
                        Bitmap imageBitmap = correctImage(file,isSmall);
                        user.setProfileimage(imageBitmap);
                        User.UpdateUser(user);
                        if(pos!=-1) {
                            for(FileListeners fileListener :fileListeners)
                                if(fileListener!=null)
                                    fileListener.OnFileDownloaded(pos);
                        } else {
                            for(FileListeners fileListener :fileListeners)
                                if(fileListener!=null)
                                    fileListener.OnFileDownloaded();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...

            }
        });


    }


    public static class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap>{
        ImageView imageView;
        ProgressBar bar;
        User user;
        int pos=-1;
        public DownloadImageFromInternet(ImageView imageView,ProgressBar bar) {
            this.imageView = imageView;
            this.bar=bar;

        }
        public DownloadImageFromInternet(User user) {
           this.user =user;

        }
        public DownloadImageFromInternet(User user,int pos) {
            this.user =user;
            this.pos =pos;
        }
        protected Bitmap doInBackground(String... urls) {

                String imageURL = urls[0];
                Bitmap bimage = null;
                try {
                    InputStream in = new java.net.URL(imageURL).openStream();
                    bimage = BitmapFactory.decodeStream(in);

                } catch (Exception e) {
                    Log.e("Error Message", e.getMessage());
                    e.printStackTrace();
                }
                return bimage;

        }

        protected void onPostExecute(Bitmap result) {
            if(user!=null) {
                user.setProfileimage(result);
                User.UpdateUser(user);
            }
            else {
                imageView.setImageBitmap(result);
                bar.setVisibility(View.GONE);
                User.getCurrentUser().setProfileimage(result);
            }
            if(pos!=-1)
                for(FileListeners fileListener :fileListeners)
                    if(fileListener!=null)
                        fileListener.OnFileDownloaded(pos);
        }
    }

}
