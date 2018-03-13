package could.bluepay.renyumvvm.bindingAdapter;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.utils.TimeUtils;
import could.bluepay.renyumvvm.view.adapter.UserListFocusAdapter;
import could.bluepay.renyumvvm.widget.GlideCircleTransform;
import could.bluepay.widget.xrecyclerview.XRecyclerView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by bluepay on 2017/11/24.
 */

public class ImgLoadUtil {

    private static ImgLoadUtil instance;

    private ImgLoadUtil() {
    }

    public static ImgLoadUtil getInstance() {
        if (instance == null) {
            instance = new ImgLoadUtil();
        }
        return instance;
    }

    //todo imageView.getContext()会引起内存泄漏？？
    @BindingAdapter("android:showBookImg")
    public static void showBookImg(ImageView imageView, String url) {
        RequestOptions options = new RequestOptions()

                .centerCrop()
                .placeholder(getDefaultPic(2))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(getDefaultPic(2));

        Glide.with(imageView.getContext())
                .load(url)
                .transition(withCrossFade(500))
//                .crossFade(500)
//                .override((int) getResource().getDimens(R.dimen.book_detail_width), (int) CommonUtils.getDimens(R.dimen.book_detail_height))
//                .placeholder(getDefaultPic(2))
//                .error(getDefaultPic(2))
                .apply(options)
                .into(imageView);
    }

    @BindingAdapter("android:showCircleImg")
    public static void showCircleImg(ImageView imageView,String url){
        RequestOptions options = new RequestOptions()
                .bitmapTransform(new GlideCircleTransform(imageView.getContext()))
//                .placeholder(R.drawable.head)
                .error(R.drawable.head);

        Glide.with(imageView.getContext())
                .load(url)
                .transition(withCrossFade(500))
//                .crossFade(500)
//                .bitmapTransform(new GlideCircleTransform(imageView.getContext()))
//                .placeholder(R.drawable.head)
//                .error(R.drawable.head)
                .apply(options)
                .into(imageView);

    }


    private static int getDefaultPic(int type) {
        switch (type) {
            case 0:// 电影
                return R.drawable.img_default_meizi;
        }
        return R.drawable.img_default_meizi;
    }

}
