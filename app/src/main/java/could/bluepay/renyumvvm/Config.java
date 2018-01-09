package could.bluepay.renyumvvm;

/**
 * Created by bluepay on 2017/11/23.
 */

public class Config {

    public static final String API_HOST_BLUEPAY = "http://192.168.4.210:8165/";

    public static final String AppName = "RenyuMVVM";

    /** 用户信息模块SharedPreferences文件名 */
    public static final String PREFS_USER = "prefs_user";



    /** 匿名(未登录)用户编号,默认是-1*/
    public static final int ANONYMOUS_UID = -1;

    /** 用户UID SP键名,long型*/
    public static final String SP_KEY_UID = "uid";
    public static final String SP_KEY_NICKNAME = "nickname";
}
