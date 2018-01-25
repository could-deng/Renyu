package could.bluepay.renyumvvm.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.ActivityLoginBinding;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.viewmodel.LoginActivityViewModel;
import could.bluepay.renyumvvm.viewmodel.ViewModel;

/**
 * 登录
 */

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    @Override
    protected int setContent() {
        return R.layout.activity_login;
    }

    @Override
    protected ViewModel setViewModel() {
        if(viewModel == null) {
            this.viewModel = new LoginActivityViewModel();
        }
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((LoginActivityViewModel) setViewModel()).init();
        binding.setViewModel((LoginActivityViewModel) setViewModel());
        setToolbarTitle(getResources().getString(R.string.title_activity_login));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e(Logger.DEBUG_TAG,"LoginActivity,onDestroy()");
    }

    public void setToolbarTitle(String title){
        if(TextUtils.isEmpty(title)){
            title = getResources().getString(R.string.app_name);
        }
        binding.toolbar.indicator.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setVisibility(View.VISIBLE);
        binding.toolbar.tvToolbarTitle.setText(title);
    }
}
