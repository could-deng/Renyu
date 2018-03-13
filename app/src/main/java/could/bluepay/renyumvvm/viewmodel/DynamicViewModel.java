package could.bluepay.renyumvvm.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import java.util.Iterator;
import java.util.List;
import could.bluepay.renyumvvm.BR;
import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.http.HttpClient;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.http.bean.HotDynamicBean;
import could.bluepay.renyumvvm.http.bean.WeiboBean;
import could.bluepay.renyumvvm.model.MainModel;
import could.bluepay.renyumvvm.model.MemExchange;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.fragment.BaseFragment;
import could.bluepay.renyumvvm.widget.pulltorefresh.RefreshLayout;
import could.bluepay.renyumvvm.widget.pulltorefresh.SwipeRefreshDirection;
import could.bluepay.widget.jiaozivideoplayer.JZMediaManager;
import could.bluepay.widget.jiaozivideoplayer.JZUtils;
import could.bluepay.widget.jiaozivideoplayer.JZVideoPlayer;
import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter.BaseItemViewSelector;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * DynamicFragment的viewModel
 */

public class DynamicViewModel<SV extends ViewDataBinding> extends BaseFragmentViewModel{
    public static final String TOKEN_PRAISE_INDICATOR = "token_update_praise" + MixApp.appName;//赞


    private MainModel mModel;


    private SV binding;
    private BaseFragment context;

    private int page = 0;

    public DynamicViewModel(){

    }
    public void init(SV binding,BaseFragment context){
        this.binding = binding;
        this.context = context;
        mModel = new MainModel();

    }

    // TODO: 2017/12/6 传入的context是否造成内存泄漏

    //region=====提供给view层调用======

    public void loadData(){
        if(MemExchange.getInstance().getWeiboBeanList()!=null && MemExchange.getInstance().getWeiboBeanList().size()>0 && MemExchange.getInstance().getWeiboBeanListPage()!=0){
            page = MemExchange.getInstance().getWeiboBeanListPage();
            Iterator iterator = MemExchange.getInstance().getWeiboBeanList().iterator();
            while (iterator.hasNext()) {
                WeiboBean bean = (WeiboBean) iterator.next();
                if(bean!=null) {
                    viewModelList.add(new DynamicItemViewModel(bean,context.getContext()));
                }
            }
            showContentView();
        }else {
            MemExchange.getInstance().clearWeiboData();
            if(!onNetWorkDisConnecte(true)){
                loadCustomData(MainModel.getmInstance().getUid(),true) ;
            }

        }

    }

    //endregion=====提供给view层调用======
    /**
     * visible时自动调用、错误时点击重新加载
     */
    @Override
    public void onloadData() {
        super.onloadData();
        loadData();
    }



    //region=========Command===============
    /**
     * Data
     */
    public final ObservableBoolean loadingData = new ObservableBoolean(false);//是否正在加载数据

    public final ObservableList<DynamicItemViewModel> viewModelList = new ObservableArrayList<>();
    public final ItemViewSelector<DynamicItemViewModel> itemView = new BaseItemViewSelector<DynamicItemViewModel>(){

        @Override
        public void select(ItemView itemView, int position, DynamicItemViewModel item) {
            if(viewModelList.get(position).getItemType() == Config.RECYCLER_VIEW_DYNAMIC_TYPE_video) {
                itemView.set(BR.viewModel, R.layout.item_dynamic_video);
            }else{
                itemView.set(BR.viewModel, R.layout.item_dynamic_image);
            }
        }

        @Override
        public int viewTypeCount() {
            return 2;
        }
    };

    public final RefreshLayout.OnRefreshListener onRefreshListener = new RefreshLayout.OnRefreshListener(){

        @Override
        public void onRefresh(SwipeRefreshDirection direction) {
            loadingData.set(true);
            if(direction == SwipeRefreshDirection.TOP){
                MemExchange.getInstance().clearWeiboData();
                loadCustomData(MainModel.getmInstance().getUid(),true) ;
            }else if(direction == SwipeRefreshDirection.BOTTOM){
                loadCustomData(MainModel.getmInstance().getUid(),false);
            }
        }
    };


    public final RecyclerView.OnChildAttachStateChangeListener attachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {
            JZVideoPlayer videoVideo = (JZVideoPlayer) view.findViewById(R.id.video_view);
            if(videoVideo!=null && JZUtils.dataSourceObjectsContainsUri(videoVideo.dataSourceObjects, JZMediaManager.getCurrentDataSource())){
                JZVideoPlayer.releaseAllVideos();
            }
        }
    };

    //endregion=========Command===============


    //region=====model层==========

    private void loadCustomData(long uid, boolean ifFirstPage) {
        if(onNetWorkDisConnecte(false)){
            loadingData.set(false);
            return;
        }
        if(ifFirstPage) {
            page = 1;
        }else{
            page++;
        }

        //获取到全部Notification
        Observable<Notification<HotDynamicBean>> data = HttpClient.Builder.getAppServer().getHotDynamics(HttpClient.Builder.getHeader(), uid, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(RxLifecycle.bind())
//                .compose((ObservableTransformer<? super UserListBean, UserListBean>) ((FragmentLifecycleProvider) mFragment).bindToLifecycle())
//                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .materialize().share();

        //正常情况
        data
                .filter(new Predicate<Notification<HotDynamicBean>>() {
                    @Override
                    public boolean test(Notification<HotDynamicBean> userListBeanNotification) throws Exception {
                        return userListBeanNotification.isOnNext();
                    }
                })//过滤onNext
                .map(new Function<Notification<HotDynamicBean>, HotDynamicBean>() {
                    @Override
                    public HotDynamicBean apply(Notification<HotDynamicBean> userListBeanNotification) throws Exception {
                        return userListBeanNotification.getValue();
                    }
                })//notification<data>->data
                .filter(new Predicate<HotDynamicBean>() {
                    @Override
                    public boolean test(HotDynamicBean userListBean) throws Exception {
                        boolean haveData = (userListBean != null && userListBean.getData() != null && userListBean.getData().getWeibo().size()> 0);
                        if (!haveData && page > 1) {
                            page--;
                        }
                        return haveData;
                    }
                })//过滤空数据
                .doOnNext(new Consumer<HotDynamicBean>() {
                    @Override
                    public void accept(HotDynamicBean userListBean) throws Exception {
                        MemExchange.getInstance().setWeiboBeanList(userListBean.getData().getWeibo(),page);
                    }
                })//本地存储model
                .doOnNext(new Consumer<HotDynamicBean>() {
                    @Override
                    public void accept(HotDynamicBean bean) throws Exception {
                        Observable.just(page == 1).filter(new Predicate<Boolean>() {
                            @Override
                            public boolean test(Boolean aBoolean) throws Exception {
                                return aBoolean;
                            }
                        }).subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if(aBoolean){
                                    viewModelList.clear();
                                }
                            }
                        });
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        loadingData.set(false);
                        showContentView();
                    }
                })//SwipeRefreshLayout刷新状态标示
                .flatMap(new Function<HotDynamicBean, Observable<WeiboBean>>() {
                    @Override
                    public Observable<WeiboBean> apply(HotDynamicBean userListBean) throws Exception {
                        return Observable.fromIterable(userListBean.getData().getWeibo());
                    }
                })//list -> item
                .subscribe(new Consumer<WeiboBean>() {
                    @Override
                    public void accept(WeiboBean beanItem) throws Exception {
                        viewModelList.add(new DynamicItemViewModel(beanItem,context.getContext()));
                    }

                });


        //错误情况
        data
                .filter(new Predicate<Notification<HotDynamicBean>>() {
                    @Override
                    public boolean test(Notification<HotDynamicBean> userListBeanNotification) throws Exception {
                        return userListBeanNotification.isOnError();
                    }
                })
                .map(new Function<Notification<HotDynamicBean>, Throwable>() {
                    @Override
                    public Throwable apply(Notification<HotDynamicBean> beanNotification) throws Exception {
                        return beanNotification.getError();
                    }
                })
                .subscribe(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e(Logger.DEBUG_TAG, throwable.getMessage());
                        loadingData.set(false);
                        if (page == 1) {
                            showError();
                        } else {
                            page--;
                        }
                    }
                });

    }

    //endregion=====model层==========





    /**
     * ViewModel与adapter的交流
     */
    public interface poupWindowClick{
        void updateRecycleView(int index);
        void addSubscription(Disposable disposable);
        void doLike(String nickName,long pid,RequestImpl request);
        void doDeleteLike(long favoriteId,RequestImpl request);
//        void clickPicture(int weiboPosition,View view,String pictureUrl, int PhotoPosition);
        void onImageItemClick(View view, int position, WeiboBean dynamicItem, List<ImageView> imagesList, List<String> imagesUrlList);
    }

}
