package could.bluepay.renyumvvm.view.Total;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import could.bluepay.renyumvvm.view.MainActivity;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.view.adapter.MyFragmentPagerAdapter;
import could.bluepay.renyumvvm.view.base.BaseFragment;
import could.bluepay.renyumvvm.databinding.FragmentTotalBinding;
import could.bluepay.renyumvvm.rx.RxBus;
import could.bluepay.renyumvvm.rx.RxCodeConstants;
import could.bluepay.renyumvvm.view.Total.child.FocusFragment;
import could.bluepay.renyumvvm.view.Total.child.InviteFragment;
import could.bluepay.renyumvvm.view.Total.child.PopularFragment;
import could.bluepay.renyumvvm.utils.Logger;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by bluepay on 2017/11/20.
 */

public class TotalFragment extends BaseFragment<FragmentTotalBinding>{
    public static final String TAG = "TotalFragment";
    public String setFragmentName(){
        return TAG;
    }

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
        Logger.e(Logger.DEBUG_TAG,"onActivityCreated");

        super.onActivityCreated(savedInstanceState);
        //显示加载中
        showLoading();
        initFragmentList();

        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻3个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(),mFragments,mTitles);
        bindingView.vpTotal.setAdapter(myAdapter);
        bindingView.vpTotal.setOffscreenPageLimit(mFragments.size()-1);
        myAdapter.notifyDataSetChanged();
        ((MainActivity)getActivity()).setIndicator(bindingView.vpTotal);

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
     * 注册事件
     */
    private void initRxBus(){
        Subscription subscription = RxBus.getDefault().toObservable(RxCodeConstants.JUMP_TYPE, Integer.class)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {

                    }
                });
        addSubscription(subscription);
    }
}
