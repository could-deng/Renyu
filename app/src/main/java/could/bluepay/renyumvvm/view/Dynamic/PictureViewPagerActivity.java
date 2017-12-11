package could.bluepay.renyumvvm.view.Dynamic;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
import could.bluepay.couldutils.transAnim.ActivityTransition;
import could.bluepay.couldutils.transAnim.ExitActivityTransition;
import could.bluepay.couldutils.util.ThreadManager;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.ActivityPictureViewPagerBinding;
import could.bluepay.renyumvvm.http.bean.PhotoInfo;
import could.bluepay.renyumvvm.model.MemExchange;
import could.bluepay.renyumvvm.utils.ViewUtils;
import could.bluepay.renyumvvm.view.adapter.PhotoViewPagerAdapter;

/**
 * Created by bluepay on 2017/12/8.
 */

public class PictureViewPagerActivity extends Activity{
    ActivityPictureViewPagerBinding binding;
    List<PhotoInfo> photolist;
    int WeiboPosition = -1;
    int PhotoPosition = -1;
    PhotoViewPagerAdapter adapter;
    List<ImageView> dotViewList;
    private ViewPager.OnPageChangeListener pageChangeListener;


    private ExitActivityTransition exitTransition;//退场动画

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_picture_view_pager);

        adapter = new PhotoViewPagerAdapter(this, new PhotoViewPagerAdapter.PhotoViewClick(){

            @Override
            public void onPhotoViewClick() {
                onBackPressed();
            }
        });

        dotViewList = new ArrayList<>();

        binding.picViewPager.setAdapter(adapter);
        binding.picViewPager.addOnPageChangeListener(getPageChangeListener());

        getData();
        translateFinish(savedInstanceState);
    }
    private void getData(){
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle == null) {
                return;
            }
            WeiboPosition = bundle.getInt("WeiboPosition");
            PhotoPosition = bundle.getInt("PhotoPosition");
            photolist = MemExchange.getInstance().getWeiboBeanList().get(WeiboPosition).getPic();
            if (photolist == null) {
                throw new Exception("photoList == null");
            }
            adapter.setPhotolist(photolist);

            if (photolist.size() > 1) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewUtils.dp2px(this,7),ViewUtils.dp2px(this,7));
                params.setMargins(ViewUtils.dp2px(this,3),ViewUtils.dp2px(this,1),ViewUtils.dp2px(this,3),ViewUtils.dp2px(this,1));
                binding.llDotContainer.removeAllViews();
                dotViewList.clear();
                for (int i = 0; i < photolist.size(); i++) {
                    ImageView imageView = new ImageView(this);
                    if(i == PhotoPosition){
                        imageView.setImageResource(R.drawable.dot_focus);
                    }else{
                        imageView.setImageResource(R.drawable.dot_normal);
                    }
                    binding.llDotContainer.addView(imageView,params);
                    dotViewList.add(imageView);
                }
                binding.llDotContainer.setGravity(Gravity.CENTER);
                binding.picViewPager.setOffscreenPageLimit(photolist.size()-1);

                //如果通过在layout文件中设置viewpager的adapter，执行到这步时，viewpager的adapter仍然为空，因此设置currentItem失败
                binding.picViewPager.setCurrentItem(PhotoPosition, false);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void translateFinish(Bundle arg0){
        Intent intent = getIntent();
        if (intent != null) {
            exitTransition = ActivityTransition
                    .with(intent)
                    .to(binding.imgTemp)
                    .interpolator(new FastOutSlowInInterpolator())
                    .start(arg0);

            ThreadManager.getMainHandler().postDelayed(new Runnable() {
                @Override
                public void run() {//动画结束后,显示本来的布局,隐藏临时图标
                    binding.rlRoot.setVisibility(View.VISIBLE);
                    binding.imgTemp.setVisibility(View.GONE);

                }
            }, 400);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//复写返回操作,回退动画
        switch (item.getItemId()) {
            case android.R.id.home://返回
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (exitTransition != null) {
            //先隐藏本来布局
            binding.rlRoot.setVisibility(View.GONE);
            binding.imgTemp.setVisibility(View.VISIBLE);
            exitTransition.exit(this);//退场动画
        } else {
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }


    public ViewPager.OnPageChangeListener getPageChangeListener(){
        if(pageChangeListener == null){
            pageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(binding.picViewPager!=null && dotViewList!=null && dotViewList.size()>0){
                        for(int i =0;i<dotViewList.size();i++){
                            dotViewList.get(i).setImageResource(R.drawable.dot_normal);
                        }
                        dotViewList.get(position).setImageResource(R.drawable.dot_focus);
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };
        }
        return pageChangeListener;
    }


}
