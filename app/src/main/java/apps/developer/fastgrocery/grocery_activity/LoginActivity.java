package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import apps.developer.fastgrocery.MainActivity;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.CustPrograssbar;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.Utils.Utiles;
import apps.developer.fastgrocery.Utils.Validation;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.RestResponse;
import apps.developer.fastgrocery.model.login_model.LoginResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_username;
    private EditText ed_password;
    private Button btnForgotPassword;
    private Button btnSignUP;
    private Button btnlogin;
    private BaseApiService apiService;
    private CustPrograssbar custPrograssbar;
    private SessionManager sessionManager;
    private Dialog dialog;
    private EditText edt_Forgot_Password;
    private Button btnSend_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        apiService = RetrofitClient.getClient().create(BaseApiService.class);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(LoginActivity.this);

        ed_username=(EditText)findViewById(R.id.ed_username);
        ed_password=(EditText)findViewById(R.id.ed_password);
        btnSignUP=(Button)findViewById(R.id.btnSignUP);
        btnForgotPassword=(Button)findViewById(R.id.btnForgotPassword);
        btnlogin=(Button)findViewById(R.id.btnlogin);

        btnForgotPassword.setOnClickListener(this);
        btnSignUP.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnlogin:
                if (CheckValidation()) {
                    LoginUser();
                }
                break;
            case R.id.btnSignUP:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.btnForgotPassword:
                loadDialog();
                break;
        }

    }

    private void LoginUser() {
        custPrograssbar.PrograssCreate(LoginActivity.this);
         String mobile =ed_username.getText().toString();
         String password =ed_password.getText().toString();
         String imei = Utiles.getIMEI(LoginActivity.this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile", mobile);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("imei", imei);

       apiService.getLogin(jsonObject).enqueue(new Callback<LoginResponseData>() {
           @Override
           public void onResponse(Call<LoginResponseData> call, Response<LoginResponseData> response) {
               custPrograssbar.ClosePrograssBar();
               Toast.makeText(LoginActivity.this, "" + response.body().getResponseMsg(), Toast.LENGTH_LONG).show();

               if (response.body().getResult().equals("true")) {
                   sessionManager.setUserDetails("", response.body().getUser());
                   sessionManager.setIntData("dcharge", Integer.parseInt(response.body().getDCharge()));
                   startActivity(new Intent(LoginActivity.this, MainActivity.class));
                   finish();
               }
           }

           @Override
           public void onFailure(Call<LoginResponseData> call, Throwable t) {
               Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
           }
       });
    }

    private void loadDialog() {
        dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.pop_up_dialog_forgot_password);
        dialog.setCancelable(true);

        edt_Forgot_Password = dialog.findViewById(R.id.edt_Forgot_Password);
        btnSend_Password = (Button)dialog.findViewById(R.id.btnSend_Password);

        btnSend_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ckeckValidation()){
                    SendForgotPassword();
                }
            }

            private void SendForgotPassword() {
                JsonObject  jsonObject = new JsonObject();
                jsonObject.addProperty("email", edt_Forgot_Password.getText().toString());
                custPrograssbar.PrograssCreate(LoginActivity.this);

                apiService.getForgot(jsonObject).enqueue(new Callback<RestResponse>() {
                    @Override
                    public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                        custPrograssbar.ClosePrograssBar();
                        Toast.makeText(LoginActivity.this, "" + response.body().getResponseMsg(), Toast.LENGTH_LONG).show();
                        if (response.body().getResult().contains("true")) {
                            sessionManager.setBooleanData("forgot", true);
                            dialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, ForgotPassword.class));

                        }
                    }

                    @Override
                    public void onFailure(Call<RestResponse> call, Throwable t) {
                        Log.e("error"," --> "+t.toString());
                    }
                });
            }

            private boolean ckeckValidation() {
                boolean ret = true;
                if (!Validation.isPassword(edt_Forgot_Password, true)) ret = false;
                return ret;
            }
        });


        dialog.show();
    }


    private boolean CheckValidation() {
        boolean ret = true;
        if (!Validation.hasText(ed_username)) ret = false;
        if (!Validation.isPassword(ed_password, true)) ret = false;
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