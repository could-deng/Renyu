package could.bluepay.renyumvvm.model;

import android.text.TextUtils;
import org.json.JSONObject;
import java.util.HashMap;
import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.common.PrefsHelper;
import could.bluepay.renyumvvm.http.HttpClient;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.http.bean.BaseBean;
import could.bluepay.renyumvvm.http.bean.FavortResultBean;
import could.bluepay.renyumvvm.http.bean.HotDynamicBean;
import could.bluepay.renyumvvm.utils.Logger;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Model层
 */

public class MainModel {

    public static MainModel mInstance;
    private static long uid = Config.ANONYMOUS_UID;

//    private LinearLayoutManager layoutManager;

    public void setData(long uid){
        this.uid = uid;
    }
    public static MainModel getmInstance(){
        if(mInstance==null){
            mInstance = new MainModel();
        }
        return mInstance;
    }

    public long getUid(){
        if(uid == Config.ANONYMOUS_UID){
            PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).readLong(Config.SP_KEY_UID,1);
        }
        return uid;
    }
    public void setUid(long uid){
        this.uid = uid;
        PrefsHelper.with(MixApp.getContext(),Config.PREFS_USER).writeLong(Config.SP_KEY_UID,uid);

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
     * 获取写真集合
     * @param listener
     */
    public void getHotDynamicsData(int page,final RequestImpl listener){
         HttpClient.Builder.getAppServer().getHotDynamics(HttpClient.Builder.getHeader(),uid,page)
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
     * 点赞
     */
    public void doDynamicLike(String nickName, long pid, final RequestImpl listener){

        if(TextUtils.isEmpty(nickName) || pid == 0){
            return;
        }

        HashMap<String,Object> map = new HashMap<>();
        map.put("uid",uid);
        map.put("nickname",nickName);
        map.put("pid",pid);

        RequestBody body = RequestBody.create(MediaType.parse("Content-Type, application/json"),(new JSONObject(map)).toString());
        HttpClient.Builder.getAppServer().dynamicLike(HttpClient.Builder.getHeader(),body)
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
        map.put("uid",uid);
        map.put("lid",favouriteId);
        RequestBody body = RequestBody.create(MediaType.parse("Content-Type, application/json"),(new JSONObject(map)).toString());

        HttpClient.Builder.getAppServer().deleteDynamicLike(HttpClient.Builder.getHeader(),body)
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
