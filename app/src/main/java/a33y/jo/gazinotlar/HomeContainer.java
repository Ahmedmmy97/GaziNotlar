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

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeContainer extends Fragment {
   boolean focused = true;
   HomeFrag homeFrag = new HomeFrag();
    public HomeContainer() {
        // Required empty public constructor
    }

    public  boolean isFocused() {
        return focused;
    }

    public  void setFocused(boolean focused) {
        this.focused = focused;
        if(!focused){
            if(getFragmentManager()!=null) {
                Fragment frag = getFragmentManager().findFragmentByTag("cat");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if(frag!=null && frag.isVisible()){
                    ft.remove(frag);

                    for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                        getFragmentManager().popBackStack();
                    }
                    ft.replace(R.id.frag, new HomeFrag());
                    ft.commit();
                }


            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.home_container,container,false);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frag, new HomeFrag(),"home");
        ft.commit();
        return v;
    }
}
