package could.bluepay.renyumvvm.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;
import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.bindingAdapter.messenger.Messenger;
import could.bluepay.renyumvvm.common.PrefsHelper;
import could.bluepay.renyumvvm.databinding.FragmentDynamicBinding;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.http.bean.WeiboBean;
import could.bluepay.renyumvvm.model.MainModel;
import could.bluepay.renyumvvm.model.MemExchange;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.view.adapter.bindingAdapter.DynamicBindingAdapter;
import could.bluepay.renyumvvm.view.bean.ImageWatchBean;
import could.bluepay.renyumvvm.view.fragment.BaseFragment;
import could.bluepay.widget.jiaozivideoplayer.JZMediaManager;
import could.bluepay.widget.jiaozivideoplayer.JZUtils;
import could.bluepay.widget.jiaozivideoplayer.JZVideoPlayer;
import could.bluepay.widget.xrecyclerview.XRecyclerView;
import io.reactivex.disposables.Disposable;

/**
 * DynamicFragment的viewModel
 */

public class DynamicViewModel<SV extends ViewDataBinding> extends BaseFragmentViewModel{


    private MainModel mModel;


    private SV binding;
    private BaseFragment context;

    protected DynamicBindingAdapter adapter;
    private int page = 0;


    private LinearLayoutManager linearLayoutManager;

    public DynamicViewModel(){

    }
    public void init(SV binding,BaseFragment context){
        this.binding = binding;
        this.context = context;
        mModel = new MainModel();
    }

    // TODO: 2017/12/6 传入的context是否造成内存泄漏

    //region=====提供给view层调用======

    public void loadData(){
        this.loadDynamicData(true);
    }
    public boolean haveData(){
        return adapter!=null && adapter.haveData();
    }

    //endregion=====提供给view层调用======
    /**
     * visible时自动调用、错误时点击重新加载
     */
    @Override
    public void onloadData() {
        super.onloadData();
        loadData();
    }



    //region=========Command===============

    public final XRecyclerView.LoadingListener loadingListener = new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
            loadDynamicData(true);
        }

        @Override
        public void onLoadMore() {
            loadDynamicData(false);
        }
    };

    public final RecyclerView.OnChildAttachStateChangeListener attachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {
            JZVideoPlayer videoVideo = (JZVideoPlayer) view.findViewById(R.id.video_view);
            if(videoVideo!=null && JZUtils.dataSourceObjectsContainsUri(videoVideo.dataSourceObjects, JZMediaManager.getCurrentDataSource())){
                JZVideoPlayer.releaseAllVideos();
            }
        }
    };

    //endregion=========Command===============


//
    public void setAdapterData(){
        if(adapter == null){
            adapter = new DynamicBindingAdapter(context.getContext());
        }
        adapter.setUid((MainModel.getmInstance().getUid()));
        adapter.setNickName(PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).read(Config.SP_KEY_NICKNAME));

        adapter.addType(DynamicBindingAdapter.RECYCLER_VIEW_DYNAMIC_TYPE_image, R.layout.item_dynamic_image);
        adapter.addType(DynamicBindingAdapter.RECYCLER_VIEW_DYNAMIC_TYPE_video,R.layout.item_dynamic_video);

        adapter.setClick(new poupWindowClick() {
            @Override
            public void updateRecycleView(int dynamicIndex) {
                if(linearLayoutManager == null){
                    return;
                }
                int f = linearLayoutManager.findFirstVisibleItemPosition();
                int e = linearLayoutManager.findLastVisibleItemPosition();

                //因为recyclerView的默认加了一个空白头，所以index从1开始，而非0
                dynamicIndex++;
                Logger.e(Logger.DEBUG_TAG,"first:"+f+",last:"+e+",dynamicIndex:"+dynamicIndex);

                if(f<=(dynamicIndex) && (dynamicIndex)>=e){
                    adapter.notifyItemChanged(dynamicIndex);
                }


                Logger.e(Logger.DEBUG_TAG,"updateRecyclerView");
                binding.executePendingBindings();

            }

            @Override
            public void addSubscription(Disposable subscription) {
                Logger.e(Logger.DEBUG_TAG,"DynamicViewModel,addSubscription");
                context.addSubscription(subscription);
            }

            @Override
            public void doLike(String nickName, long pid, RequestImpl request) {
                mModel.doDynamicLike(nickName,pid,request);
            }

            @Override
            public void doDeleteLike(long pid, RequestImpl request) {
                mModel.deleteDynamicLike(pid,request);
            }
            @Override
            public void onImageItemClick(View view, int position, WeiboBean dynamicItem, List<ImageView> imagesList, List<String> imagesUrlList) {
//                if(context!=null) {
//                    ((MainActivity) (context.getActivity())).showImageWatch(view,imagesList,imagesUrlList);//会造成内存泄漏
//                }
            }
        });

        ((FragmentDynamicBinding)(binding)).setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(context.getContext());
        ((FragmentDynamicBinding)(binding)).setLayoutManager(linearLayoutManager);

//        ((FragmentDynamicBinding)(binding)).xrvDynamic.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
//            @Override
//            public void onChildViewAttachedToWindow(View view) {
//            }
//
//            @Override
//            public void onChildViewDetachedFromWindow(View view) {
//                JZVideoPlayer videoVideo = (JZVideoPlayer) view.findViewById(R.id.video_view);
//                if(videoVideo!=null && JZUtils.dataSourceObjectsContainsUri(videoVideo.dataSourceObjects, JZMediaManager.getCurrentDataSource())){
//                    JZVideoPlayer.releaseAllVideos();
//                }
//            }
//        });
    }

    /**
     * 请求数据
     * @param ifFirstPage
     */
    public void loadDynamicData(boolean ifFirstPage){
        if(ifFirstPage) {
            page = 1;
        }else{
            page++;
        }

        //todo 请求下一页时，接口需要定义每页长度。


        mModel.getHotDynamicsData(page,new RequestImpl() {

            @Override
            public void loadSuccess(Object object) {
                ObservableArrayList<WeiboBean> dataList = (ObservableArrayList<WeiboBean>)object;
                showContentView();
                if(null == dataList || dataList.size() == 0){
                    if(page>1) {
                        page--;
                    }
                    ((FragmentDynamicBinding)(binding)).xrvDynamic.noMoreLoading();
                    return;
                }
                MemExchange.getInstance().setWeiboBeanList(dataList,page == 1);

                setData(page == 1,MemExchange.getInstance().getWeiboBeanList());

                ((FragmentDynamicBinding)(binding)).xrvDynamic.refreshComplete();
            }

            @Override
            public void loadFailed() {
                if(page == 1) {
                    showError();
                }else{
                    page--;
                }

                ((FragmentDynamicBinding)(binding)).xrvDynamic.refreshComplete();
                Logger.e(Logger.DEBUG_TAG,"loadFailed()");
            }

            @Override
            public void addSubscription(Disposable disposable) {
                Logger.e(Logger.DEBUG_TAG,"DynamicViewModel,addSubscription");
                context.addSubscription(disposable);
            }
        });

    }


    public void setData(boolean ifFirst,ArrayList<WeiboBean> dataList){
        if(ifFirst){
            adapter.removeAll();
            adapter.addAll(dataList, DynamicBindingAdapter.RECYCLER_VIEW_DYNAMIC_TYPE);
        }else{
            adapter.addAll(dataList, DynamicBindingAdapter.RECYCLER_VIEW_DYNAMIC_TYPE);
        }
    }






    /**
     * ViewModel与adapter的交流
     */
    public interface poupWindowClick{
        void updateRecycleView(int index);
        void addSubscription(Disposable disposable);
        void doLike(String nickName,long pid,RequestImpl request);
        void doDeleteLike(long favoriteId,RequestImpl request);
//        void clickPicture(int weiboPosition,View view,String pictureUrl, int PhotoPosition);
        void onImageItemClick(View view, int position, WeiboBean dynamicItem, List<ImageView> imagesList, List<String> imagesUrlList);
    }

}
