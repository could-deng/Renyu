package could.bluepay.renyumvvm.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by bluepay on 2017/11/22.
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitleLabel!=null){
            return mTitleLabel.get(position);
        }else{
            return "";
        }
    }
}
