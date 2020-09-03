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
import apps.developer.fastgrocery.model.home.Catlist;

public class HomeCategoryListAdapter extends RecyclerView.Adapter<HomeCategoryListAdapter.CategoryViewHolder> {
    Context context;
    List<Catlist> categorylist;
    public HomeCategoryListAdapter(Context context, List<Catlist> categorylist) {
        this.context=context;
        this.categorylist=categorylist;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview_row_category_item, null);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final Catlist listData= categorylist.get(position);
        holder.title.setText(listData.getCatname()+ "(" + listData.getCount() + ")");

        Glide.with(context)
                .load(RetrofitClient.Base_URL + "/" + listData.getCatimg())
                .placeholder(R.drawable.ezgifresize)
                .error(R.drawable.lodingimage)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final ImageView thumbnail;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.txt_title);
            thumbnail = itemView.findViewById(R.id.imageView);
        }
    }
}
