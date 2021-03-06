package could.bluepay.renyumvvm.view.fragment;

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
import could.bluepay.renyumvvm.databinding.FragmentBaseBinding;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.viewmodel.BaseFragmentViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by bluepay on 2017/11/20.
 */

public abstract class BaseFragment<SV extends ViewDataBinding,SVM extends BaseFragmentViewModel> extends Fragment {

    public abstract String setFragmentName();

    protected SVM baseFragmentViewModel;

    //根布局
    protected FragmentBaseBinding baseBindView;

    //布局view
    protected SV bindingView;

    //fragment是否显示了
    protected boolean mIsVisible = false;
    //setUserVisibleHit()和onCreateView()的先后顺序没有明确界定。应次loadData()在onActivityCreate()加载完控件之后才是可以的
    protected boolean isPrepared = false;

    private CompositeDisposable mCompositeDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            Logger.e(Logger.DEBUG_TAG,"BaseFragment,getUserVisibleHint(),true");
            mIsVisible = true;
            onVisible();
        }else{
            mIsVisible = false;
            onInvisible();
        }
    }
    public abstract SVM setViewModel();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //就是往基础fragment_base里面加入各自实现的bindingView
//        View ll = inflater.inflate(R.layout.fragment_base,null);
        baseBindView = DataBindingUtil.inflate(getActivity().getLayoutInflater(),R.layout.fragment_base,null,false);
        baseBindView.setBaseViewModel(setViewModel());

        bindingView = DataBindingUtil.inflate(getActivity().getLayoutInflater(),getContent(),null,false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        baseBindView.container.addView(bindingView.getRoot());
        onCreateViewExtra();

        return baseBindView.getRoot();
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
    }

    /**
     * 显示正在加载中
     */
    protected void showLoading(){
        setViewModel().viewStyle.loadingProcess.set(true);
        setViewModel().viewStyle.loadingSuccess.set(false);
        setViewModel().viewStyle.loadingError.set(false);
    }

    /**
     * 显示内容
     */
    protected void showContentView(){
        setViewModel().viewStyle.loadingProcess.set(false);
        setViewModel().viewStyle.loadingSuccess.set(true);
        setViewModel().viewStyle.loadingError.set(false);
    }

    /**
     * 显示加载失败
     */
    protected void showError(){
        setViewModel().viewStyle.loadingProcess.set(false);
        setViewModel().viewStyle.loadingSuccess.set(false);
        setViewModel().viewStyle.loadingError.set(true);
    }




//    public void addSubscription(Disposable s){
//        if(this.mCompositeDisposable == null){
//            mCompositeDisposable = new CompositeDisposable();
//        }
//        mCompositeDisposable.add(s);
//    }

    protected void clearBeforeVMClear(){

    }
    private void clear(){
        if(baseFragmentViewModel!=null){
            baseFragmentViewModel = null;
        }
    }
    @Override
    public void onDestroy() {
        Logger.e(Logger.DEBUG_TAG,"onDestroy,"+setFragmentName());
        super.onDestroy();
        clearBeforeVMClear();
        clear();
//        if(this.mCompositeDisposable!=null && mCompositeDisposable.size()>0){
//            this.mCompositeDisposable.clear();
//        }
    }
}
