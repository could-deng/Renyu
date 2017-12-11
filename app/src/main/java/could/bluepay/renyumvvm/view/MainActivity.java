package could.bluepay.renyumvvm.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.common.PrefsHelper;
import could.bluepay.renyumvvm.databinding.ActivityMainBinding;
import could.bluepay.renyumvvm.model.MemExchange;
import could.bluepay.renyumvvm.rx.RxBus;
import could.bluepay.renyumvvm.rx.RxBusBaseMessage;
import could.bluepay.renyumvvm.rx.RxCodeConstants;
import could.bluepay.renyumvvm.view.Dynamic.DynamicFragment;
import could.bluepay.renyumvvm.view.Total.TotalFragment;
import could.bluepay.renyumvvm.view.Vip.VipFragment;
import could.bluepay.renyumvvm.view.my.MyFragment;
import could.bluepay.renyumvvm.widget.imageWatcher.ImageWatcher;
import could.bluepay.renyumvvm.widget.statusbar.StatusBarUtil;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    //id
    Toolbar toolbar;
    TextView tv_toolbar_title;
    TabLayout tabLayout;
    RadioGroup rg_outer_fragment;
    RadioButton rb_list;
    RadioButton rb_dynamic;
    RadioButton rb_vip;
    RadioButton rb_my;
    Button btn_public;
    FrameLayout fl_container;

    long uid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initStatusView();
        initId();
        initToolbar();
        initRxBus();
        initImageWatch();
//        initContentFragment();
        startShowFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MemExchange.getInstance().clear();
    }

    public long getUid(){
        //// TODO: 2017/11/28 暂时写死
        PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).writeLong(Config.SP_KEY_UID,1);
        PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).write(Config.SP_KEY_NICKNAME,"二蛋");

        if(uid == 0) {
            uid = PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).readLong(Config.SP_KEY_UID);
        }
        return uid;
    }
    /**
     * 生成状态栏
     */
    private void initStatusView(){
        ViewGroup.LayoutParams layoutParams = binding.viewStatus.getLayoutParams();
        layoutParams.height = StatusBarUtil.getStatusBarHeight(this);
        binding.viewStatus.setLayoutParams(layoutParams);
    }

    private void initId(){
        toolbar = binding.toolbar;
        tv_toolbar_title = binding.tvToolbarTitle;
        tabLayout = binding.indicator;
        rg_outer_fragment = binding.rgOuterFragment;
        rb_list = binding.rbList;
        rb_dynamic = binding.rbDynamic;
        rb_vip = binding.rbVip;
        rb_my = binding.rbMy;
        btn_public = binding.btnPublic;
        fl_container = binding.flContainer;

    }

//region================以viewaPager展示fragment=========================
//    private ViewPager.OnPageChangeListener onPageChangeListener;

//    private void initContentFragment(){
//        ArrayList<Fragment> mFragmentList = new ArrayList<>();
//        mFragmentList.add(new TotalFragment());
//        mFragmentList.add(new DynamicFragment());
//        mFragmentList.add(new VipFragment());
//        mFragmentList.add(new MyFragment());
//        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),mFragmentList);
//        vp_container.setAdapter(adapter);
//        vp_container.setOffscreenPageLimit(2);
//
//        vp_container.addOnPageChangeListener(getOnPageChangeListener());
//        binding.rbList.setChecked(true);
//        vp_container.setCurrentItem(0);
//    }

//    private ViewPager.OnPageChangeListener getOnPageChangeListener(){
//        if(onPageChangeListener == null){
//            onPageChangeListener = new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                }
//
//                @Override
//                public void onPageSelected(int position) {
//                    RadioButton rb = (RadioButton) rg_outer_fragment.getChildAt(position);
//                    rb.setChecked(true);
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//
//                }
//            };
//        }
//        return onPageChangeListener;
//    }

//endregion================以viewaPager展示fragment=========================


//region=====================fragment间以replace方式切换===============

    private static final int FRAGMENT_Total = 0;
    private static final int FRAGMENT_Dynamic = 1;
    private static final int FRAGMENT_Vip = 2;
    private static final int FRAGMENT_My = 3;

    private int currentNavIndex = -1;
    private TotalFragment totalFragment;
    private DynamicFragment dynamicFragment;
    private VipFragment vipFragment;
    private MyFragment myFragment;


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
//        startShowFragment();
    }
    private void startShowFragment(){
        if (currentNavIndex == FRAGMENT_Total || currentNavIndex == -1) {
            TotalFragment fragment = (TotalFragment) getSupportFragmentManager().findFragmentByTag(TotalFragment.TAG);
            if (fragment != null) {
                if (currentNavIndex == -1) {
                    if (binding.rbList != null) {
                        binding.rbList.setChecked(true);
                    }
                } else {
                    switchToFragment(FRAGMENT_Total);
                }
            } else {
                switchToFragment(FRAGMENT_Total);
            }
        }
    }

    private void switchToFragment(int pageIndex){
        Fragment fragment ;
        String fragmentTag;

        //todo 存在bug，如果切换过快,currentNavIndex改变了，但是fragment
        if(currentNavIndex == pageIndex){
            return;
        }

        switch (pageIndex){
            case FRAGMENT_Total:
                fragmentTag = TotalFragment.TAG;
                fragment = getSupportFragmentManager().findFragmentByTag(TotalFragment.TAG);
                if(fragment == null) {
                    fragment = getTotalFragment();
                }else{
                    return;
                }
                break;
            case FRAGMENT_Dynamic:
                fragmentTag = DynamicFragment.TAG;
                fragment = getSupportFragmentManager().findFragmentByTag(DynamicFragment.TAG);
                if(fragment == null) {
                    fragment = getDynamicFragment();
                }else{
                    return;
                }
                break;
            case FRAGMENT_Vip:
                fragmentTag = VipFragment.TAG;
                fragment = getSupportFragmentManager().findFragmentByTag(VipFragment.TAG);
                if(fragment == null) {
                    fragment = getVipFragment();
                }else{
                    return;
                }
                break;
            case FRAGMENT_My:
                fragmentTag = MyFragment.TAG;
                fragment = getSupportFragmentManager().findFragmentByTag(MyFragment.TAG);
                if(fragment == null) {
                    fragment = getMyFragment();
                }else{
                    return;
                }
                break;

            default:
                return;
        }
        int animationIdIn = 0;
        int animationIdOut = 0;
//        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
//        if(backStackEntryCount == 0){
        if(this.currentNavIndex < 0){

        }else if(this.currentNavIndex > pageIndex){
            animationIdIn = R.anim.push_left_in;
            animationIdOut = R.anim.push_right_out;
        }else if(this.currentNavIndex < pageIndex){
            animationIdIn = R.anim.push_right_in;
            animationIdOut = R.anim.push_left_out;
        }else{
            return;
        }
//        }
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(animationIdIn,animationIdOut,0,0)
                .replace(R.id.fl_container,fragment,fragmentTag)
                .commitAllowingStateLoss();

        RadioButton rb = (RadioButton) rg_outer_fragment.getChildAt(pageIndex);
        rb.setChecked(true);
        this.currentNavIndex = pageIndex;
    }
    private TotalFragment getTotalFragment(){
        if(totalFragment == null){
            totalFragment = new TotalFragment();
        }
        return totalFragment;
    }
    private DynamicFragment getDynamicFragment(){
        if(dynamicFragment == null){
            dynamicFragment = new DynamicFragment();
        }
        return dynamicFragment;
    }
    private VipFragment getVipFragment(){
        if(vipFragment == null){
            vipFragment = new VipFragment();
        }
        return vipFragment;
    }
    private MyFragment getMyFragment(){
        if(myFragment == null){
            myFragment = new MyFragment();
        }
        return myFragment;
    }

//endregion=====================fragment间以replace方式切换===============


    public void setIndicator(ViewPager viewpager){
        if(viewpager!=null){
            tv_toolbar_title.setVisibility(View.GONE);

            binding.indicator.setVisibility(View.VISIBLE);
            binding.indicator.setTabMode(TabLayout.GRAVITY_CENTER);
            binding.indicator.setupWithViewPager(viewpager);
        }
    }
    public void setToolbarTitle(String title){
        if(TextUtils.isEmpty(title)){
            title = getResources().getString(R.string.app_name);
        }
        binding.indicator.setVisibility(View.GONE);
        tv_toolbar_title.setVisibility(View.VISIBLE);
        tv_toolbar_title.setText(title);
    }


    private void initToolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.rb_list:
                if(currentNavIndex!=FRAGMENT_Total) {
                    switchToFragment(FRAGMENT_Total);
                }
                break;
            case R.id.rb_dynamic:
                if(currentNavIndex!=FRAGMENT_Dynamic){
                    switchToFragment(FRAGMENT_Dynamic);
                }
                break;
            case R.id.rb_vip:
                if(currentNavIndex!=FRAGMENT_Vip){
                    switchToFragment(FRAGMENT_Vip);
                }
                break;
            case R.id.rb_my:
                if(currentNavIndex!=FRAGMENT_My){
                    switchToFragment(FRAGMENT_My);
                }
                break;
        }
    }


    /**
     * 注册事件
     */
    private void initRxBus() {
        RxBus.getDefault().toObservable(RxCodeConstants.JUMP_TYPE_TO_ONE, RxBusBaseMessage.class)
                .subscribe(new Action1<RxBusBaseMessage>() {
                    @Override
                    public void call(RxBusBaseMessage integer) {
//                        binding.flContainer.setCurrentItem(1);

                    }
                });
    }


    //region================imageWatch相关==============

    private void initImageWatch(){
        binding.imageWatcher.setTranslucentStatus(StatusBarUtil.getStatusBarHeight(this));
        // 配置error图标
        binding.imageWatcher.setErrorImageRes(R.drawable.error_picture);
        // 长按图片的回调，你可以显示一个框继续提供一些复制，发送等功能
        binding.imageWatcher.setOnPictureLongPressListener(new ImageWatcher.OnPictureLongPressListener() {
            @Override
            public void onPictureLongPress(ImageView v, String url, int pos) {

            }
        });
    }
    /**
     * 显示ImageWatch
     */
    public void showImageWatch(View view, List<ImageView> imagesList,List<String> imagesUrlList){
        binding.imageWatcher.show((ImageView) view,imagesList,imagesUrlList);

    }

    //endregion================imageWatch相关==============

}
