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
}
