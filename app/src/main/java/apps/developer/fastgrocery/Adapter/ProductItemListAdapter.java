package apps.developer.fastgrocery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import apps.developer.fastgrocery.DataBase.DatabaseHelper;
import apps.developer.fastgrocery.DataBase.MyCart;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.ItemClickListener;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.product.Price;
import apps.developer.fastgrocery.model.product.ProductDataList;

import static apps.developer.fastgrocery.Utils.SessionManager.CURRUNCY;
import static apps.developer.fastgrocery.grocery_fragment.ItemListFragment.itemListFragment;

public class ProductItemListAdapter extends RecyclerView.Adapter<ProductItemListAdapter.ProductItemViewHolder> {

    Context context;
    List<ProductDataList> productlist;
    private SessionManager sessionManager;
    ItemClickListener mClickListener;
    public int discount;

    public ProductItemListAdapter(Context context, List<ProductDataList> productlist) {
        this.productlist = productlist;
        this.context = context;
        sessionManager=new SessionManager(context);
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_custome, null);
        return new ProductItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        final ProductDataList datum = productlist.get(position);
        Glide.with(context).load(RetrofitClient.Base_URL + "/" + datum.getProductImage()).thumbnail(Glide.with(context).load(R.drawable.ezgifresize)).into(holder.imgIcon);
        holder.txtTitle.setText("" + datum.getProductName());
        if (!datum.getSellerName().equals("")) {
            holder.sellerName.setText("" + datum.getSellerName());
        } else {
            holder.sellerName.setVisibility(View.GONE);
        }
        if (!datum.getShortDesc().equals("")) {
            holder.shortDesc.setText("" + datum.getShortDesc());
            if (holder.shortDesc.getText().toString().length() < 90) {

            } else {
               makeTextViewResizable(holder.shortDesc, 3, "See More", true);
            }
        } else {
            holder.shortDesc.setVisibility(View.GONE);
        }

        holder.imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //   context.startActivity(new Intent(context, ItemDetailsActivity.class).putExtra("MyClass", datum).putParcelableArrayListExtra("MyList", datum.getPrice()));
            }
        });
         discount = datum.getDiscount();
        if (discount > 0) {
            holder.lvlOffer.setVisibility(View.VISIBLE);
            holder.txtOffer.setText(datum.getDiscount() + "% Off");
        } else {
            holder.lvlOffer.setVisibility(View.GONE);

        }
        setJoinPlayrList(holder.lvlSubitem, datum);
    }



    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class ProductItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle;
        TextView txtOffer;
        TextView sellerName;
        TextView shortDesc;
        LinearLayout lvlSubitem;
        LinearLayout lvlOffer;
        ImageView imgIcon;
        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle=(TextView)itemView.findViewById(R.id.txtTitle);
            txtOffer=(TextView)itemView.findViewById(R.id.txt_offer);
            sellerName=(TextView)itemView.findViewById(R.id.seller_name);
            shortDesc=(TextView)itemView.findViewById(R.id.short_desc);
            imgIcon=(ImageView)itemView.findViewById(R.id.img_icon);
            lvlSubitem=(LinearLayout)itemView.findViewById(R.id.lvl_subitem);
            lvlOffer=(LinearLayout)itemView.findViewById(R.id.lvl_offer);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }



    private void setJoinPlayrList(LinearLayout lnrView, ProductDataList datum) {
        List<Price> priceList = datum.getPrice();
        lnrView.removeAllViews();
        final int[] count = {0};
        final DatabaseHelper helper = new DatabaseHelper(lnrView.getContext());
        if (priceList != null && priceList.size() > 0) {
            for (int i = 0; i < priceList.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(context);

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
                myCart.setWeight(datum.getPrice().get(i).getProductType());
                myCart.setCost(datum.getPrice().get(i).getProductPrice());
                myCart.setDiscount(datum.getDiscount());
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
                        itemListFragment.updateItem();
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
                        itemListFragment.updateItem();
                    }
                });


                txt_gram.setText("" + priceList.get(i).getProductType());

                if (discount > 0) {
                    double  res = (Double.parseDouble(priceList.get(i).getProductPrice()) / 100.0f)* discount;
                    res = Integer.parseInt(priceList.get(i).getProductPrice()) - res;
                    txt_offer.setText(sessionManager.getStringData(CURRUNCY) + priceList.get(i).getProductPrice());
                    txt_offer.setPaintFlags(txt_offer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    txt_price.setText(sessionManager.getStringData(CURRUNCY)  + res);
                } else {
                    txt_offer.setVisibility(View.GONE);
                    txt_price.setText(sessionManager.getStringData(CURRUNCY)  + priceList.get(i).getProductPrice());
                }
                lnrView.addView(view);
            }
        }
    }

    public void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }

        ViewTreeObserver vto = tv.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                     final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public class MySpannable extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(Color.parseColor("#00A55D"));
        }

        @Override
        public void onClick(View widget) {


        }
    }


}
