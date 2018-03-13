package could.bluepay.renyumvvm.utils;

import android.content.Context;

/**
 * Created by bluepay on 2017/11/28.
 */

public class ViewUtils {
    public static int dp2px(Context context, int dp){
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp*density);
    }
    public static float sp2px(Context context,int sp){
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return scaledDensity*sp;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
