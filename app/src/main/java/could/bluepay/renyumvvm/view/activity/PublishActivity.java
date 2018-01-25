package could.bluepay.renyumvvm.view.activity;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.common.entity.LocalMedia;
import could.bluepay.renyumvvm.common.config.PictureConfig;
import could.bluepay.renyumvvm.common.config.PictureMimeType;
import could.bluepay.renyumvvm.common.manager.FullyGridLayoutManager;
import could.bluepay.renyumvvm.common.memoryleak.TextLineUtils;
import could.bluepay.renyumvvm.common.permissions.RxPermissions;
import could.bluepay.renyumvvm.databinding.ActivityPublicBinding;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.utils.ViewUtils;
import could.bluepay.renyumvvm.utils.pictureSelector.PictureFileUtils;
import could.bluepay.renyumvvm.utils.pictureSelector.PictureSelector;
import could.bluepay.renyumvvm.view.adapter.GridImageAdapter;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 发布朋友圈
 */

public class PublishActivity extends BaseActivity<ActivityPublicBinding>{
    public static final int PublishTypeNormalPicture = 1;
    public static final int PublishTypeRedPackagePicture = 2;
    public static final int PublishTypeNormalVideo = 3;
    public static final int PublishTypeRedPackageVideo = 4;

    private int publishType = PublishTypeNormalPicture;//发布的类型
    public static final String PUBLISHTYPETAG = "publishType";

    private int maxSelectSize = 1;

    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter imageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        showBackTab();
        initView();
        if(savedInstanceState!=null){
            publishType = savedInstanceState.getInt("publishType");
            maxSelectSize = savedInstanceState.getInt("maxSelectSize");
        }
        permissionAllow();
    }

    private void initToolbar(){
        ifShowMenuTextView = true;
        textMenuTextView = getResources().getString(R.string.publish);

        setSupportActionBar((Toolbar) binding.toolbar.getRoot());
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }

        binding.toolbar.indicator.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setVisibility(View.VISIBLE);
        binding.toolbar.tvToolbarTitle.setText("");
    }

    private void initView(){

        if(getIntent()!=null) {
            publishType = getIntent().getIntExtra(PUBLISHTYPETAG, PublishTypeNormalPicture);
            if (publishType == PublishTypeNormalPicture || publishType == PublishTypeRedPackagePicture) {
                maxSelectSize = 9;
            } else if (publishType == PublishTypeNormalVideo || publishType == PublishTypeRedPackageVideo) {
                maxSelectSize = 1;
            }
        }

        imageAdapter = new GridImageAdapter(PublishActivity.this,onAddPicClickListener)
                .setSelectMax(9)
                .setDataList(selectList);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(PublishActivity.this, 4, GridLayoutManager.VERTICAL, false);

        (binding).rvActivityPublish.setLayoutManager(manager);

        (binding).rvActivityPublish.setAdapter(imageAdapter);
//        binding.tvPublishDivider.setHeight(ViewUtils.dp2px(this,1));
    }

    private void permissionAllow(){
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(PublishActivity.this);
                } else {
                    Toast.makeText(PublishActivity.this,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private GridImageAdapter.OnAddPicClickListener onAddPicClickListener = new GridImageAdapter.OnAddPicClickListener() {
        @Override
        public void onAddPicClick() {
// 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(PublishActivity.this)
                    .openGallery(getPictureSelectorType(publishType))// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectSize)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选//PictureConfig.MULTIPLE : PictureConfig.SINGLE
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(true)// 是否可预览视频
                    .enablePreviewAudio(false) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    .enableCrop(true)// 是否裁剪
                    .compress(true)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                    .compressSavePath(getPath())//压缩图片保存地址
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                    .isGif(true)// 是否显示gif图片
                    .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };

//    /**
//     * 自定义压缩存储地址
//     * @return
//     */
//    private String getPath() {
//        String path = Environment.getExternalStorageDirectory() + "/" + Config.AppName+ "/Luban/image/";
//        File file = new File(path);
//        if (file.mkdirs()) {
//            return path;
//        }
//        return path;
//    }

    private int getPictureSelectorType(int type){
        int selectorType;//默认是全部
        switch (type){
            case PublishActivity.PublishTypeNormalPicture:
            case PublishActivity.PublishTypeRedPackagePicture:
                selectorType = PictureMimeType.ofImage();
                break;
            case PublishActivity.PublishTypeNormalVideo:
            case PublishActivity.PublishTypeRedPackageVideo:
                selectorType = PictureMimeType.ofVideo();
                break;
            default:
                selectorType = PictureMimeType.ofAll();
        }
        return selectorType;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode){
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    for(LocalMedia media:selectList){
                        Logger.e(Logger.DEBUG_TAG,"PublishActivity,选择图片结束->"+media.getPath());
                    }
                    imageAdapter.setDataList(selectList);
                    imageAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("publishType",publishType);
        outState.putInt("maxSelectSize",maxSelectSize);
    }

    public void onMenuTabFirstClick(){
        Toast.makeText(this,"发布",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int setContent() {
        return R.layout.activity_public;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TextLineUtils.clearTextLineCache();
    }
}
