package a33y.jo.gazinotlar.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Note implements Serializable{
    String id;
    String title;
    String uploader;
    String date;
    float rating;
    String fileType;
    int downloads;
    List<Review> reviews = new ArrayList<>();

    @Exclude
    File file;





    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Note() {
    }

    public Note(String id, String title, String uploader, String date) {
        this.title = title;
        this.uploader = uploader;
        this.date = date;
        this.id =id;
    }
    public Note(String id,String title, String uploader, String date,String fileType,int downloads,float rating) {
        this.title = title;
        this.uploader = uploader;
        this.date = date;
        this.id =id;
        this.fileType =fileType;
        this.downloads = downloads;
        this.rating = rating;
    }
    public Note(String id,String title, String uploader, String date,String fileType,int downloads,float rating,List<Review> reviews) {
        this.title = title;
        this.uploader = uploader;
        this.date = date;
        this.id =id;
        this.fileType =fileType;
        this.downloads = downloads;
        this.rating = rating;
        this.reviews =reviews;
    }
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Exclude
    public File getFile() {
        return file;
    }

    @Exclude
    public void setFile(File file) {
        this.file = file;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


}
