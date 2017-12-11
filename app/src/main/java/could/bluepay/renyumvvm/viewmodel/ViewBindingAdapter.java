package could.bluepay.renyumvvm.viewmodel;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;

import could.bluepay.renyumvvm.view.adapter.PhotoViewPagerAdapter;
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

    //recyclerView
    @BindingAdapter("layoutManager")
    public static void setLayoutManager(XRecyclerView view, XRecyclerView.LayoutManager manager) {
        view.setLayoutManager(manager);
    }

    @BindingAdapter("adapter")
    public static void setAdapter(XRecyclerView view, DynamicBindingAdapter adapter) {
        view.setAdapter(adapter);
    }


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
