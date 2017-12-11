package could.bluepay.couldutils.transAnim;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

//import com.facebook.drawee.view.SimpleDraweeView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;

public class TransitionAnimation {
    public static final Object lock = new Object();
    private static final String TAG = "Transition";
    private static final int MAX_TIME_TO_WAIT = 3000;
    public static WeakReference<Bitmap> bitmapCache;
    public static boolean isImageFileReady = false;

    public static MoveData startAnimation(Context context, final View toView, Bundle transitionBundle, Bundle savedInstanceState, final int duration, final TimeInterpolator interpolator) {
        final TransitionData transitionData = new TransitionData(context, transitionBundle);
        if (transitionData.imageFilePath != null) {
            setImageToView(toView, transitionData.imageFilePath);
        }
        final MoveData moveData = new MoveData();
        moveData.toView = toView;
        moveData.duration = duration;
        if (savedInstanceState == null) {

            ViewTreeObserver observer = toView.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    toView.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screenLocation = new int[2];
                    toView.getLocationOnScreen(screenLocation);
                    moveData.leftDelta = transitionData.thumbnailLeft - screenLocation[0];
                    moveData.topDelta = transitionData.thumbnailTop - screenLocation[1];

                    moveData.widthScale = (float) transitionData.thumbnailWidth / toView.getWidth();
                    moveData.heightScale = (float) transitionData.thumbnailHeight / toView.getHeight();

                    runEnterAnimation(moveData, interpolator);

                    return true;
                }
            });
        }
        return moveData;
    }


    private static void runEnterAnimation(MoveData moveData, TimeInterpolator interpolator) {
        final View toView = moveData.toView;
        toView.setPivotX(0);
        toView.setPivotY(0);
        toView.setScaleX(moveData.widthScale);
        toView.setScaleY(moveData.heightScale);
        toView.setTranslationX(moveData.leftDelta);
        toView.setTranslationY(moveData.topDelta);//设置起始的scale和位置

        toView.animate().setDuration(moveData.duration).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                setInterpolator(interpolator);

    }

    private static void setImageToView(View toView, String imageFilePath) {

        //目前只用于facebook提供的SimpleDraweeView
//        if(toView instanceof SimpleDraweeView){
//            if (!TextUtils.isEmpty(imageFilePath)) {
//                ((SimpleDraweeView)toView).setImageURI(Uri.parse(imageFilePath));
//            }
//        }
        //Glide加载图片
        try {
            if(toView instanceof ImageView){
                Glide.with(toView.getContext())
                        .load(imageFilePath)
                        .crossFade(500)
                        .into((ImageView) toView);
//                ((ImageView)toView).setScaleType(ImageView.ScaleType.CENTER);
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        //加载SD卡的图片文件
//        Bitmap bitmap;
//        if (bitmapCache == null || (bitmap = bitmapCache.get()) == null) {
//            synchronized (lock) {
//                while (!isImageFileReady) {
//                    try {
//                        lock.wait(MAX_TIME_TO_WAIT);
//                    } catch (InterruptedException e) {
//                    }
//                }
//            }
//            // Cant get bitmap by static field
//            bitmap = BitmapFactory.decodeFile(imageFilePath);
//        } else {
//            bitmapCache.clear();
//        }
//        setImageToView(toView, bitmap);

    }

//    private static void setImageToView(View toView, Bitmap bitmap) {
//        if (toView instanceof ImageView) {
//            final ImageView toImageView = (ImageView) toView;
//            toImageView.setImageBitmap(bitmap);
//        } else {
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
//                toView.setBackground(new BitmapDrawable(toView.getResources(), bitmap));
//            } else {
//                toView.setBackgroundDrawable(new BitmapDrawable(toView.getResources(), bitmap));
//            }
//        }
//    }

    public static void startExitAnimation(MoveData moveData, TimeInterpolator interpolator, final Runnable endAction) {
        View view = moveData.toView;
        int duration = moveData.duration;
        int leftDelta = moveData.leftDelta;
        int topDelta = moveData.topDelta;
        float widthScale = moveData.widthScale;
        float heightScale = moveData.heightScale;
        view.animate()
                .setDuration(duration)
                .scaleX(widthScale).scaleY(heightScale)
                .setInterpolator(interpolator).
                translationX(leftDelta).translationY(topDelta);
        view.postDelayed(endAction, duration);
    }
}
