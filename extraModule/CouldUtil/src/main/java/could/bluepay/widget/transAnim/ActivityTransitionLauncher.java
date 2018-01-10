package could.bluepay.widget.transAnim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

public class ActivityTransitionLauncher {
    private static final String TAG = "TransitionLauncher";

    private final Activity activity;
    private View fromView;
    private Bitmap bitmap;
    private String imagePath;


    private ActivityTransitionLauncher(Activity activity) {
        this.activity = activity;
    }

    public static ActivityTransitionLauncher with(Activity activity) {
        return new ActivityTransitionLauncher(activity);
    }

    public ActivityTransitionLauncher from(View fromView) {
        this.fromView = fromView;
        return this;
    }

    public ActivityTransitionLauncher image(final Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    /**
     * 图片路径
     * */
    public ActivityTransitionLauncher image(String imagePath){
        this.imagePath = imagePath;
        return this;
    }

    /**
     * 创建Transition动画所需的Bundle
     *
     * @param isImageFilePath 是否传入的是SimpleDraweeView所认可的路径字符串
     * */
    private Bundle createBundle(boolean isImageFilePath) {
        if(isImageFilePath){
            return TransitionBundleFactory.createTransitionBundle(activity,fromView,imagePath);
        }else {
            return TransitionBundleFactory.createTransitionBundle(activity, fromView, bitmap);
        }
    }

    public void launch(Intent intent, boolean isImageFilePath) {
        intent.putExtras(createBundle(isImageFilePath));
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }
}
