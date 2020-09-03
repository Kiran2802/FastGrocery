package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import apps.developer.fastgrocery.Adapter.HomeCategoryListAdapter;
import apps.developer.fastgrocery.DataBase.DatabaseHelper;
import apps.developer.fastgrocery.DataBase.MyCart;
import apps.developer.fastgrocery.MainActivity;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.RestResponse;
import apps.developer.fastgrocery.model.category.CategoryResponseData;
import apps.developer.fastgrocery.model.login_model.LoginResponseData;
import apps.developer.fastgrocery.model.login_model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static apps.developer.fastgrocery.Utils.SessionManager.CURRUNCY;

public class Order_Summary extends AppCompatActivity {

    public String DATE,TIME,PAYMENT;
    double TOTAL;
    public static int paymentsucsses = 0;
    public static boolean ISORDER = false;
    LinearLayout lvlordersumry;
    LinearLayout lvlone;
    TextView txtSubtotal;
    TextView txtDelivery; TextView txtAddress,txt_name;
    TextView txtDelevritital; TextView txtChangeadress;
    TextView txtTotal,txtTotal1;
    Button btnCuntinus;
    LinearLayout lvltwo;
    DatabaseHelper databaseHelper;
    List<MyCart> myCarts;
    SessionManager sessionManager;

    User user;
    private BaseApiService apiService;
    private TextView txt_trackorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__summary);
        getSupportActionBar().setTitle("Placed Order");
        if(getSupportActionBar()!=null){       //Back Press Option add
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        txt_name =(TextView)findViewById(R.id.txt_name);
        txtAddress =(TextView)findViewById(R.id.txt_address);
        txtChangeadress =(TextView)findViewById(R.id.txt_changeadress);
        btnCuntinus =(Button)findViewById(R.id.btn_cuntinus);
        txtTotal =(TextView)findViewById(R.id.txt_total);
        txtTotal1 =(TextView)findViewById(R.id.txt_total1);
        txtDelevritital =(TextView)findViewById(R.id.txt_delevritital);
        txtDelivery =(TextView)findViewById(R.id.txt_delivery);
        txt_trackorder =(TextView)findViewById(R.id.txt_trackorder);
        txtSubtotal =(TextView)findViewById(R.id.txt_subtotal);
        lvlordersumry =(LinearLayout)findViewById(R.id.lvlordersumry);
        lvlone =(LinearLayout)findViewById(R.id.lvlone);
        lvltwo =(LinearLayout)findViewById(R.id.lvltwo);

        Bundle bundle = getIntent().getExtras();
        if (bundle !=null) {
            DATE = bundle.getString("DATE");
            TIME = bundle.getString("TIME");
            PAYMENT = bundle.getString("PAYMENT");
        }
        apiService = RetrofitClient.getClient().create(BaseApiService.class);
        databaseHelper = new DatabaseHelper(Order_Summary.this);
        sessionManager = new SessionManager(Order_Summary.this);
        user = sessionManager.getUserDetails("");
        txt_name.setText( "Deliver to -" + user.getName());
        txtAddress.setText(user.getHno() + "," + user.getSociety() + "," + user.getArea() + "," + user.getLandmark());
        myCarts = new ArrayList<>();
        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            // show message
            Toast.makeText(Order_Summary.this, "NO DATA FOUND", Toast.LENGTH_LONG).show();
        }
        while (res.moveToNext()) {
            MyCart rModel = new MyCart();
            rModel.setID(res.getString(0));
            rModel.setPID(res.getString(1));
            rModel.setImage(res.getString(2));
            rModel.setTitle(res.getString(3));
            rModel.setWeight(res.getString(4));
            rModel.setCost(res.getString(5));
            rModel.setQty(res.getString(6));
            rModel.setDiscount(res.getInt(7));
            myCarts.add(rModel);
        }
        getDeliveryCharch();

        txtChangeadress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Order_Summary.this, AddressActivity.class));
            }
        });
        txt_trackorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Order_Summary.this, MyOrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        btnCuntinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCuntinus.setClickable(false);

                if (PAYMENT.equalsIgnoreCase(getResources().getString(R.string.pay_online))) {
                    int temtoal = (int) Math.round(TOTAL);
                    startActivity(new Intent(Order_Summary.this, RazerpayActivity.class).putExtra("amount", temtoal));
                } else {
                    sendorderServer();
                }
            }
        });

    }

    private void setJoinPlayrList(LinearLayout lnrView, List<MyCart> myCarts) {
        lnrView.removeAllViews();
        final int[] count = {0};
        double[] totalAmount = {0};
        DatabaseHelper helper = new DatabaseHelper(Order_Summary.this);
        if (myCarts != null && myCarts.size() > 0) {
            for (int i = 0; i < myCarts.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(Order_Summary.this);
                MyCart cart = myCarts.get(i);
                View view = inflater.inflate(R.layout.custome_sumrry, null);
                ImageView img_icon = view.findViewById(R.id.img_icon);
                TextView txt_title = view.findViewById(R.id.txt_title);
                TextView txt_priceitem = view.findViewById(R.id.txt_priceanditem);
                TextView txt_price = view.findViewById(R.id.txt_price);

                Glide.with(Order_Summary.this).load(RetrofitClient.Base_URL + "/" + cart.getImage()).thumbnail(Glide.with(Order_Summary.this).load(R.drawable.lodingimage)).into(img_icon);

                double res = (Double.parseDouble(cart.getCost()) / 100.0f) * cart.getDiscount();

                res = Double.parseDouble(cart.getCost()) - res;
                txt_priceitem.setText(sessionManager.getStringData(CURRUNCY) + res);
                txt_title.setText("" + cart.getTitle());

                MyCart myCart = new MyCart();
                myCart.setPID(cart.getPID());
                myCart.setImage(cart.getImage());
                myCart.setTitle(cart.getTitle());
                myCart.setWeight(cart.getWeight());
                myCart.setCost(cart.getCost());


                int qrt = helper.getCard(myCart.getPID(), myCart.getCost());
                txt_priceitem.setText(qrt + " item x " + sessionManager.getStringData(CURRUNCY) + res);

                double temp = res * qrt;
                txt_price.setText(sessionManager.getStringData(CURRUNCY) + temp);
                totalAmount[0] = totalAmount[0] + temp;


                lnrView.addView(view);
            }
        }
        txtSubtotal.setText(sessionManager.getStringData(CURRUNCY) + new DecimalFormat("##.##").format(totalAmount[0]));
        if (PAYMENT.equalsIgnoreCase(getResources().getString(R.string.pic_myslf))) {
            txtDelivery.setVisibility(View.VISIBLE);
            txtDelevritital.setVisibility(View.VISIBLE);
            txtDelivery.setText(sessionManager.getStringData(CURRUNCY) + "0");
        } else {
            totalAmount[0] = totalAmount[0] + sessionManager.getIntData("dcharge");
            txtDelivery.setText(sessionManager.getStringData(CURRUNCY) + sessionManager.getIntData("dcharge"));
        }
        txtTotal.setText(sessionManager.getStringData(CURRUNCY) + new DecimalFormat("##.##").format(totalAmount[0]));
        txtTotal1.setText(sessionManager.getStringData(CURRUNCY) + new DecimalFormat("##.##").format(totalAmount[0]));
        btnCuntinus.setText("Place Order - " + sessionManager.getStringData(CURRUNCY) + new DecimalFormat("##.##").format(totalAmount[0]));
        TOTAL = totalAmount[0];
    }

    private void getDeliveryCharch() {
        MainActivity.custPrograssbar.PrograssCreate(Order_Summary.this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uid", user.getId());

        apiService.getdcharges(jsonObject).enqueue(new Callback<LoginResponseData>() {
            @Override
            public void onResponse(Call<LoginResponseData> call, Response<LoginResponseData> response) {
                MainActivity.custPrograssbar.ClosePrograssBar();

                if (response.body().getResult().contains("true")){
                    sessionManager.setIntData("dcharge", Integer.parseInt(response.body().getDCharge()));
                    sessionManager.setUserDetails("", response.body().getUser());
                    setJoinPlayrList(lvlordersumry, myCarts);
                }else {
                    Toast.makeText(Order_Summary.this, ""+response.body().getResponseMsg(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Order_Summary.this, AddressActivity.class));
                }
            }

            @Override
            public void onFailure(Call<LoginResponseData> call, Throwable t) {
                Toast.makeText(Order_Summary.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sessionManager != null) {
            user = sessionManager.getUserDetails("");
            txt_name.setText( "Deliver to - " + user.getName());
            txtAddress.setText(user.getHno() + "," + user.getSociety() + "," + user.getArea() + "," + user.getLandmark()+ "-" + user.getPincode());
            getDeliveryCharch();
        }
        if (paymentsucsses == 1) {
            paymentsucsses = 0;
            sendorderServer();
        }
    }

    private void sendorderServer() {
        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            // show message
            return;
        }
        if (user.getArea() != null || user.getSociety() != null || user.getHno() != null || user.getMobile() != null) {
            JSONArray jsonArray = new JSONArray();
            while (res.moveToNext()) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", res.getString(0));
                    jsonObject.put("pid", res.getString(1));
                    jsonObject.put("image", res.getString(2));
                    jsonObject.put("title", res.getString(3));
                    jsonObject.put("weight", res.getString(4));
                    jsonObject.put("cost", res.getString(5));
                    jsonObject.put("qty", res.getString(6));
                    jsonArray.put(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            OrderPlace(jsonArray);
            Log.e("JsonList", jsonArray.toString());
        } else {
            startActivity(new Intent(Order_Summary.this, AddressActivity.class));
        }
    }

    private void OrderPlace(JSONArray jsonArray) {

        MainActivity.custPrograssbar.PrograssCreate(Order_Summary.this);

        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("timesloat", TIME);
            jsonObject.put("ddate", DATE);
            jsonObject.put("total", TOTAL);
            jsonObject.put("p_method", PAYMENT);
            jsonObject.put("pname", jsonArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        apiService.getOrder((JsonObject) jsonParser.parse(jsonObject.toString())).enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                MainActivity.custPrograssbar.ClosePrograssBar();
                lvlone.setVisibility(View.GONE);
                lvltwo.setVisibility(View.VISIBLE);

                Toast.makeText(Order_Summary.this, "" + response.body().getResponseMsg(), Toast.LENGTH_LONG).show();
                if (response.body().getResult().equals("true")) {
                    databaseHelper.DeleteCard();
                    ISORDER = true;
                }
            }
            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                Toast.makeText(Order_Summary.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Order_Summary.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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