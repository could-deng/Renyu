package could.bluepay.renyumvvm.view.my;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.ActivityMyBinding;
import could.bluepay.renyumvvm.view.base.BaseActivity;

/**
 * Created by bluepay on 2017/12/5.
 */

public class MyActivity extends BaseActivity {
    ActivityMyBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my);
        super.onCreate(savedInstanceState);
//        binding.setAdapter();
    }

}
