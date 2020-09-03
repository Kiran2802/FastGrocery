package apps.developer.fastgrocery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import java.util.List;

import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.api.RetrofitClient;
import apps.developer.fastgrocery.model.home.Banner;

public class SilderAdapter extends PagerAdapter {

    private List<Banner> bannerDataList;
      Context context;

    public SilderAdapter(Context context, List<Banner> bannerDataList) {
        this.bannerDataList = bannerDataList;
        this.context=context;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_list,container,false);
        final ImageView banner=view.findViewById(R.id.imageView);

        Glide.with(context).load(RetrofitClient.Base_URL + "/" + bannerDataList.get(position).getBimg()).placeholder(R.drawable.empty).into(banner);

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Intent intent=new Intent(context, BannerActivity.class);
                intent.putExtra("bannerlink",bannerDataList.get(position).getBimg());
                context.startActivity(intent);*/
                Toast.makeText(context, "Slider"+bannerDataList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });


        container.addView(view,0);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return bannerDataList.size();
    }
}

