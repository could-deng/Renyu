package could.yuanqiang.http;

import android.content.Context;
import android.util.Log;

import could.yuanqiang.http.utils.CheckNetwork;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jingbin on 2017/2/14.
 * 网络请求工具类
 * <p>
 * 豆瓣api:
 * 问题：API限制为每分钟40次，一不小心就超了，马上KEY就被封,用不带KEY的API，每分钟只有可怜的10次。
 * 返回：code:112（rate_limit_exceeded2 IP 访问速度限制）
 * 解决：1.使用每分钟访问次数限制（客户端）2.更换ip (更换wifi)
 * 豆瓣开发者服务使用条款: https://developers.douban.com/wiki/?title=terms
 * 使用时请在"CloudReaderApplication"下初始化。
 */

public class HttpUtils {
    private static HttpUtils instance;
    private Gson gson;
    private Context context;
    private Object gankHttps;
    private Object doubanHttps;
    private Object dongtingHttps;

    private Object appHttps;//app服务器

    private IpmlTokenGetListener listener;
    private boolean debug;
    // gankio、豆瓣、（轮播图）
    private final static String API_GANKIO = "https://gank.io/api/";
    private final static String API_DOUBAN = "Https://api.douban.com/";
    private final static String API_TING = "https://tingapi.ting.baidu.com/v1/restserver/";

    /**
     * 分页数据，每页的数量
     */
    public static int per_page = 10;
    public static int per_page_more = 20;

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    public void init(Context context, boolean debug) {
        this.context = context;
        this.debug = debug;
        HttpHead.init(context);
    }

    /**
     * 指定app服务器
     * @param a
     * @param serverHost
     * @param <T>
     * @return
     */
    public <T> T getAPPServer(Class<T> a,String serverHost){
        if(appHttps == null){
            synchronized (HttpUtils.class){
                if(appHttps == null){
                    appHttps = getBuilder(serverHost).build().create(a);
                }
            }
        }
        return (T) appHttps;
    }

    public <T> T getBestServer(Class<T> a,String serverHost){
        if(appHttps == null){
            synchronized (HttpUtils.class){
                if(appHttps == null){
                    appHttps = getBuilder(serverHost).build().create(a);
                }
            }
        }
        return (T) appHttps;
    }

    public <T> T getGankIOServer(Class<T> a) {
        if (gankHttps == null) {
            synchronized (HttpUtils.class) {
                if (gankHttps == null) {
                    gankHttps = getBuilder(API_GANKIO).build().create(a);
                }
            }
        }
        return (T) gankHttps;
    }

    public <T> T getDouBanServer(Class<T> a) {
        if (doubanHttps == null) {
            synchronized (HttpUtils.class) {
                if (doubanHttps == null) {
                    doubanHttps = getBuilder(API_DOUBAN).build().create(a);
                }
            }
        }
        return (T) doubanHttps;
    }

    public <T> T getTingServer(Class<T> a) {
        if (dongtingHttps == null) {
            synchronized (HttpUtils.class) {
                if (dongtingHttps == null) {
                    dongtingHttps = getBuilder(API_TING).build().create(a);
                }
            }
        }
        return (T) dongtingHttps;
    }
    private Retrofit.Builder getBuilder(String apiUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(getOkClient());
        builder.baseUrl(apiUrl);//设置远程地址
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create(getGson()));//该convert是对Call<T>中T的转换。。。使得:接受的回复Call<ResponseBody>,枚举类型不再是ResponseBody，可以接收自定义的Gson，当然也可以不传
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());//该CallAdapter是对Call的转换。。。（retrofit与Rxjava结合）把retrofit2.Call对象适配转换为Observable<T>对象，
        return builder;
    }

    //region=====获取converterFactory(对返回类型的自动转换)=========

    private Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setLenient();
            builder.setFieldNamingStrategy(new AnnotateNaming());
            builder.serializeNulls();
            gson = builder.create();
        }
        return gson;
    }


    private static class AnnotateNaming implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }

    //endregion=====获取converterFactory(对返回类型的自动转换)=========


    //region=====获取okHttpClient=================

    public OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};
            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
            okBuilder.readTimeout(30, TimeUnit.SECONDS);//20
            okBuilder.connectTimeout(20, TimeUnit.SECONDS);//10
            okBuilder.writeTimeout(30, TimeUnit.SECONDS);//20
//            okBuilder.cache()//设置缓存
            okBuilder.addInterceptor(new HttpHeadInterceptor());//请求头拦截器
            okBuilder.addInterceptor(getInterceptor());//log拦截器
            okBuilder.sslSocketFactory(sslSocketFactory);
            okBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
//                    Log.d("HttpUtils", "==come");
                    return true;
                }
            });

            return okBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public OkHttpClient getOkClient() {
        OkHttpClient client1;
        client1 = getUnsafeOkHttpClient();
        return client1;
    }

    //endregion=====获取okHttpClient=================

    public void setTokenListener(IpmlTokenGetListener listener) {
        this.listener = listener;
    }


    //region=========在OkhttpClient中增加的拦截器=======

    class HttpHeadInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Accept", "application/json;versions=1");
            if (CheckNetwork.isNetworkConnected(context)) {//该拦截器的意义在于如果无网络，但是我还调接口，说明只要求我已经缓存的数据
                int maxAge = 60;
                builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
            } else {
                int maxStale = 60 * 60 * 24 * 28;
                builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }
            // 可添加token
//            if (listener != null) {
//                builder.addHeader("token", listener.getToken());
//            }
            // 如有需要，添加请求头
//            builder.addHeader("a", HttpHead.getHeader(request.method()));
            return chain.proceed(builder.build());
        }
    }

    private HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (debug) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 测试
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE); // 打包
        }
        return interceptor;
    }
    //endregion=========在OkhttpClient中增加的拦截器=======
}
