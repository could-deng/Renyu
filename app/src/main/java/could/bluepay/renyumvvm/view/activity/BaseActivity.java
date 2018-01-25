package could.bluepay.renyumvvm.view.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.common.memoryleak.LeakHandler;
import could.bluepay.renyumvvm.viewmodel.ViewModel;
import could.bluepay.renyumvvm.widget.dialog.PictureDialog;

/**
 * Created by bluepay on 2017/11/21.
 */

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {


    protected T binding;

    private MenuItem itemTextView;
    protected boolean ifShowMenuTextView = false;
    protected String textMenuTextView = "";
    protected PictureDialog loadingDialog;

    //region==========viewModel相关====================
    protected ViewModel viewModel;

    protected ViewModel setViewModel(){
        return viewModel;
    }
    protected ViewModel getViewModel(){
        return viewModel;
    }

    //endregion==========viewModel相关====================
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, setContent());

    }

//    @Override
//    public void setContentView(@LayoutRes int layoutResID) {
//        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this),setContent(),null,false);
//        super.setContentView(mBinding.getRoot());
//
//    }

//    protected void setToolbar(){
//        mBinding.toolbar
//    }
    protected abstract int setContent();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(ifShowMenuTextView) {
            itemTextView = menu.add(0,Menu.FIRST+5,5,textMenuTextView).setIcon(getMenuTabFirstIcon());
            itemTextView.setActionView(R.layout.menu_first);//MenuFirstbinding.getRoot()
            itemTextView.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            ((TextView)(itemTextView.getActionView().findViewById(R.id.tv_menu_first))).setText(textMenuTextView);
            ((itemTextView.getActionView().findViewById(R.id.tv_menu_first))).setClickable(true);
            ((itemTextView.getActionView().findViewById(R.id.tv_menu_first))).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(itemTextView);
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }else if(item.getItemId() == Menu.FIRST+5){
            onMenuTabFirstClick();
        }
        return super.onOptionsItemSelected(item);
    }

    protected int getMenuTabFirstIcon(){
        return R.drawable.ic_setting;
    }
    protected void onMenuTabFirstClick(){

    }

    protected void showBackTab() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_back);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
        LeakHandler.fixInputMethodManagerLeak(this);
    }



    /**
     * compress loading dialog
     */
    public void showLoadingDialog() {
        if (!isFinishing()) {
            dismissLoadingDialog();
            loadingDialog = new PictureDialog(this);
            loadingDialog.show();
        }
    }

    /**
     * dismiss loading dialog
     */
    public void dismissLoadingDialog() {
        try {
            if (!isFinishing()
                    && loadingDialog != null
                    && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
