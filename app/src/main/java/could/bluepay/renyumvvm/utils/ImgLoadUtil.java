package could.bluepay.renyumvvm.utils;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.widget.GlideCircleTransform;

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
        Glide.with(imageView.getContext())
                .load(url)
                .crossFade(500)
//                .override((int) getResource().getDimens(R.dimen.book_detail_width), (int) CommonUtils.getDimens(R.dimen.book_detail_height))
                .placeholder(getDefaultPic(2))
                .error(getDefaultPic(2))
                .into(imageView);
    }

    @BindingAdapter("android:showCircleImg")
    public static void showCircleImg(ImageView imageView,String url){
        Glide.with(imageView.getContext())
                .load(url)
                .crossFade(500)
                .bitmapTransform(new GlideCircleTransform(imageView.getContext()))
                .placeholder(R.drawable.head)
                .error(R.drawable.head)
                .into(imageView);

    }



    @BindingAdapter("android:ttext")
    public static void setText(TextView tv,int text){
        tv.setText(text+"");
    }

    @BindingAdapter({"android:inviteShot","android:inviteCity"})
    public static void setInviteState(TextView tv,int shot,String city){
        if(shot == 2){
            tv.setText(city+"-"+"可约拍");
            tv.setTextColor(tv.getContext().getResources().getColor(R.color.font_color_41));
        }else{
            tv.setText(city+"-"+"暂停约拍");
            tv.setTextColor(tv.getContext().getResources().getColor(R.color.font_color_7));
        }
    }

    @BindingAdapter({"android:inviteShot","android:startTime","android:endTime"})
    public static void setInviteTime(TextView tv,int shot,String shotStart,String shotEnd){
        if(TextUtils.isEmpty(shotStart)|| TextUtils.isEmpty(shotEnd)){
            return;
        }
        if(shot == 2){
            tv.setVisibility(View.VISIBLE);
            tv.setText(TimeUtils.getMonthDayDate(shotStart)+"-"+TimeUtils.getMonthDayDate(shotEnd));
        }else{
            tv.setVisibility(View.GONE);
        }
    }



    private static int getDefaultPic(int type) {
        switch (type) {
            case 0:// 电影
                return R.drawable.img_default_meizi;
        }
        return R.drawable.img_default_meizi;
    }

}
