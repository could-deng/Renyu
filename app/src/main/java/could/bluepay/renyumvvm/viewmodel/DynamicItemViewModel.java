package could.bluepay.renyumvvm.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.bindingAdapter.command.ReplyCommand;
import could.bluepay.renyumvvm.bindingAdapter.messenger.Messenger;
import could.bluepay.renyumvvm.common.PrefsHelper;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.http.bean.ActionItem;
import could.bluepay.renyumvvm.http.bean.FavortItem;
import could.bluepay.renyumvvm.http.bean.PhotoInfo;
import could.bluepay.renyumvvm.http.bean.WeiboBean;
import could.bluepay.renyumvvm.model.MainModel;
import could.bluepay.renyumvvm.rx.RxApiManager;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.view.bean.ImageWatchBean;
import could.bluepay.renyumvvm.view.fragment.DynamicFragment;
import could.bluepay.renyumvvm.widget.MultiImageView;
import could.bluepay.renyumvvm.widget.SnsPopupWindow;
import io.reactivex.disposables.Disposable;

import static could.bluepay.renyumvvm.Config.RECYCLER_VIEW_DYNAMIC_TYPE_image;
import static could.bluepay.renyumvvm.Config.RECYCLER_VIEW_DYNAMIC_TYPE_video;

/**
 * 用于DynamicFragment里面RecyclerView的item，双向绑定
 */

public class DynamicItemViewModel  {

    private Context context;
    private WeiboBean bean;
    public final ObservableField<String> userIcon = new ObservableField<>();
    public final ObservableField<Integer> userIconVip = new ObservableField<>();
    public final ObservableField<String> userName = new ObservableField<>();

    public final ObservableField<String> contentText = new ObservableField<>();
    public final ObservableBoolean ifTextExpand = new ObservableBoolean(false);//默认文字超过四行不显示全部

    public final ObservableArrayList<String> praiseList = new ObservableArrayList<>();
    public final ObservableBoolean praiseViewVisible = new ObservableBoolean(false);

    public final ObservableField<Integer> showDeleteBtn = new ObservableField<>();


    public final ObservableField<List<PhotoInfo>> photosList = new ObservableField<>();

    public final ObservableField<String> videoUrl = new ObservableField<>();

    protected SnsPopupWindow snsPopupWindow ;

    public final View.OnClickListener doMoreClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(snsPopupWindow == null){
                snsPopupWindow = new SnsPopupWindow(context);
            }

            long curUserFavortId = bean.getCurUserFavortId(getUid());

            if(curUserFavortId!=0){//是否已点赞
                snsPopupWindow.getmActionItems().get(0).setType(ActionItem.ACTION_TYPE_CANCEL);
            }else{
                snsPopupWindow.getmActionItems().get(0).setType(ActionItem.ACTION_TYPE_DIG);
            }
            snsPopupWindow.update();

            snsPopupWindow.setmItemClickListener(new PopupItemClickListener());

            snsPopupWindow.showPopupWindow(v);
        }
    };

    public final ReplyCommand delteDynamicItemCommand = new ReplyCommand(new Runnable() {
        @Override
        public void run() {
            Logger.e(Logger.DEBUG_TAG,"deleteBtn,onClick");
        }
    });


    public final MultiImageView.OnItemClickListener onMultiItemClicklistener = new MultiImageView.OnItemClickListener(){

        @Override
        public void onItemClick(View view, int position, List<ImageView> imagesList, List<String> imagesUrlList) {
            Messenger.getDefault().send(new ImageWatchBean(view,imagesList,imagesUrlList), MainActivity.SHOW_IMAGE_WATCH);
        }
    };


    public class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener{

        @Override
        public void onItemClick(ActionItem item, int position) {
            if(item.getType() == ActionItem.ACTION_TYPE_DIG) {
                MainModel.getmInstance().doDynamicLike(PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).read(Config.SP_KEY_NICKNAME), bean.getPid(), new RequestImpl() {

                    @Override
                    public void loadSuccess(Object object) {
                        FavortItem favortItem = (FavortItem) object;
                        bean.getFavorters().add(favortItem);
                        praiseList.clear();
                        List<FavortItem> favorites = bean.getFavorters();
                        for (FavortItem item:favorites){
                            praiseList.add(item.getNick_name());
                        }
                    }

                    @Override
                    public void loadFailed() {

                    }

                    @Override
                    public void addSubscription(Disposable disposable) {
                        RxApiManager.get().add(DynamicFragment.TAG,disposable);
                    }
                });
            }else if(item.getType() == ActionItem.ACTION_TYPE_CANCEL){
                MainModel.getmInstance().deleteDynamicLike(bean.getPid(), new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        List<FavortItem> favortItemList = bean.getFavorters();
                        for(FavortItem item:favortItemList){
                            if(item.getPid() == bean.getPid()){
                                favortItemList.remove(item);
                                praiseList.remove(item);
                                break;
                            }
                        }

                    }

                    @Override
                    public void loadFailed() {

                    }

                    @Override
                    public void addSubscription(Disposable disposable) {
                        RxApiManager.get().add(DynamicFragment.TAG,disposable);
                    }
                });
            }
        }
    }
    public DynamicItemViewModel(WeiboBean bean,Context context){
        this.context = context;
        this.bean = bean;
        userIcon.set(bean.getUser().getPhoto());
        userIconVip.set(bean.getUser().getVip()==0? View.INVISIBLE:View.VISIBLE);
        userName.set(bean.getUser().getNick_name());
        contentText.set(bean.getPcontent()
                +"~123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123~"
                +"~123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123~");

        praiseList.clear();
        List<FavortItem> favorites = bean.getFavorters();
        for (FavortItem item:favorites){
            praiseList.add(item.getNick_name());
        }
        praiseViewVisible.set(bean.hasFavort());

        showDeleteBtn.set(getUid() == bean.getUser().getQid()?View.VISIBLE:View.GONE);
        if(!TextUtils.isEmpty(bean.getVideo())){
            videoUrl.set(bean.getVideo());
            contentText.set(bean.getPcontent());
            Logger.e(Logger.DEBUG_TAG,"videoUrl:"+ videoUrl.get()+",contentText:"+contentText.get());
        }else{
            photosList.set(bean.getPic());
        }
    }
    public long getUid(){
        return PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).readLong(Config.SP_KEY_UID,1);
    }

    /**
     * 获取item的类型
     * @return
     */
    public int getItemType(){
        if(TextUtils.isEmpty(bean.getVideo())){
           return RECYCLER_VIEW_DYNAMIC_TYPE_image;
        }
        return RECYCLER_VIEW_DYNAMIC_TYPE_video;
    }

}
