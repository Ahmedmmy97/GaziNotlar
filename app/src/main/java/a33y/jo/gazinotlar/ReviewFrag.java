package a33y.jo.gazinotlar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Listeners.DialogListener;
import a33y.jo.gazinotlar.Listeners.ReviewListeners;
import a33y.jo.gazinotlar.Models.Category;
import a33y.jo.gazinotlar.Models.Note;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFrag extends Fragment implements DialogListener, ReviewListeners{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button add;
    LinearLayout loading;
    ReviewListAdapter adapter;
    AddDialog addDialog;
    public ReviewFrag() {
        // Required empty public constructor
    }

   Note note;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.reviewfrag,container,false);
        note = Category.getNoteById(getArguments().getString("id"));
        recyclerView = v.findViewById(R.id.recycler);
        adapter = new ReviewListAdapter(getActivity(),note.getReviews());
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        add = v.findViewById(R.id.addrevbtn);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                addDialog = AddDialog.newInstance(note.getId(),AddDialog.TYPE_ADD,ReviewFrag.this);
                addDialog.show(fm, "Add_dialog");

            }
        });
        loading =v.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        return v;
    }

    @Override
    public void OnDialogOkClicked() {
        loading.setVisibility(View.VISIBLE);
        addDialog.dismiss();
        DataHelper.setReviewListeners(this);
        DataHelper.sendReview(getContext(),Category.getCatByKey(getArguments().getString("key")));

    }

    @Override
    public void OnReviewUploaded() {
        loading.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnReviewEdited() {

    }
}
