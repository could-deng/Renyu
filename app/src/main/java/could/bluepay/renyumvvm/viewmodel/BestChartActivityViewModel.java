package could.bluepay.renyumvvm.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.support.annotation.IdRes;
import android.widget.RadioGroup;
import java.util.List;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.bindingAdapter.messenger.Messenger;
import could.bluepay.renyumvvm.databinding.ActivityBestChartBinding;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.http.bean.BestResultBean;
import could.bluepay.renyumvvm.model.MainModel;
import could.bluepay.renyumvvm.rx.RxApiManager;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.activity.BestChartActivity;
import could.yuanqiang.http.utils.CheckNetwork;
import io.reactivex.disposables.Disposable;

/**
 * Created by dengyuanqiang on 2018/3/2.
 */

public class BestChartActivityViewModel {
//    1 代表本周；2 代表本月；3 最近三月
    public static final int TIME_Week = 1;
    public static final int TIME_Month = 2;
    public static final int TIME_Three_Month = 3;

    public static final int TYPE_Consume = 1;
    public static final int TYPE_Subscription = 2;
    public static final int TYPE_All = 3;

    public static final String AREA_Default = "THB";

    private Context context;
    private ActivityBestChartBinding binding;
    private MainModel model;

    //region=====Data==============

    public ObservableArrayList<BestResultBean.DateBean> dataList = new ObservableArrayList<>();

    //endregion=====Data===========


    public BestChartActivityViewModel(Context context,ActivityBestChartBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    public void init(){
        binding.rgSearchType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.rb_search_consume){
                    searchInput(TIME_Week,AREA_Default,178,TYPE_Consume);

                }else if(checkedId == R.id.rb_search_subscription){
                    searchInput(TIME_Week,AREA_Default,178,TYPE_Subscription);

                }else if(checkedId == R.id.rb_search_all){
                    searchInput(TIME_Week,AREA_Default,178,TYPE_All);
                }
            }
        });
    }


    //region======model层==============

    private MainModel getModel(){
        if(model == null){
            model = new MainModel();
        }
        return model;
    }


    public void searchInput(int time,String currency,int producerId,int type){
        if (CheckNetwork.isNetworkConnected(MixApp.getContext())) {
            Messenger.getDefault().sendNoMsg(BestChartActivity.Message_Show_Dialog);

            getModel().searchBestInput(time, currency, producerId, type, new RequestImpl() {
                @Override
                public void loadSuccess(Object object) {
                    Messenger.getDefault().send(true,BestChartActivity.Message_Dismiss_Dialog);
                    List<BestResultBean.DateBean> data = (List<BestResultBean.DateBean>) object;
                    if(dataList!=null){
                        dataList.clear();
                    }
                    dataList.addAll(data);
                    Logger.e(Logger.DEBUG_TAG, "成功,dataSize:" + data.size());
                }

                @Override
                public void loadFailed() {
                    Messenger.getDefault().send(false,BestChartActivity.Message_Dismiss_Dialog);
                    Logger.e(Logger.DEBUG_TAG, "失败,dataSize:0" );
                }

                @Override
                public void addSubscription(Disposable disposable) {
                    RxApiManager.get().add(BestChartActivity.TAG,disposable);
                }
            });
        }else{
            Messenger.getDefault().send("网络异常",BestChartActivity.Message_Toast);
        }
    }


    //endregion======model层==============
}
