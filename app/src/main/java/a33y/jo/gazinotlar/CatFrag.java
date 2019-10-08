package a33y.jo.gazinotlar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import a33y.jo.gazinotlar.Adapters.RecyclerAdapter;
import a33y.jo.gazinotlar.Models.Category;

/**
 * A simple {@link Fragment} subclass.
 */
public class CatFrag extends Fragment {

    CustomRecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ScrollView scrollView;
    TextView title;
    public CatFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.category_layout,container,false);
        recyclerView = v.findViewById(R.id.recycler);
        int id = getArguments().getInt("subject");
        RecyclerAdapter adapter = new RecyclerAdapter(getActivity(), Category.categories.get(id).getNotes());
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setExpanded(true);
        title = v.findViewById(R.id.cat);
        title.setText(getArguments().getString("title"));
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
