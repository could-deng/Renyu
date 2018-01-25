package could.bluepay.renyumvvm.view.adapter.bindingAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import could.bluepay.renyumvvm.BR;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.bindingAdapter.messenger.Messenger;
import could.bluepay.renyumvvm.databinding.ItemDynamicImageBinding;
import could.bluepay.renyumvvm.databinding.ItemDynamicVideoBinding;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.http.bean.ActionItem;
import could.bluepay.renyumvvm.http.bean.CommentItem;
import could.bluepay.renyumvvm.http.bean.FavortItem;
import could.bluepay.renyumvvm.http.bean.PhotoInfo;
import could.bluepay.renyumvvm.http.bean.WeiboBean;
import could.bluepay.renyumvvm.utils.AppUtils;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.utils.PerfectClickListener;
import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.view.bean.ImageWatchBean;
import could.bluepay.renyumvvm.viewmodel.DynamicViewModel;
import could.bluepay.renyumvvm.widget.CommentListView;
import could.bluepay.renyumvvm.widget.ExpandTextView;
import could.bluepay.renyumvvm.widget.MultiImageView;
import could.bluepay.renyumvvm.widget.PraiseListView;
import could.bluepay.renyumvvm.widget.SnsPopupWindow;
import could.bluepay.widget.jiaozivideoplayer.JZVideoPlayer;
import io.reactivex.disposables.Disposable;

/**
 * Created by bluepay on 2017/12/5.
 */

public class DynamicBindingAdapter extends RecyclerView.Adapter<BindingViewHolder> {

    //image和video都为同一种类型
    public static final int RECYCLER_VIEW_DYNAMIC_TYPE = 1;
    public static final int RECYCLER_VIEW_DYNAMIC_TYPE_image = 2;
    public static final int RECYCLER_VIEW_DYNAMIC_TYPE_video = 3;

    private Context context;
    protected LayoutInflater mLayoutInflater;
    //item类型对应布局文件的map
    private HashMap<Integer,Integer> itemTypeToLayoutMap ;


    //数据资源
    private List<Object> mCollections;
    //类别集合,与数据资源一一对应
    private ArrayList<Integer> mCollectionTypes;




    protected SnsPopupWindow snsPopupWindow ;
    private long uid = 0;
    private String nickName;


    //region=====adapter与viewmodel的交互==============

    private DynamicViewModel.poupWindowClick click;
    public void setClick(DynamicViewModel.poupWindowClick click){
        this.click = click;
    }

    //endregion=====adapter与viewmodel的交互==============

    public DynamicBindingAdapter(Context context,long uid,String nickName,HashMap<Integer,Integer> map,DynamicViewModel.poupWindowClick click){
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if(map!=null && map.size()>0) {
//            itemTypeToLayoutMap = map;
//        }else{
            itemTypeToLayoutMap = new HashMap<>();
//        }
        // TODO: 2018/1/12 暂时写死
        itemTypeToLayoutMap.put(DynamicBindingAdapter.RECYCLER_VIEW_DYNAMIC_TYPE_image, R.layout.item_dynamic_image);
        itemTypeToLayoutMap.put(DynamicBindingAdapter.RECYCLER_VIEW_DYNAMIC_TYPE_video, R.layout.item_dynamic_video);

        mCollections = new ArrayList<>();
        mCollectionTypes = new ArrayList<>();

        if(snsPopupWindow == null){
            snsPopupWindow = new SnsPopupWindow(context);
        }
        this.uid = uid;
        this.nickName = nickName;
        this.click = click;
    }

    public DynamicBindingAdapter(Context context) {
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemTypeToLayoutMap = new HashMap<>();

        mCollections = new ArrayList<>();
        mCollectionTypes = new ArrayList<>();

        if(snsPopupWindow == null){
            snsPopupWindow = new SnsPopupWindow(context);
        }

    }
    public void setUid(long uid){
        this.uid = uid;
    }

    public void setNickName(String nickName){
        this.nickName = nickName;
    }

    public String getNickName(){
        return nickName;
    }


    public void addType(int type, int layoutId){
        if(itemTypeToLayoutMap == null){
            itemTypeToLayoutMap = new HashMap<>();
        }
        itemTypeToLayoutMap.put(type,layoutId);

    }

    public void addAll(List collections, int type){
        mCollections.addAll(collections);
        for(int i = 0;i<collections.size();i++){
            mCollectionTypes.add(type);
        }
        notifyDataSetChanged();
    }

    public void removeAll(){
        mCollections.clear();
        mCollectionTypes.clear();
    }

    public boolean haveData(){
        return mCollections!=null && mCollections.size()>0;
    }

    @Override
    public int getItemViewType(int position) {
        int type = mCollectionTypes.get(position);
        if(type == RECYCLER_VIEW_DYNAMIC_TYPE){
            if(!TextUtils.isEmpty(((WeiboBean)mCollections.get(position)).getVideo())){
                type = RECYCLER_VIEW_DYNAMIC_TYPE_video;
            }else{
                type = RECYCLER_VIEW_DYNAMIC_TYPE_image;
            }
        }
        return type;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new BindingViewHolder(DataBindingUtil.inflate(mLayoutInflater,getLayoutId(viewType),parent,false,null));

    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, final int position) {
        final WeiboBean object = (WeiboBean) mCollections.get(position);

        if(holder.binding instanceof ItemDynamicImageBinding){
            final ItemDynamicImageBinding binding = ((ItemDynamicImageBinding)(holder.binding));

            binding.setVariable(BR.bean,object);


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

            if(object.hasFavort()||object.hasComment()){
                binding.digCommentBody.setVisibility(View.VISIBLE);
                binding.linDig.setVisibility(View.VISIBLE);
            }else{
                binding.digCommentBody.setVisibility(View.GONE);
                binding.linDig.setVisibility(View.GONE);
            }


            binding.snsBtn.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onDoubleClick(View view) {

                    if(snsPopupWindow == null){
                        snsPopupWindow = new SnsPopupWindow(context);
                    }

                    long curUserFavortId = object.getCurUserFavortId(uid);

                    if(curUserFavortId!=0){//是否已点赞
                        snsPopupWindow.getmActionItems().get(0).setType(ActionItem.ACTION_TYPE_CANCEL);
                    }else{
                        snsPopupWindow.getmActionItems().get(0).setType(ActionItem.ACTION_TYPE_DIG);
                    }
                    snsPopupWindow.update();

                    snsPopupWindow.setmItemClickListener(new PopupItemClickListener(object,position,curUserFavortId));

                    snsPopupWindow.showPopupWindow(binding.snsBtn);
                }
            });

            List<PhotoInfo> photoInfos = object.getPic();
            if(photoInfos!=null && photoInfos.size()>0){
                binding.multiImageview.setDynamicItem(object);
                binding.multiImageview.setVisibility(View.VISIBLE);
                binding.multiImageview.setList(photoInfos);
                binding.multiImageview.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, WeiboBean dynamicItem, List<ImageView> imagesList, List<String> imagesUrlList) {
//                        click.onImageItemClick(view,position,dynamicItem,imagesList,imagesUrlList);
                        Messenger.getDefault().send(new ImageWatchBean(view,imagesList,imagesUrlList), MainActivity.SHOW_IMAGE_WATCH);
                    }

                });
            }else{
                binding.multiImageview.setVisibility(View.GONE);
            }



        }
        else if(holder.binding instanceof ItemDynamicVideoBinding){//video

            final ItemDynamicVideoBinding binding = ((ItemDynamicVideoBinding)(holder.binding));


            binding.setVariable(BR.bean,object);


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

            if(object.hasFavort()||object.hasComment()){
                binding.digCommentBody.setVisibility(View.VISIBLE);
                binding.linDig.setVisibility(View.VISIBLE);
            }else{
                binding.digCommentBody.setVisibility(View.GONE);
                binding.linDig.setVisibility(View.GONE);
            }


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

                    snsPopupWindow.setmItemClickListener(new PopupItemClickListener(object,position,curUserFavortId));

                    snsPopupWindow.showPopupWindow(binding.snsBtn);
                }
            });
            // TODO: 2017/12/18 更替数据源
            binding.videoView.setUp(
                    "http://jzvd.nathen.cn/384d341e000145fb82295bdc54ecef88/103eab5afca34baebc970378dd484942-5287d2089db37e62345123a1be272f8b.mp4", JZVideoPlayer.SCREEN_WINDOW_LIST,
                    object.getPcontent());


            Glide.with(binding.videoView.getContext())
                    .load("http://jzvd-pic.nathen.cn/jzvd-pic/2adde364-9be1-4864-b4b9-0b0bcc81ef2e.jpg")
                    .into(binding.videoView.thumbImageView);



        }

    }

    @Override
    public int getItemCount() {
        return mCollections.size();
    }

    public int getLayoutId(int viewType){
        return itemTypeToLayoutMap.get(viewType);
    }



    /**
     * PopupWindow的点击监听器
     */
    public class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener{
        private WeiboBean weiboBean;
        //点击的item位于列表的位置
        private int dynamicPosition;
        private long favouriteId;//点赞的id，如果没有点赞则为0

        public PopupItemClickListener(WeiboBean weiboBean,int dynamicPosition,long favouriteId){
            this.weiboBean = weiboBean;
            this.dynamicPosition = dynamicPosition;
            this.favouriteId = favouriteId;
        }

        @Override
        public void onItemClick(final ActionItem item, int position) {
            if(click!=null) {
                if(item.getType() == ActionItem.ACTION_TYPE_DIG) {
                    click.doLike(nickName, weiboBean.getPid(), new RequestImpl() {
                        @Override
                        public void loadSuccess(Object object) {
                            Logger.e(Logger.DEBUG_TAG, "doDynamicLike,loadSuccess()");
                            try {
                                FavortItem bean = (FavortItem) object;
                                weiboBean.getFavorters().add(bean);

                                //// TODO: 2017/11/30 更新点赞列表及弹出框显示的字
                                if(click!=null){
                                    click.updateRecycleView(dynamicPosition);
                                }

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
                            click.addSubscription(disposable);
                        }
                    });
                }else if(item.getType() == ActionItem.ACTION_TYPE_CANCEL){//取消点赞
                    if(favouriteId!=0) {
                        click.doDeleteLike(favouriteId, new RequestImpl() {
                            @Override
                            public void loadSuccess(Object object) {
                                Logger.e(Logger.DEBUG_TAG,"deleteDynamicList,loadSuccess()");
                                List<FavortItem> favortItemList = weiboBean.getFavorters();
                                for(FavortItem item:favortItemList){
                                    if(item.getId() == favouriteId){
                                        favortItemList.remove(item);
                                        if(click!=null){
                                            click.updateRecycleView(dynamicPosition);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void loadFailed() {
                                Logger.e(Logger.DEBUG_TAG, "deleteDynamicList,loadFailed()");
                            }

                            @Override
                            public void addSubscription(Disposable disposable) {
                                click.addSubscription(disposable);
                            }
                        });
                    }
                }
            }
        }
    }


}
