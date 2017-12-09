package could.bluepay.renyumvvm.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.http.bean.PhotoInfo;
import could.bluepay.renyumvvm.widget.photo.PhotoView;

/**
 * Created by bluepay on 2017/12/8.
 */

public class PhotoViewPagerAdapter extends PagerAdapter {

    private Context context;
    List<PhotoInfo> photolist;
    public PhotoViewPagerAdapter(Context context) {
        this.context = context;
        photolist = new ArrayList<>();
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

        Glide.with(context)
                .load(getPhotolist().get(position).url)
                .crossFade(500)
                .error(R.drawable.img_default_meizi)
                .into(view);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}