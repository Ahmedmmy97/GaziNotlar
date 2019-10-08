package a33y.jo.gazinotlar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.database.FirebaseDatabase;

import a33y.jo.gazinotlar.Adapters.ViewPagerAdapter;
import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Listeners.DataListeners;
import a33y.jo.gazinotlar.Models.Category;

public class MainActivity extends AppCompatActivity implements DataListeners{
      Fragment[] fragments = {new HomeContainer(),new MyNotesFrag(),new ChatFrag(),new MoreFrag()};
      ViewPager viewPager;
      BottomNavigationView bnv;
    ViewPagerAdapter adapter;
    private MenuItem prevMenuItem;
    DataListeners dataListeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager);
        bnv= findViewById(R.id.bnv);
        setupViewPager(viewPager);
        DataHelper.setDataListeners(this);
        Category.categories.clear();


        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }catch (Exception e){

        }
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homebtn:
                        /*fragments[0]= new HomeContainer();
                        adapter.notifyDataSetChanged();*/
                        if(!((HomeContainer)fragments[0]).isFocused())
                            ((HomeContainer)fragments[0]).setFocused(false);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.ntsbtn:
                        if(((HomeContainer)fragments[0]).isFocused())
                            ((HomeContainer)fragments[0]).setFocused(false);
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.chtbtn:
                        if(((HomeContainer)fragments[0]).isFocused())
                            ((HomeContainer)fragments[0]).setFocused(false);
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.morebtn:
                        if(((HomeContainer)fragments[0]).isFocused())
                            ((HomeContainer)fragments[0]).setFocused(false);
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    bnv.getMenu().getItem(0).setChecked(false);

                bnv.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bnv.getMenu().getItem(position);
                if(position!=0 &&  ((HomeContainer)fragments[0]).isFocused())
                ((HomeContainer)fragments[0]).setFocused(false);
                if(position==0 &&  !((HomeContainer)fragments[0]).isFocused())
                    ((HomeContainer)fragments[0]).setFocused(true);

hideKeyboard();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
       // DataHelper.create(this);
        new Thread(new Runnable(){
            @Override
            public void run() {
                DataHelper.getData(getApplicationContext());
            }
        }).start();

    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void setupViewPager(ViewPager viewPager)
    {
         adapter= new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
    }


    @Override
    public void OnChildChanged(int pos) {
        if(dataListeners!=null)
            dataListeners.OnChildChanged(pos);
    }

    @Override
    public void OnChildAdded(int pos) {

    }

    @Override
    public void OnDataAdded() {

     if(dataListeners!=null)
         dataListeners.OnDataAdded();

     findViewById(R.id.loading).setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        Fragment frag = getSupportFragmentManager().findFragmentByTag("cat");
        if(frag==null || !frag.isVisible())
        moveTaskToBack(true);
        else{
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
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

    @Override
    protected void onResume() {
        super.onResume();
        DataHelper.updateStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataHelper.updateStatus("offline");
    }


}
