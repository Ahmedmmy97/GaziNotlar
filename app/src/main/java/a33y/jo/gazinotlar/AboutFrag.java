package a33y.jo.gazinotlar;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Listeners.FileListeners;
import a33y.jo.gazinotlar.Models.Category;
import a33y.jo.gazinotlar.Models.Note;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFrag extends Fragment implements FileListeners{


    public AboutFrag() {
        // Required empty public constructor
    }

    TextView downloads;
    TextView fileType;
    TextView uploader;
    RatingBar ratingBar;
    TextView rating;
    Button download;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.aboutfrag,container,false);
        getViews(v);
        bindViews(getArguments());
        return v;
    }
    private void getViews(View v){
        downloads = v.findViewById(R.id.downloads);
        fileType = v.findViewById(R.id.filetype);
        uploader = v.findViewById(R.id.uploader);
        ratingBar = v.findViewById(R.id.ratingbar);
        rating = v.findViewById(R.id.rating);
        download = v.findViewById(R.id.download);
    }
    private void bindViews(Bundle bundle){
        final Note note = Category.getNoteById(bundle.getString("id"));
        downloads.setText(String.valueOf(bundle.getInt("downloads",0)));
        fileType.setText(bundle.getString("filetype","PDF"));
        uploader.setText(bundle.getString("uploader"));
        ratingBar.setRating(bundle.getFloat("rating",0));
        rating.setText(String.valueOf(ratingBar.getRating()));
        if(DataHelper.exists(note,getContext())){
            download.setText("Open");
            download.setTag("open");
        }else {
            download.setText("Download");
            download.setTag("download");
        }
        download.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(v.getTag().equals("download")) {
                    DataHelper.addFileListeners(AboutFrag.this);
                    DataHelper.DownloadRemFile(note, getContext());
                    download.setText("Downloading....");
                    download.setEnabled(false);
                }else{
                    Intent intent = new Intent(getContext(),PdfViewer.class);
                    intent.putExtra("id",note.getId());

                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void OnFileDownloaded() {
        download.setText("Open");
        download.setTag("open");
        download.setEnabled(true);
    }

    @Override
    public void OnFileDownloaded(int postion) {

    }
}
