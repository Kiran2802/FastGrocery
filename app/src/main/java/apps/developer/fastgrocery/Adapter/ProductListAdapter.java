package apps.developer.fastgrocery.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import apps.developer.fastgrocery.DataBase.DatabaseHelper;
import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.ProductItemClickListener;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.home.Productlist;

import static apps.developer.fastgrocery.Utils.SessionManager.CURRUNCY;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.AllProductViewHolder> {


    Context context;
    List<Productlist> productList;
    SessionManager sessionManager;
    DatabaseHelper helper;
    ProductItemClickListener mClickListener;


    public ProductListAdapter(Context context, List<Productlist> productList, ProductItemClickListener mClickListener) {
        this.context=context;
        this.productList=productList;
        sessionManager = new SessionManager(context);
        helper = new DatabaseHelper(context);
        this.mClickListener=mClickListener;
    }


    @NonNull
    @Override
    public AllProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.releteditem_all_custome, null);
        return new AllProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductViewHolder holder, int position) {
        holder.priceoofer.setPaintFlags(holder.priceoofer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Productlist datum = productList.get(position);
        Glide.with(context).load(RetrofitClient.Base_URL + "/" + datum.getProductImage()).thumbnail(Glide.with(context).load(R.drawable.lodingimage)).into(holder.imgIcon);

        holder.txtTitle.setText("" + datum.getProductName());
        if (datum.getmDiscount() > 0) {
            double res = (Double.parseDouble(datum.getPrice().get(0).getProductPrice()) / 100.0f) * datum.getmDiscount();
            res = Integer.parseInt(datum.getPrice().get(0).getProductPrice()) - res;
            holder.priceoofer.setText(sessionManager.getStringData(CURRUNCY) + datum.getPrice().get(0).getProductPrice());
            holder.txtPrice.setText(sessionManager.getStringData(CURRUNCY) + res);
            holder.lvlOffer.setVisibility(View.VISIBLE);
            holder.txtOffer.setText(datum.getmDiscount() + "% Off");
        } else {
            holder.lvlOffer.setVisibility(View.GONE);
            holder.priceoofer.setVisibility(View.GONE);
            holder.txtPrice.setText(sessionManager.getStringData(CURRUNCY) + datum.getPrice().get(0).getProductPrice());
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class AllProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle;
        TextView txtOffer;
        TextView txtPrice;
        TextView priceoofer;
        LinearLayout lvlSubitem;
        LinearLayout lvlOffer;
        ImageView imgIcon;
      //  LinearLayout lvlCardbg;
      //  ImageView imgCard;

        public AllProductViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle=itemView.findViewById(R.id.txtTitle);
            txtOffer=itemView.findViewById(R.id.txt_offer);
            txtPrice=itemView.findViewById(R.id.price);
            priceoofer=itemView.findViewById(R.id.priceoofer);
            lvlSubitem=itemView.findViewById(R.id.lvl_subitem);
            lvlOffer=itemView.findViewById(R.id.lvl_offer);
            imgIcon=itemView.findViewById(R.id.img_icon);
          //  lvlCardbg=itemView.findViewById(R.id.lvl_cardbg);
          //  imgCard=itemView.findViewById(R.id.img_card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemClick(productList.get(getAdapterPosition()), getAdapterPosition());
        }
    }
}
