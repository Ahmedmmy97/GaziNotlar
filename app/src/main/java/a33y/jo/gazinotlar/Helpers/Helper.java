package a33y.jo.gazinotlar.Helpers;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import a33y.jo.gazinotlar.Models.Chat;
import a33y.jo.gazinotlar.Models.User;

public class Helper {
    public static String createTransactionID() {
        return UUID.randomUUID().toString();
    }
    private static long dateDiff(Date date1, Date date2){
       return date1.getTime() - date2.getTime();

    }
    public static void fillChatUsers(){
        User.getCurrentUser().getChatUsers().clear();
        for(Chat chat:Chat.getAllchats()){
            User sender = User.getUser(chat.getSender());
            User reciever = User.getUser(chat.getReceiver());
            if(sender!=null) {
                if(!isUserAvailable(sender))
                    User.getCurrentUser().getChatUsers().add(sender);
            }
            if(reciever!=null) {
                if(!isUserAvailable(reciever))
                    User.getCurrentUser().getChatUsers().add(reciever);
            }
        }
    }
    public static void makeMessageSeen(List<Chat> chats,User sender){
        for(Chat chat : chats){
            if(chat.getSender().equals(sender.getUid())) {
                chat.setSeen(true);
                DataHelper.updateMessage(chat,null);
            }
        }
    }
    public static Chat CheckIfSeen(List<Chat> chats,User sender){
        Chat seenMessage=null;
        for(Chat chat : chats){
            if(chat.getSender().equals(sender.getUid())) {
                if(chat.isSeen())
                    seenMessage =chat;
            }
        }
        return  seenMessage;
    }
    public static int getNewMessages(User sender){
        List<Chat> chats = fetchChatList(sender);
        int unseenMessages=0;
        for(Chat chat : chats){
            if(chat.getSender().equals(sender.getUid())) {
                if(!chat.isSeen())
                    unseenMessages ++;
            }
        }
        return  unseenMessages;
    }
    private static boolean isUserAvailable(User  u){
        for (User user :User.getCurrentUser().getChatUsers()){
            if(user.getUid().equals(u.getUid()))
                return true;
        }
        return false;
    }
    public static String getLastMessage(User user){

        List<Chat> chats = fetchChatList(user);

        if(chats.size()>0)
        return chats.get(chats.size()-1).getMessage();
        else
            return "";
    }
    public static List<Chat> fetchChatList(User user){
        List<Chat> chats = new ArrayList<>();
        for(Chat chat:Chat.getAllchats()) {

            if ((chat.getSender().equals(User.getCurrentUser().getUid()) && chat.getReceiver().equals(user.getUid()))
                    || (chat.getSender().equals(user.getUid()) && chat.getReceiver().equals(User.getCurrentUser().getUid()))) {
                chats.add(chat);
            }
        }
        return chats;
    }
    public static String getFacebookProviderId(FirebaseUser user){
        for(UserInfo profile : user.getProviderData()) {
            // check if the provider id matches "facebook.com"
            if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
               return  profile.getUid();
            }
        }
        return null;
    }
    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        if(image==null)
            return null;
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public  static long getSeconds(Date date1, Date date2){
       return  dateDiff(date1,date2) / 1000;
    }
    public  static long getMinutes(Date date1, Date date2){
        return  getSeconds(date1,date2) / 60;
    }
    public  static long getHours(Date date1, Date date2){
        return  getMinutes(date1,date2) / 60;
    }
    public  static long getDays(Date date1, Date date2){
        return  getHours(date1,date2) / 24;
    }
    public  static long getMonths(Date date1, Date date2){
        return  getDays(date1,date2) / 30;
    }
    public  static long getYears(Date date1, Date date2){
        return  getMonths(date1,date2) / 12;
    }
    public static Bitmap UserImage;

  /*  private static Bitmap getBitmapFromVectorDrawable(Context context, File file) {
        if(file.exists()) {
            Drawable drawable=null;
            XmlResourceParser parser = null;
            try {
                parser = context.getAssets()
                        .openXmlResourceParser("drawable/ic_history.xml");
                drawable = VectorDrawableCompat.createFromXml(context.getResources(), parser);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


            //Drawable drawable = Drawable.createFromPath(file.getAbsolutePath());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable = (DrawableCompat.wrap(drawable)).mutate();
            }

            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        }
        return null;
    }*/
}
