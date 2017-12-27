package could.bluepay.renyumvvm.view.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.ItemDynamicBinding;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.http.bean.ActionItem;
import could.bluepay.renyumvvm.http.bean.CommentItem;
import could.bluepay.renyumvvm.http.bean.FavortItem;
import could.bluepay.renyumvvm.http.bean.PhotoInfo;
import could.bluepay.renyumvvm.http.bean.WeiboBean;
import could.bluepay.renyumvvm.model.MainModel;
import could.bluepay.renyumvvm.utils.AppUtils;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.utils.PerfectClickListener;
import could.bluepay.renyumvvm.view.adapter.baseadapter.BaseRecyclerViewAdapter;
import could.bluepay.renyumvvm.view.adapter.baseadapter.BaseRecyclerViewHolder;
import could.bluepay.renyumvvm.widget.CommentListView;
import could.bluepay.renyumvvm.widget.ExpandTextView;
import could.bluepay.renyumvvm.widget.MultiImageView;
import could.bluepay.renyumvvm.widget.PraiseListView;
import could.bluepay.renyumvvm.widget.SnsPopupWindow;
import io.reactivex.disposables.Disposable;

/**
 * Created by bluepay on 2017/11/27.
 */

public class DynamicAdapter extends BaseRecyclerViewAdapter<WeiboBean> {

    MainModel model;
    long uid = 0;
    String nickName = "";


    public void setUid(long uid){
        this.uid = uid;
    }
    public void setNickName(String nickName){
        this.nickName = nickName;
    }
    public void setModel(MainModel model){
        this.model = model;
    }

    private poupWindowClick click;
    public void setClick(poupWindowClick click){
        this.click = click;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder holder = null;
        if(viewType == ViewHolder.VIEWTYPE_IMAGE){
            holder = new ImageViewHolder(parent,R.layout.item_dynamic,click);
        }else if(viewType == ViewHolder.VIEWTYPE_VIDEO){
            holder = new VideoViewHolder(parent,R.layout.item_dynamic,click);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        int itemType =0;

        if(!TextUtils.isEmpty(data.get(position).getVideo())){
            itemType = ViewHolder.VIEWTYPE_VIDEO;
        }else{
            itemType = ViewHolder.VIEWTYPE_IMAGE;
        }
        return itemType;
    }

    private class VideoViewHolder extends ViewHolder{


        public VideoViewHolder(ViewGroup viewGroup, int layoutId,poupWindowClick click) {
            super(viewGroup, layoutId,ViewHolder.VIEWTYPE_VIDEO,click);
        }

        @Override
        public void initStub(View view, int ViewType,WeiboBean object) {
            // TODO: 2017/11/28
        }

    }

    private class ImageViewHolder extends ViewHolder{
        MultiImageView multiImageView;

        public ImageViewHolder(ViewGroup viewGroup, int layoutId,poupWindowClick click) {
            super(viewGroup, layoutId, ViewHolder.VIEWTYPE_IMAGE,click);
        }

        @Override
        public void initStub(View view, int ViewType,WeiboBean object) {
            multiImageView = (MultiImageView) view.findViewById(R.id.multi_imageview);
            List<PhotoInfo> photoInfos = object.getPic();
            if(photoInfos!=null && photoInfos.size()>0){
                // TODO: 2017/11/28 暂时注释
//                multiImageView.setDynamicItem(dynamicItem);
                multiImageView.setVisibility(View.VISIBLE);
                multiImageView.setList(photoInfos);
//                multiImageView.setOnItemClickListener(mListener);
            }else{
                multiImageView.setVisibility(View.GONE);
            }
        }

    }

    private abstract class ViewHolder extends BaseRecyclerViewHolder<WeiboBean,ItemDynamicBinding>{
        protected static final int VIEWTYPE_IMAGE = 1;
        protected static final int VIEWTYPE_VIDEO = 2;

        int viewType = -1;

        private poupWindowClick click;

        protected SnsPopupWindow snsPopupWindow ;

        public ViewHolder(ViewGroup viewGroup, int layoutId, int viewType,poupWindowClick click) {
            super(viewGroup,layoutId);
            this.viewType = viewType;
            this.click = click;

            if(snsPopupWindow == null){
                snsPopupWindow = new SnsPopupWindow(viewGroup.getContext());
            }

        }

        @Override
        public void onBindViewHolder(final WeiboBean object, final int position) {
            binding.headIv.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onDoubleClick(View view) {
                    Logger.e(Logger.DEBUG_TAG,view.getId()+"onDoubleClick");
                }
            });
            if(!TextUtils.isEmpty(object.getPcontent())) {
                binding.contentTv.setExpand(object.isExpand());
                binding.contentTv.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
                    @Override
                    public void statusChange(boolean isExpand) {
                        object.setExpand(isExpand);
                    }
                });
                binding.contentTv.setText(AppUtils.formatUrlString(object.getPcontent()));
                binding.contentTv.setVisibility(View.VISIBLE);
            }else {
                binding.contentTv.setVisibility(View.GONE);
            }
            binding.deleteBtn.setVisibility((uid == object.getUser().getQid())?View.VISIBLE:View.GONE);

            binding.deleteBtn.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onDoubleClick(View view) {
                    //// TODO: 2017/11/28 Rxjava to observable delete matter
                    Logger.e(Logger.DEBUG_TAG,"deleteBtn,onClick");
                }
            });

            final List<FavortItem> favortDatas = object.getFavorters();
            final List<CommentItem> commentsDatas = object.getComments();

            if(object.hasFavort()){
                binding.praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Logger.e(Logger.DEBUG_TAG,favortDatas.get(position).getNick_name()+",onClick");
                    }
                });
                List<String> temp = new ArrayList<>();
                for(FavortItem item:favortDatas){
                    temp.add(item.getNick_name());
                }
                binding.praiseListView.setDatas(temp);
                binding.praiseListView.setVisibility(View.VISIBLE);
            }else{
                binding.praiseListView.setVisibility(View.GONE);
            }

            if(object.hasComment()){
                binding.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // TODO: 2017/11/28
                    }
                });
                binding.commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(int position) {
                        // TODO: 2017/11/28

                    }
                });
                binding.commentList.setDatas(commentsDatas);
                binding.commentList.setVisibility(View.VISIBLE);
            }else{
                binding.commentList.setVisibility(View.GONE);
            }
            if(!object.hasComment() && !object.hasFavort()){
                binding.digCommentBody.setVisibility(View.GONE);
            }else{
                binding.digCommentBody.setVisibility(View.VISIBLE);
            }

            binding.linDig.setVisibility((object.hasFavort() || object.hasComment()) ? View.VISIBLE : View.GONE);

            binding.snsBtn.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onDoubleClick(View view) {
                    long curUserFavortId = object.getCurUserFavortId(uid);

                    if(curUserFavortId!=0){//是否已点赞
                        snsPopupWindow.getmActionItems().get(0).setType(ActionItem.ACTION_TYPE_CANCEL);
                    }else{
                        snsPopupWindow.getmActionItems().get(0).setType(ActionItem.ACTION_TYPE_DIG);
                    }
                    snsPopupWindow.update();

                    snsPopupWindow.setmItemClickListener(new PopupItemClickListener(object,position,curUserFavortId,click));

                    snsPopupWindow.showPopupWindow(binding.snsBtn);
                }
            });



            if(!binding.viewStub.isInflated()) {
                if (viewType == VIEWTYPE_IMAGE) {
                    binding.viewStub.getViewStub().setLayoutResource(R.layout.viewstub_imgbody);
                } else {
                    binding.viewStub.getViewStub().setLayoutResource(R.layout.viewstub_videobody);
                }
                View viewStub = binding.viewStub.getViewStub().inflate();
                initStub(viewStub, viewType, object);
            }


        }
        public abstract void initStub(View view, int ViewType,WeiboBean object);

    }

    /**
     * PopupWindow的点击监听器
     */
    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener{
        private WeiboBean weiboBean;
        //点击的item位于列表的位置
        private int dynamicPosition;
        private long favouriteId;//点赞的id，如果没有点赞则为0
        private poupWindowClick click;

        public PopupItemClickListener(WeiboBean weiboBean,int dynamicPosition,long favouriteId,poupWindowClick click){
            this.weiboBean = weiboBean;
            this.dynamicPosition = dynamicPosition;
            this.favouriteId = favouriteId;
            this.click = click;
        }

        @Override
        public void onItemClick(final ActionItem item, int position) {
            if(model!=null) {
                if(item.getType() == ActionItem.ACTION_TYPE_DIG) {
                    model.doDynamicLike(nickName, weiboBean.getPid(), new RequestImpl() {
                        @Override
                        public void loadSuccess(Object object) {
                            Logger.e(Logger.DEBUG_TAG, "doDynamicLike,loadSuccess()");
                            try {
                                FavortItem bean = (FavortItem) object;
                                weiboBean.getFavorters().add(bean);
                                //// TODO: 2017/11/30 更新点赞列表及弹出框显示的字
//                                if(click!=null){
//                                    click.updateRecycleView(dynamicPosition);
//                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void loadFailed() {
                            Logger.e(Logger.DEBUG_TAG, "doDynamicLike,loadFailed()");
                        }

                        @Override
                        public void addSubscription(Disposable disposable) {
                            click.addDisposable(disposable);
                        }
                    });
                }else if(item.getType() == ActionItem.ACTION_TYPE_CANCEL){//取消点赞
                    if(favouriteId!=0) {
                        model.deleteDynamicLike(favouriteId, new RequestImpl() {
                            @Override
                            public void loadSuccess(Object object) {
                                Logger.e(Logger.DEBUG_TAG,"deleteDynamicList,loadSuccess()");
                                List<FavortItem> favortItemList = weiboBean.getFavorters();
                                for(FavortItem item:favortItemList){
                                    if(item.getId() == favouriteId){
                                        favortItemList.remove(item);
//                                        if(click!=null){
//                                            click.updateRecycleView(dynamicPosition);
//                                        }
                                    }
                                }
                            }

                            @Override
                            public void loadFailed() {
                                Logger.e(Logger.DEBUG_TAG, "deleteDynamicList,loadFailed()");
                            }

                            @Override
                            public void addSubscription(Disposable subscription) {
                                click.addDisposable(subscription);
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 通知给Fragment的回调
     */
    public interface poupWindowClick{
        void updateRecycleView(int index);
        void addDisposable(Disposable subscription);
    }

}
