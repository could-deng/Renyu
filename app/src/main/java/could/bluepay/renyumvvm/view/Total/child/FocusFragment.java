package could.bluepay.renyumvvm.view.Total.child;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.view.MainActivity;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.view.adapter.UserListAdapter;
import could.bluepay.renyumvvm.view.adapter.UserListFocusAdapter;
import could.bluepay.renyumvvm.view.adapter.UserListPopularAdapter;
import could.bluepay.renyumvvm.view.base.BaseFragment;
import could.bluepay.renyumvvm.databinding.FragmentForcusBinding;
import could.bluepay.renyumvvm.http.HttpClient;
import could.bluepay.renyumvvm.http.bean.UserListBean;
import could.bluepay.renyumvvm.utils.Logger;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bluepay on 2017/11/20.
 */

public class FocusFragment <T extends UserListAdapter>extends BaseFragment<FragmentForcusBinding> {

    //Fragment内容类型
    public static final int ContentTypeFocus = 1;
    public static final int ContentTYpePopular = 2;
    public static final int ContentTypeInvite = 3;

    //内容类型
    private int mType ;
    private static final String TYPE = "param1";
    private String city = "全国";



    //是否首页
    private boolean mIsFirst = true;
    // 开始请求的角标
    private int mStart = 1;
    // 一次请求的数量
//    private int mCount = 18;

    private MainActivity activity;


    private LinearLayoutManager mLayoutManager;
    private T adapter;

    @Override
    protected int getContent() {
        return R.layout.fragment_forcus;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    /**
     * 实例化的唯一途径
     * @param param1
     * @return
     */
    public static FocusFragment newInstance(int param1){
        FocusFragment fragment = new FocusFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE,param1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mType = getArguments().getInt(TYPE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.e(Logger.DEBUG_TAG,((mType == ContentTypeFocus)?"FocusFragment":"PopularFragment")+"serUserVisibleHit()"+(getUserVisibleHint()?"visible":"invisible"));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //显示加载中
//        showLoading();
        //显示内容
        showContentView();
        bindingView.srlFocus.setColorSchemeColors(getResources().getColor(R.color.colorTheme));
        bindingView.srlFocus.setProgressViewOffset(true,0,200);
        bindingView.srlFocus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bindingView.srlFocus.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mStart = 1;
                        loadCustomData(((MainActivity)getActivity()).getUid(),mStart);
                    }
                },500);
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity());
        bindingView.xrvFocus.setLayoutManager(mLayoutManager);
        scrollRecycleView();

        //准备就绪
        isPrepared = true;

        /**
         * 因为启动时先走loadData()再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData();

    }

    @Override
    protected void loadData() {
        Logger.e(Logger.DEBUG_TAG,((mType == ContentTypeFocus)?"FocusFragment":"PopularFragment")+"loadData(),"+(isPrepared?"1":"0")+","+(mIsVisible?"1":"0")+","+(mIsFirst?"1":"0"));
        if(!isPrepared || !mIsVisible || !mIsFirst){
            return;
        }

        bindingView.srlFocus.setRefreshing(true);
        bindingView.srlFocus.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCustomData(((MainActivity)getActivity()).getUid(),mStart);
            }
        },500);
    }


    private void loadCustomData(long uid, int page){
        Observable<UserListBean> totalData ;
        if(mType == ContentTypeFocus){
            totalData = HttpClient.Builder.getAppServer().getFocusList(HttpClient.Builder.getHeader(),uid,page);
        }else if(mType == ContentTYpePopular){
            totalData = HttpClient.Builder.getAppServer().getHotList(HttpClient.Builder.getHeader(),uid,page);
        }else{
            totalData = HttpClient.Builder.getAppServer().getInvite(HttpClient.Builder.getHeader(),uid,page, TextUtils.isEmpty(city)?"全国":city);
        }
        Subscription get = totalData
                .subscribeOn(Schedulers.io())//请求在主线程中执行
                .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程处理
                .subscribe(new Observer<UserListBean>() {
                    @Override
                    public void onCompleted() {
                        showContentView();
                        if(bindingView.srlFocus.isRefreshing()){
                            bindingView.srlFocus.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(Logger.DEBUG_TAG,"onError"+e.getMessage());
                        showContentView();
                        if(bindingView.srlFocus.isRefreshing()){
                            bindingView.srlFocus.setRefreshing(false);
                        }
                        if(mStart == 1){
                            showError();
                            return;
                        }
                        mStart --;
                    }

                    @Override
                    public void onNext(UserListBean userListBean) {
                        Logger.e(Logger.DEBUG_TAG,userListBean.toString());
                        if(mStart == 1){
                            if(userListBean!=null &&userListBean.getData()!=null
                                    && userListBean.getData().getUser()!=null
                                    &&userListBean.getData().getUser().size()>0){
                                if(adapter == null){
                                    if(mType == ContentTypeFocus) {
                                        adapter = (T) new UserListPopularAdapter(getActivity());
                                    }else if(mType == ContentTYpePopular){
                                        adapter = (T) new UserListFocusAdapter(getActivity());
                                    }
                                }
                                adapter.setList(userListBean.getData().getUser());
                                bindingView.xrvFocus.setAdapter(adapter);
                            }
                            mIsFirst = false;
                        }else {
                            if(userListBean!=null &&userListBean.getData()!=null
                                    && userListBean.getData().getUser()!=null
                                    && userListBean.getData().getUser().size()>0) {
                                adapter.addList(userListBean.getData().getUser());

                            }else{
                                mStart --;
                                adapter.updateLoadStatus(UserListAdapter.LOAD_END);
                                //todo 需要做footView逐渐消失的功能

                                return;
                            }
                        }
                        if(adapter!=null){
                            adapter.updateLoadStatus(UserListAdapter.LOAD_PULL_TO);
                        }
                    }
                });
        addSubscription(get);
    }

    /**
     * RecycleView滚动监听
     */
    private void scrollRecycleView(){
        bindingView.xrvFocus.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                /**new State 一共有三种状态
                 * SCROLL_STATE_IDLE：0,目前RecyclerView不是滚动。
                 * SCROLL_STATE_DRAGGING：1,RecyclerView目前被外部输入如用户触摸输入。
                 * SCROLL_STATE_SETTLING：2,RecyclerView目前动画虽然不是在最后一个位置
                 外部控制。
                 * */

                Logger.e(Logger.DEBUG_TAG,"onScrollStateChanged(),newState = "+newState);


                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                    if(adapter ==null){
                        return;
                    }
                    if(mLayoutManager.getItemCount() == 0){
                        adapter.updateLoadStatus(UserListAdapter.LOAD_NONE);
                        return;
                    }

                    if(lastVisibleItem+1 == mLayoutManager.getItemCount()
                            && adapter.getLoadStatus()!=UserListAdapter.LOAD_MORE){

                        adapter.updateLoadStatus(UserListAdapter.LOAD_MORE);
                        bindingView.xrvFocus.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mStart ++;
                                loadCustomData(((MainActivity)getActivity()).getUid(),mStart);
                            }
                        },500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            }
        });
    }





    //region=======约拍特定布局相关====================



    //endregion=======约拍特定布局相关====================
}
