package could.bluepay.renyumvvm.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.text.TextUtils;
import could.bluepay.renyumvvm.BR;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.bindingAdapter.command.ReplyCommand;
import could.bluepay.renyumvvm.http.HttpClient;
import could.bluepay.renyumvvm.http.bean.UserBeanItem;
import could.bluepay.renyumvvm.http.bean.UserListBean;
import could.bluepay.renyumvvm.model.MainModel;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.fragment.InviteFragment;
import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter.BaseItemViewSelector;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

public class InviteFragmentViewModel extends BaseFragmentViewModel{

    private String city = "全国";


    // 开始请求的角标
    private int mStart = 1;

    /**
     * model
     */
    private UserListBean dataBean;

    public final ViewStyle inviteFragmentViewStyle = new ViewStyle();


    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
    }

    /**
     * Data
     */
    public final ObservableList<InviteFragmentItemViewModel> viewModelList = new ObservableArrayList<>();
    public final ItemViewSelector<InviteFragmentItemViewModel> itemView = new BaseItemViewSelector<InviteFragmentItemViewModel>(){

        @Override
        public void select(ItemView itemView, int position, InviteFragmentItemViewModel item) {
            itemView.set(BR.viewModel, R.layout.item_lv_invite);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };

    /**
     * Command
     */
    //下拉刷新
    public final ReplyCommand onRefreshCommand = new ReplyCommand(new Runnable() {
        @Override
        public void run() {
            inviteFragmentViewStyle.isRefreshing.set(true);
            loadData(true);
        }
    });

    //加载更多
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<Integer>(new Runnable() {
        @Override
        public void run() {
            loadData(false);
        }
    });


    //region===提供给View层=====

    public void loadData(boolean ifRefresh){
        if(onNetWorkDisConnecte(false)){
            return;
        }
        if(ifRefresh) {
            mStart = 1;
        }else{
            ++mStart;
        }
        this.loadCustomData(MainModel.getmInstance().getUid(),mStart);
    }

    public boolean haveData(){
        return dataBean!=null;
    }

    //endregion===提供给View层=====


    /**
     * visible时自动调用、错误时点击重新加载
     */
    @Override
    public void onloadData() {
        super.onloadData();
        inviteFragmentViewStyle.isRefreshing.set(true);
        Logger.e(Logger.DEBUG_TAG,"inviteFragmentViewStyle.isRefreshing.set(true);");
        if(!onNetWorkDisConnecte(true)) {
            loadData(true);
        }
    }

    //region=====model层==========

    private void loadCustomData(long uid, int page) {

        //获取到全部Notification
        Observable<Notification<UserListBean>> data = HttpClient.Builder.getAppServer().getInvite(HttpClient.Builder.getHeader(), uid, page, TextUtils.isEmpty(city) ? "全国" : city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())// TODO: 2018/1/11 回收消息
//                .compose(RxLifecycle.bind())
//                .compose((ObservableTransformer<? super UserListBean, UserListBean>) ((FragmentLifecycleProvider) mFragment).bindToLifecycle())
//                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .materialize().share();

        //正常情况
        data
                .filter(new Predicate<Notification<UserListBean>>() {
                    @Override
                    public boolean test(Notification<UserListBean> userListBeanNotification) throws Exception {
                        return userListBeanNotification.isOnNext();
                    }
                })//过滤onNext
                .map(new Function<Notification<UserListBean>, UserListBean>() {
                    @Override
                    public UserListBean apply(Notification<UserListBean> userListBeanNotification) throws Exception {
                        return userListBeanNotification.getValue();
                    }
                })//notification<data>->data
                .filter(new Predicate<UserListBean>() {
                    @Override
                    public boolean test(UserListBean userListBean) throws Exception {
                        boolean haveData = (userListBean != null && userListBean.getData() != null && userListBean.getData().getUsercount() != 0);
                        if (!haveData && mStart > 1) {
                            mStart--;
                        }
                        return haveData;
                    }
                })//过滤空数据
                .doOnNext(new Consumer<UserListBean>() {
                    @Override
                    public void accept(UserListBean userListBean) throws Exception {
                        dataBean = userListBean;
                    }
                })//本地存储model
                .doOnNext(new Consumer<UserListBean>() {
                    @Override
                    public void accept(UserListBean userListBean) throws Exception {
                        Observable.just(mStart == 1).filter(new Predicate<Boolean>() {
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
                        inviteFragmentViewStyle.isRefreshing.set(false);
                        showContentView();
                    }
                })//SwipeRefreshLayout刷新状态标示
                .flatMap(new Function<UserListBean, Observable<UserBeanItem>>() {
                    @Override
                    public Observable<UserBeanItem> apply(UserListBean userListBean) throws Exception {
                        return Observable.fromIterable(userListBean.getData().getUser());
                    }
                })//list -> item
                .subscribe(new Consumer<UserBeanItem>() {
                    @Override
                    public void accept(UserBeanItem beanItem) throws Exception {
                        viewModelList.add(new InviteFragmentItemViewModel(beanItem));
                    }

                });


        //错误情况
        data
                .filter(new Predicate<Notification<UserListBean>>() {
                    @Override
                    public boolean test(Notification<UserListBean> userListBeanNotification) throws Exception {
                        return userListBeanNotification.isOnError();
                    }
                })
                .map(new Function<Notification<UserListBean>, Throwable>() {
                    @Override
                    public Throwable apply(Notification<UserListBean> userListBeanNotification) throws Exception {
                        return userListBeanNotification.getError();
                    }
                })
                .subscribe(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e(Logger.DEBUG_TAG, throwable.getMessage());
                        inviteFragmentViewStyle.isRefreshing.set(false);
                        if (mStart == 1) {
                            showError();
                        } else {
                            mStart--;
                        }
                    }
                });

    }

    //endregion=====model层==========


//    public static void dealWithResponseError(Observable<Throwable> throwableObservable){
//        ReplaySubject throwableReplaySubject = ReplaySubject.create();
//        throwableObservable.subscribe(throwableReplaySubject);

//        throwableObservable
//                .repeat(5)
//                .scan(new Function)
//                .scan((n, c) -> n.getCause())
//                .takeUntil(n -> n.getCause() == null)
//                .filter(n -> n instanceof ApiException)
//                .cast(ApiException.class)
//                .subscribe(e -> Log.v("error", e.msg));
//    }

}
