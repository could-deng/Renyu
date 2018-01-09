package could.bluepay.renyumvvm.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.FragmentMyBinding;

/**
 * Created by bluepay on 2017/11/22.
 */

public class MyFragment extends BaseFragment<FragmentMyBinding> {
    public static final String TAG = "MyFragment";
    public String setFragmentName(){
        return TAG;
    }
    @Override
    protected int getContent() {
        return R.layout.fragment_my;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //显示加载中
        showLoading();

        //内容
        ((MainActivity)getActivity()).setToolbarTitle(getString(R.string.ui_title_my));
        //
        showContentView();
        loadData();
    }

    @Override
    protected void loadData() {
        super.loadData();
        bindingView.setViewModel(this);
    }


}
