package could.bluepay.renyumvvm.viewmodel;
import android.databinding.ObservableBoolean;

import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.bindingAdapter.command.ReplyCommand;
import could.bluepay.renyumvvm.bindingAdapter.messenger.Messenger;
import could.bluepay.renyumvvm.view.activity.BestChartActivity;
import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.yuanqiang.http.utils.CheckNetwork;

/**
 * Created by bluepay on 2018/1/10.
 */

public class BaseFragmentViewModel implements ViewModel{

    //collection of view style,wrap to a class to manage conveniently!
    public final ViewStyle viewStyle = new ViewStyle();


    public class ViewStyle {
        public ObservableBoolean loadingProcess = new ObservableBoolean(true);
        public ObservableBoolean loadingSuccess = new ObservableBoolean(false);
        public ObservableBoolean loadingError = new ObservableBoolean(false);
    }

    public final ReplyCommand onReloadCommand = new ReplyCommand(new Runnable() {
        @Override
        public void run() {
            showLoading();
            onloadData();
        }
    });

    //region====数据驱动UI模块,更改数据========

    /**
     * 显示正在加载中
     */
    protected void showLoading(){
        viewStyle.loadingProcess.set(true);
        viewStyle.loadingSuccess.set(false);
        viewStyle.loadingError.set(false);
    }

    /**
     * 显示内容
     */
    protected void showContentView(){
        viewStyle.loadingProcess.set(false);
        viewStyle.loadingSuccess.set(true);
        viewStyle.loadingError.set(false);
    }

    /**
     * 显示加载失败
     */
    protected void showError(){
        viewStyle.loadingProcess.set(false);
        viewStyle.loadingSuccess.set(false);
        viewStyle.loadingError.set(true);
    }



    //endregion====数据驱动UI模块,更改数据========

    protected void onloadData(){
    }

    /**
     * 网络是否断开
     * @param showErrorLayout
     * @return true为断开，false为正常
     */
    protected boolean onNetWorkDisConnecte(boolean showErrorLayout){
        if (!CheckNetwork.isNetworkConnected(MixApp.getContext())) {
            if(showErrorLayout) {
                showError();
            }else{
                Messenger.getDefault().send("网络异常", MainActivity.MainActivity_Message_Toast);
            }
            return true;
        }
        return false ;
    }
}
