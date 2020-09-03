package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.grocery_fragment.Info1Fragment;
import apps.developer.fastgrocery.grocery_fragment.Info2Fragment;
import apps.developer.fastgrocery.grocery_fragment.Info3Fragment;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    public static ViewPager vpPager;
    MyPagerAdapter adapterViewPager;

    int selectPage = 0;
    private static TextView btnNext;
    private TextView btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().hide();

        btnNext = (TextView) findViewById(R.id.btn_next);
        btnSkip = (TextView) findViewById(R.id.btn_skip);
        vpPager = findViewById(R.id.vpPager);


        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        ExtensiblePageIndicator extensiblePageIndicator = (ExtensiblePageIndicator) findViewById(R.id.flexibleIndicator);
        extensiblePageIndicator.initViewPager(vpPager);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPage = position;
                if (position == 0) {
                    btnNext.setText("Next >");
                } else if (position == 1) {
                    btnNext.setText("Next >");
                } else if (position == 2) {
                    btnNext.setText("Finish >");
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnSkip.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (selectPage == 0) {
                    vpPager.setCurrentItem(1);
                } else if (selectPage == 1) {
                    vpPager.setCurrentItem(2);
                } else if (selectPage == 2) {
                    startActivity(new Intent(InfoActivity.this, LoginActivity.class));
                    finish();
                }
                break;
            case R.id.btn_skip:
                startActivity(new Intent(InfoActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return Info1Fragment.newInstance("0", "Next");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return Info2Fragment.newInstance("1", "Next");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return Info3Fragment.newInstance("2", "Finish");
                default:
                    return null;
            }

        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            Log.e("page", "" + position);
            return "Page " + position;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
//
            return fragment;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}