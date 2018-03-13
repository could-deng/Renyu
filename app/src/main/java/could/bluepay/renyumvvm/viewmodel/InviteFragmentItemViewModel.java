package could.bluepay.renyumvvm.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.http.bean.UserBeanItem;
import could.bluepay.renyumvvm.utils.TimeUtils;

/**
 * Created by bluepay on 2018/1/11.
 */

public class InviteFragmentItemViewModel implements ViewModel {
    //model
    private UserBeanItem itemData;

    //field to presenter
    public final ObservableField<String> photo = new ObservableField<>();
    public final ObservableField<String> nickName = new ObservableField<>();
    public final ObservableField<String> fdcount = new ObservableField<>();
    public final ObservableField<String> inviteStatus = new ObservableField<>();
    public final ObservableInt inviteColor = new ObservableInt();
    public final ObservableField<String> InviteTime = new ObservableField<>();
    public final ObservableBoolean InviteTimeVisibility = new ObservableBoolean(false);

    public final View.OnClickListener OnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fdcount.set(String.valueOf(itemData.getFdcount()+1));
        }
    } ;

    public InviteFragmentItemViewModel(UserBeanItem itemData){
        this.itemData = itemData;

        photo.set(itemData.getPhoto());
        nickName.set(itemData.getNick_name());
        fdcount.set(itemData.getFdcount()+"");

        if(itemData.getShot() == 2){
            inviteStatus.set(itemData.getCity()+"-"+"可约拍");
            inviteColor.set(MixApp.getContext().getResources().getColor(R.color.font_color_41));

            InviteTimeVisibility.set(true);
            InviteTime.set(TimeUtils.getMonthDayDate((String)itemData.getShot_starttime()) + "-" +TimeUtils.getMonthDayDate((String) itemData.getShot_endtime()));

        }else{
            inviteStatus.set(itemData.getCity()+"-"+"暂停约拍");
            inviteColor.set(MixApp.getContext().getResources().getColor(R.color.font_color_7));
            InviteTimeVisibility.set(false);
        }
    }

}
