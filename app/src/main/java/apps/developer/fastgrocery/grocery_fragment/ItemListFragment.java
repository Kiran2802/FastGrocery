package apps.developer.fastgrocery.grocery_fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.List;

import apps.developer.fastgrocery.Adapter.ProductItemListAdapter;
import apps.developer.fastgrocery.DataBase.DatabaseHelper;
import apps.developer.fastgrocery.DataBase.MyCart;
import apps.developer.fastgrocery.MainActivity;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.grocery_activity.Cart_Activity;
import apps.developer.fastgrocery.model.login_model.User;
import apps.developer.fastgrocery.model.product.ItemDataResponseData;
import apps.developer.fastgrocery.model.product.ProductDataList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static apps.developer.fastgrocery.Utils.SessionManager.CURRUNCY;
import static apps.developer.fastgrocery.Utils.SessionManager.ISCART;


public class ItemListFragment extends Fragment implements View.OnClickListener {
    int CID = 0;
    int SCID = 0;
    SessionManager sessionManager;
    private BaseApiService apiService;
    private User user;
    private List<ProductDataList> productlist;
    private ProductItemListAdapter productItemListAdapter;
    public RecyclerView my_recycler_view;
    public StaggeredGridLayoutManager gaggeredGridLayoutManager;
    public static ItemListFragment itemListFragment;
    public DatabaseHelper databaseHelper;
    public TextView txtPrice,txtItem,txtNodatatitle;
    LinearLayout lvlbacket,lvlNodata;
    private TextView txt_gocart;

    public ItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CID = Integer.parseInt(getArguments().getString("cid"));
            SCID = Integer.parseInt(getArguments().getString("scid"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_item_lists, container, false);
        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getClient().create(BaseApiService.class);
        user = sessionManager.getUserDetails("");
        databaseHelper = new DatabaseHelper(getActivity());
        itemListFragment = this;
        my_recycler_view=(RecyclerView)view.findViewById(R.id.my_recycler_view);
        txtPrice=(TextView)view.findViewById(R.id.txt_price);
        txtItem=(TextView)view.findViewById(R.id.txt_item);
        txtNodatatitle=(TextView)view.findViewById(R.id.txt_nodatatitle);
        txt_gocart=(TextView)view.findViewById(R.id.txt_gocart);
        lvlbacket=(LinearLayout)view.findViewById(R.id.lvlbacket);
        lvlNodata=(LinearLayout)view.findViewById(R.id.lvl_nodata);

        getProduct();
        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            lvlbacket.setVisibility(View.GONE);
        } else {
            lvlbacket.setVisibility(View.VISIBLE);
            updateItem();
        }
        txt_gocart.setOnClickListener(this);
        return view;

    }

    private void getProduct() {
        MainActivity.custPrograssbar.PrograssCreate(getContext());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("cid", CID);
        jsonObject.addProperty("sid", SCID);

        apiService.getGetProduct(jsonObject).enqueue(new Callback<ItemDataResponseData>() {
            @Override
            public void onResponse(Call<ItemDataResponseData> call, Response<ItemDataResponseData> response) {
                MainActivity.custPrograssbar.ClosePrograssBar();
                try {
                    if (response.body().getResult().contains("true")){
                        productlist=response.body().getData();

                        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, 1);
                        my_recycler_view.setLayoutManager(gaggeredGridLayoutManager);
                        productItemListAdapter = new ProductItemListAdapter(getContext(), productlist);
                        my_recycler_view.setAdapter(productItemListAdapter);
                    }else {
                        Toast.makeText(getContext(), ""+response.body().getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }catch (NullPointerException e){

                }
            }
            @Override
            public void onFailure(Call<ItemDataResponseData> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateItem() {
        try {
            Cursor res = databaseHelper.getAllData();
            if (res.getCount() == 0) {
                lvlbacket.setVisibility(View.GONE);
            } else {
                lvlbacket.setVisibility(View.VISIBLE);

                double totalRs = 0;
                double ress = 0;
                double totalItem = 0;
                while (res.moveToNext()) {
                    MyCart rModel = new MyCart();
                    rModel.setCost(res.getString(5));
                    rModel.setQty(res.getString(6));
                    rModel.setDiscount(res.getInt(7));
                    ress = (Double.parseDouble(res.getString(5)) * rModel.getDiscount()) / 100;
                    ress = Double.parseDouble(res.getString(5)) - ress;
                    double temp = Integer.parseInt(res.getString(6)) * ress;
                    totalItem = totalItem + Integer.parseInt(res.getString(6));
                    totalRs = totalRs + temp;
                }
                // txtPrice.setText("Total :" + totalRs);
                txtItem.setText("Total Item:(" + new DecimalFormat("##.##").format(totalItem) + ")");
                txtPrice.setText(sessionManager.getStringData(CURRUNCY) + totalRs);
            }
        } catch (Exception e) {

        }
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.img_cart);
        if (item != null)
            item.setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateItem();
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(), Cart_Activity.class));
    }
}