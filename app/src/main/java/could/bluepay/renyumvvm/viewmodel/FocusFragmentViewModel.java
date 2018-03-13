package could.bluepay.renyumvvm.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;
import could.bluepay.renyumvvm.bindingAdapter.command.ReplyCommand;
import could.bluepay.renyumvvm.databinding.FragmentForcusBinding;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.http.bean.UserBeanItem;
import could.bluepay.renyumvvm.model.MainModel;
import could.bluepay.renyumvvm.model.MemExchange;
import could.bluepay.renyumvvm.rx.RxApiManager;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.adapter.UserListAdapter;
import could.bluepay.renyumvvm.view.adapter.UserListFocusAdapter;
import could.bluepay.renyumvvm.view.adapter.UserListPopularAdapter;
import could.bluepay.renyumvvm.view.fragment.BaseFragment;
import could.bluepay.renyumvvm.view.fragment.FocusFragment;
import io.reactivex.disposables.Disposable;

/**
 * Focus viewModel
 */

public class FocusFragmentViewModel<SV extends ViewDataBinding> extends BaseFragmentViewModel {

    private SV bindingView;
    private BaseFragment context;

    private MainModel mainModel;


    private int type = FocusFragment.ContentTypeFocus;
    private String city = "";

    // 开始请求的角标
    private int mStart = 1;

    private UserListAdapter adapter;


    public void init(SV binding,BaseFragment context, int type){
        this.bindingView = binding;
        this.context = context;
        this.type = type;
        mainModel = new MainModel();
    }
    private void setCity(String city){
        this.city = city;
    }

    public final ObservableBoolean isRefreshing = new ObservableBoolean(false);

    //region=======Command=======

    //下拉刷新
    public final ReplyCommand onRefreshCommand = new ReplyCommand(new Runnable() {
        @Override
        public void run() {
            isRefreshing.set(true);
            if(!onNetWorkDisConnecte(false)) {
                isRefreshing.set(false);
                mStart = 1;
                loadCustomData(mStart);
            }
        }
    });

    //滚动加载更多监听
    public final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;
        private WeakReference<LinearLayoutManager> llm;
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            /**new State 一共有三种状态
             * SCROLL_STATE_IDLE：0,目前RecyclerView不是滚动。
             * SCROLL_STATE_DRAGGING：1,RecyclerView目前被外部输入如用户触摸输入。
             * SCROLL_STATE_SETTLING：2,RecyclerView目前动画虽然不是在最后一个位置
             外部控制。
             * */

//            Logger.e(Logger.DEBUG_TAG,"onScrollStateChanged(),newState = "+newState);
            super.onScrollStateChanged(recyclerView, newState);

            if(llm == null){
                llm = new WeakReference<>((LinearLayoutManager) recyclerView.getLayoutManager());
            }
            if(newState == RecyclerView.SCROLL_STATE_IDLE){
                lastVisibleItem = llm.get().findLastVisibleItemPosition();
                if(adapter ==null){
                    return;
                }
                if(llm.get().getItemCount() == 0){
                    adapter.updateLoadStatus(UserListAdapter.LOAD_NONE);
                    return;
                }

                if(lastVisibleItem+1 == llm.get().getItemCount()
                        && adapter.getLoadStatus()!=UserListAdapter.LOAD_MORE){

                    adapter.updateLoadStatus(UserListAdapter.LOAD_MORE);
                    if(!onNetWorkDisConnecte(false)) {
                        adapter.updateLoadStatus(UserListAdapter.LOAD_NONE);
                        mStart++;
                        loadCustomData(mStart);
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(llm == null){
                llm = new WeakReference<>((LinearLayoutManager) recyclerView.getLayoutManager());
            }
            lastVisibleItem = llm.get().findLastVisibleItemPosition();
        }
    };

    //endregion========Command=====

    public boolean haveData(){
        return adapter!=null && adapter.haveData();
    }
    public void loadData(){
        List<UserBeanItem> beanList;
        int page;
        if(type == FocusFragment.ContentTypeFocus){
            beanList = MemExchange.getInstance().getFocusList();
            page = MemExchange.getInstance().getFocusListPage();
        }else{
            beanList = MemExchange.getInstance().getPopularList();
            page = MemExchange.getInstance().getPopularListPage();
        }
        if(beanList.size()>0 && page!=0){
//            adapter.setList(MemExchange.getInstance().getFocusList(),true);
            adapter.setDataAll(beanList);
            showContentView();
        }else {
            Logger.e(Logger.DEBUG_TAG, "focusFragmentViewModel,网络请求加载第一页数据");
            if(!onNetWorkDisConnecte(true)) {
                mStart = 1;
                this.loadCustomData(mStart);
            }
        }
    }/**
     * visible时自动调用、错误时点击重新加载
     */
    @Override
    public void onloadData() {
        super.onloadData();
        loadData();
    }


    public void setAdapterData(){
        if(adapter == null){
            if(type == FocusFragment.ContentTypeFocus) {
                adapter = new UserListFocusAdapter(context.getContext());
            }else{
                adapter = new UserListPopularAdapter(context.getContext());
            }
        }

        ((FragmentForcusBinding)bindingView).setAdapter(adapter);

    }
    public void clearData(){
        ((FragmentForcusBinding)bindingView).xrvFocus.removeOnScrollListener(onScrollListener);
    }


    //region=====Model层============



    private void loadCustomData(final int page){
        mainModel.getFocusData(page, type,city,context,new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<UserBeanItem> itemList = (List<UserBeanItem>) object;

                if(adapter == null){
                    return;
                }
                if(mStart == 1) {
                    if (itemList.size() > 0) {
                        if(type == FocusFragment.ContentTypeFocus) {
                            MemExchange.getInstance().setFocusList(itemList, page);
                            adapter.setDataAll(MemExchange.getInstance().getFocusList());
                        }else{
                            MemExchange.getInstance().setPopularList(itemList,page);
                            adapter.setDataAll(MemExchange.getInstance().getPopularList());
                        }
                    }
                }else{
                    if(itemList.size() > 0) {
                        if(type == FocusFragment.ContentTypeFocus) {
                            MemExchange.getInstance().setFocusList(itemList, page);
//                        adapter.setList(itemList,false);
                            adapter.setDataAll(MemExchange.getInstance().getFocusList());
                        }else{
                            MemExchange.getInstance().setPopularList(itemList,page);
                            adapter.setDataAll(MemExchange.getInstance().getPopularList());
                        }
                    }else{
                        mStart --;
                        adapter.updateLoadStatus(UserListAdapter.LOAD_END);
                        //todo 需要做footView逐渐消失的功能

                        return;
                    }
                    if(adapter!=null){
                        adapter.updateLoadStatus(UserListAdapter.LOAD_PULL_TO);
                    }
                }
                showContentView();
                isRefreshing.set(false);
            }

            @Override
            public void loadFailed() {
                isRefreshing.set(false);
                if(mStart == 1){
                    showError();
                    return;
                }
                showContentView();
                mStart --;
            }

            @Override
            public void addSubscription(Disposable disposable) {
                Logger.e(Logger.DEBUG_TAG,"FocusFragmentViewModel,addSubscription");
//                context.addSubscription(disposable);
                RxApiManager.get().add(FocusFragment.TAG+FocusFragment.ContentTypeFocus,disposable);
            }
        });
    }

    //endregion=====Model层=========
}
