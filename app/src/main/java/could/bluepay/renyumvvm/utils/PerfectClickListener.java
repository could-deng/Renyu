package could.bluepay.renyumvvm.utils;

import android.view.View;

import java.util.Calendar;

/**
 * 防止一定时间内重复点击
 */

public abstract class PerfectClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_ = 1000;
    private long lastClickTime = 0;
    private int id = -1;
    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        int mId = v.getId();
        if(id!=mId){
            id = mId;
            lastClickTime = currentTime;
            onDoubleClick(v);
            return;
        }
        if(currentTime- lastClickTime >MIN_CLICK_){
            lastClickTime = currentTime;
            onDoubleClick(v);
        }


    }
    protected abstract void onDoubleClick(View view);
}
