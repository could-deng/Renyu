package could.bluepay.renyumvvm.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.squareup.leakcanary.RefWatcher;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.FragmentDynamicBinding;
import could.bluepay.renyumvvm.viewmodel.DynamicViewModel;

/**
 * 动态Fragment
 */

public class DynamicFragment extends BaseFragment<FragmentDynamicBinding,DynamicViewModel> {

    public static final String TAG = "DynamicFragment";
    public String setFragmentName(){
        return TAG;
    }

    @Override
    public DynamicViewModel setViewModel() {
        if(baseFragmentViewModel == null){
            baseFragmentViewModel = new DynamicViewModel();
        }
        return baseFragmentViewModel;
    }

    public static DynamicFragment getInstance(){
        DynamicFragment instance = new DynamicFragment();
        return instance;
    }


    @Override
    protected int getContent() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //显示加载中
        showLoading();

        setViewModel().init(bindingView,DynamicFragment.this);
        bindingView.setViewModel(setViewModel());

        //内容
        ((MainActivity)getActivity()).setToolbarTitle(getString(R.string.ui_title_dynamic));

        setViewModel().setAdapterData();


        isPrepared = true;
        /**
         * 因为启动时先走loadData()再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData();
    }

    @Override
    protected void loadData() {
        Logger.e(Logger.DEBUG_TAG,"DynamicFragment,loadData(),"+(isPrepared?"1":"0")+","+(mIsVisible?"1":"0"));
        if (!isPrepared || setViewModel().haveData()) {
            return;
        }
        // TODO: 2017/11/28  缓存,如果有缓存，则先showContentView()

        setViewModel().onloadData();
    }

    @Override
    protected void onRefresh() {
        loadData();
    }


    @Override
    public void onDestroy() {
        Logger.e(Logger.DEBUG_TAG,TAG+"onDestroy");
        super.onDestroy();
        RefWatcher refWatcher = MixApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
