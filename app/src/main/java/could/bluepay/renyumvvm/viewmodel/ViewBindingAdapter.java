package could.bluepay.renyumvvm.viewmodel;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import could.bluepay.renyumvvm.bindingAdapter.command.ReplyCommand;
import could.bluepay.renyumvvm.view.adapter.PhotoViewPagerAdapter;
import could.bluepay.renyumvvm.view.adapter.UserListFocusAdapter;
import could.bluepay.renyumvvm.view.adapter.bindingAdapter.DynamicBindingAdapter;
import could.bluepay.widget.xrecyclerview.XRecyclerView;

/**
 * Created by bluepay on 2017/12/5.
 */

public class ViewBindingAdapter {

//    @BindingAdapter("adapter")
//    public static void setAdapter(RecyclerView recyclerView, DynamicAdapter adapter){
//        recyclerView.setAdapter(adapter);
//    }


    //region============DynamicFragment布局相关===========

    @BindingAdapter("android:setLoadingListener")
    public static void setLoadingListener(XRecyclerView view, XRecyclerView.LoadingListener listener){
        view.setLoadingListener(listener);
    }

    @BindingAdapter("android:addOnChildAttachStateChangeListener")
    public static void addOnChildAttachStateChangeListener(RecyclerView view , RecyclerView.OnChildAttachStateChangeListener listener){
        view.addOnChildAttachStateChangeListener(listener);
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
