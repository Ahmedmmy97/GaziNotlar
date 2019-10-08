package a33y.jo.gazinotlar;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import a33y.jo.gazinotlar.Adapters.TabAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFrag extends Fragment {
    ViewPager viewPager;
    TabLayout tabs;
    android.support.v4.app.Fragment[] fragments;
    Fragment convFrag = new ConvFrag();
    Fragment userFrag= new UserFrag();
    TabAdapter adapter;

    public ChatFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.chat,container,false);
        viewPager = v.findViewById(R.id.viewpager);
        tabs = v.findViewById(R.id.tablayout);

        setTabs();
        return v;
    }
    private void setTabs(){
        FragmentManager fm = getChildFragmentManager();
        fragments= new android.support.v4.app.Fragment[]{convFrag,userFrag};

        adapter = new TabAdapter(fm,fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabs.setScrollPosition(position,positionOffset,true);
                hideKeyboard();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),true);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),true);

            }
        });
    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getContext());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
