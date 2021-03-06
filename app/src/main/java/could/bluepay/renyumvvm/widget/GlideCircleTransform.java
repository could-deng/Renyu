package could.bluepay.renyumvvm.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Glide的Bitmap转化方法
 * 将图片转为圆形图片
 */

public class GlideCircleTransform extends BitmapTransformation {

//    private static final String ID = "com.bumptech.glide.transformations.FillSpace";
//    private static final String ID_BYTES = ID.getBytes(STRING_CHARSET_NAME);

    public GlideCircleTransform(Context context) {
        super(context);
    }


    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return getCircleBitmap(pool,toTransform);
    }

    private Bitmap getCircleBitmap(BitmapPool pool,Bitmap toTransform){
        if(pool == null){
            return null;
        }
        int size = Math.min(toTransform.getHeight(),toTransform.getWidth());

        int x = (toTransform.getWidth()-size)/2;
        int y = (toTransform.getHeight()-size)/2;
        //要的bitmap
        Bitmap square = Bitmap.createBitmap(toTransform,x,y,size,size);


        Bitmap b_resource = pool.get(size,size, Bitmap.Config.ARGB_8888);
        if(b_resource == null){
            b_resource = Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(b_resource);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(square, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        float r = size/2;
        canvas.drawCircle(r,r,r,paint);
        square.recycle();

        return b_resource;
    }

//    @Override
//    public String getId() {
//        return getClass().getName();
//    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof GlideCircleTransform;
//        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();

    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest){
        try {
            messageDigest.update(getClass().getName().getBytes(STRING_CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
