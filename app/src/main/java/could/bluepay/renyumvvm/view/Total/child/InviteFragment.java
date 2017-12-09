package could.bluepay.renyumvvm.view.Total.child;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.http.HttpClient;
import could.bluepay.renyumvvm.http.bean.UserListBean;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.MainActivity;
import could.bluepay.renyumvvm.view.adapter.UserListInviteAdapter;
import could.bluepay.renyumvvm.view.base.BaseFragment;
import could.bluepay.renyumvvm.databinding.FragmentInviteBinding;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bluepay on 2017/11/22.
 */

public class InviteFragment extends BaseFragment<FragmentInviteBinding> {
    public static final String TAG = "InviteFragment";
    public String setFragmentName(){
        return TAG;
    }

    //Fragment内容类型
    public static final int ContentTypeInvite = 3;

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
    private UserListInviteAdapter adapter;

    @Override
    protected int getContent() {
        return R.layout.fragment_invite;
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
    public static InviteFragment newInstance(int param1){
        InviteFragment fragment = new InviteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE,param1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        Logger.e(Logger.DEBUG_TAG,"InviteFragment,serUserVisibleHit()"+(getUserVisibleHint()?"visible":"invisible"));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //显示加载中
//        showLoading();
        //显示内容
        showContentView();
        bindingView.srlInvite.setColorSchemeColors(getResources().getColor(R.color.colorTheme));
        bindingView.srlInvite.setProgressViewOffset(true,0,200);
        bindingView.srlInvite.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bindingView.srlInvite.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mStart = 1;
                        loadCustomData(((MainActivity)getActivity()).getUid(),mStart);
                    }
                },500);
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity());
        bindingView.xrvInvite.setLayoutManager(mLayoutManager);
        scrollRecycleView();

        //准备就绪
        isPrepared = true;

        loadData();

    }

    @Override
    protected void loadData() {
        Logger.e(Logger.DEBUG_TAG,"InviteFragment,loadData(),"+(isPrepared?"1":"0")+","+(mIsVisible?"1":"0"));
        if(!isPrepared || !mIsVisible || !mIsFirst){
            return;
        }

        bindingView.srlInvite.setRefreshing(true);
        bindingView.srlInvite.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCustomData(((MainActivity)getActivity()).getUid(),mStart);
            }
        },500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsFirst = true;//销毁时，重新加载数据时，认为第一次请求
    }

    private void loadCustomData(long uid, int page){
        Subscription get = HttpClient.Builder.getAppServer().getInvite(HttpClient.Builder.getHeader(),uid,page, TextUtils.isEmpty(city)?"全国":city)
                .subscribeOn(Schedulers.io())//请求在主线程中执行
                .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程处理
                .subscribe(new Observer<UserListBean>() {
                    @Override
                    public void onCompleted() {
                        showContentView();
                        if(bindingView.srlInvite.isRefreshing()){
                            bindingView.srlInvite.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(Logger.DEBUG_TAG,"onError"+e.getMessage());
                        showContentView();
                        if(bindingView.srlInvite.isRefreshing()){
                            bindingView.srlInvite.setRefreshing(false);
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
                                    adapter = new UserListInviteAdapter(getActivity());
                                }
                                adapter.setList(userListBean.getData().getUser());

                                bindingView.xrvInvite.setAdapter(adapter);
                            }
                            mIsFirst = false;
                        }else {
                            if(userListBean!=null &&userListBean.getData()!=null
                                    && userListBean.getData().getUser()!=null
                                    && userListBean.getData().getUser().size()>0) {
                                adapter.addList(userListBean.getData().getUser());

                            }else{
                                mStart --;
                                adapter.updateLoadStatus(UserListInviteAdapter.LOAD_END);
                                //todo 需要做footView逐渐消失的功能

                                return;
                            }
                        }
                        if(adapter!=null){
                            adapter.updateLoadStatus(UserListInviteAdapter.LOAD_PULL_TO);
                        }
                    }
                });
        addSubscription(get);
    }

    /**
     * RecycleView滚动监听
     */
    private void scrollRecycleView(){
        bindingView.xrvInvite.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        adapter.updateLoadStatus(UserListInviteAdapter.LOAD_NONE);
                        return;
                    }

                    if(lastVisibleItem+1 == mLayoutManager.getItemCount()
                            && adapter.getLoadStatus()!=UserListInviteAdapter.LOAD_MORE){

                        adapter.updateLoadStatus(UserListInviteAdapter.LOAD_MORE);
                        bindingView.xrvInvite.postDelayed(new Runnable() {
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


}
