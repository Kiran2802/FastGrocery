package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import apps.developer.fastgrocery.MainActivity;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.CustPrograssbar;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.RestResponse;
import apps.developer.fastgrocery.model.login_model.User;
import apps.developer.fastgrocery.model.orderHistroy.OrderHistoryDataList;
import apps.developer.fastgrocery.model.orderHistroy.OrderHistoryResponseData;
import apps.developer.fastgrocery.model.orderHistroy.OrderProductResponseData;
import apps.developer.fastgrocery.model.orderHistroy.Productinfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static apps.developer.fastgrocery.Utils.SessionManager.CURRUNCY;

public class MyOrderActivity extends AppCompatActivity {

    private CustPrograssbar custPrograssbar;
    private SessionManager sessionManager;
    private User user;
    private BaseApiService apiService;
    private List<OrderHistoryDataList> orderHistoryDataLists;
    LinearLayout lvlMycard;
    LinearLayout lvlNodata;
    TextView txtNodatatitle;
    private List<Productinfo> productinfolist;
    private TextView txttotal,txtDcharge,txtSubtotal, txtstatus,timesloat,txtpayment,orderDate;
    private LinearLayout lvlMyorder;

    private String oid;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        getSupportActionBar().setTitle("My Order");
        if(getSupportActionBar()!=null){       //Back Press Option add
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        apiService = RetrofitClient.getClient().create(BaseApiService.class);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(MyOrderActivity.this);
        user = sessionManager.getUserDetails("");

        lvlNodata=(LinearLayout)findViewById(R.id.lvl_nodata);
        lvlMycard=(LinearLayout)findViewById(R.id.lvl_mycard);
        txtNodatatitle=(TextView) findViewById(R.id.txt_nodatatitle);



        getHistry();


    }

    private void getHistry() {

        MainActivity.custPrograssbar.PrograssCreate(MyOrderActivity.this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uid", user.getId());

        apiService.getHistroy(jsonObject).enqueue(new Callback<OrderHistoryResponseData>() {
            @Override
            public void onResponse(Call<OrderHistoryResponseData> call, Response<OrderHistoryResponseData> response) {
                MainActivity.custPrograssbar.ClosePrograssBar();
                try {
                    if (response.body().getResult().equals("true")) {
                        orderHistoryDataLists = new ArrayList<>();
                        orderHistoryDataLists.addAll(response.body().getData());
                        if (orderHistoryDataLists.size() != 0) {
                            lvlNodata.setVisibility(View.GONE);
                            setJoinPlayrList(lvlMycard, orderHistoryDataLists);
                        } else {
                            lvlNodata.setVisibility(View.VISIBLE);
                            txtNodatatitle.setText("" + response.body().getResponseMsg());
                        }
                    }
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponseData> call, Throwable t) {
                Toast.makeText(MyOrderActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setJoinPlayrList(LinearLayout lnrView, final List<OrderHistoryDataList> orderData) {

        if (lnrView == null) {
            return;
        }

        lnrView.removeAllViews();
        int a = 0;
        if (orderData != null && orderData.size() > 0) {
            for (int i = 0; i < orderData.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(MyOrderActivity.this);
                a = a + 1;
                View view = inflater.inflate(R.layout.custome_oder, null);
                TextView txt_orderid = view.findViewById(R.id.txt_orderid);
                TextView txt_odate = view.findViewById(R.id.txt_odate);
                TextView txt_status = view.findViewById(R.id.txt_status);
                TextView txt_total = view.findViewById(R.id.txt_total);
                Button btn_details = view.findViewById(R.id.btn_details);

                txt_orderid.setText("Order ID #" + orderData.get(i).getId());
                txt_odate.setText("  " + orderData.get(i).getOrderDate());
                txt_total.setText(sessionManager.getStringData(CURRUNCY) + orderData.get(i).getTotal());
                if (orderData.get(i).getStatus().equalsIgnoreCase("completed")) {
                    txt_status.setTextColor(getResources().getColor(R.color.colorAccent));
                }else if (orderData.get(i).getStatus().equalsIgnoreCase("pending")) {
                    txt_status.setTextColor(getResources().getColor(R.color.orenge));
                }else {
                    txt_status.setTextColor(getResources().getColor(R.color.red));
                }
                txt_status.setText("" + orderData.get(i).getStatus());

                lnrView.addView(view);

                final int finalI = i;

                btn_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        oid= orderData.get(finalI).getId();
                        showBottomSheetDialog();
                    }
                });
            }
        }
    }


    public void showBottomSheetDialog() {

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_register, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        txttotal=(TextView)dialog.findViewById(R.id.txttotal);
        txtDcharge=(TextView)dialog.findViewById(R.id.txt_dcharge);
        txtSubtotal=(TextView)dialog.findViewById(R.id.txtsubtotal);
        txtstatus=(TextView)dialog.findViewById(R.id.txtstatus);
        orderDate=(TextView)dialog.findViewById(R.id.order_date);
        timesloat=(TextView)dialog.findViewById(R.id.timesloat);
        txtpayment=(TextView)dialog.findViewById(R.id.txtpayment);
        btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel);
        lvlMyorder=(LinearLayout)dialog.findViewById(R.id.lvl_myorder);
        getOrderHistry();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDailog();
            }
        });

        dialog.show();
    }

    private void LoadDailog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        //Setting message manually and performing action on button click
        builder.setMessage("Are you sour cancel this order ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getOdercancle();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.show();


    }

    private void getOdercancle() {
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        try {
            jsonObject.put("oid", oid);
            jsonObject.put("uid", user.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiService.getOCancel((JsonObject) jsonParser.parse(jsonObject.toString())).enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                MainActivity.custPrograssbar.ClosePrograssBar();
                Toast.makeText(MyOrderActivity.this, response.body().getResponseMsg(), Toast.LENGTH_LONG).show();
                if (response.body().getResult().equals("true")) {
                    finish();
                }
            }
            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                Toast.makeText(MyOrderActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOrderHistry()  {
        MainActivity.custPrograssbar.PrograssCreate(MyOrderActivity.this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id",oid);
        jsonObject.addProperty("uid", user.getId());


        apiService.getOHistroy(jsonObject).enqueue(new Callback<OrderProductResponseData>() {
            @Override
            public void onResponse(Call<OrderProductResponseData> call, Response<OrderProductResponseData> response) {
                MainActivity.custPrograssbar.ClosePrograssBar();

                if (response.body().getResult().equals("true")) {
                    productinfolist=response.body().getProductinfo();
                    double temp = response.body().getTotalAmt() - Integer.parseInt(response.body().getDCharge());
                    if (response.body().getStatus().equalsIgnoreCase(getResources().getString(R.string.pic_myslf))) {
                        txtpayment.setVisibility(View.VISIBLE);
                    } else {
                        txtpayment.setVisibility(View.GONE);
                    }

                    if (response.body().getStatus().contains("cancelled")) {
                        btn_cancel.setVisibility(View.GONE);
                    } else {
                        btn_cancel.setVisibility(View.VISIBLE);
                    }

                    txtstatus.setText("Status : " + response.body().getStatus());
                    orderDate.setText("Date : " + response.body().getOrderDate());
                    timesloat.setText("Times : " + response.body().getTimesloat());
                    txtDcharge.setText("Delivery : " + sessionManager.getStringData(CURRUNCY) + response.body().getDCharge());
                    txtSubtotal.setText("Total : " + sessionManager.getStringData(CURRUNCY) + temp);
                    txttotal.setText("Grand Total : " + sessionManager.getStringData(CURRUNCY) + response.body().getTotalAmt());
                    //  oid = response.body().getOrderid();

                    setOrderList(lvlMyorder, productinfolist);
                }
            }

            @Override
            public void onFailure(Call<OrderProductResponseData> call, Throwable t) {
                Toast.makeText(MyOrderActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOrderList(LinearLayout lnrView, List<Productinfo> list) {
        lnrView.removeAllViews();
        int a = 0;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(MyOrderActivity.this);
                a = a + 1;
                View view = inflater.inflate(R.layout.custome_myoder, null);
                ImageView img_icon = view.findViewById(R.id.img_icon);
                TextView txt_name = view.findViewById(R.id.txt_name);
                TextView txt_qty = view.findViewById(R.id.txt_qty);
                TextView txt_weight = view.findViewById(R.id.txt_weight);
                TextView txt_price = view.findViewById(R.id.txt_price);

                Glide.with(MyOrderActivity.this).load(RetrofitClient.Base_URL + "/" + list.get(i).getProductImage()).thumbnail(Glide.with(MyOrderActivity.this).load(R.drawable.lodingimage)).into(img_icon);
                txt_name.setText(" " + list.get(i).getProductName());
                txt_qty.setText(" Qty :" + list.get(i).getProductQty());
                txt_weight.setText(" " + list.get(i).getProductWeight());

                double ress = (Double.parseDouble(list.get(i).getProductPrice()) * list.get(i).getDiscount()) / 100;
                ress = Double.parseDouble(list.get(i).getProductPrice()) - ress;


                txt_price.setText(sessionManager.getStringData(CURRUNCY) + ress);

                lnrView.addView(view);

            }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyOrderActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}