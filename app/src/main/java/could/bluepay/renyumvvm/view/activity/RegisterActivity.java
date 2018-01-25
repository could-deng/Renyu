package could.bluepay.renyumvvm.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.ActivityRegisterBinding;
import could.bluepay.renyumvvm.viewmodel.RegisterViewModel;
import could.bluepay.renyumvvm.viewmodel.ViewModel;

/**
 * 注册
 */

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {
    @Override
    protected int setContent() {
        return R.layout.activity_register;
    }

    @Override
    protected ViewModel setViewModel() {
        if(viewModel == null){
            viewModel = new RegisterViewModel();
        }
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        ((ActivityRegisterBinding)binding).setViewModel((RegisterViewModel)setViewModel());
    }

    private void initToolbar(){
        ifShowMenuTextView = true;
        textMenuTextView = getResources().getString(R.string.activity_register);

        setSupportActionBar((Toolbar) binding.toolbar.getRoot());
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }

        binding.toolbar.indicator.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setVisibility(View.VISIBLE);
        binding.toolbar.tvToolbarTitle.setText("");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_back);
        }
    }

}
