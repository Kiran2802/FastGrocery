package apps.developer.fastgrocery.grocery_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.List;

import apps.developer.fastgrocery.Adapter.HomeCategoryListAdapter;
import apps.developer.fastgrocery.MainActivity;
import apps.developer.fastgrocery.R;


import apps.developer.fastgrocery.Utils.ClickListener;
import apps.developer.fastgrocery.Utils.RecyclerTouchListener;
import apps.developer.fastgrocery.Utils.SessionManager;


import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.category.CategoryResponseData;
import apps.developer.fastgrocery.model.home.Catlist;
import apps.developer.fastgrocery.model.login_model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SessionManager sessionManager;
    private User user;
    private RecyclerView recyclerView;
    private BaseApiService apiService;
    private List<Catlist> categorylist;
    private HomeCategoryListAdapter categoryAdaptor;


    public CategoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_item_list, container, false);
        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getClient().create(BaseApiService.class);
        user = sessionManager.getUserDetails("");
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getCategory();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
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


        return view;
    }

    private void getCategory() {
        MainActivity.custPrograssbar.PrograssCreate(getContext());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uid", user.getId());

        apiService.getCat(jsonObject).enqueue(new Callback<CategoryResponseData>() {
            @Override
            public void onResponse(Call<CategoryResponseData> call, Response<CategoryResponseData> response) {
                MainActivity.custPrograssbar.ClosePrograssBar();
                try {
                    if (response.body().getResult().contains("true")){
                        categorylist=response.body().getData();

                        categoryAdaptor = new HomeCategoryListAdapter(getContext(), categorylist);
                        recyclerView.setAdapter(categoryAdaptor);
                    }else {
                        Toast.makeText(getContext(), ""+response.body().getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onFailure(Call<CategoryResponseData> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // TODO: Rename and change types and number of parameters
    public static CategoryListFragment newInstance(String param1, String param2) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


}