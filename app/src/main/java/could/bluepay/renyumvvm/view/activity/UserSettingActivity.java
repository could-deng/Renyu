package could.bluepay.renyumvvm.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.ActivityUserSettingBinding;
import could.bluepay.renyumvvm.viewmodel.UserSettingViewModel;
import could.bluepay.renyumvvm.viewmodel.ViewModel;

/**
 * Created by bluepay on 2018/1/24.
 */

public class UserSettingActivity extends BaseActivity<ActivityUserSettingBinding>{
    @Override
    protected ViewModel setViewModel() {
        if(viewModel == null){
            viewModel = new UserSettingViewModel();
        }
        return viewModel;
    }

    @Override
    protected int setContent() {
        return R.layout.activity_user_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        ((UserSettingViewModel)setViewModel()).init(binding,this);
        binding.setViewModel((UserSettingViewModel) setViewModel());

    }
    private void initToolbar(){
        setSupportActionBar((Toolbar) binding.toolbar.getRoot());
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }

        binding.toolbar.indicator.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setVisibility(View.VISIBLE);
        binding.toolbar.tvToolbarTitle.setText(getResources().getString(R.string.activity_user_setting));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_back);
        }
    }

}
