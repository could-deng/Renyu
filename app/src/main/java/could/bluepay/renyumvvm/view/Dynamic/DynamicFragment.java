package could.bluepay.renyumvvm.view.Dynamic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.view.base.BaseFragment;
import could.bluepay.renyumvvm.databinding.FragmentDynamicBinding;
import could.bluepay.renyumvvm.viewmodel.DynamicViewModel;
import could.bluepay.widget.xrecyclerview.XRecyclerView;
import rx.Subscription;

/**
 * Created by bluepay on 2017/11/22.
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
//                viewModel.loadDynamicData(false);
            }
        });


        viewModel = new DynamicViewModel(bindingView,getActivity());

        viewModel.setUpdateView(new DynamicViewModel.UpdateView() {
            @Override
            public void showFragmentContentView() {
                showContentView();
            }

            @Override
            public void fragmentAddSubscription(Subscription subscription) {
                addSubscription(subscription);
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



//    private void loadDynamicData(){
//        page = 1;
//        mModel.setData(((MainActivity)(getActivity())).getUid(),page);
//
//        mModel.getHotDynamicsData(new RequestImpl() {
//
//            @Override
//            public void loadSuccess(Object object) {
//                ObservableArrayList<WeiboBean> dataList = (ObservableArrayList<WeiboBean>)object;
//                showContentView();
//                if(null == dataList || dataList.size() == 0){
//                    return;
//                }
//                viewModel.setData(page,dataList);
//                if(page!=1){
//                    bindingView.xrvDynamic.refreshComplete();
//                }
//            }
//
//            @Override
//            public void loadFailed() {
//                showContentView();
//                Logger.e(Logger.DEBUG_TAG,"loadFailed()");
//            }
//
//            @Override
//            public void addSubscription(Subscription subscription) {
//                DynamicFragment.this.addSubscription(subscription);
//            }
//        });
//
//    }




//    private void setAdapter(ObservableArrayList<WeiboBean> dataList){
//
//        dynamicAdapter.removeAll();
//        dynamicAdapter.addAll(dataList);
//        ll = new LinearLayoutManager(getActivity());
//        bindingView.xrvDynamic.setLayoutManager(ll);
//        bindingView.xrvDynamic.setAdapter(dynamicAdapter);
//
//        dynamicAdapter.setClick(new DynamicAdapter.poupWindowClick() {
//            @Override
//            public void updateRecycleView(int dynamicIndex) {
//                int f = ll.findFirstVisibleItemPosition();
//                int e = ll.findLastVisibleItemPosition();
//
//                //因为recyclerView的默认加了一个空白头，所以index从1开始，而非0
//                dynamicIndex++;
//                Logger.e(Logger.DEBUG_TAG,"first:"+f+",last:"+e+",dynamicIndex:"+dynamicIndex);
//
//                if(f<=(dynamicIndex) && (dynamicIndex)>=e){
//                    dynamicAdapter.notifyItemChanged(dynamicIndex);
//                }
//
//            }
//
//            @Override
//            public void addSubscription(Subscription subscription) {
//                DynamicFragment.this.addSubscription(subscription);
//            }
//        });
//
//        dynamicAdapter.notifyDataSetChanged();
//        bindingView.xrvDynamic.refreshComplete();
//        mIsFirst = false;
//
//    }

}
