package could.bluepay.renyumvvm.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Looper;
import android.provider.Browser;
import android.support.annotation.LayoutRes;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.Type;

import could.bluepay.renyumvvm.widget.action.SpannableClickable;

/**
 * Created by bluepay on 2017/11/23.
 */

public class AppUtils {

    //获取本地语言
    public static String getCountryLangue() {
        return Locale.getDefault().getLanguage();
    }

//region========json转换=========
    /**
     * 获取实体的json字符串
     *
     * @param value 实体
     * @return 实体的json字符串
     */
    public static String createJsonString(Object value) {
        Gson gson = new Gson();
        return gson.toJson(value);
    }

    /**
     * 根据json字符串,实体类型获取实例,用法:
     * <p>Login login = JsonHelper.getObject(jsonString,Login.class);</p>
     *
     * @param jsonString json字符串
     * @param cls        要转换的实体类型
     * @return 与json字符串对应的实例, 注意null值判断
     */
    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            if (isValidJsonStr(jsonString)) {
                Gson gson = new Gson();
                t = gson.fromJson(jsonString, cls);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 根据json字符串,实体类型获取实例集合,用法:
     * <p>List&lt; ArrayList&lt;Login> > list = JsonHelper.getList(jsonString, new TypeToken&lt;ArrayList<Login>>(){});</p>
     *
     * @param jsonString json字符串
     * @param tt         要转换的实体类型集合TypeToken
     * @return 与json字符串对应的实例集合
     */
    public static <T> List<T> getList(String jsonString, Type tt) {
        List<T> list = new ArrayList<>();
        try {
            if (isValidJsonStr(jsonString)) {
                Gson gson = new Gson();
                list = gson.fromJson(jsonString, tt);
            }
        } catch (Exception e) {
        }
        return list;
    }


    /**
     * 判断json字符串是否有效
     *
     * @param json 要判断的json字符串
     * @return true:有效,false:无效
     */
    public static boolean isValidJsonStr(String json) {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
        } catch (JsonParseException e) {
            return false;
        }
        return true;
    }
//endregion========json转换=========




    public static SpannableStringBuilder formatUrlString(String contentStr){

        SpannableStringBuilder sp;
        if(!TextUtils.isEmpty(contentStr)){

            sp = new SpannableStringBuilder(contentStr);
            try {
                //处理url匹配
                Pattern urlPattern = Pattern.compile("(http|https|ftp|svn)://([a-zA-Z0-9]+[/?.?])" +
                        "+[a-zA-Z0-9]*\\??([a-zA-Z0-9]*=[a-zA-Z0-9]*&?)*");
                Matcher urlMatcher = urlPattern.matcher(contentStr);

                while (urlMatcher.find()) {
                    final String url = urlMatcher.group();
                    if(!TextUtils.isEmpty(url)){
                        sp.setSpan(new SpannableClickable(){
                            @Override
                            public void onClick(View widget) {
                                Uri uri = Uri.parse(url);
                                Context context = widget.getContext();
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                                context.startActivity(intent);
                            }
                        }, urlMatcher.start(), urlMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                //处理电话匹配
                Pattern phonePattern = Pattern.compile("[1][34578][0-9]{9}");
                Matcher phoneMatcher = phonePattern.matcher(contentStr);
                while (phoneMatcher.find()) {
                    final String phone = phoneMatcher.group();
                    if(!TextUtils.isEmpty(phone)){
                        sp.setSpan(new SpannableClickable(){
                            @Override
                            public void onClick(View widget) {
                                Context context = widget.getContext();
                                //用intent启动拨打电话
                                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }, phoneMatcher.start(), phoneMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            sp = new SpannableStringBuilder();
        }
        return sp;
    }



    /**
     * Helper to throw an exception when {@link android.databinding.ViewDataBinding#setVariable(int,
     * Object)} returns false.
     */
    public static void throwMissingVariable(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes) {
        Context context = binding.getRoot().getContext();
        Resources resources = context.getResources();
        String layoutName = resources.getResourceName(layoutRes);
        String bindingVariableName = DataBindingUtil.convertBrIdToString(bindingVariable);
        throw new IllegalStateException("Could not bind variable '" + bindingVariableName + "' in layout '" + layoutName + "'");
    }

    /**
     * Ensures the call was made on the main thread. This is enforced for all ObservableList change
     * operations.
     */
    public static void ensureChangeOnMainThread() {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            throw new IllegalStateException("You must only modify the ObservableList on the main thread.");
        }
    }




}
