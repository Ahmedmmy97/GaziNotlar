package a33y.jo.gazinotlar.Models;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable{
    String id;
    String section;
    String publisher;
    Date date;
    float rating;
    String comment;
    String noteId;
    String uid;

    public Review(String noteId,String id, String section, String publisher, Date date, float rating, String comment,String uid) {
        this.noteId =noteId;
        this.id = id;
        this.section = section;
        this.publisher = publisher;
        this.date = date;
        this.rating = rating;
        this.comment = comment;
        this.uid= uid;
    }

    public Review() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
}
