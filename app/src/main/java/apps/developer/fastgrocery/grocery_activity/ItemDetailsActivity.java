package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import apps.developer.fastgrocery.DataBase.DatabaseHelper;
import apps.developer.fastgrocery.DataBase.MyCart;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.home.Price;
import apps.developer.fastgrocery.model.home.Productlist;
import apps.developer.fastgrocery.model.product.ProductDataList;

import static apps.developer.fastgrocery.Utils.SessionManager.CURRUNCY;
import static apps.developer.fastgrocery.grocery_fragment.ItemListFragment.itemListFragment;

public class ItemDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private SessionManager sessionManager;
    private DatabaseHelper databaseHelper;
     Productlist productItem;
     ArrayList<Price> priceslist;
    ImageView imgP; ImageView imgBack;
    ImageView imgCart;
    TextView txtTcount;
    RelativeLayout lvlCart; TextView txtTitle;
    TextView txtDesc;
    LinearLayout lvlPricelist;
    Button btnAddtocart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        sessionManager=new SessionManager(this);
        databaseHelper = new DatabaseHelper(ItemDetailsActivity.this);
        getSupportActionBar().setTitle("Product Details");
        if(getSupportActionBar()!=null){       //Back Press Option add
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        productItem = (Productlist) getIntent().getParcelableExtra("MyClass");
        priceslist = getIntent().getParcelableArrayListExtra("MyList");
        imgP=(ImageView)findViewById(R.id.imgP);
        imgCart=(ImageView)findViewById(R.id.img_cart);
        txtTcount=(TextView)findViewById(R.id.txt_tcount);
        txtTitle=(TextView)findViewById(R.id.txt_title);
        txtDesc=(TextView)findViewById(R.id.txt_desc);
        lvlCart=(RelativeLayout)findViewById(R.id.lvl_cart);
        lvlPricelist=(LinearLayout)findViewById(R.id.lvl_pricelist);
        btnAddtocart=(Button)findViewById(R.id.btn_addtocart);

        if (productItem != null) {
            txtTitle.setText("" + productItem.getProductName());
            txtDesc.setText("" + productItem.getShortDesc());
            Glide.with(this).load(RetrofitClient.Base_URL + "/" + productItem.getProductImage()).thumbnail(Glide.with(this).load(R.drawable.ezgifresize)).into(imgP);
            setJoinPlayrList(lvlPricelist, productItem, priceslist);
            updateItem();
        }
        btnAddtocart.setOnClickListener(this);
        lvlCart.setOnClickListener(this);


    }



    private void setJoinPlayrList(LinearLayout lnrView, Productlist datum, ArrayList<Price> priceList) {
        lnrView.removeAllViews();
        final int[] count = {0};
        final DatabaseHelper helper = new DatabaseHelper(lnrView.getContext());
        if (priceList != null && priceList.size() > 0) {
            for (int i = 0; i < priceList.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(this);

                View view = inflater.inflate(R.layout.custome_prize, null);

                TextView txt_price = view.findViewById(R.id.txt_price);
                TextView txt_gram = view.findViewById(R.id.txt_gram);
                TextView txt_offer = view.findViewById(R.id.txt_offer);
                final TextView txtcount = view.findViewById(R.id.txtcount);
                final LinearLayout img_mins = view.findViewById(R.id.img_mins);
                LinearLayout img_plus = view.findViewById(R.id.img_plus);
                final MyCart myCart = new MyCart();
                myCart.setPID(datum.getId());
                myCart.setImage(datum.getProductImage());
                myCart.setTitle(datum.getProductName());
                myCart.setWeight(priceList.get(i).getProductType());
                myCart.setCost(priceList.get(i).getProductPrice());
                myCart.setDiscount(datum.getmDiscount());
                int qrt = helper.getCard(myCart.getPID(), myCart.getCost());
                if (qrt != -1) {
                    count[0] = qrt;
                    txtcount.setText("" + count[0]);
                    txtcount.setVisibility(View.VISIBLE);
                } else {
                    txtcount.setVisibility(View.VISIBLE);
                    img_mins.setVisibility(View.VISIBLE);
                }
                img_mins.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        count[0] = Integer.parseInt(txtcount.getText().toString());

                        count[0] = count[0] - 1;
                        if (count[0] <= 0) {
                            img_mins.setVisibility(View.VISIBLE);
                            txtcount.setText("0");
                            txtcount.setVisibility(View.VISIBLE);
                            helper.deleteRData(myCart.getPID(), myCart.getCost());
                        } else {
                            txtcount.setVisibility(View.VISIBLE);
                            txtcount.setText("" + count[0]);
                            myCart.setQty(String.valueOf(count[0]));
                            Log.e("INsert", "--> " + helper.insertData(myCart));
                        }
                        if (itemListFragment != null)
                            itemListFragment.updateItem();
                        updateItem();
                    }
                });

                img_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtcount.setVisibility(View.VISIBLE);
                        img_mins.setVisibility(View.VISIBLE);
                        count[0] = Integer.parseInt(txtcount.getText().toString());

                        count[0] = count[0] + 1;
                        txtcount.setText("" + count[0]);
                        myCart.setQty(String.valueOf(count[0]));
                        Log.e("INsert", "--> " + helper.insertData(myCart));
                        if (itemListFragment != null)
                            itemListFragment.updateItem();
                        updateItem();
                    }
                });


                txt_gram.setText("" + priceList.get(i).getProductType());


                if (datum.getmDiscount() > 0) {
                    double res = (Double.parseDouble(priceList.get(i).getProductPrice()) / 100.0f) * datum.getmDiscount();
                    res = Integer.parseInt(priceList.get(i).getProductPrice()) - res;
                    txt_offer.setText(sessionManager.getStringData(CURRUNCY) + priceList.get(i).getProductPrice());
                    txt_offer.setPaintFlags(txt_offer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    txt_price.setText(sessionManager.getStringData(CURRUNCY) + res);
                } else {
                    txt_offer.setVisibility(View.GONE);
                    txt_price.setText(sessionManager.getStringData(CURRUNCY) + priceList.get(i).getProductPrice());
                }
                lnrView.addView(view);
            }

        }

    }

    public void updateItem() {
        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            txtTcount.setText("0");

        } else {
            txtTcount.setText("" + res.getCount());
        }
    }

    public void fragment() {
        SessionManager.ISCART = true;
        finish();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lvl_cart:
                fragment();
                break;
            case R.id.btn_addtocart:
                finish();
                break;
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