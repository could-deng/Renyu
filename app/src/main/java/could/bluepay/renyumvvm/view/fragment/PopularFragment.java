package could.bluepay.renyumvvm.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.view.base.BaseFragment;
import could.bluepay.renyumvvm.databinding.FragmentPopularBinding;

/**
 * Created by bluepay on 2017/11/20.
 */

public class PopularFragment extends BaseFragment<FragmentPopularBinding> {
    public static final String TAG = "PopularFragment";
    public String setFragmentName(){
        return TAG;
    }

    @Override
    protected int getContent() {
        return R.layout.fragment_popular;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //显示加载中
        showLoading();

        showContentView();

        loadData();
    }

    @Override
    protected void loadData() {
        super.loadData();

    }
}
