package could.bluepay.renyumvvm.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import could.bluepay.renyumvvm.bindingAdapter.command.ReplyCommand;
import could.bluepay.renyumvvm.http.bean.BestResultBean;
import could.bluepay.renyumvvm.http.bean.PhotoInfo;
import could.bluepay.renyumvvm.http.bean.WeiboBean;
import could.bluepay.renyumvvm.utils.AppUtils;
import could.bluepay.renyumvvm.view.adapter.PhotoViewPagerAdapter;
import could.bluepay.renyumvvm.view.adapter.UserListFocusAdapter;
import could.bluepay.renyumvvm.view.adapter.bindingAdapter.DynamicBindingAdapter;
import could.bluepay.renyumvvm.widget.BestChartView;
import could.bluepay.renyumvvm.widget.CommentListView;
import could.bluepay.renyumvvm.widget.ExpandTextView;
import could.bluepay.renyumvvm.widget.MultiImageView;
import could.bluepay.renyumvvm.widget.PraiseListView;
import could.bluepay.renyumvvm.widget.pulltorefresh.RefreshLayout;
import could.bluepay.renyumvvm.widget.pulltorefresh.SwipeRefreshDirection;
import could.bluepay.widget.jiaozivideoplayer.JZVideoPlayer;
import could.bluepay.widget.jiaozivideoplayer.JZVideoPlayerStandard;
import could.bluepay.widget.xrecyclerview.XRecyclerView;

/**
 * Created by bluepay on 2017/12/5.
 */

public class ViewBindingAdapter {


    @BindingAdapter(value = "chartData")
    public static void setChartData(BestChartView view, ObservableArrayList<BestResultBean.DateBean> data){
        view.setBeanDataList(data);
    }

    @BindingAdapter(value = {"setRefreshLayoutListener","setOnRefreshing"},requireAll = false)
    public static void setRefreshLayoutListener(RefreshLayout view, RefreshLayout.OnRefreshListener listener,boolean refreshing){
        if(view!=null){
            view.setDirection(SwipeRefreshDirection.BOTH);
            view.setOnRefreshListener(listener);
            view.setRefreshing(refreshing);
        }
    }


    //region============DynamicFragment布局相关===========

    @BindingAdapter("android:setLoadingListener")
    public static void setLoadingListener(XRecyclerView view, XRecyclerView.LoadingListener listener){
        view.setLoadingListener(listener);
    }




    @BindingAdapter("android:ttext")
    public static void setText(TextView tv, int text){
        tv.setText(text+"");
    }

    //recyclerView
    @BindingAdapter("layoutManager")
    public static void setLayoutManager(XRecyclerView view, XRecyclerView.LayoutManager manager) {
        view.setLayoutManager(manager);
    }

    //DynamicFragment
    @BindingAdapter("adapter")
    public static void setAdapter(XRecyclerView view, DynamicBindingAdapter adapter) {
        view.setAdapter(adapter);
    }


    @BindingAdapter("addOnChildAttachStateChangeListener")
    public static void addOnChildAttachStateChangeListener(RecyclerView view , RecyclerView.OnChildAttachStateChangeListener listener){
        view.addOnChildAttachStateChangeListener(listener);
    }


    @BindingAdapter(value = {"expandText","isExpand"},requireAll = false)
    public static void setExpandTextViewText(ExpandTextView view, String expandText,boolean isExpand){
        if(!expandText.isEmpty()){
            view.setVisibility(View.VISIBLE);
            view.setText(AppUtils.formatUrlString(expandText));
            view.setExpand(isExpand);
        }else{
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter(value = {"videoUrl","contentText"})
    public static void setJZVideoData(JZVideoPlayerStandard view, String videoUrl, String contentText){
        videoUrl = "http://jzvd.nathen.cn/35b3dc97fbc240219961bd1fccc6400b/8d9b76ab5a584bce84a8afce012b72d3-5287d2089db37e62345123a1be272f8b.mp4";
        if(!TextUtils.isEmpty(videoUrl) && !TextUtils.isEmpty(contentText)) {
            view.setUp(videoUrl,
                    JZVideoPlayer.SCREEN_WINDOW_LIST,
                    contentText);
        }
        Glide.with(view.getContext())
                .load("http://jzvd-pic.nathen.cn/jzvd-pic/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg")
                .into(view.thumbImageView);
    }

    @BindingAdapter(value = {"photoInfos","bean","onItemClickListener"},requireAll = false)
    public static void setMultiImage(MultiImageView view, List<PhotoInfo> photoInfos, WeiboBean bean, MultiImageView.OnItemClickListener onItemClickListener){
        if(photoInfos!=null && !photoInfos.isEmpty()) {
            view.setVisibility(View.VISIBLE);
            view.setList(photoInfos);
            view.setOnItemClickListener(onItemClickListener);
        }else{
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("setPraiseList")
    public static void setPraiseList(PraiseListView view, ObservableArrayList<String> list){
        view.setDatas(list);
    }


//    public static setCommentListView(CommentListView view,)

    //endregion============DynamicFragment布局相关===========

    //region==========FocusFragment============








//    @BindingAdapter("setOnRefreshListener")
//    public static void setOnRefreshListener(SwipeRefreshLayout view, SwipeRefreshLayout.OnRefreshListener listener){
//        view.setOnRefreshListener(listener);
//    }

    @BindingAdapter("adapter")
    public static void setAdapter(RecyclerView view, UserListFocusAdapter adapter){
        view.setAdapter(adapter);
    }
    @BindingAdapter("addOnScrollListener")
    public static void addOnScrollListener(RecyclerView view,RecyclerView.OnScrollListener listener){
        view.addOnScrollListener(listener);
    }

    @BindingAdapter("setOnRefreshing")
    public static void setRefreshing(SwipeRefreshLayout view,boolean isRefreshing){
        view.setRefreshing(isRefreshing);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView view,RecyclerView.LayoutManager manager){
        view.setLayoutManager(manager);
    }


    //endregion==========FocusFragment============



    //viewPager
    @BindingAdapter(value = {"adapter","onPageChangeListener"},requireAll = false)
    public static void setAdapter(ViewPager view, PhotoViewPagerAdapter adapter, ViewPager.OnPageChangeListener onPageChangeListener){
        view.setAdapter(adapter);
        view.addOnPageChangeListener(onPageChangeListener);
    }


//     ViewPager
//    @SuppressWarnings("unchecked")
//    @BindingAdapter(value = {"itemBinding", "items", "adapter", "pageTitles"}, requireAll = false)
//    public static <T> void setAdapter(ViewPager viewPager, ItemBinding<T> itemBinding, List items, BindingViewPagerAdapter<T> adapter, BindingViewPagerAdapter.PageTitles<T> pageTitles) {
//        if (itemBinding == null) {
//            throw new IllegalArgumentException("onItemBind must not be null");
//        }
//        BindingViewPagerAdapter<T> oldAdapter = (BindingViewPagerAdapter<T>) viewPager.getAdapter();
//        if (adapter == null) {
//            if (oldAdapter == null) {
//                adapter = new BindingViewPagerAdapter<>();
//            } else {
//                adapter = oldAdapter;
//            }
//        }
//        adapter.setItemBinding(itemBinding);
//        adapter.setItems(items);
//        adapter.setPageTitles(pageTitles);
//
//        if (oldAdapter != adapter) {
//            viewPager.setAdapter(adapter);
//        }
//    }

//    @BindingAdapter("photoUrl")
//    public static void setPhotoView(PhotoView photoView, String url){
//                Glide.with(photoView.getContext())
//                .load(url)
//                .crossFade(500)
//                .error(R.drawable.img_default_meizi)
//                .into(photoView);
//        photoView.enable();
//        photoView.setScaleType(ImageView.ScaleType.CENTER);
//    }


}
