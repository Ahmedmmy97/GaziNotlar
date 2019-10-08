package a33y.jo.gazinotlar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import a33y.jo.gazinotlar.Helpers.Helper;
import a33y.jo.gazinotlar.Listeners.DialogListener;
import a33y.jo.gazinotlar.Models.Category;
import a33y.jo.gazinotlar.Models.Note;
import a33y.jo.gazinotlar.Models.Review;

/**
 * Created by ahmed on 24/8/2018.
 */

public class AddDialog extends DialogFragment{
    EditText title;
    ImageButton add;
    ImageButton cancel;
    EditText reviewText;
    RatingBar mRatingbar;
    Note note;
    DialogListener dialogListener;
    public  static final String TYPE_EDIT = "Edit";
    public   static final String TYPE_ADD = "Add";
    String type;
    public void setNote(Note note) {
        this.note = note;
    }

    public DialogListener getDialogListener() {
        return dialogListener;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public static AddDialog newInstance(Date date, String type) {
        AddDialog frag = new AddDialog();
        Bundle args = new Bundle();
        args.putLong("date", date.getTime());
        args.putString("type", type);
        frag.setArguments(args);
        return frag;
    }
    public static AddDialog newInstance(String id , String type,DialogListener dialogListener) {
        AddDialog frag = new AddDialog();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("type", type);
        frag.setArguments(args);
        frag.setDialogListener(dialogListener);
        return frag;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_review, null);
        alertDialogBuilder.setView(view);
        type= getArguments().getString("type");
        note= Category.getNoteById(getArguments().getString("id"));
        getViews(view);
        TextView title = new TextView(getContext());
// You Can Customise your Title here

        if(type.equals(TYPE_ADD))
            title.setText("Add Review");
        else
            title.setText("Edit Review");
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark1));
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        alertDialogBuilder.setCustomTitle(title);
        return alertDialogBuilder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.Dialog_Animation;

    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_review,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view

    }
    private  void getViews(View v)
    {


        // Create your application here
        title = v.findViewById(R.id.title);
        add = v.findViewById(R.id.add);
        cancel = v.findViewById(R.id.cancel);
        mRatingbar = v.findViewById(R.id.ratingbar);
        reviewText = v.findViewById(R.id.review);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Review review = new Review(note.getId(), Helper.createTransactionID(),
                        "Section 2", FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                        new Date(),mRatingbar.getRating(),reviewText.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid());

                note.getReviews().add(0,review);
                float rating = getRating(note);
                note.setRating(rating);

                  if(dialogListener!=null)
                      dialogListener.OnDialogOkClicked();
            }
        });
    }
    private float getRating(Note n){
        float rating=0;
        for(Review r : n.getReviews())
            rating+= r.getRating();
        rating /= n.getReviews().size();

        return  rating;
    }


}
