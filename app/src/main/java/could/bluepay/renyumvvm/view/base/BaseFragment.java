package could.bluepay.renyumvvm.view.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.utils.PerfectClickListener;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by bluepay on 2017/11/20.
 */

public abstract class BaseFragment<SV extends ViewDataBinding> extends Fragment {

    public abstract String setFragmentName();

    //布局view
    protected SV bindingView;

    //fragment是否显示了
    protected boolean mIsVisible = false;
    //setUserVisibleHit()和onCreateView()的先后顺序没有明确界定。应次loadData()在onActivityCreate()加载完控件之后才是可以的
    protected boolean isPrepared = false;

    //加载中
    private LinearLayout mLlProgressbar;
    //加载失败
    private LinearLayout mRefresh;
    //内容布局
    protected RelativeLayout mContainer;

//    private CompositeSubscription mCompositeSubscription;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            mIsVisible = true;
            onVisible();
        }else{
            mIsVisible = false;
            onInvisible();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //就是往基础fragment_base里面加入各自实现的bindingView
        View ll = inflater.inflate(R.layout.fragment_base,null);
        bindingView = DataBindingUtil.inflate(getActivity().getLayoutInflater(),getContent(),null,false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        mContainer = (RelativeLayout) ll.findViewById(R.id.container);
        mContainer.addView(bindingView.getRoot());
        onCreateViewExtra();

        return ll;
    }
    protected void onCreateViewExtra(){

    }
    protected abstract int getContent();

    /**
     * 显示时加载数据,需要这样的使用
     * 注意声明 isPrepared，先初始化
     * 生命周期会先执行 setUserVisibleHint 再执行onActivityCreated
     * 在 onActivityCreated 之后第一次显示加载数据，只加载一次
     */
    protected void loadData(){
        //这里的return,没有实质意义。在子类return才有意义
        if(!isPrepared ||!mIsVisible){
            return;
        }

    }
    protected void onVisible(){
        loadData();
    }

    protected void onInvisible() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLlProgressbar = getView(R.id.ll_progress_bar);

        mRefresh = getView(R.id.ll_error_refresh);
        mRefresh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onDoubleClick(View view) {
                showLoading();
                onRefresh();
            }
        });
    }
    protected <T extends View>T getView(int id){
        return (T) getView().findViewById(id);
    }

    /**
     * 加载失败后点击的操作
     */
    protected void onRefresh(){

    }
    /**
     * 显示正在加载中
     */
    protected void showLoading(){
        if(mLlProgressbar.getVisibility()!=View.VISIBLE){
            mLlProgressbar.setVisibility(View.VISIBLE);
        }
        if(bindingView.getRoot().getVisibility() !=View.GONE){
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if(mRefresh.getVisibility()!= View.GONE){
            mRefresh.setVisibility(View.GONE);
        }
    }

    /**
     * 显示内容
     */
    protected void showContentView(){
        if(mLlProgressbar.getVisibility()!=View.GONE){
            mLlProgressbar.setVisibility(View.GONE);
        }
        if(bindingView.getRoot().getVisibility() !=View.VISIBLE){
            bindingView.getRoot().setVisibility(View.VISIBLE);
        }
        if(mRefresh.getVisibility()!= View.GONE){
            mRefresh.setVisibility(View.GONE);
        }
    }

    /**
     * 显示加载失败
     */
    protected void showError(){
        if(mLlProgressbar.getVisibility()!=View.GONE){
            mLlProgressbar.setVisibility(View.GONE);
        }
        if(bindingView.getRoot().getVisibility() !=View.GONE){
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if(mRefresh.getVisibility()!= View.VISIBLE){
            mRefresh.setVisibility(View.VISIBLE);
        }
    }




    public void addSubscription(Disposable s){
//        if(this.mCompositeSubscription == null){
//            mCompositeSubscription = new CompositeSubscription();
//        }
//        this.mCompositeSubscription.add(s);
        if(this.mCompositeDisposable == null){
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(s);
    }
    @Override
    public void onDestroy() {
        Logger.e(Logger.DEBUG_TAG,"onDestroy,"+setFragmentName());
        super.onDestroy();
        if(this.mCompositeDisposable!=null && mCompositeDisposable.size()>0){
            this.mCompositeDisposable.clear();
        }
    }
}
