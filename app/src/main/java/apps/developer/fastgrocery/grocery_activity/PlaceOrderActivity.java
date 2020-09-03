package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import apps.developer.fastgrocery.MainActivity;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.CustPrograssbar;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.login_model.LoginResponseData;
import apps.developer.fastgrocery.model.timeslot.TimeslotDataList;
import apps.developer.fastgrocery.model.timeslot.TimeslotResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderActivity extends AppCompatActivity  {
    RadioGroup rdgTime;
    RadioGroup rdPayment;
    Button btnCuntinus;
    ImageView imgLdate;
    TextView txtSelectdate;
    ImageView imgRdate;
    int Day = 1;
    CustPrograssbar custPrograssbar;
    private BaseApiService apiService;
    private SessionManager sessionManager;
    private List<TimeslotDataList> timeslotDataLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        getSupportActionBar().setTitle("Placed Order");
        if(getSupportActionBar()!=null){       //Back Press Option add
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        apiService = RetrofitClient.getClient().create(BaseApiService.class);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(PlaceOrderActivity.this);

        rdgTime=(RadioGroup)findViewById(R.id.radiogroup);
        rdPayment=(RadioGroup)findViewById(R.id.radiogroup2);
        btnCuntinus=(Button)findViewById(R.id.btn_cuntinus);
        imgLdate=(ImageView)findViewById(R.id.img_ldate);
        imgRdate=(ImageView)findViewById(R.id.img_rdate);
        txtSelectdate=(TextView)findViewById(R.id.txt_selectdate);

        custPrograssbar = new CustPrograssbar();
        getTimeSlot();
        setPaymentList();
        txtSelectdate.setText("" + getCurrentDate());

        imgRdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDate(txtSelectdate.getText().toString());
            }
        });

        imgLdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusDate(txtSelectdate.getText().toString());
            }
        });
        btnCuntinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rdgTime.getCheckedRadioButtonId();
                RadioButton selectTime = rdgTime.findViewById(selectedId);
                int selectedId2 = rdPayment.getCheckedRadioButtonId();
                RadioButton selectpayment = rdPayment.findViewById(selectedId2);

                rdgTime.check(selectedId);
                Bundle bundle = new Bundle();
                bundle.putString("DATE", txtSelectdate.getText().toString());
                bundle.putString("TIME", selectTime.getText().toString());
                bundle.putString("PAYMENT", selectpayment.getText().toString());
                Intent intent = new Intent(PlaceOrderActivity.this, Order_Summary.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    private void getTimeSlot() {
        custPrograssbar.PrograssCreate(PlaceOrderActivity.this);
        JsonObject jsonObject = new JsonObject();

        apiService.getTimeslot(jsonObject).enqueue(new Callback<TimeslotResponseData>() {
            @Override
            public void onResponse(Call<TimeslotResponseData> call, Response<TimeslotResponseData> response) {
                custPrograssbar.ClosePrograssBar();
                RadioButton rdbtn = null;

                timeslotDataLists =response.body().getData();

                for (int i=0;i< timeslotDataLists.size() ;i++){
                    rdbtn = new RadioButton(PlaceOrderActivity.this);
                    rdbtn.setId(View.generateViewId());
                    rdbtn.setText(timeslotDataLists.get(i).getMintime() + " - " + timeslotDataLists.get(i).getMaxtime());
                   // rdbtn.setOnClickListener(PlaceOrderActivity.this);
                    rdgTime.addView(rdbtn);
                }
                rdgTime.check(rdbtn.getId());
            }

            @Override
            public void onFailure(Call<TimeslotResponseData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setPaymentList() {
//        String[] paymen = {"Pickup myself", "Cash on delivery", "Pay with online"};
        String[] paymen = {getResources().getString(R.string.pic_myslf), getResources().getString(R.string.cash_delivery), getResources().getString(R.string.pay_online)};
        RadioButton rdbtn = null;
        for (int i = 0; i < 3; i++) {
            rdbtn = new RadioButton(this);
            rdbtn.setId(View.generateViewId());
            rdbtn.setText(paymen[i]);
         //   rdbtn.setOnClickListener(this);
            rdPayment.addView(rdbtn);
        }
        rdPayment.check(rdbtn.getId());

    }

    private String getCurrentDate() {
        Date d = Calendar.getInstance().getTime();
        System.out.println("Current time => " + d);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(d);
        try {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, Day);  // number of days to add
            formattedDate = df.format(c.getTime());
            c.setTime(df.parse(formattedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    private String addDate(String dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Date strDate = null;
        try {
            strDate = sdf.parse(dt);
            if ((System.currentTimeMillis() + 432000000) < strDate.getTime()) {
                Log.e("date change ", "--> 1");
                return dt;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, Day);  // number of days to add
            dt = sdf.format(c.getTime());
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Day++;
        txtSelectdate.setText("" + dt);
        return dt;
    }

    private String minusDate(String dt) {
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
      //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = null;
        try {
            strDate = sdf.parse(dt);
            if ((System.currentTimeMillis() + 86400000) > strDate.getTime()) {
                Log.e("date change ", "--> 1");
                return dt;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Day--;
        try {

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, Day);  // number of days to add
            dt = sdf.format(c.getTime());
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtSelectdate.setText("" + dt);
        return dt;
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