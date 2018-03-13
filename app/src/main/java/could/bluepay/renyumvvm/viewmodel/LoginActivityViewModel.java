package could.bluepay.renyumvvm.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.bindingAdapter.command.ReplyCommand;
import could.bluepay.renyumvvm.common.PrefsHelper;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.model.MainModel;
import could.bluepay.renyumvvm.rx.RxApiManager;
import could.bluepay.renyumvvm.view.activity.LoginActivity;
import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.view.activity.RegisterActivity;
import could.yuanqiang.http.utils.CheckNetwork;
import io.reactivex.disposables.Disposable;

/**
 * 登录ViewModel
 */

public class LoginActivityViewModel implements ViewModel {

    private MainModel mainModel;

    //region===布局文件的事件绑定============
    public final ViewStyle loginViewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableField<String> userName = new ObservableField<>();
        public final ObservableField<String> psw = new ObservableField<>();
    }
    public void init(){
        mainModel = new MainModel();
    }

    public void loginCallback(final View view){
        //点击登录
        ((LoginActivity)view.getContext()).showLoadingDialog();

        //TODO: 2017/11/28 暂时写死
        PrefsHelper.with((view.getContext()), Config.PREFS_USER).writeInt(Config.SP_KEY_LAST_LOGIN_TYPE, 1);
        PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).writeLong(Config.SP_KEY_UID,1);
        PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).write(Config.SP_KEY_NICKNAME,"二蛋");

        Intent intent = new Intent(view.getContext(), MainActivity.class);
        view.getContext().startActivity(intent);

        //后续
        ((LoginActivity)view.getContext()).dismissLoadingDialog();
        ((LoginActivity)view.getContext()).finish();

//        if(!TextUtils.isEmpty(loginViewStyle.userName.get()) && !TextUtils.isEmpty(loginViewStyle.psw.get())){
//            if (CheckNetwork.isNetworkConnected(MixApp.getContext())) {
//                mainModel.login(loginViewStyle.userName.get(), loginViewStyle.psw.get(), 1, new RequestImpl() {
//                    @Override
//                    public void loadSuccess(Object object) {
//                        Intent intent = new Intent(view.getContext(), MainActivity.class);
//                        view.getContext().startActivity(intent);
//                    }
//
//                    @Override
//                    public void loadFailed() {

//                    }
//
//                    @Override
//                    public void addSubscription(Disposable disposable) {
//                        RxApiManager.get().add(LoginActivity.TAG,disposable);
//                    }
//                });
//            }else{
//
//            }
//        }
    }
    public void toRegister(View view){
        Intent intent = new Intent(view.getContext(), RegisterActivity.class);
        view.getContext().startActivity(intent);
    }

    //endregion===布局文件的事件绑定============
}
