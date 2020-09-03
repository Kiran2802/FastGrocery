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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import apps.developer.fastgrocery.Adapter.HomeCategoryListAdapter;
import apps.developer.fastgrocery.Adapter.SubCategoryAdapter;
import apps.developer.fastgrocery.MainActivity;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.ClickListener;
import apps.developer.fastgrocery.Utils.RecyclerTouchListener;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.category.SubcategoryDataList;
import apps.developer.fastgrocery.model.category.SubcategoryResponseData;
import apps.developer.fastgrocery.model.home.HomeResponseData;
import apps.developer.fastgrocery.model.login_model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sub_Category#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sub_Category extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String cid;
    private RecyclerView recyclerView;
    private LinearLayout lvlNodata;
    private TextView txtNodatatitle;
    private SessionManager sessionManager;

    private User user;
    private BaseApiService apiService;
    private List<SubcategoryDataList> subcategorylist;
    private SubCategoryAdapter subCategoryAdapter;


    public Sub_Category() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
             cid = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sub__category, container, false);
        apiService = RetrofitClient.getClient().create(BaseApiService.class);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        lvlNodata=(LinearLayout)view.findViewById(R.id.lvl_nodata);
        txtNodatatitle=(TextView)view.findViewById(R.id.txt_nodatatitle);
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");

        getSubCategory(cid);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                String  id =subcategorylist.get(position).getId();
                String category_id=subcategorylist.get(position).getCatId();

                Fragment newFragment = new ItemListFragment();
                Bundle args = new Bundle();
                args.putString("cid", category_id);
                args.putString("scid", id);
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

    private void getSubCategory(String cid) {

        MainActivity.custPrograssbar.PrograssCreate(getContext());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("category_id", cid);

        apiService.getSubcategory(jsonObject).enqueue(new Callback<SubcategoryResponseData>() {
            @Override
            public void onResponse(Call<SubcategoryResponseData> call, Response<SubcategoryResponseData> response) {
                MainActivity.custPrograssbar.ClosePrograssBar();
                try {
                    if (response.body().getResult().contains("true")){
                        subcategorylist=response.body().getData();
                        SubCategorybinding(subcategorylist);
                    }else {
                        Toast.makeText(getContext(), ""+response.body().getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }catch (NullPointerException e){

                }
            }
            @Override
            public void onFailure(Call<SubcategoryResponseData> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SubCategorybinding(List<SubcategoryDataList> subcategorylist) {
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        subCategoryAdapter = new SubCategoryAdapter(getContext(), subcategorylist);
        recyclerView.setAdapter(subCategoryAdapter);
    }

    // TODO: Rename and change types and number of parameters
    public static Sub_Category newInstance(String param1, String param2) {
        Sub_Category fragment = new Sub_Category();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

}