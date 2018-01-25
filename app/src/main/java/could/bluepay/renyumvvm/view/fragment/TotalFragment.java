package could.bluepay.renyumvvm.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.squareup.leakcanary.RefWatcher;
import java.util.ArrayList;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.view.adapter.MyFragmentPagerAdapter;
import could.bluepay.renyumvvm.databinding.FragmentTotalBinding;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.viewmodel.BaseFragmentViewModel;

/**
 * MainActivity底部第一个tab内容
 */

public class TotalFragment extends BaseFragment<FragmentTotalBinding,BaseFragmentViewModel>{
    public static final String TAG = "TotalFragment";



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

    private MyFragmentPagerAdapter myAdapter;
    private ArrayList<String> mTitles ;
    private ArrayList<Fragment> mFragments;

    @Override
    protected int getContent() {
        return R.layout.fragment_total;
    }

    @Override
    protected void loadData() {
        super.loadData();

    }

    @Override
    protected void onCreateViewExtra() {
        super.onCreateViewExtra();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Logger.e(Logger.DEBUG_TAG,TAG+"onActivityCreated");

        super.onActivityCreated(savedInstanceState);
        //显示加载中
        showLoading();
        initFragmentList();

        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻3个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(),mFragments,mTitles);
        bindingView.vpTotal.setAdapter(myAdapter);
        bindingView.vpTotal.setOffscreenPageLimit(mFragments.size()-1);
        myAdapter.notifyDataSetChanged();
        String[] titles = new String[]{mTitles.get(0),mTitles.get(1),mTitles.get(2)};
        ((MainActivity)getActivity()).setIndicator(bindingView.vpTotal,titles);

        //显示内容
        showContentView();

        //注册事件
        initRxBus();
    }

    private void initFragmentList(){
        if(mTitles == null) {
            mTitles = new ArrayList<>();
            mTitles.add(this.getResources().getString(R.string.title_focus));
            mTitles.add(this.getResources().getString(R.string.title_popular));
            mTitles.add(this.getResources().getString(R.string.title_invite));
        }
        if(mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.clear();
            mFragments.add(FocusFragment.newInstance(FocusFragment.ContentTypeFocus));
            mFragments.add(FocusFragment.newInstance(FocusFragment.ContentTYpePopular));
            mFragments.add(InviteFragment.newInstance(InviteFragment.ContentTypeInvite));
        }
    }

    /**
     *
     * 注册事件
     */
    private void initRxBus(){
        // TODO: 2017/12/25
//        Subscription subscription = RxBus.getDefault().toObservable(RxCodeConstants.JUMP_TYPE, Integer.class)
//                .subscribe(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer integer) {
//
//                    }
//                });
//        addSubscription(subscription);
    }

    private void clear(){
        mTitles.clear();
        mTitles = null;
        myAdapter.clear();
        mFragments.clear();
        mFragments = null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        clear();
        RefWatcher refWatcher = MixApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
