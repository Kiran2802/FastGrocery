package apps.developer.fastgrocery.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import apps.developer.fastgrocery.Adapter.HomeCategoryListAdapter;
import apps.developer.fastgrocery.Adapter.HomeProductListAdapter;
import apps.developer.fastgrocery.Adapter.SilderAdapter;
import apps.developer.fastgrocery.MainActivity;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.ClickListener;
import apps.developer.fastgrocery.Utils.ProductItemClickListener;
import apps.developer.fastgrocery.Utils.RecyclerTouchListener;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.grocery_activity.ItemDetailsActivity;
import apps.developer.fastgrocery.grocery_fragment.CartFragment;
import apps.developer.fastgrocery.grocery_fragment.CategoryListFragment;
import apps.developer.fastgrocery.grocery_fragment.PopularProdutsFragment;
import apps.developer.fastgrocery.grocery_fragment.Sub_Category;
import apps.developer.fastgrocery.model.home.Banner;
import apps.developer.fastgrocery.model.home.Catlist;
import apps.developer.fastgrocery.model.home.HomeResponseData;
import apps.developer.fastgrocery.model.home.Productlist;
import apps.developer.fastgrocery.model.login_model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static apps.developer.fastgrocery.Utils.SessionManager.ISCART;

public class HomeFragment extends Fragment implements ProductItemClickListener {

    private HomeViewModel homeViewModel;
    private SessionManager sessionManager;
    private ViewPager viewPager;
    private BaseApiService apiService;
    private TabLayout tabview;
    private RecyclerView recyclerview_categorylist;
    private RecyclerView recycler_productList;
    private Button txtViewll;
    private Button txtViewllproduct;
    private User user;
    private List<Catlist> categorylist;
    private List<Banner> bannerDataList;
    public static List<Productlist> productList;
    public String currency;
    public Integer notification;
    public static HomeFragment HomeListFragment;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 800;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    private int NUM_PAGES;
    private HomeProductListAdapter homeProductListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getClient().create(BaseApiService.class);
        HomeListFragment = this;
        viewPager=(ViewPager)root.findViewById(R.id.viewPager);
        tabview=(TabLayout)root.findViewById(R.id.tabview);
        recyclerview_categorylist=(RecyclerView)root.findViewById(R.id.recycler_view);
        recycler_productList=(RecyclerView)root.findViewById(R.id.recycler_releted);
        txtViewll=(Button)root.findViewById(R.id.btn_HE_ViewMore);
        txtViewllproduct=(Button)root.findViewById(R.id.btn_LS_ViewMore);

        user = sessionManager.getUserDetails("");
        getHomePageData();

        txtViewll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment newFragment = new CategoryListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        txtViewllproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new PopularProdutsFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        recyclerview_categorylist.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerview_categorylist, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                String category_id=categorylist.get(position).getId();

                Fragment newFragment = new Sub_Category();
                Bundle args = new Bundle();
                args.putString("id", category_id);
                newFragment.setArguments(args);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return root;
    }

    private void getHomePageData() {

      /*  if (productList != null && productList.size() > 0) {
            productList.clear();
        }if (categorylist != null && categorylist.size() > 0) {
            categorylist.clear();
        }if (categorylist != null && categorylist.size() > 0) {
            categorylist.clear();
        }*/


        MainActivity.custPrograssbar.PrograssCreate(getContext());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("uid", user.getId());

            apiService.getHome(jsonObject).enqueue(new Callback<HomeResponseData>() {
                @Override
                public void onResponse(Call<HomeResponseData> call, Response<HomeResponseData> response) {
                    MainActivity.custPrograssbar.ClosePrograssBar();

                    if (response.body().getResult().contains("true")){
                        categorylist=response.body().getResultData().getCatlist();
                        bannerDataList=response.body().getResultData().getBanner();
                        productList=response.body().getResultData().getProductlist();
                        currency=response.body().getResultData().getCurrency();
                        notification=response.body().getResultData().getRemainNotification();

                        Categorybinding(categorylist);
                        Productbinding(productList);
                        BannerBinding(bannerDataList);
                    }else {
                        Toast.makeText(getContext(), ""+response.body().getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<HomeResponseData> call, Throwable t) {
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    private void BannerBinding(List<Banner> bannerDataList) {
        SilderAdapter myCustomPagerAdapter = new SilderAdapter(getContext(), bannerDataList);
        viewPager.setAdapter(myCustomPagerAdapter);
        tabview.setupWithViewPager(viewPager, true);

        NUM_PAGES = myCustomPagerAdapter.getCount();
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES-1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }


    private void Productbinding(List<Productlist> productList) {
        recycler_productList.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL);
        recycler_productList.setLayoutManager(staggeredGridLayoutManager);
         homeProductListAdapter = new HomeProductListAdapter(getContext(), productList,this);
        recycler_productList.setAdapter(homeProductListAdapter);
    }

    private void Categorybinding(List<Catlist> categorylist) {
        recyclerview_categorylist.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerview_categorylist.setLayoutManager(gridLayoutManager);
        HomeCategoryListAdapter homeCategoryListAdapter = new HomeCategoryListAdapter(getContext(), categorylist);
        recyclerview_categorylist.setAdapter(homeCategoryListAdapter);
    }

    @Override
    public void onItemClick(Productlist productItem, int position) {
        startActivity(new Intent(getContext(), ItemDetailsActivity.class).putExtra("MyClass", productItem).putParcelableArrayListExtra("MyList", productItem.getPrice()));
    }


   @Override
    public void onResume() {
        super.onResume();
       getHomePageData();
/*
        if (ISCART) {
            ISCART = false;
            Fragment newFragment = new CartFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
*/
    }

}