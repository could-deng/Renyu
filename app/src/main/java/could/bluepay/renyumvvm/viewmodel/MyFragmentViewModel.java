package could.bluepay.renyumvvm.viewmodel;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.common.PrefsHelper;
import could.bluepay.renyumvvm.databinding.FragmentDynamicBinding;
import could.bluepay.renyumvvm.databinding.FragmentMyBinding;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.http.bean.WeiboBean;
import could.bluepay.renyumvvm.model.MainModel;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.activity.MainActivity;
import could.bluepay.renyumvvm.view.adapter.bindingAdapter.DynamicBindingAdapter;
import could.bluepay.renyumvvm.view.fragment.BaseFragment;
import could.bluepay.widget.jiaozivideoplayer.JZMediaManager;
import could.bluepay.widget.jiaozivideoplayer.JZUtils;
import could.bluepay.widget.jiaozivideoplayer.JZVideoPlayer;
import could.bluepay.widget.xrecyclerview.XRecyclerView;
import io.reactivex.disposables.Disposable;

/**
 * 个人中心模块
 */

public class MyFragmentViewModel <SV extends ViewDataBinding> extends BaseFragmentViewModel {
    private SV binding;

    private BaseFragment context;



    private LinearLayoutManager linearLayoutManager;
    private DynamicBindingAdapter adapter;

    private MainModel mModel;

    public void init(SV binding,BaseFragment context){
        this.binding = binding;
        this.context = context;
        mModel = new MainModel();
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


    public void setAdapterData(){
        if(adapter == null){
            adapter = new DynamicBindingAdapter(context.getContext());
        }
        adapter.setUid((MainModel.getmInstance().getUid()));
        adapter.setNickName(PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).read(Config.SP_KEY_NICKNAME));

        adapter.addType(DynamicBindingAdapter.RECYCLER_VIEW_DYNAMIC_TYPE_image, R.layout.item_dynamic_image);
        adapter.addType(DynamicBindingAdapter.RECYCLER_VIEW_DYNAMIC_TYPE_video,R.layout.item_dynamic_video);

        adapter.setClick(new DynamicViewModel.poupWindowClick() {
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
//                    ((MainActivity) (context.getActivity())).showImageWatch(view,imagesList,imagesUrlList);
//                }
            }
        });

        ((FragmentMyBinding)(binding)).setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(context.getContext());
        ((FragmentMyBinding)(binding)).setLayoutManager(linearLayoutManager);

    }

    /**
     * 请求数据
     * @param ifFirstPage
     */
    public void loadDynamicData(boolean ifFirstPage){

    }
}
