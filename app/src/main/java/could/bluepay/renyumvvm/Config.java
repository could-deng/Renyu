package could.bluepay.renyumvvm;

/**
 * Created by bluepay on 2017/11/23.
 */

public class Config {
    //best查询使用的服务器
    public static final String API_HOST_BEST_SEARCH = "http://192.168.4.210:8200/";
    //Renyu使用的服务器
    public static final String API_HOST_BLUEPAY = "http://192.168.4.210:8165/";

    public static final String AppName = "Hi Girl!";

    /** 用户信息模块SharedPreferences文件名 */
    public static final String PREFS_USER = "prefs_user";



    /** 匿名(未登录)用户编号,默认是-1*/
    public static final int ANONYMOUS_UID = -1;

    /** 用户登录类型（-1代表未登录，1代表用户名登录）     */
    public static final String SP_KEY_LAST_LOGIN_TYPE = "last_login_type";
    /** 用户UID SP键名,long型*/
    public static final String SP_KEY_UID = "uid";
    public static final String SP_KEY_NICKNAME = "nickname";




    /** Dynamic模块中adapter中item类型 */
    public static final int RECYCLER_VIEW_DYNAMIC_TYPE = 1;//image和video都为同一种类型
    public static final int RECYCLER_VIEW_DYNAMIC_TYPE_image = 2;
    public static final int RECYCLER_VIEW_DYNAMIC_TYPE_video = 3;

}
