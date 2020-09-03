package apps.developer.fastgrocery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.category.SubcategoryDataList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {
    Context context;
    List<SubcategoryDataList> subcategorylist;

    public SubCategoryAdapter(Context context, List<SubcategoryDataList> subcategorylist) {
        this.context=context;
        this.subcategorylist=subcategorylist;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview_row_category_item, null);
        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        SubcategoryDataList category = subcategorylist.get(position);

        holder.title.setText(category.getName() + "(" + category.getCount() + ")");
        Glide.with(context).load(RetrofitClient.Base_URL + "/" + category.getImg()).thumbnail(Glide.with(context).load(R.drawable.ezgifresize)).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return subcategorylist.size();
    }

    public class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail, overflow;

        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.txt_title);
            thumbnail = itemView.findViewById(R.id.imageView);
        }
    }
}
