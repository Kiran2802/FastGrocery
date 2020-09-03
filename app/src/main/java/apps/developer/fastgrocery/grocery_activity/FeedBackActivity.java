package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.CustPrograssbar;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.BaseApiService;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.login_model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private User user;
    private CustPrograssbar custPrograssbar;
    private EditText edFeedback;
    private Button btnSubmit;
    private RatingBar ratingBar;
    private BaseApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        getSupportActionBar().setTitle("FeedBack");
        if(getSupportActionBar()!=null){       //Back Press Option add
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        sessionManager = new SessionManager(FeedBackActivity.this);
        user = sessionManager.getUserDetails("");
        apiService = RetrofitClient.getClient().create(BaseApiService.class);

        custPrograssbar = new CustPrograssbar();
        edFeedback=(EditText)findViewById(R.id.ed_feedback);
        btnSubmit=(Button)findViewById(R.id.btn_submit);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    UserFeedBack();
                }
            }
        });
    }

    private void UserFeedBack() {
        custPrograssbar.PrograssCreate(FeedBackActivity.this);
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        try {
            jsonObject.put("msg", edFeedback.getText().toString());
            jsonObject.put("rate", String.valueOf(ratingBar.getRating()));
            jsonObject.put("uid", user.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        apiService.SenfFeedback((JsonObject) jsonParser.parse(jsonObject.toString())).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                custPrograssbar.ClosePrograssBar();
                try {
                    if (response != null) {
                        try {
                            JSONObject object = new JSONObject(response.toString());
                            Toast.makeText(FeedBackActivity.this, "" + object.getString("ResponseMsg"), Toast.LENGTH_SHORT).show();
                            if (object.getString("Result").equals("true")) {
                                ratingBar.setRating(0.0f);
                                edFeedback.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {

                } 
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(FeedBackActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private boolean validation() {

        if (edFeedback.getText().toString().isEmpty()) {
            edFeedback.setError("Enter FeedBack");
            return false;
        }
        if (String.valueOf(ratingBar.getRating()).equals("0.0")) {
            Toast.makeText(FeedBackActivity.this, "Give Rating", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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