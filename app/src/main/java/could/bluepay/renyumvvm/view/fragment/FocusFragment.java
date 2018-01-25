package could.bluepay.renyumvvm.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.squareup.leakcanary.RefWatcher;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.FragmentForcusBinding;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.viewmodel.FocusFragmentViewModel;

/**
 * TotalFragment之下的关注模块
 */

public class FocusFragment extends BaseFragment<FragmentForcusBinding,FocusFragmentViewModel> {

    public static final String TAG = "FocusFragment";
    public String setFragmentName(){
        return TAG;
    }

    @Override
    public FocusFragmentViewModel setViewModel() {
        if(baseFragmentViewModel == null){
            baseFragmentViewModel = new FocusFragmentViewModel();
        }
        return baseFragmentViewModel;
    }

    //Fragment内容类型
    public static final int ContentTypeFocus = 1;
    public static final int ContentTYpePopular = 2;
    public static final int ContentTypeInvite = 3;

    //内容类型
    private int mType;
    private static final String TYPE = "param1";



    @Override
    protected int getContent() {
        return R.layout.fragment_forcus;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
//        Logger.e(Logger.DEBUG_TAG,((mType == ContentTypeFocus)?"FocusFragment":"PopularFragment")+",setUserVisibleHint()"+(getUserVisibleHint()?"visible":"invisible"));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //显示内容
        showContentView();

        setViewModel().init(bindingView,FocusFragment.this,mType);

        bindingView.setViewModel(setViewModel());

        bindingView.srlFocus.setColorSchemeColors(getResources().getColor(R.color.colorTheme));
        bindingView.srlFocus.setProgressViewOffset(true,0,200);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        bindingView.setLayoutManager(mLayoutManager);
        setViewModel().setAdapterData();

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
        Logger.e(Logger.DEBUG_TAG,((mType == ContentTypeFocus)?"FocusFragment":"PopularFragment")+"loadData(),"+(isPrepared?"1":"0")+","+(mIsVisible?"1":"0")+",");
        if(!isPrepared  || setViewModel().haveData()|| !mIsVisible){
            return;
        }

        setViewModel().loadData();
    }

    public void clearBeforeVMClear(){
        setViewModel().clearData();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MixApp.getRefWatcher(getActivity());
		refWatcher.watch(this);
    }


    @Override
    protected void onRefresh() {
        loadData();
    }

    //region=======约拍特定布局相关====================



    //endregion=======约拍特定布局相关====================
}
