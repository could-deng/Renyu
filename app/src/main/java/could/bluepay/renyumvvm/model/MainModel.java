package could.bluepay.renyumvvm.model;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import could.bluepay.renyumvvm.http.HttpClient;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.http.bean.BaseBean;
import could.bluepay.renyumvvm.http.bean.FavortResultBean;
import could.bluepay.renyumvvm.http.bean.HotDynamicBean;
import could.bluepay.renyumvvm.utils.Logger;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bluepay on 2017/11/28.
 */

public class MainModel {

    private long uid;
    private int page;
//    private LinearLayoutManager layoutManager;

    public void setData(long uid,int page){
        this.uid = uid;
        this.page = page;
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
    public void getHotDynamicsData(final RequestImpl listener){
        Subscription subscription = HttpClient.Builder.getAppServer().getHotDynamics(HttpClient.Builder.getHeader(),uid,page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotDynamicBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.loadFailed();
                        e.printStackTrace();
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
        listener.addSubscription(subscription);
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
        Subscription subscription = HttpClient.Builder.getAppServer().dynamicLike(HttpClient.Builder.getHeader(),body)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FavortResultBean>(){
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.loadFailed();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(FavortResultBean favortResultBean) {
                        listener.loadSuccess(favortResultBean.getData());
                    }
                });
        listener.addSubscription(subscription);
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

        Subscription subscription = HttpClient.Builder.getAppServer().deleteDynamicLike(HttpClient.Builder.getHeader(),body)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.loadFailed();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        listener.loadSuccess(baseBean);
                    }
                });
        listener.addSubscription(subscription);
    }



}
