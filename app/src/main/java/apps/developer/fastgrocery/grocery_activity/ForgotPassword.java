package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.CustPrograssbar;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.Utils.Validation;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.RestResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private CustPrograssbar custPrograssbar;
    private SessionManager sessionManager;
    private TextInputEditText ed_phone,edPassword,edConpassword,edPin;
    private Button btnSubmit;
    private TextView btnReenter;

    private BaseApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        apiService = RetrofitClient.getClient().create(BaseApiService.class);
        custPrograssbar = new CustPrograssbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(ForgotPassword.this);

        edPassword = (TextInputEditText)findViewById(R.id.ed_password);
        edConpassword = (TextInputEditText)findViewById(R.id.ed_conpassword);
        edPin = (TextInputEditText)findViewById(R.id.ed_pin);
        btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnReenter = (TextView)findViewById(R.id.btn_reenter);


        if (sessionManager.getBooleanData("forgot")) {
            btnSubmit.setVisibility(View.VISIBLE);
        }

        btnSubmit.setOnClickListener(this);
        btnReenter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reenter:
               finish();
                break;
            case R.id.btn_submit:
                if (CheckValidation()) {
                    String edPass =edPassword.getText().toString();
                    String edConpass =edConpassword.getText().toString();
                    if(!edPass.equals(edConpass)){
                        Toast.makeText(this, "Passwords  not match", Toast.LENGTH_SHORT).show();
                    }else {
                        SetPassword();
                    }
                }
                break;
        }
    }

    private void SetPassword() {
        JsonObject  jsonObject = new JsonObject();
        jsonObject.addProperty("pin", edPin.getText().toString());
        jsonObject.addProperty("password", edPassword.getText().toString());
        custPrograssbar.PrograssCreate(ForgotPassword.this);

        apiService.getPinmatch(jsonObject).enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                custPrograssbar.ClosePrograssBar();
                Toast.makeText(ForgotPassword.this, "" + response.body().getResponseMsg(), Toast.LENGTH_LONG).show();
                if (response.body().getResult().contains("true")) {
                    sessionManager.setBooleanData("forgot", false);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable e) {
                Log.e("error"," --> "+e.toString());
            }
        });
    }

    private boolean CheckValidation() {
        boolean ret = true;
        if (!Validation.hasText(edPin)) ret = false;
        if (!Validation.isPassword(edPassword, true)) ret = false;
        if (!Validation.isPassword(edConpassword, true)) ret = false;

        return ret;

    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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