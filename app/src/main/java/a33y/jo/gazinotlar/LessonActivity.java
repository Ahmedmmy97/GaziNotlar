package a33y.jo.gazinotlar;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import a33y.jo.gazinotlar.Adapters.TabAdapter;
import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Models.Category;
import a33y.jo.gazinotlar.Models.Note;


public class LessonActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabs;
    android.support.v4.app.Fragment[] fragments;
    Fragment aboutFrag = new AboutFrag();
    Fragment revFragment= new ReviewFrag();
    TabAdapter adapter;
    TextView title;
    Note note;
    String key;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        toolbar = findViewById(R.id.my_toolbar);
        title = findViewById(R.id.title);
        getNote();
        title.setText(note.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
       viewPager = findViewById(R.id.viewpager);
       tabs = findViewById(R.id.tablayout);

       setTabs();
    }
    private void getNote(){
        String id = getIntent().getStringExtra("noteId");
        if(id !=null) {
            for(Category c : Category.categories)
                for(Note note : c.getNotes())
                    if(id.equals(note.getId())) {
                        this.note = note;
                        this.key = c.getKey();
                        break;
                    }
        }
    }
    private Bundle createBundleFromNote(){
        Bundle bundle = new Bundle();
        bundle.putString("uploader",note.getUploader());
        bundle.putFloat("rating",note.getRating());
        bundle.putInt("downloads",note.getDownloads());
        bundle.putString("filetype",note.getFileType());
        bundle.putString("id",note.getId());
        bundle.putString("key",key);
        return bundle;
    }
    private void setTabs(){
        FragmentManager fm = getSupportFragmentManager();
        fragments= new android.support.v4.app.Fragment[]{aboutFrag,revFragment};

        adapter = new TabAdapter(fm,fragments,createBundleFromNote());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabs.setScrollPosition(position,positionOffset,true);

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
    @Override
    protected void onResume() {
        super.onResume();
        DataHelper.updateStatus("online");
    }



}
