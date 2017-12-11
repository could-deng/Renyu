package could.bluepay.renyumvvm.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.common.PrefsHelper;
import could.bluepay.renyumvvm.databinding.FragmentDynamicBinding;
import could.bluepay.renyumvvm.http.RequestImpl;
import could.bluepay.renyumvvm.http.bean.WeiboBean;
import could.bluepay.renyumvvm.model.MainModel;
import could.bluepay.renyumvvm.model.MemExchange;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.view.MainActivity;
import could.bluepay.renyumvvm.view.adapter.bindingAdapter.DynamicBindingAdapter;
import rx.Subscription;

/**
 * DynamicFragment的viewModel
 */

public class DynamicViewModel<SV extends ViewDataBinding> {


    private MainModel mModel;
    private SV binding;
    private Context context;

    private DynamicBindingAdapter adapter;
    private int page = 0;


    private LinearLayoutManager linearLayoutManager;

    // TODO: 2017/12/6 传入的context是否造成内存泄漏

    public DynamicViewModel(SV binding, Context context) {
        this.binding = binding;
        this.context = context;
        mModel = new MainModel();
    }

    public void setAdapterData(){
        if(adapter == null){
            adapter = new DynamicBindingAdapter(context);
        }
        adapter.setUid(((MainActivity)context).getUid());
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
            public void addSubscription(Subscription subscription) {
                if (updateView!=null){
                    updateView.fragmentAddSubscription(subscription);
                }
            }

            @Override
            public void doLike(String nickName, long pid, RequestImpl request) {
                mModel.doDynamicLike(nickName,pid,request);
            }

            @Override
            public void doDeleteLike(long pid, RequestImpl request) {
                mModel.deleteDynamicLike(pid,request);
            }

//            @Override
//            public void clickPicture(int WeiboPosition, View view, String pictureUrl, int PhotoPosition) {
//                Intent intent = new Intent();
//                intent.setClass(context, PictureViewPagerActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("WeiboPosition", WeiboPosition);
//                bundle.putInt("PhotoPosition",PhotoPosition);
//                intent.putExtras(bundle);
////                context.startActivity(intent);
//
//                //
//                ActivityTransitionLauncher.with((MainActivity)context)
//                        .from(view)
//                        .image(pictureUrl)
//                        .launch(intent, true);//传入的是SimpleDraweeView所认可的路径字符串
//            }

            @Override
            public void onImageItemClick(View view, int position, WeiboBean dynamicItem, List<ImageView> imagesList, List<String> imagesUrlList) {
                if(context!=null) {
                    ((MainActivity) (context)).showImageWatch(view,imagesList,imagesUrlList);
                }
            }
        });

        ((FragmentDynamicBinding)(binding)).setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(context);
        ((FragmentDynamicBinding)(binding)).setLayoutManager(linearLayoutManager);

    }

    /**
     * 请求数据
     * @param ifFirstPage
     */
    public void loadDynamicData(boolean ifFirstPage){
        if(ifFirstPage) {
            page = 1;
            mModel.setData(((MainActivity) context).getUid(), page);
        }

        //todo 请求下一页时，接口需要定义每页长度。


        mModel.getHotDynamicsData(new RequestImpl() {

            @Override
            public void loadSuccess(Object object) {
                ObservableArrayList<WeiboBean> dataList = (ObservableArrayList<WeiboBean>)object;
                updateView.showFragmentContentView();
                if(null == dataList || dataList.size() == 0){
                    return;
                }
                MemExchange.getInstance().setWeiboBeanList(dataList,page);


                setData(page,MemExchange.getInstance().getWeiboBeanList());

                ((FragmentDynamicBinding)(binding)).xrvDynamic.refreshComplete();
            }

            @Override
            public void loadFailed() {
                updateView.showFragmentContentView();
                ((FragmentDynamicBinding)(binding)).xrvDynamic.refreshComplete();
                Logger.e(Logger.DEBUG_TAG,"loadFailed()");
            }

            @Override
            public void addSubscription(Subscription subscription) {
                updateView.fragmentAddSubscription(subscription);
            }
        });

    }


    public void setData(int page,ArrayList<WeiboBean> dataList){
        if(page == 1){
            adapter.removeAll();
            adapter.addAll(dataList, DynamicBindingAdapter.RECYCLER_VIEW_DYNAMIC_TYPE);
        }else{
            adapter.addAll(dataList, DynamicBindingAdapter.RECYCLER_VIEW_DYNAMIC_TYPE);
        }
        updateView.haveSetData();

    }


    //region========viewmodel与DynamicFragment交互===========

    private UpdateView updateView;
    public void setUpdateView(UpdateView updateView){
        this.updateView = updateView;
    }
    public interface UpdateView{
        void showFragmentContentView();
        void fragmentAddSubscription(Subscription subscription);
        //通知已经设置了数据
        void haveSetData();
    }

    //endregion========viewmodel与DynamicFragment交互===========





    /**
     * ViewModel与adapter的交流
     */
    public interface poupWindowClick{
        void updateRecycleView(int index);
        void addSubscription(Subscription subscription);
        void doLike(String nickName,long pid,RequestImpl request);
        void doDeleteLike(long favoriteId,RequestImpl request);
//        void clickPicture(int weiboPosition,View view,String pictureUrl, int PhotoPosition);
        void onImageItemClick(View view, int position, WeiboBean dynamicItem, List<ImageView> imagesList, List<String> imagesUrlList);
    }

}
