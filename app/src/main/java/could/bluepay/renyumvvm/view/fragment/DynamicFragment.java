package could.bluepay.renyumvvm.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.squareup.leakcanary.RefWatcher;

import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.view.base.BaseFragment;
import could.bluepay.renyumvvm.databinding.FragmentDynamicBinding;
import could.bluepay.renyumvvm.viewmodel.DynamicViewModel;
import could.bluepay.widget.xrecyclerview.XRecyclerView;
import io.reactivex.disposables.Disposable;

/**
 * MainActivity底部第二个tab内容
 */

public class DynamicFragment extends BaseFragment<FragmentDynamicBinding> {
    public static final String TAG = "DynamicFragment";
    public String setFragmentName(){
        return TAG;
    }

//    private MainModel mModel;
//    private DynamicAdapter dynamicAdapter;
    private boolean mIsFirst = true;

//    private int page = 0;
    LinearLayoutManager ll;

    public static DynamicFragment getInstance(){
        DynamicFragment instance = new DynamicFragment();
        return instance;
    }


    private DynamicViewModel viewModel;

    @Override
    protected int getContent() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //显示加载中
        showLoading();

        //内容
        ((MainActivity)getActivity()).setToolbarTitle(getString(R.string.ui_title_dynamic));



//        mModel = new MainModel();
//        dynamicAdapter = new DynamicAdapter();
//        dynamicAdapter.setModel(mModel);
//        dynamicAdapter.setUid(((MainActivity)(getActivity())).getUid());
//        dynamicAdapter.setNickName(PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).read(Config.SP_KEY_NICKNAME));
//
        bindingView.xrvDynamic.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                viewModel.loadDynamicData(true);
            }

            @Override
            public void onLoadMore() {
                viewModel.loadDynamicData(false);
            }
        });


        viewModel = new DynamicViewModel(bindingView,getActivity());

        viewModel.setUpdateView(new DynamicViewModel.UpdateView() {
            @Override
            public void showFragmentContentView(boolean ifSuccess) {
                showContentView();
                if(!ifSuccess){//如果是第一页并且是加载失败情况
                    showError();
                }
            }

            @Override
            public void fragmentAddSubscription(Disposable disposable) {
                addSubscription(disposable);
            }

            @Override
            public void haveSetData() {
                mIsFirst = false;
            }
        });
        viewModel.setAdapterData();


        isPrepared = true;
        /**
         * 因为启动时先走loadData()再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData();
    }

    @Override
    protected void loadData() {
        Logger.e(Logger.DEBUG_TAG,"DynamicFragment,loadData(),"+(isPrepared?"1":"0")+","+(mIsVisible?"1":"0")+","+(mIsFirst?"1":"0"));
        if (!isPrepared ) {//|| !mIsFirst
            return;
        }
        // TODO: 2017/11/28  缓存,如果有缓存，则先showContentView()

        viewModel.loadDynamicData(true);

    }

    @Override
    protected void onRefresh() {
        loadData();
    }


    private void clear(){
        viewModel=null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        clear();
        RefWatcher refWatcher = MixApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
