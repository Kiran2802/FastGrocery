package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import apps.developer.fastgrocery.MainActivity;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.CustPrograssbar;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.login_model.User;
import apps.developer.fastgrocery.model.notification.NotificationList;
import apps.developer.fastgrocery.model.notification.NotificationResponseData;
import apps.developer.fastgrocery.model.notification.ReadNotification;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static apps.developer.fastgrocery.MainActivity.custPrograssbar;

public class NotificationDetailsActivity extends AppCompatActivity {
    TextView txtTitel;
    TextView txtDate;
    ImageView imgNoti;
    TextView txtDesc;
    User user;
    NotificationList noti;
    SessionManager sessionManager;
    private BaseApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        getSupportActionBar().setTitle("Notification Details");
        if(getSupportActionBar()!=null){       //Back Press Option add
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        txtDesc=(TextView)findViewById(R.id.txt_desc);
        txtDate=(TextView)findViewById(R.id.txt_date);
        txtTitel=(TextView)findViewById(R.id.txt_titel);
        imgNoti=(ImageView)findViewById(R.id.img_noti);
        apiService = RetrofitClient.getClient().create(BaseApiService.class);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(NotificationDetailsActivity.this);
        user = sessionManager.getUserDetails("");
        noti = getIntent().getParcelableExtra("myclass");
        txtTitel.setText("" + noti.getTitle());
        txtDate.setText("" + noti.getDate());
        txtDesc.setText("" + noti.getMsg());

        Glide.with(this).asBitmap().load(RetrofitClient.Base_URL + noti.getImg()).placeholder(R.drawable.empty_noti).into(imgNoti);

        readNotification(noti.getId());

    }

    private void readNotification(String id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uid", user.getId());
        jsonObject.addProperty("nid", id);
        custPrograssbar.PrograssCreate(NotificationDetailsActivity.this);

        apiService.getReadNotification(jsonObject).enqueue(new Callback<ReadNotification>() {
            @Override
            public void onResponse(Call<ReadNotification> call, Response<ReadNotification> response) {
                custPrograssbar.ClosePrograssBar();
                Toast.makeText(NotificationDetailsActivity.this, "" + response.body().getResponseMsg(), Toast.LENGTH_LONG).show();
                if (response.body().getResult().contains("true")) {
                    MainActivity.notificationCount(response.body().getRemainNotification());
                }
            }

            @Override
            public void onFailure(Call<ReadNotification> call, Throwable t) {
                Log.e("error"," --> "+t.toString());
            }
        });
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