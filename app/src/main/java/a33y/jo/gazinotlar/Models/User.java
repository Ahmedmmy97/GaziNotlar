package a33y.jo.gazinotlar.Models;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User{

    String uid;
    String username;
    String email;
    boolean isFacebook;
    String facebook_uid;
    String key;
    String imagename;
    @Exclude
    Bitmap profileimage;
    String status;
    String search;
    static List<User> users = new ArrayList<>();
    @Exclude
    List<User> chatUsers = new ArrayList<>();
    static User currentUser;
    public User(String uid, String username, String email, boolean isFacebook, String facebook_uid) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.isFacebook = isFacebook;
        this.facebook_uid = facebook_uid;
        this.status = "offline";
        this.search = username.toLowerCase();
    }


    public User() {

    }

    @Exclude
    public Bitmap getProfileimage() {
        return profileimage;
    }
    @Exclude
    public void setProfileimage(Bitmap profileimage) {
        this.profileimage = profileimage;
    }
    public static User getCurrentUser() {
        return currentUser;
    }
    @Exclude
    public List<User> getChatUsers() {
        return chatUsers;
    }
    @Exclude
    public void setChatUsers(List<User> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }
    public static List<User> getUsers() {
        return users;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public static void setUsers(List<User> users) {
        User.users = users;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFacebook() {
        return isFacebook;
    }

    public void setFacebook(boolean facebook) {
        isFacebook = facebook;
    }

    public String getFacebook_uid() {
        return facebook_uid;
    }

    public void setFacebook_uid(String facebook_uid) {
        this.facebook_uid = facebook_uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public static User getUser(String uid){

        for(User user : users){
            if(user.getUid().equals(uid))
                return user;
        }
        return null;
    }
    public static void UpdateUser(User user){

        for(User u : users){
            if(user.getUid().equals(u.getUid())) {
               u.set(user);
            }
        }
    }

    public void set(User u){
        this.isFacebook = u.isFacebook();
        this.email = u.getEmail();
        this.facebook_uid = u.getFacebook_uid();
        this.imagename = u.getImagename();
        this.profileimage = u.getProfileimage();
        this.username = u.getUsername();
    }

    public void update(User u){
        this.isFacebook = u.isFacebook();
        this.email = u.getEmail();
        this.facebook_uid = u.getFacebook_uid();
        this.imagename = u.getImagename();
        this.username = u.getUsername();
        this.status = u.getStatus();
    }
}
