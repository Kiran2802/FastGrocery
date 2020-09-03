package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import apps.developer.fastgrocery.MainActivity;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.Utils.Utiles;
import apps.developer.fastgrocery.model.login_model.User;

public class splashscreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();
        sessionManager = new SessionManager(splashscreen.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
               if (Utiles.isNetworkAvailable(splashscreen.this)) {
                    User user = sessionManager.getUserDetails("");
                    if (user != null) {
                        Intent i = new Intent(splashscreen.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(splashscreen.this, InfoActivity.class);
                        startActivity(i);
                    }
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

    }
}