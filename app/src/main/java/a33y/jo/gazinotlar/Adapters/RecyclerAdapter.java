package a33y.jo.gazinotlar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import a33y.jo.gazinotlar.LessonActivity;
import a33y.jo.gazinotlar.Models.Note;
import a33y.jo.gazinotlar.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    Context context;
    List<Note> notes ;

    public RecyclerAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_item,viewGroup,false);
        return new RecyclerViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder viewHolder, int i) {
           viewHolder.bind_data(notes.get(i));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

   ImageView icon;
   TextView title;
   TextView date;
   TextView uploader;
   String id;
   Context c;
    public RecyclerViewHolder(@NonNull View itemView,Context c) {
        super(itemView);
        this.c= c;
        assign_views(itemView);
        itemView.setOnClickListener(this);
    }
    void assign_views(View v){
        icon = v.findViewById(R.id.icon);
        title = v.findViewById(R.id.title);
        date = v.findViewById(R.id.date);
        uploader = v.findViewById(R.id.uploader);

    }
    public  void bind_data(Note note){

        title.setText(note.getTitle());
        uploader.setText("by "+note.getUploader());
        date.setText(note.getDate());
        this.id = note.getId();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(c, LessonActivity.class);
        intent.putExtra("noteId",id);
        c.startActivity(intent);
    }
}}