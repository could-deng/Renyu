package could.bluepay.renyumvvm.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.squareup.leakcanary.RefWatcher;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.databinding.FragmentInviteBinding;
import could.bluepay.renyumvvm.viewmodel.InviteFragmentViewModel;

/**
 * Created by bluepay on 2017/11/22.
 */

public class InviteFragment extends BaseFragment<FragmentInviteBinding,InviteFragmentViewModel> {
    public static final String TAG = "InviteFragment";
    public String setFragmentName(){
        return TAG;
    }

    @Override
    public InviteFragmentViewModel setViewModel() {
        if(baseFragmentViewModel == null) {
            baseFragmentViewModel = new InviteFragmentViewModel();
        }
        return baseFragmentViewModel;
    }

    //Fragment内容类型
    public static final int ContentTypeInvite = 3;

    private static final String TYPE = "param1";
//    private String city = "全国";

    private MainActivity activity;

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //显示内容
        showContentView();
        bindingView.setViewModel(setViewModel());

        bindingView.srlInvite.setColorSchemeColors(getResources().getColor(R.color.colorTheme));
        bindingView.srlInvite.setProgressViewOffset(true,0,200);

        //准备就绪
        isPrepared = true;

        loadData();
    }

    @Override
    protected void loadData() {
        Logger.e(Logger.DEBUG_TAG,"InviteFragment,loadData(),"+(isPrepared?"1":"0")+","+(mIsVisible?"1":"0"));
        if(!isPrepared  || setViewModel().haveData()|| !mIsVisible){
            return;
        }
        setViewModel().onloadData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mIsFirst = true;//销毁时，重新加载数据时，认为第一次请求
        RefWatcher refWatcher = MixApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }


}
