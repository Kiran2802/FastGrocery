package apps.developer.fastgrocery.grocery_fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apps.developer.fastgrocery.Adapter.HomeProductListAdapter;
import apps.developer.fastgrocery.Adapter.ProductListAdapter;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.ProductItemClickListener;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.grocery_activity.ItemDetailsActivity;
import apps.developer.fastgrocery.model.home.Productlist;
import apps.developer.fastgrocery.model.login_model.User;
import apps.developer.fastgrocery.ui.home.HomeFragment;

import static apps.developer.fastgrocery.ui.home.HomeFragment.productList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopularProdutsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularProdutsFragment extends Fragment implements ProductItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SessionManager sessionManager;
    private User userData;
    private RecyclerView reyCategory;

    public PopularProdutsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PopularProdutsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PopularProdutsFragment newInstance(String param1, String param2) {
        PopularProdutsFragment fragment = new PopularProdutsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_popular_produts, container, false);
        sessionManager = new SessionManager(getActivity());
        userData = sessionManager.getUserDetails("");

        reyCategory =(RecyclerView)view.findViewById(R.id.recycler_view);

        reyCategory.setHasFixedSize(true);
        reyCategory.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        ProductListAdapter itemAdp = new ProductListAdapter(getContext(), productList,this);
        reyCategory.setAdapter(itemAdp);

        return view;
    }

    @Override
    public void onItemClick(Productlist productItem, int position) {
        getActivity().startActivity(new Intent(getActivity(), ItemDetailsActivity.class).putExtra("MyClass", productItem).putParcelableArrayListExtra("MyList", productItem.getPrice()));
    }
}