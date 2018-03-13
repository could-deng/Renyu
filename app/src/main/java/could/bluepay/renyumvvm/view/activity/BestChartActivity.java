package could.bluepay.renyumvvm.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.bindingAdapter.messenger.Messenger;
import could.bluepay.renyumvvm.common.memoryleak.LeakHandler;
import could.bluepay.renyumvvm.databinding.ActivityBestChartBinding;
import could.bluepay.renyumvvm.rx.RxApiManager;
import could.bluepay.renyumvvm.viewmodel.BestChartActivityViewModel;
import could.bluepay.renyumvvm.widget.dialog.PictureDialog;
import io.reactivex.functions.Consumer;

/**
 * Best平台搜索Activity
 */

public class BestChartActivity extends Activity {
    public static String TAG = "BestChartActivity";

    public static String Message_Show_Dialog = "Message_Show_Dialog";
    public static String Message_Dismiss_Dialog = "Message_Dismiss_Dialog";
    public static String Message_Toast = "Message_Toast";

    ActivityBestChartBinding binding;
    BestChartActivityViewModel viewModel;

    PictureDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_best_chart);
        binding.setViewModel(getViewModel());


        registerMatter();

        getViewModel().init();

        getViewModel().searchInput(BestChartActivityViewModel.TIME_Week,
                BestChartActivityViewModel.AREA_Default,
                178,
                BestChartActivityViewModel.TYPE_All);
    }

    private BestChartActivityViewModel getViewModel(){
        if(viewModel == null){
            viewModel = new BestChartActivityViewModel(this,binding);
        }
        return viewModel;
    }

    /**
     * 注册Messenger
     */
    private void registerMatter(){
        Messenger.getDefault().register(this, Message_Show_Dialog, new Runnable() {
            @Override
            public void run() {
                showDialog();
            }
        });
        Messenger.getDefault().register(this, Message_Dismiss_Dialog, Boolean.class, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                dismissLoadingDialog();
                if(!aBoolean){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BestChartActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        Messenger.getDefault().register(this, Message_Toast, String.class, new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if(!TextUtils.isEmpty(s)) {
                    Toast.makeText(BestChartActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     *  显示dialog
     */
    private void showDialog(){
        if (!isFinishing()) {
            dismissLoadingDialog();
            loadingDialog = new PictureDialog(this);
            loadingDialog.setCancelable(false);
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
        RxApiManager.get().cancel(BestChartActivity.TAG);

        LeakHandler.fixInputMethodManagerLeak(this);
        Messenger.getDefault().unregister(this);

    }
}
