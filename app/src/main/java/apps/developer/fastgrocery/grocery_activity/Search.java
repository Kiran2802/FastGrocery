package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import apps.developer.fastgrocery.Adapter.ProductItemListAdapter;
import apps.developer.fastgrocery.Adapter.SearchProductListAdapter;
import apps.developer.fastgrocery.DataBase.DatabaseHelper;
import apps.developer.fastgrocery.DataBase.MyCart;
import apps.developer.fastgrocery.MainActivity;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.CustPrograssbar;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.grocery_fragment.ItemListFragment;
import apps.developer.fastgrocery.model.login_model.LoginResponseData;
import apps.developer.fastgrocery.model.login_model.User;
import apps.developer.fastgrocery.model.product.ItemDataResponseData;
import apps.developer.fastgrocery.model.product.ProductDataList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static apps.developer.fastgrocery.Utils.SessionManager.CURRUNCY;
import static apps.developer.fastgrocery.Utils.SessionManager.ISCART;

public class Search extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerview_Advace_Search;
    private EditText edt_Advance_search;
    private ImageView imgbtn_SearchHome;
    private String searchValue;
    private CustPrograssbar custPrograssbar;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private User user;
    private BaseApiService apiService;
    private List<ProductDataList> productlist;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    public SearchProductListAdapter searchProductListAdapter;
    public static Search search;

    public TextView txtPrice,txtItem,txtNodatatitle;
    LinearLayout lvlbacket,lvlNodata;
    private TextView txt_gocart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("Search");
        if(getSupportActionBar()!=null){       //Back Press Option add
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        search=this;
        custPrograssbar = new CustPrograssbar();
        apiService = RetrofitClient.getClient().create(BaseApiService.class);
        databaseHelper = new DatabaseHelper(Search.this);
        sessionManager = new SessionManager(Search.this);
        user = sessionManager.getUserDetails("");

        recyclerview_Advace_Search=(RecyclerView)findViewById(R.id.my_recycler_view);
        txtPrice=(TextView)findViewById(R.id.txt_price);
        txtItem=(TextView)findViewById(R.id.txt_item);
        txtNodatatitle=(TextView)findViewById(R.id.txt_nodatatitle);
        txt_gocart=(TextView)findViewById(R.id.txt_gocart);
        lvlbacket=(LinearLayout)findViewById(R.id.lvlbacket);
        lvlNodata=(LinearLayout)findViewById(R.id.lvl_nodata);


        edt_Advance_search=(EditText)findViewById(R.id.edt_Advance_search);
        imgbtn_SearchHome = (ImageView)findViewById(R.id.imgbtn_SearchHome);
        productlist = new ArrayList<>();

        imgbtn_SearchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchValue=edt_Advance_search.getText().toString();
                if (searchValue.trim().length() != 0) {
                    getSearchAdvanceCourse(searchValue);
                } else {
                    Toast.makeText(Search.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_gocart.setOnClickListener(this);
    }

    private void getSearchAdvanceCourse(String searchValue) {
        Toast.makeText(this, ""+searchValue, Toast.LENGTH_SHORT).show();
        custPrograssbar.PrograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        try {
            jsonObject.put("keyword", searchValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (productlist != null && productlist.size() > 0) {
            productlist.clear();
        }
        apiService.getSearch((JsonObject) jsonParser.parse(jsonObject.toString())).enqueue(new Callback<ItemDataResponseData>() {
            @Override
            public void onResponse(Call<ItemDataResponseData> call, Response<ItemDataResponseData> response) {
                custPrograssbar.ClosePrograssBar();

                try {
                    if (response.body().getResult().contains("true")) {
                        productlist=response.body().getData();
                        lvlNodata.setVisibility(View.GONE);
                        Searchbinding(productlist);
                    } else {
                        lvlNodata.setVisibility(View.VISIBLE);
                        txtNodatatitle.setText(""+response.body().getResponseMsg());
                    }
                    searchProductListAdapter.notifyDataSetChanged();
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onFailure(Call<ItemDataResponseData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void Searchbinding(List<ProductDataList> productlist) {
        recyclerview_Advace_Search.setHasFixedSize(true);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        recyclerview_Advace_Search.setLayoutManager(gaggeredGridLayoutManager);
        searchProductListAdapter = new SearchProductListAdapter(this,getApplicationContext(), productlist);
        recyclerview_Advace_Search.setAdapter(searchProductListAdapter);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, Cart_Activity.class));
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
    public void onResume() {
        super.onResume();
        updateItem();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}