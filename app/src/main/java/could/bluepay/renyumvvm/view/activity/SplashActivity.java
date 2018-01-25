package could.bluepay.renyumvvm.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;
import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.common.PrefsHelper;
import could.bluepay.renyumvvm.databinding.ActivitySplashBinding;

/**
 * 闪屏界面
 */

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    boolean bStartingMainActivity;

    @Override
    protected int setContent() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myHandler = new MyHandler(this);
        now_time+=DELAYED;
        myHandler.sendEmptyMessageDelayed(UpdateProgress,DELAYED);
    }

    private int now_time = 0;
    private MyHandler myHandler;
    public static final int UpdateProgress =111;
    public static final int DELAYED = 200;
    public static final int FULL_TIME = 3000;

    public static class MyHandler extends Handler{
        private WeakReference<SplashActivity> mActivities;
        public MyHandler(SplashActivity activity){
            mActivities = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SplashActivity.UpdateProgress:
                    (mActivities.get()).updateProgress();
                    break;
            }
        }
    }
    private void updateProgress(){
        if(now_time>=FULL_TIME){
            startNextActivity();
        }else{
            now_time+=DELAYED;
            myHandler.sendEmptyMessageDelayed(SplashActivity.UpdateProgress,DELAYED);
        }
    }

    /**
     * 根据是否登录,启动主界面或登录界面
     */
    private void startNextActivity() {
        if (bStartingMainActivity) return;
        bStartingMainActivity = true;
        Intent intent;
        int loginType = PrefsHelper.with(this, Config.PREFS_USER).readInt(Config.SP_KEY_LAST_LOGIN_TYPE, -1);
        if (loginType > 0) {//当前是已登录状态,启动主界面
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

}
