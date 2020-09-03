package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import apps.developer.fastgrocery.Utils.Utiles;
import apps.developer.fastgrocery.Utils.Validation;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.area.AreaDataList;
import apps.developer.fastgrocery.model.area.AreaResponseData;
import apps.developer.fastgrocery.model.login_model.LoginResponseData;
import apps.developer.fastgrocery.model.login_model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static apps.developer.fastgrocery.Utils.SessionManager.AREA;

public class ProfileActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private User user;
    private TextView edUsername,edHoousno,ed_city,txt_Edit,edPinno,edEmail,edAlternatmob,edPassword;
    private Button btn_save;
    String areaSelect;
    CustPrograssbar custPrograssbar;
    private BaseApiService apiService;
    private Spinner spinner;
    private List<AreaDataList> areaList;
    private JsonObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("My Profile");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        sessionManager = new SessionManager(ProfileActivity.this);
        user = sessionManager.getUserDetails("");

        edUsername=(TextView)findViewById(R.id.ed_username);
        edHoousno=(TextView)findViewById(R.id.ed_hoousno);
        ed_city=(TextView)findViewById(R.id.ed_city);
        edPinno=(TextView)findViewById(R.id.ed_pinno);
        edEmail=(TextView)findViewById(R.id.ed_email);
        edAlternatmob=(TextView)findViewById(R.id.ed_alternatmob);
        txt_Edit=(TextView)findViewById(R.id.txt_Edit);

        edUsername.setText("" + user.getName());
        edHoousno.setText("" + user.getHno() +" ," + user.getSociety() +" ," + user.getLandmark());
        ed_city.setText("" + user.getArea());
        edPinno.setText("" + user.getPincode());
        edEmail.setText("" + user.getEmail());
        edAlternatmob.setText("" + user.getMobile());
        txt_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, Update_profile.class));
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