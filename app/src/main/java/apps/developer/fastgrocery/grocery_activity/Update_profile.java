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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
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

public class Update_profile extends AppCompatActivity {
    private SessionManager sessionManager;
    private User user;
    private TextInputEditText edUsername,edHoousno,edSociety,edLandmark,edPinno,edEmail,edAlternatmob,edPassword;
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
        setContentView(R.layout.activity_update_profile);

        getSupportActionBar().setTitle("Edit Profile");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        sessionManager = new SessionManager(Update_profile.this);
        user = sessionManager.getUserDetails("");
        apiService = RetrofitClient.getClient().create(BaseApiService.class);


        edUsername=(TextInputEditText)findViewById(R.id.ed_username);
        edHoousno=(TextInputEditText)findViewById(R.id.ed_hoousno);
        edSociety=(TextInputEditText)findViewById(R.id.ed_society);
        edLandmark=(TextInputEditText)findViewById(R.id.ed_landmark);
        edPinno=(TextInputEditText)findViewById(R.id.ed_pinno);
        edEmail=(TextInputEditText)findViewById(R.id.ed_email);
        edAlternatmob=(TextInputEditText)findViewById(R.id.ed_alternatmob);
        edPassword=(TextInputEditText)findViewById(R.id.ed_password);
        btn_save=(Button)findViewById(R.id.btn_save);
        spinner=(Spinner)findViewById(R.id.spinner);
        GetArea();
        setcountaint(user);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaSelect = areaList.get(position).getName();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckValidation()) {
                    UpdateUser();
                }
            }
        });
    }

    private void UpdateUser() {
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("name", edUsername.getText().toString());
            jsonObject.put("hno", edHoousno.getText().toString());
            jsonObject.put("society", edSociety.getText().toString());
            jsonObject.put("area", areaSelect);
            jsonObject.put("landmark", edLandmark.getText().toString());
            jsonObject.put("pincode", edPinno.getText().toString());
            jsonObject.put("email", edEmail.getText().toString());
            jsonObject.put("mobile", edAlternatmob.getText().toString());
            jsonObject.put("password", edPassword.getText().toString());
            jsonObject.put("imei", Utiles.getIMEI(Update_profile.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        apiService.getUpdateProfile((JsonObject) jsonParser.parse(jsonObject.toString())).enqueue(new Callback<LoginResponseData>() {
            @Override
            public void onResponse(Call<LoginResponseData> call, Response<LoginResponseData> response) {

                Toast.makeText(Update_profile.this, "" + response.body().getResponseMsg(), Toast.LENGTH_LONG).show();
                if (response.body().getResult().equals("true")) {
                    sessionManager.setUserDetails("", response.body().getUser());
                    sessionManager.setIntData("dcharge", Integer.parseInt(response.body().getDCharge()));
                    startActivity(new Intent(Update_profile.this, ProfileActivity.class));
                    finish();
                }
            }
            @Override
            public void onFailure(Call<LoginResponseData> call, Throwable t) {
                Toast.makeText(Update_profile.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setcountaint(User user) {
        edUsername.setText("" + user.getName());
        edHoousno.setText("" + user.getHno());
        edSociety.setText("" + user.getSociety());
//        edArea.setText("" + user.getArea());
        edPinno.setText("" + user.getPincode());
        edLandmark.setText("" + user.getLandmark());
        edEmail.setText("" + user.getEmail());
        edAlternatmob.setText("" + user.getMobile());
        edPassword.setText("" + user.getPassword());
    }

    private void GetArea() {
        if (areaList != null && areaList.size() > 0) {
            areaList.clear();
        }
        jsonObject = new JsonObject();
        apiService.getArea(jsonObject).enqueue(new Callback<AreaResponseData>() {
            @Override
            public void onResponse(Call<AreaResponseData> call, Response<AreaResponseData> response) {

                if (response.body().getResult().contains("true")){

                    areaList=response.body().getData();
                    List<String> Arealist = new ArrayList<>();
                    for (int i = 0; i < areaList.size(); i++) {
                        if (areaList.get(i).getStatus().equalsIgnoreCase("1")) {
                            Arealist.add(areaList.get(i).getName());
                        }
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Update_profile.this, android.R.layout.simple_dropdown_item_1line, Arealist);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                    spinner.setSelection(dataAdapter.getPosition(sessionManager.getStringData(AREA)));
                }
            }

            @Override
            public void onFailure(Call<AreaResponseData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean CheckValidation() {
        boolean ret = true;
        if (!Validation.hasText(edUsername)) ret = false;
        if (!Validation.hasText(edHoousno)) ret = false;
        if (!Validation.hasText(edSociety)) ret = false;
        if (!Validation.hasText(edLandmark)) ret = false;
        if (!Validation.isPhoneNumber(edAlternatmob, true)) ret = false;
        if (!Validation.isPassword(edPassword, true)) ret = false;
        if (!Validation.isPinCode(edPinno, true)) ret = false;
        if (!Validation.isEmailAddress(edEmail, true)) ret = false;
        return ret;

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