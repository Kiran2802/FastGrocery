package apps.developer.fastgrocery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import apps.developer.fastgrocery.DataBase.DatabaseHelper;
import apps.developer.fastgrocery.Utils.CustPrograssbar;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.grocery_activity.AboutActivity;
import apps.developer.fastgrocery.grocery_activity.AddressActivity;
import apps.developer.fastgrocery.grocery_activity.Cart_Activity;
import apps.developer.fastgrocery.grocery_activity.Contact_Us;
import apps.developer.fastgrocery.grocery_activity.FeedBackActivity;
import apps.developer.fastgrocery.grocery_activity.LoginActivity;
import apps.developer.fastgrocery.grocery_activity.MyOrderActivity;
import apps.developer.fastgrocery.grocery_activity.NotificationActivity;
import apps.developer.fastgrocery.grocery_activity.ProfileActivity;
import apps.developer.fastgrocery.grocery_activity.Search;
import apps.developer.fastgrocery.grocery_activity.TramsAndConditionActivity;
import apps.developer.fastgrocery.grocery_fragment.ItemListFragment;
import apps.developer.fastgrocery.model.login_model.User;

import static apps.developer.fastgrocery.grocery_fragment.ItemListFragment.itemListFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    private NavController navController;
    private NavigationView navigationView;
    SessionManager sessionManager;
    User user;
    public static TextView txtNoti;
    public static CustPrograssbar custPrograssbar;
    DatabaseHelper databaseHelper;
    public static TextView txt_countcard;
    public EditText edSearch;
    public LinearLayout lvlActionsearch;
    public ImageView imgNoti;
    public ImageView imgCart;
    public ImageView imgSearch;
    public TextView txtActiontitle;
    AppBarLayout appBarLayout;
    public LinearLayout lvlHidecart;
    public static MainActivity mainActivity = null;
    public TextView User_Name , txtEmail;
    public RelativeLayout rltNoti ,rltCart;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        custPrograssbar = new CustPrograssbar();
        databaseHelper = new DatabaseHelper(MainActivity.this);
        sessionManager = new SessionManager(MainActivity.this);
        user = sessionManager.getUserDetails("");


        rltCart = (RelativeLayout) findViewById(R.id.rlt_cart);
        rltNoti = (RelativeLayout) findViewById(R.id.rlt_noti);
        lvlHidecart = (LinearLayout) findViewById(R.id.lvl_hidecart);
        lvlActionsearch = (LinearLayout) findViewById(R.id.lvl_actionsearch);
        imgNoti = (ImageView) findViewById(R.id.img_noti);
        imgCart = (ImageView) findViewById(R.id.img_cart);
        imgSearch = (ImageView) findViewById(R.id.img_search);
        edSearch = (EditText) findViewById(R.id.ed_search);

        txtActiontitle = (TextView) findViewById(R.id.txt_actiontitle);
        txtNoti = (TextView) findViewById(R.id.txt_noti);
        txt_countcard = (TextView)findViewById(R.id.txt_countcard);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        appBarLayout =(AppBarLayout) findViewById(R.id.appBarLayout);



        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawer);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        User_Name = (TextView)navigationView.getHeaderView(0).findViewById(R.id.txt_drawer_User_Name);
        txtEmail = (TextView)navigationView.getHeaderView(0).findViewById(R.id.txt_drawer_email);

        txtActiontitle.setText("Hello " + user.getName());

        char first = user.getName().charAt(0);
        Log.e("first", "-->" + first);
       // txtfirstl.setText("" + first);
        User_Name.setText("" + user.getName());
        txtEmail.setText("" + user.getEmail());

        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            txt_countcard.setText("0");
        } else {
            txt_countcard.setText("" + res.getCount());
        }

        edSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Search.class));
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // txtActiontitle.setVisibility(View.VISIBLE);
                rltNoti.setVisibility(View.VISIBLE);
                rltCart.setVisibility(View.VISIBLE);
              startActivity(new Intent(MainActivity.this, Cart_Activity.class));
            }
        });
        imgNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
            }
        });

    }


    public static MainActivity getInstance() {
        return mainActivity;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, drawer)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawer.closeDrawers();
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_home:
                navController.navigate(R.id.nav_home);
                break;
            case R.id.nav_myOrder:
                startActivity(new Intent(MainActivity.this, MyOrderActivity.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;

            case R.id.nav_address:
                startActivity(new Intent(MainActivity.this, AddressActivity.class));
                break;
            case R.id.nav_feedback:
                startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
                break;

            case R.id.nav_logout:
                sessionManager.logoutUser();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.nav_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.nav_privacy:
                startActivity(new Intent(MainActivity.this, TramsAndConditionActivity.class));
                break;
            case R.id.nav_share:
                getShareApp();
                break;
            case R.id.nav_contact:
                startActivity(new Intent(MainActivity.this, Contact_Us.class));
                break;
        }
        return false;
    }

    private void getShareApp() {

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }


    public static void notificationCount(int i) {
        if (i <= 0) {
            txtNoti.setVisibility(View.GONE);
        } else {
            txtNoti.setVisibility(View.VISIBLE);
            txtNoti.setText("" + i);
        }
    }

    public void titleChange(String s) {
        txtActiontitle.setText(s);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        edSearch.setText("");
        txtActiontitle.setText("Hello " + user.getName());

        if (itemListFragment == null) {

            rltNoti.setVisibility(View.VISIBLE);
            rltCart.setVisibility(View.VISIBLE);

        }else {
            rltNoti.setVisibility(View.VISIBLE);
            rltCart.setVisibility(View.VISIBLE);
        }

    }
}