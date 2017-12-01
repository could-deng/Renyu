package could.bluepay.renyumvvm.view.base;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

/**
 * Created by bluepay on 2017/11/21.
 */

public class BaseActivity<T extends ViewDataBinding> extends Activity {


    protected T mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this),layoutResID,null,false);
        super.setContentView(mBinding.getRoot());
    }
//    protected void setToolbar(){
//        mBinding.toolbar
//    }
}
