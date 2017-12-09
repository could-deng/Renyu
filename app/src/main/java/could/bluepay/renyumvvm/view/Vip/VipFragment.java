package could.bluepay.renyumvvm.view.Vip;

import android.os.Bundle;
import android.support.annotation.Nullable;

import could.bluepay.renyumvvm.view.MainActivity;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.view.base.BaseFragment;
import could.bluepay.renyumvvm.databinding.FragmentVipBinding;

/**
 * Created by bluepay on 2017/11/22.
 */

public class VipFragment extends BaseFragment<FragmentVipBinding> {
    public static final String TAG = "VipFragment";
    public String setFragmentName(){
        return TAG;
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
}
