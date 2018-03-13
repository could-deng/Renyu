package could.bluepay.renyumvvm.model;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.common.PrefsHelper;
import could.bluepay.renyumvvm.http.HttpClient;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.http.bean.BaseBean;
import could.bluepay.renyumvvm.http.bean.BestResultBean;
import could.bluepay.renyumvvm.http.bean.FavortResultBean;
import could.bluepay.renyumvvm.http.bean.HotDynamicBean;
import could.bluepay.renyumvvm.http.bean.UserListBean;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.fragment.FocusFragment;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

/**
 * Model层
 */

public class MainModel {

    public static MainModel mInstance;
    private static long uid = Config.ANONYMOUS_UID;

//    private LinearLayoutManager layoutManager;


    public static MainModel getmInstance(){
        if(mInstance==null){
            mInstance = new MainModel();
        }
        return mInstance;
    }

    public long getUid(){
        if(uid == Config.ANONYMOUS_UID){
            uid = PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).readLong(Config.SP_KEY_UID,1);
        }
        return uid;
    }

//    public void setLayoutManager(LinearLayoutManager manager){
//        layoutManager = manager;
//    }
//    public void updateAdapterIndex(int dynamicIndex, RecyclerView.Adapter adapter){
//        int f = layoutManager.findFirstVisibleItemPosition();
//        int e = layoutManager.findLastVisibleItemPosition();
//        if(f<=dynamicIndex && dynamicIndex>=e){
//            adapter.notifyItemChanged(dynamicIndex);
//        }
//        Logger.e(Logger.DEBUG_TAG,"first:"+f+",last:"+e);
//    }



    /**
     * Best平台搜索收入
     * http://192.168.4.210:8200/tools/avenue?time=1&currency=THB&producerId=178&type=1
     * @param time
     * @param currency
     * @param producerId
     * @param type
     */
    public void searchBestInput(int time,String currency,int producerId,int type,final RequestImpl listener){
        HttpClient.Builder.getBestService().bestSearch(HttpClient.Builder.getHeader(),time,
                    currency,producerId,type)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())//上游。在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())//下游。回到主线程去处理请求结果
                .subscribe(new Observer<BestResultBean>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BestResultBean bestResultBean) {
                        if(bestResultBean!=null && bestResultBean.getStatus() == 200) {
                            listener.loadSuccess(bestResultBean.getDate());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.loadFailed();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    /**
     * 登录
     * @param useName
     * @param psw
     * @param userType
     */
    public void login(String useName,String psw,int userType,final RequestImpl listener){
        HashMap<String,Object> map = new HashMap<>();
        map.put("msisdn",useName);
        map.put("password",psw);
        map.put("userType",userType);

        RequestBody body = RequestBody.create(MediaType.parse("Content-Type, application/json"),(new JSONObject(map)).toString());

        HttpClient.Builder.getAppServer().login(HttpClient.Builder.getHeader(),body)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.addSubscription(d);
                    }

                    @Override
                    public void onNext(String s) {
                        Logger.e(Logger.DEBUG_TAG,s.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.loadFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     *
     * @param type
     * @param username
     * @param mobile
     * @param password
     * @param headIcon 文件名
     * @param file 上传的文件
     */
    public void register(int type,String username,String mobile,String password,String headIcon,File file){
        Map<String, Object> params = new HashMap<>();
        params.put("userType", type);
        params.put("nickname", username);
        params.put("msisdn", mobile);
        params.put("password", password);
        params.put("headIcon", headIcon);

        RequestBody body = RequestBody.create(MediaType.parse("Content-Type, application/json"),(new JSONObject(params)).toString());

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("headIcon",headIcon,requestFile);

        HttpClient.Builder.getAppServer().register(HttpClient.Builder.getHeader(),body,filePart);
    }

    public Request uploadToService(String url, List<String> files, List<String> tags){
        if(TextUtils.isEmpty(url)){
            return null;
        }
        if(files ==null || tags == null || (files.size()!=tags.size())){
            return null;
        }
        boolean bExist = false;
        List<File> listFiles = new ArrayList<>();
        for(String fileName:files){
            if(TextUtils.isEmpty(fileName))
                continue;
            File file = new File(fileName);
            if(file.exists()){
                bExist = true;
            }else{
                file = null;
            }
            listFiles.add(file);
        }
        Request request = null;
        if(bExist){
            MultipartBody.Builder builder = new MultipartBody.Builder();
            for(int i =0; i< files.size();i++){
                if(listFiles.get(i) == null) continue;
                String sString = "form-data;name=\"" + tags.get(i) + "\";filename=\""
                        + listFiles.get(i).getName() + "\"";
                builder.addPart(
                        Headers.of("Content-Disposition", sString),
                        RequestBody.create(
                                MediaType.parse("application/octet-stream"),
                                listFiles.get(i)));
            }
            RequestBody requestBody = builder.setType(MultipartBody.FORM).build();
            request = new Request.Builder().url(url)
                    .addHeader("User-Agent", String.format("Fitmix/%s/%s (Android %s)", "1","2", ""))
                    .post(requestBody)
                    .build();
        }
        return request;
    }



    /**
     * 获取写真集合
     * @param listener
     */
    public void getHotDynamicsData(int page,final RequestImpl listener){
         HttpClient.Builder.getAppServer().getHotDynamics(HttpClient.Builder.getHeader(),getUid(),page)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())//上游。在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())//下游。回到主线程去处理请求结果
                .subscribe(new Observer<HotDynamicBean>() {
//                    @Override
//                    public void onCompleted() {
//                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.loadFailed();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.addSubscription(d);
                    }

                    @Override
                    public void onNext(HotDynamicBean item) {
                        if(null != item.getData() && null!=item.getData().getWeibo()) {
                            listener.loadSuccess(item.getData().getWeibo());
                        }else{
                            listener.loadFailed();
                        }
                    }

                });
//        listener.addDisposable(subscription);
    }

    /**
     * 获取关注/热门/受邀的集合
     * @param page
     * @param listener
     */
    public void getFocusData(int page, int mType, String city, Fragment mFragment, final RequestImpl listener){
        Observable<UserListBean> totalData ;
        if(mType == FocusFragment.ContentTypeFocus){
            totalData = HttpClient.Builder.getAppServer().getFocusList(HttpClient.Builder.getHeader(),getUid(),page);
        }else if(mType == FocusFragment.ContentTYpePopular){
            totalData = HttpClient.Builder.getAppServer().getHotList(HttpClient.Builder.getHeader(),getUid(),page);
        }else{
            totalData = HttpClient.Builder.getAppServer().getInvite(HttpClient.Builder.getHeader(),getUid(),page, TextUtils.isEmpty(city)?"全国":city);
        }
        totalData
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())//请求在主线程中执行
                .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程处理
//                .compose((ObservableTransformer<? super UserListBean, UserListBean>) ((FragmentLifecycleProvider) mFragment).bindToLifecycle())
                .subscribe(new Observer<UserListBean>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.addSubscription(d);
                    }

                    @Override
                    public void onNext(UserListBean item) {
                        if(null != item.getData() && null!=item.getData().getUser()) {
                            listener.loadSuccess(item.getData().getUser());
                        }else{
                            listener.loadFailed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.loadFailed();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 点赞
     */
    public void doDynamicLike(String nickName, long pid, final RequestImpl listener){

        if(TextUtils.isEmpty(nickName) || pid == 0){
            return;
        }

        HashMap<String,Object> map = new HashMap<>();
        map.put("uid",getUid());
        map.put("nickname",nickName);
        map.put("pid",pid);

        RequestBody body = RequestBody.create(MediaType.parse("Content-Type, application/json"),(new JSONObject(map)).toString());
        HttpClient.Builder.getAppServer().dynamicLike(HttpClient.Builder.getHeader(),body)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FavortResultBean>(){

                    @Override
                    public void onError(Throwable e) {
                        listener.loadFailed();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Logger.e(Logger.DEBUG_TAG,"doDynamicLike,onComplete()");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.addSubscription(d);
                    }

                    @Override
                    public void onNext(FavortResultBean favortResultBean) {
                        listener.loadSuccess(favortResultBean.getData());
                    }
                });

    }

    /**
     * 取消点赞
     * @param favouriteId
     * @param listener
     */
    public void deleteDynamicLike(long favouriteId, final RequestImpl listener){
        HashMap<String ,Object> map = new HashMap<>();
        map.put("uid",getUid());
        map.put("lid",favouriteId);
        RequestBody body = RequestBody.create(MediaType.parse("Content-Type, application/json"),(new JSONObject(map)).toString());

        HttpClient.Builder.getAppServer().deleteDynamicLike(HttpClient.Builder.getHeader(),body)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseBean>() {

                    @Override
                    public void onError(Throwable e) {
                        listener.loadFailed();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Logger.e(Logger.DEBUG_TAG,"deleteDynamicLike,onComplete()");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        listener.loadSuccess(baseBean);
                    }
                });

    }


}
