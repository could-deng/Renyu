package could.bluepay.renyumvvm.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.squareup.leakcanary.RefWatcher;

import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.FragmentVipBinding;
import could.bluepay.renyumvvm.viewmodel.BaseFragmentViewModel;

/**
 * Created by bluepay on 2017/11/22.
 */

public class VipFragment extends BaseFragment<FragmentVipBinding,BaseFragmentViewModel> {
    public static final String TAG = "VipFragment";
    public String setFragmentName(){
        return TAG;
    }
    @Override
    public BaseFragmentViewModel setViewModel() {
        if(baseFragmentViewModel == null) {
            baseFragmentViewModel = new BaseFragmentViewModel();
        }
        return baseFragmentViewModel;
    }

    @Override
    protected int getContent() {
        return R.layout.fragment_vip;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //显示加载中
        showLoading();

        //内容
        ((MainActivity)getActivity()).setToolbarTitle(getString(R.string.ui_title_vip));
        //
        showContentView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MixApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
