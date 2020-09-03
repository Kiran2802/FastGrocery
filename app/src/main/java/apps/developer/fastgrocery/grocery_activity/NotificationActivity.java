package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.util.List;

import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.CustPrograssbar;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.RestResponse;
import apps.developer.fastgrocery.model.login_model.User;
import apps.developer.fastgrocery.model.notification.NotificationList;
import apps.developer.fastgrocery.model.notification.NotificationResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static apps.developer.fastgrocery.MainActivity.custPrograssbar;

public class NotificationActivity extends AppCompatActivity {

    private TextView txtNodatatitle;
    LinearLayout lvlMyorder;
    LinearLayout lvlNodata;
    User user;
    SessionManager sessionManager;
    private BaseApiService apiService;
    private List<NotificationList> notificatiolist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getSupportActionBar().setTitle("Notification");
        if(getSupportActionBar()!=null){       //Back Press Option add
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        txtNodatatitle=(TextView)findViewById(R.id.txt_nodatatitle);
        lvlNodata=(LinearLayout)findViewById(R.id.lvl_nodata);
        lvlMyorder=(LinearLayout)findViewById(R.id.lvl_myorder);
        apiService = RetrofitClient.getClient().create(BaseApiService.class);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(NotificationActivity.this);
        user = sessionManager.getUserDetails("");

        getNotification();

    }

    private void getNotification() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uid", user.getId());
        custPrograssbar.PrograssCreate(NotificationActivity.this);

        apiService.getNotification(jsonObject).enqueue(new Callback<NotificationResponseData>() {
            @Override
            public void onResponse(Call<NotificationResponseData> call, Response<NotificationResponseData> response) {
                custPrograssbar.ClosePrograssBar();
                Toast.makeText(NotificationActivity.this, "" + response.body().getResponseMsg(), Toast.LENGTH_LONG).show();
                if (response.body().getResult().contains("true")) {
                    notificatiolist=response.body().getData();
                    if (notificatiolist.size() != 0) {
                        lvlNodata.setVisibility(View.GONE);
                        setNotiList(lvlMyorder, notificatiolist);
                    } else {
                        lvlNodata.setVisibility(View.VISIBLE);
                        txtNodatatitle.setText(""+response.body().getResponseMsg());

                    }


                }else {
                    lvlNodata.setVisibility(View.VISIBLE);
                    txtNodatatitle.setText(""+response.body().getResponseMsg());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponseData> call, Throwable t) {
                Log.e("error"," --> "+t.toString());
            }
        });
    }

    private void setNotiList(LinearLayout lnrView, List<NotificationList> list) {
        lnrView.removeAllViews();
        int a = 0;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                final NotificationList noti = list.get(i);
                LayoutInflater inflater = LayoutInflater.from(NotificationActivity.this);
                a = a + 1;
                View view = inflater.inflate(R.layout.custome_noti, null);
                LinearLayout lvl_bgcolor = view.findViewById(R.id.lvl_bgcolor);
                TextView txt_name = view.findViewById(R.id.txt_orderid);
                ImageView imgNoti = view.findViewById(R.id.imag_noti);

                txt_name.setText(" " + noti.getTitle());
                Glide.with(this).asBitmap().load(RetrofitClient.Base_URL + noti.getImg()).placeholder(R.drawable.empty_noti).into(imgNoti);
//
                if (noti.getISread() == 0) {
                    lvl_bgcolor.setBackgroundColor(getResources().getColor(R.color.secondary_text));
                } else {
                    lvl_bgcolor.setBackgroundColor(getResources().getColor(R.color.icons));
                }
                lnrView.addView(view);
                lvl_bgcolor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noti.setISread(1);
                        startActivity(new Intent(NotificationActivity.this, NotificationDetailsActivity.class).putExtra("myclass", noti));
                    }
                });
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
}