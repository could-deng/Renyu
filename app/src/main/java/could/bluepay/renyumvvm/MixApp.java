package could.bluepay.renyumvvm;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;

import could.yuanqiang.http.HttpUtils;

/**
 * Created by bluepay on 2017/11/23.
 */

public class MixApp extends Application {

    private static Context APPLICATION_CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        APPLICATION_CONTEXT = this;

        HttpUtils.getInstance().init(this, BuildConfig.DEBUG);
        initTextSize();
    }


    public static Context getContext(){
        return APPLICATION_CONTEXT;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }
    /**
     * 使其系统更改字体大小无效
     */
    private void initTextSize() {
        Resources res = getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

}
