package a33y.jo.gazinotlar.Models;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import java.io.Serializable;import java.util.ArrayList;
import java.util.List;


public class Category implements Serializable{
    String title;
    int icon;
    List<Note> notes;
    String key;
    Bitmap iconbitmap;
    public void set(Category c){
        this.key = c.getKey();
        this.icon = c.getIcon();
        this.notes = c.getNotes();
        this.title =c.getTitle();
    }

    public Bitmap getIconbitmap() {
        return iconbitmap;
    }

    public void setIconbitmap(Bitmap iconbitmap) {
        this.iconbitmap = iconbitmap;
    }

    public static List<Category> categories = new ArrayList() /*{
        {
            add(new Category("Physics", R.drawable.ic_physics, new ArrayList() {
                {

                    add(new Note(id, "Lesson 1", "Ahmed Yousef", "11/02/2019", "Pdf", 50, 3.5f, new ArrayList<Review>() {
                        {
                            add(new Review(id, Helper.createTransactionID(), "Section 2", "Ahmed Yousef", new Date(), 5.0f, "Realy helpful notes !!! i learned a lot from it and it’s very good , i highly recommend it for u guys"));
                        }
                    }));
                    add(new Note(Helper.createTransactionID(), "Lesson 2", "Ahmed Yousef", "12/02/2019", "zip", 70, 5));
                }
            }));
            add(new Category("Maths", R.drawable.ic_maths, new ArrayList() {
                {
                    add(new Note(Helper.createTransactionID(), "Lesson 1", "Ahmed Yousef", "11/02/2019"));
                }
            }));
            add(new Category("Chemistry", R.drawable.ic_chemistry, new ArrayList() {
                {
                    add(new Note(Helper.createTransactionID(), "Lesson 1", "Ahmed Yousef", "11/02/2019"));
                }
            }));
            add(new Category("History", R.drawable.ic_history, new ArrayList() {
                {
                    add(new Note(Helper.createTransactionID(), "Lesson 1", "Ahmed Yousef", "11/02/2019"));
                }
            }));
            add(new Category("Programming", R.drawable.ic_programming, new ArrayList() {
                {
                    add(new Note(Helper.createTransactionID(), "Lesson 1", "Ahmed Yousef", "11/02/2019"));
                }
            }));
            add(new Category("Physics", R.drawable.ic_physics, new ArrayList() {
                {
                    add(new Note(Helper.createTransactionID(), "Lesson 1", "Ahmed Yousef", "11/02/2019"));
                }
            }));
            add(new Category("Maths", R.drawable.ic_maths, new ArrayList() {
                {
                    add(new Note(Helper.createTransactionID(), "Lesson 1", "Ahmed Yousef", "11/02/2019"));
                }
            }));
        }
    }*/;

    public Category(String title, int icon, List<Note> notes) {
        this.title = title;
        this.icon = icon;
        this.notes = notes;
    }
    public Category() {

    }
    public List<Note> getNotes() {
        return notes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Nullable
    public static Note getNoteById(String id) {
        for (Category cat : categories) {
            for (Note n : cat.getNotes()) {
                if (n.getId().equals(id)) {
                    return n;
                }
            }
        }
        return null;
    }
    @Nullable
    public static Category getCatByKey(String key) {
        for (Category cat : categories) {

                if (cat.getKey().equals(key)) {
                    return cat;
                }
        }
        return null;
    }
}
