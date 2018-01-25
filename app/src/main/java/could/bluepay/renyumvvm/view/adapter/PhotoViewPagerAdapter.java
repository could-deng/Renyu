package could.bluepay.renyumvvm.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.http.bean.PhotoInfo;
import could.bluepay.renyumvvm.widget.photo.PhotoView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * PictureViewpagerActivity上的图片适配器
 */

public class PhotoViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<PhotoInfo> photolist;
    private PhotoViewClick photoViewClick;
    public PhotoViewPagerAdapter(Context context,PhotoViewClick photoViewClick) {
        this.context = context;
        photolist = new ArrayList<>();
        this.photoViewClick = photoViewClick;
    }


    public List<PhotoInfo> getPhotolist(){
        if(photolist == null){
            photolist = new ArrayList<>();
        }
        return photolist;
    }
    public void setPhotolist(List<PhotoInfo> list){
        photolist = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(photolist == null){
            return 0;
        }
        return photolist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView view = new PhotoView(context);
        view.enable();

        view.setScaleType(ImageView.ScaleType.CENTER);
        RequestOptions options = new RequestOptions()
                .error(R.drawable.img_default_meizi);
        Glide.with(context)
                .load(getPhotolist().get(position).url)
                .transition(withCrossFade(500))
//                .crossFade(500)
//                .error(R.drawable.img_default_meizi)
                .apply(options)

                .into(view);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoViewClick.onPhotoViewClick();
            }
        });
        return view;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    public interface PhotoViewClick{
        void onPhotoViewClick();
    }
}
