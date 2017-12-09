package could.bluepay.renyumvvm.viewmodel;


/**
 * Created by bluepay on 2017/12/5.
 */

public class ViewBindingAdapter {

//    @BindingAdapter("adapter")
//    public static void setAdapter(RecyclerView recyclerView, DynamicAdapter adapter){
//        recyclerView.setAdapter(adapter);
//    }



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
