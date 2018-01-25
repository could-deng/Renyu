package could.bluepay.renyumvvm.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * TotalFragment上的三个Fragment适配器
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<? extends Fragment> mFragment;
    private List<String> mTitleLabel;

    public MyFragmentPagerAdapter(FragmentManager fm,List<?extends Fragment> mFragment) {
        super(fm);
        this.mFragment = mFragment;
    }

    public MyFragmentPagerAdapter(FragmentManager fm, List<? extends Fragment> mFragment, List<String> mTitleLabel) {
        super(fm);
        this.mFragment = mFragment;
        this.mTitleLabel = mTitleLabel;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }


//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//        try {
//            container.removeView((View) object);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }


    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitleLabel!=null){
            return mTitleLabel.get(position);
        }else{
            return "";
        }
    }


//    public List<? extends Fragment> getViews(){
//        return mFragment;
//    }

//    /**
//     *
//     * @param index 指定位置
//     * @return 获取指定位置视图
//     */
//    public Fragment getViewOfIndex(int index){
//        if(getViews() == null)return null;
//        if(index < 0 || index >= getCount())return null;
//        return getViews().get(index);
//    }

    public void clear() {
        if(mFragment==null){
            return;
        }
        for (Fragment fragment : mFragment) {
            if (fragment != null && fragment.isAdded()) {
                fragment.onDestroy();
            }
        }
        mFragment.clear();
    }


}
