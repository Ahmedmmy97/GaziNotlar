package a33y.jo.gazinotlar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import a33y.jo.gazinotlar.Models.Category;

public class PdfViewer extends AppCompatActivity{
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfviewer);

        pdfView = findViewById(R.id.pdf);
        if(getFile()!=null) {
            pdfView.fromFile(getFile()).enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    // allows to draw something on the current page, usually visible in the middle of the screen
                    // allows to draw something on all pages, separately for every page. Called only for visible pages
                    // called after document is loaded and starts to be rendered
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(1)
                    .load();
        }else{
            finish();
        }

    }
    File getFile(){
        Intent intent = getIntent();


       /* for(File f : s.getFiles()) {
            if (f.getName().equals(s.getFileNames().get(intent.getIntExtra("pos", 0))))
                return f;
        }*/
       return Category.getNoteById(intent.getStringExtra("id")).getFile();
        //return null;
    }
    /*Subject getSubject(String subject) {
        for (Subject s : DataList.getTermSS(this))
            if (s.title.equals(subject)) {
                return s;
            }
            return null;
    }*/

}
