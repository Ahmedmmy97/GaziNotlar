package a33y.jo.gazinotlar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;

import a33y.jo.gazinotlar.Adapters.Adapter;
import a33y.jo.gazinotlar.Listeners.DataListeners;
import a33y.jo.gazinotlar.Models.Category;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment implements DataListeners{
    CustomGridView gridView;
    RecyclerView.LayoutManager layoutManager;
    ScrollView scrollView;
    public HomeFrag() {
        // Required empty public constructor
    }


    Adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.home,container,false);
        gridView = v.findViewById(R.id.recycler);
        adapter = new Adapter(getActivity());
        gridView.setAdapter(adapter);
        gridView.setExpanded(true);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if(dm.densityDpi<240){
            gridView.setNumColumns(3);
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CatFrag catFrag =new CatFrag();
                Bundle bundle = new Bundle();
                bundle.putInt("subject",i);
                bundle.putString("key", Category.categories.get(i).getKey());
                bundle.putString("title",Category.categories.get(i).getTitle());
                catFrag.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frag,catFrag,"cat");
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        ((MainActivity)getActivity()).dataListeners =this;
        return v;
    }

    @Override
    public void OnChildChanged(int pos) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnChildAdded(int pos) {

    }

    @Override
    public void OnDataAdded() {
                adapter.notifyDataSetChanged();
    }
}
