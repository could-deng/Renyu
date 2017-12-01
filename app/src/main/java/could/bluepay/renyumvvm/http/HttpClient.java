package could.bluepay.renyumvvm.http;


import java.util.HashMap;
import java.util.Map;

import could.bluepay.renyumvvm.BuildConfig;
import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.common.PrefsHelper;
import could.bluepay.renyumvvm.http.bean.BaseBean;
import could.bluepay.renyumvvm.http.bean.FavortResultBean;
import could.bluepay.renyumvvm.http.bean.HotDynamicBean;
import could.bluepay.renyumvvm.http.bean.UserListBean;
import could.bluepay.renyumvvm.utils.AppUtils;
import could.yuanqiang.http.HttpUtils;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by bluepay on 2017/11/23.
 */

public interface HttpClient {

    class Builder{
        public static HttpClient getAppServer(){
            return HttpUtils.getInstance().getAPPServer(HttpClient.class,Config.API_HOST_BLUEPAY);
        }
        public static Map<String,String > getHeader(){
            //// TODO: 2017/11/28 请求头需要删掉uid

            Map<String ,String> headers = new HashMap<>();
                headers.put("language", AppUtils.getCountryLangue());
                headers.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
                headers.put("Charsert", "utf-8");
                headers.put("user_id", PrefsHelper.with(MixApp.getContext(),Config.PREFS_USER).readLong(Config.SP_KEY_UID) + "");
                headers.put("api_version", BuildConfig.VERSION_NAME);
            return headers;
        }
    }


    @GET("/higirls/getMyFocusUsers")
    Observable<UserListBean> getFocusList(@HeaderMap Map<String, String> headers, @Query("uid")long uid, @Query("page")int page);


    @GET("/higirls/getHotUsers")
    Observable<UserListBean> getHotList(@HeaderMap Map<String, String> headers, @Query("uid")long uid,@Query("page")int page);


    @GET("/higirls/getShotList")
    Observable<UserListBean> getInvite(@HeaderMap Map<String, String> headers, @Query("uid")long uid,@Query("page")int page,@Query("city")String city);

    @GET("/higirls/getHotDynamics")//获取写真列表
    Observable<HotDynamicBean> getHotDynamics(@HeaderMap Map<String, String> headers, @Query("uid")long uid, @Query("page")int page);

    @POST("/higirls/dynamicLike")//点赞
    Observable<FavortResultBean> dynamicLike(@HeaderMap Map<String,String> headers, @Body RequestBody body);

//    @POST("/higirls/dynamicLike")//点赞
//    Call<ResponseBody> dynamicLike2(@HeaderMap Map<String,String> headers, @Body RequestBody body);

    @POST("/higirls/deleteDynamicLike")//取消点赞
    Observable<BaseBean> deleteDynamicLike(@HeaderMap Map<String,String> headers,@Body RequestBody body);
//    ,@Field("uid") long uid,@Field("lid") long lid
}