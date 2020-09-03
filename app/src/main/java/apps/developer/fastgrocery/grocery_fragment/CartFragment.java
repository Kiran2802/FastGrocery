package apps.developer.fastgrocery.grocery_fragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import apps.developer.fastgrocery.DataBase.DatabaseHelper;
import apps.developer.fastgrocery.DataBase.MyCart;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.RetrofitClient;

import static apps.developer.fastgrocery.Utils.SessionManager.CURRUNCY;


public class CartFragment extends Fragment {
    LinearLayout lvlmucard;
    DatabaseHelper databaseHelper;
    TextView txtItem;
    TextView totleAmount;
    TextView txtCountinue;
    SessionManager sessionManager;
    LinearLayout lvlNodata;
    TextView txtNodatatitle;
    List<MyCart> myCarts;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart, container, false);
        txtNodatatitle=(TextView)view.findViewById(R.id.txt_nodatatitle);
        txtCountinue=(TextView)view.findViewById(R.id.txt_countinue);
        totleAmount=(TextView)view.findViewById(R.id.totleAmount);
        txtItem=(TextView)view.findViewById(R.id.txt_item);
        lvlNodata=(LinearLayout)view.findViewById(R.id.lvl_nodata);
        lvlmucard=(LinearLayout)view.findViewById(R.id.lvlmucard);
        databaseHelper = new DatabaseHelper(getActivity());
        sessionManager = new SessionManager(getActivity());
        myCarts = new ArrayList<>();

        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            // show message
            lvlNodata.setVisibility(View.VISIBLE);
            txtCountinue.setVisibility(View.GONE);
            txtNodatatitle.setText("Cart Empty");
        }
        while (res.moveToNext()) {
            MyCart rModel = new MyCart();
            rModel.setID(res.getString(0));
            rModel.setPID(res.getString(1));
            rModel.setImage(res.getString(2));
            rModel.setTitle(res.getString(3));
            rModel.setWeight(res.getString(4));
            rModel.setCost(res.getString(5));
            rModel.setQty(res.getString(6));
            rModel.setDiscount(res.getInt(7));
            myCarts.add(rModel);
        }
      setJoinPlayrList(lvlmucard, myCarts);

        return view;
    }

    double total = 0;

    public void setJoinPlayrList(final LinearLayout lnrView, final List<MyCart> myCarts) {

        lnrView.removeAllViews();
        final int[] count = {0};
        final double[] totalAmount = {0};
        final DatabaseHelper helper = new DatabaseHelper(lnrView.getContext());
        if (myCarts != null && myCarts.size() > 0) {
            for (int i = 0; i < myCarts.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final MyCart cart = myCarts.get(i);
                final View view = inflater.inflate(R.layout.custome_mycard, null);
                ImageView img_icon = view.findViewById(R.id.img_icon);
                ImageView img_delete = view.findViewById(R.id.img_delete);
                TextView txt_title = view.findViewById(R.id.txt_title);
                TextView txt_gram = view.findViewById(R.id.txt_gram);
                TextView txt_price = view.findViewById(R.id.txt_price);
                final TextView txtcount = view.findViewById(R.id.txtcount);
                final ImageView img_mins = view.findViewById(R.id.img_mins);
                ImageView img_plus = view.findViewById(R.id.img_plus);

                Glide.with(getContext()).load(RetrofitClient.Base_URL + "/" + cart.getImage()).thumbnail(Glide.with(getActivity()).load(R.drawable.lodingimage)).into(img_icon);
                double res = (Double.parseDouble(cart.getCost()) * myCarts.get(i).getDiscount()) / 100;
                res = Double.parseDouble(cart.getCost()) - res;
                txt_gram.setText(" " + cart.getWeight());
                txt_price.setText(sessionManager.getStringData(CURRUNCY)  + res);
                txt_title.setText("" + cart.getTitle());

                final MyCart myCart = new MyCart();
                myCart.setPID(cart.getPID());
                myCart.setImage(cart.getImage());
                myCart.setTitle(cart.getTitle());
                myCart.setWeight(cart.getWeight());
                myCart.setCost(cart.getCost());
                myCart.setDiscount(cart.getDiscount());


                int qrt = helper.getCard(myCart.getPID(), myCart.getCost());
                if (qrt != -1) {
                    count[0] = qrt;
                    txtcount.setText("" + count[0]);
                    txtcount.setVisibility(View.VISIBLE);
                } else {
                    txtcount.setVisibility(View.INVISIBLE);
                    img_mins.setVisibility(View.INVISIBLE);
                }

                double  ress = (Double.parseDouble(myCart.getCost()) / 100.0f)* myCart.getDiscount();

                ress = Integer.parseInt(myCart.getCost()) - ress;
                double temp = ress * qrt;
                totalAmount[0] = totalAmount[0] + temp;

                img_mins.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count[0] = Integer.parseInt(txtcount.getText().toString());
                        count[0] = count[0] - 1;
                        if (count[0] <= 0) {
                            txtcount.setVisibility(View.INVISIBLE);
                            img_mins.setVisibility(View.INVISIBLE);
                            txtcount.setText("" + count[0]);
                            helper.deleteRData(myCart.getPID(), myCart.getCost());
                            lnrView.removeView(view);
                            myCarts.remove(cart);
                            totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getCost());


                            Toast.makeText(getActivity(), "" + myCart.getTitle() + " " + myCart.getWeight() + " is Remove", Toast.LENGTH_LONG).show();
                            if (totalAmount[0] == 0) {
                                txtCountinue.setVisibility(View.GONE);
//                                getFragmentManager().popBackStackImmediate();
                            }
                            updateItem();
                        } else {
                            txtcount.setVisibility(View.VISIBLE);
                            txtcount.setText("" + count[0]);
                            myCart.setQty(String.valueOf(count[0]));
                            totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getCost());


                            helper.insertData(myCart);
                            updateItem();
                        }
                    }
                });
                img_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtcount.setVisibility(View.VISIBLE);
                        img_mins.setVisibility(View.VISIBLE);
                        count[0] = Integer.parseInt(txtcount.getText().toString());
                        totalAmount[0] = totalAmount[0] + Double.parseDouble(myCart.getCost());
                        count[0] = count[0] + 1;
                        txtcount.setText("" + count[0]);
                        myCart.setQty(String.valueOf(count[0]));
                        Log.e("Insert", "--> " + helper.insertData(myCart));
                        updateItem();
                    }
                });
                img_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog myDelete = new AlertDialog.Builder(getActivity())
                                .setTitle("Delete")
                                .setMessage("Do you want to Delete")
                                .setIcon(R.drawable.ic_delete)

                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //your deleting code
                                        dialog.dismiss();
                                        totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getCost());
                                        helper.deleteRData(myCart.getPID(), myCart.getCost());
                                        myCarts.remove(cart);
                                        updateItem();

                                        lnrView.removeView(view);

                                    }

                                })
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();

                                    }
                                })
                                .create();
                        myDelete.show();
                    }
                });

                lnrView.addView(view);
            }
        }
        total = totalAmount[0];
//        totleAmount.setText(myCarts.size() + "x " + total);
        updateItem();
    }

    public void updateItem() {
        Cursor res = databaseHelper.getAllData();

//            txtItem.setText("Items : " + res.getCount());
        double totalRs = 0;
        double ress = 0;
        double totalItem = 0;
        if(res.getCount()==0){
            lvlNodata.setVisibility(View.VISIBLE);
            txtCountinue.setVisibility(View.GONE);
            txtNodatatitle.setText("Cart Empty");
        }
        while (res.moveToNext()) {
            MyCart rModel = new MyCart();
            rModel.setCost(res.getString(5));
            rModel.setQty(res.getString(6));
            rModel.setDiscount(res.getInt(7));
            ress = (Double.parseDouble(res.getString(5)) * rModel.getDiscount()) / 100;
            ress = Double.parseDouble(res.getString(5)) - ress;
            double temp = Integer.parseInt(res.getString(6)) * ress;
            totalRs = totalRs + temp;
            totalItem = totalItem + Integer.parseInt(res.getString(6));

        }

        total = Double.parseDouble(String.valueOf(totalRs));

        txtItem.setText("Total Item:(" + new DecimalFormat("##.##").format(totalItem) + ")");
        totleAmount.setText(sessionManager.getStringData(CURRUNCY) + total);
    }


}