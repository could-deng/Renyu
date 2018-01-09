package could.bluepay.renyumvvm.view.activity.pictureSelector;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropMulti;
import com.yalantis.ucrop.model.CutInfo;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.common.decoration.GridSpacingItemDecoration;
import could.bluepay.renyumvvm.common.entity.EventEntity;
import could.bluepay.renyumvvm.common.entity.LocalMedia;
import could.bluepay.renyumvvm.common.entity.LocalMediaFolder;
import could.bluepay.renyumvvm.common.config.PictureConfig;
import could.bluepay.renyumvvm.common.config.PictureMimeType;
import could.bluepay.renyumvvm.common.model.LocalMediaLoader;
import could.bluepay.renyumvvm.common.observable.ImagesObservable;
import could.bluepay.renyumvvm.common.permissions.RxPermissions;
import could.bluepay.renyumvvm.rx.RxBus;
import could.bluepay.renyumvvm.rx.Subscribe;
import could.bluepay.renyumvvm.rx.ThreadMode;
import could.bluepay.renyumvvm.utils.Logger;
import could.bluepay.renyumvvm.utils.pictureSelector.AttrsUtils;
import could.bluepay.renyumvvm.utils.pictureSelector.DateUtils;
import could.bluepay.renyumvvm.utils.pictureSelector.DoubleUtils;
import could.bluepay.renyumvvm.utils.pictureSelector.LightStatusBarUtils;
import could.bluepay.renyumvvm.utils.pictureSelector.PictureFileUtils;
import could.bluepay.renyumvvm.utils.pictureSelector.PictureSelector;
import could.bluepay.renyumvvm.utils.pictureSelector.ScreenUtils;
import could.bluepay.renyumvvm.utils.pictureSelector.StringUtils;
import could.bluepay.renyumvvm.utils.pictureSelector.ToolbarUtil;
import could.bluepay.renyumvvm.view.adapter.PictureAlbumDirectoryAdapter;
import could.bluepay.renyumvvm.view.adapter.PictureImageGridAdapter;
import could.bluepay.renyumvvm.widget.dialog.CustomDialog;
import could.bluepay.renyumvvm.widget.pictureSelector.FolderPopWindow;
import could.bluepay.renyumvvm.widget.pictureSelector.PhotoPopupWindow;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * sd卡图片GridView浏览Activity
 */
public class PictureSelectorActivity extends PictureBaseActivity implements View.OnClickListener,
        PictureAlbumDirectoryAdapter.OnItemClickListener,
        PictureImageGridAdapter.OnPhotoSelectChangedListener, PhotoPopupWindow.OnItemClickListener {
    private final static String TAG = PictureSelectorActivity.class.getSimpleName();
    private static final int SHOW_DIALOG = 0;
    private static final int DISMISS_DIALOG = 1;
    private static final int STATUSBAR = 2;
    private ImageView picture_left_back;
    private TextView picture_title, picture_right, picture_tv_ok, tv_empty,
            picture_tv_img_num, picture_id_preview, tv_PlayPause, tv_Stop, tv_Quit,
            tv_musicStatus, tv_musicTotal, tv_musicTime;
    private RelativeLayout rl_picture_title, rl_bottom;
    private LinearLayout id_ll_ok;
    private RecyclerView picture_recycler;
    private PictureImageGridAdapter adapter;
    private List<LocalMedia> images = new ArrayList<>();
    private List<LocalMediaFolder> foldersList = new ArrayList<>();
    private FolderPopWindow folderWindow;
    private Animation animation = null;
    private boolean anim = false;
    private RxPermissions rxPermissions;
    private PhotoPopupWindow popupWindow;
    private LocalMediaLoader mediaLoader;
    private MediaPlayer mediaPlayer;
    private SeekBar musicSeekBar;
    private boolean isPlayAudio = false;
    private CustomDialog audioDialog;
    private int audioH;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_DIALOG://展示loading框
                    showPleaseDialog();
                    break;
                case DISMISS_DIALOG://取消loading框
                    dismissDialog();
                    break;
                case STATUSBAR://设置顶部状态栏
                    LightStatusBarUtils.setLightStatusBar(PictureSelectorActivity.this, statusFont);
                    break;
            }
        }
    };

    //仿照EventBus 3.0 回调，在RxBus，register()使用
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(EventEntity obj) {
        switch (obj.what) {
            case PictureConfig.UPDATE_FLAG:
                // 预览时勾选图片更新回调
                List<LocalMedia> selectImages = obj.medias;
                anim = true;//selectImages.size() >= 0 ? true : false
                int position = obj.position;
                adapter.bindSelectImages(selectImages);
                adapter.notifyItemChanged(position);
                break;
            case PictureConfig.PREVIEW_DATA_FLAG:
                List<LocalMedia> medias = obj.medias;
                if (medias.size() > 0) {
                    // 取出第1个判断是否是图片，视频和图片只能二选一，不必考虑图片和视频混合
                    String pictureType = medias.get(0).getPictureType();
                    if (config.isCompress && pictureType.startsWith(PictureConfig.IMAGE)) {
                        compressImage(medias);
                    } else {
                        onResult(medias);
                    }
                }
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //rxjava注册
        if (!RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().register(this);
        }
        rxPermissions = new RxPermissions(this);
        //状态栏
        mHandler.sendEmptyMessage(STATUSBAR);
        int status_color = AttrsUtils.getTypeValueColor(this, R.attr.picture_status_color);
        ToolbarUtil.setColorNoTranslucent(this, status_color);

        if (config.camera) {
            setTheme(R.style.activity_Theme_Transparent);
            if (savedInstanceState == null) {
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    onTakePhoto();
                                } else {
                                    showToast(getString(R.string.picture_camera));
                                    closeActivity();
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
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    , WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.picture_empty);
        } else {
            setContentView(R.layout.picture_selector);
            initView(savedInstanceState);
        }
    }


    /**
     * init views
     */
    private void initView(Bundle savedInstanceState) {

        rl_picture_title = (RelativeLayout) findViewById(R.id.rl_picture_title);
        picture_left_back = (ImageView) findViewById(R.id.picture_left_back);
        picture_title = (TextView) findViewById(R.id.picture_title);
        picture_right = (TextView) findViewById(R.id.picture_right);
        picture_tv_ok = (TextView) findViewById(R.id.picture_tv_ok);
        picture_id_preview = (TextView) findViewById(R.id.picture_id_preview);
        picture_tv_img_num = (TextView) findViewById(R.id.picture_tv_img_num);
        picture_recycler = (RecyclerView) findViewById(R.id.picture_recycler);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        id_ll_ok = (LinearLayout) findViewById(R.id.id_ll_ok);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        rl_bottom.setVisibility(config.selectionMode == PictureConfig.SINGLE ? View.GONE : View.VISIBLE);
        isNumComplete(numComplete);
        if (config.mimeType == PictureMimeType.ofAll()) {
            popupWindow = new PhotoPopupWindow(this);
            popupWindow.setOnItemClickListener(this);
        }
        picture_id_preview.setOnClickListener(this);
        if (config.mimeType == PictureMimeType.ofAudio()) {
            picture_id_preview.setVisibility(View.GONE);
            audioH = ScreenUtils.getScreenHeight(mContext)
                    + ScreenUtils.getStatusBarHeight(mContext);
        } else {
            picture_id_preview.setVisibility(config.mimeType == PictureConfig.TYPE_VIDEO
                    ? View.GONE : View.VISIBLE);
        }
        picture_left_back.setOnClickListener(this);
        picture_right.setOnClickListener(this);
        id_ll_ok.setOnClickListener(this);
        picture_title.setOnClickListener(this);
        String title = config.mimeType == PictureMimeType.ofAudio() ?
                getString(R.string.picture_all_audio)
                : getString(R.string.picture_camera_roll);
        picture_title.setText(title);
        folderWindow = new FolderPopWindow(this, config.mimeType);
        folderWindow.setPictureTitleView(picture_title);
        folderWindow.setOnItemClickListener(this);

        //  setHasFixedSize 的作用就是确保尺寸是通过用户输入从而确保RecyclerView的尺寸是一个常数。RecyclerView 的Item宽或者高不会变。每一个Item添加或者删除都不会变。
        //  如果你没有设置setHasFixedSized没有设置的代价将会是非常昂贵的。因为RecyclerView会需要而外计算每个item的size
        picture_recycler.setHasFixedSize(true);
        picture_recycler.addItemDecoration(new GridSpacingItemDecoration(config.imageSpanCount,
                ScreenUtils.dip2px(this, 2), false));
        picture_recycler.setLayoutManager(new GridLayoutManager(this, config.imageSpanCount));
        // 解决调用 notifyItemChanged 闪烁问题,取消默认动画
        ((SimpleItemAnimator) picture_recycler.getItemAnimator())
                .setSupportsChangeAnimations(false);
        mediaLoader = new LocalMediaLoader(this, config.mimeType, config.isGif, config.videoMaxSecond, config.videoMinSecond);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mHandler.sendEmptyMessage(SHOW_DIALOG);
                            readLocalMedia();
                        } else {
                            showToast(getString(R.string.picture_jurisdiction));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        tv_empty.setText(config.mimeType == PictureMimeType.ofAudio() ?
                getString(R.string.picture_audio_empty)
                : getString(R.string.picture_empty));
        StringUtils.tempTextFont(tv_empty, config.mimeType);
        if (savedInstanceState != null) {
            // 防止拍照内存不足时activity被回收，导致拍照后的图片未选中
            selectionMedias = PictureSelector.obtainSelectorList(savedInstanceState);
        }
        adapter = new PictureImageGridAdapter(mContext, config);
        adapter.setOnPhotoSelectChangedListener(PictureSelectorActivity.this);
        adapter.bindSelectImages(selectionMedias);
        picture_recycler.setAdapter(adapter);
        String titleText = picture_title.getText().toString().trim();
        if (config.isCamera) {
            config.isCamera = StringUtils.isCamera(titleText);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null) {
            List<LocalMedia> selectedImages = adapter.getSelectedImages();
            PictureSelector.saveSelectorList(outState, selectedImages);
        }
    }

    /**
     * none number style
     */
    private void isNumComplete(boolean numComplete) {
        picture_tv_ok.setText(numComplete ? getString(R.string.picture_done_front_num, 0, config.maxSelectNum)
                : getString(R.string.picture_please_select));
        if (!numComplete) {
            animation = AnimationUtils.loadAnimation(this, R.anim.modal_in);
        }
        animation = numComplete ? null : AnimationUtils.loadAnimation(this, R.anim.modal_in);
    }

    /**
     * 搜索本地媒体库
     */
    protected void readLocalMedia() {
        mediaLoader.loadAllMedia(new LocalMediaLoader.LocalMediaLoadListener() {
            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                Logger.i(TAG,"loadComplete:" + folders.size());
                if (folders.size() > 0) {
                    foldersList = folders;
                    LocalMediaFolder folder = folders.get(0);
                    folder.setChecked(true);
                    List<LocalMedia> localImg = folder.getImages();
                    // 这里解决有些机型会出现拍照完，相册列表不及时刷新问题
                    // 因为onActivityResult里手动添加拍照后的照片，
                    // 如果查询出来的图片大于或等于当前adapter集合的图片则取更新后的，否则就取本地的
                    if (localImg.size() >= images.size()) {
                        images = localImg;
                        folderWindow.bindFolder(folders);
                    }
                }
                if (adapter != null) {
                    if (images == null) {
                        images = new ArrayList<>();
                    }
                    adapter.bindImagesData(images);
                    tv_empty.setVisibility(images.size() > 0
                            ? View.INVISIBLE : View.VISIBLE);
                }
                mHandler.sendEmptyMessage(DISMISS_DIALOG);
            }
        });
    }

    /**
     * 启动相机
     */
    public void startCamera() {
        // 防止快速点击，但是单独拍照不管
        if (!DoubleUtils.isFastDoubleClick() || config.camera) {
            switch (config.mimeType) {
                case PictureConfig.TYPE_ALL:
                    // 如果是全部类型下，单独拍照就默认图片 (因为单独拍照不会new此PopupWindow对象)
                    if (popupWindow != null) {
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        popupWindow.showAsDropDown(rl_picture_title);
                    } else {
                        startOpenCamera();
                    }
                    break;
                case PictureConfig.TYPE_IMAGE:
                    // 拍照
                    startOpenCamera();
                    break;
                case PictureConfig.TYPE_VIDEO:
                    // 录视频
                    startOpenCameraVideo();
                    break;
                case PictureConfig.TYPE_AUDIO:
                    // 录音
                    startOpenCameraAudio();
                    break;
            }
        }
    }

    /**
     * 拍照
     */
    public void startOpenCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            int type = config.mimeType == PictureConfig.TYPE_ALL ? PictureConfig.TYPE_IMAGE : config.mimeType;
            File cameraFile = PictureFileUtils.createCameraFile(this,
                    type,
                    outputCameraPath, config.suffixType);
            cameraPath = cameraFile.getAbsolutePath();
            Uri imageUri = parUri(cameraFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
        }
    }

    /**
     * 录视频
     */
    public void startOpenCameraVideo() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File cameraFile = PictureFileUtils.createCameraFile(this, config.mimeType ==
                            PictureConfig.TYPE_ALL ? PictureConfig.TYPE_VIDEO : config.mimeType,
                    outputCameraPath, config.suffixType);
            cameraPath = cameraFile.getAbsolutePath();
            Uri imageUri = parUri(cameraFile);
            Logger.i(TAG, "video second:" + config.recordVideoSecond);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, config.recordVideoSecond);
            cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, config.videoQuality);
            startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
        }
    }

    /**
     * 录音
     */
    public void startOpenCameraAudio() {
        rxPermissions.request(Manifest.permission.RECORD_AUDIO).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    Intent cameraIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                    if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                        File cameraFile = PictureFileUtils.createCameraFile
                                (PictureSelectorActivity.this, config.mimeType,
                                        outputCameraPath, config.suffixType);
                        cameraPath = cameraFile.getAbsolutePath();
                        Uri imageUri = parUri(cameraFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
                    }
                } else {
                    showToast(getString(R.string.picture_audio));
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

    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    private Uri parUri(File cameraFile) {
        Uri imageUri;
        String authority = getPackageName() + ".provider";//要和Manifest中Provider声明的authorities一致
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android7.0获取图片权限问题解决
            //通过FileProvider创建一个content类型的Uri
            //FileProvider 是一个特殊的 ContentProvider 的子类，它使用 content:// Uri 代替了 file:/// Uri. ，更便利而且安全的为另一个app分享文件
            imageUri = FileProvider.getUriForFile(mContext, authority, cameraFile);//uri生成方式改为FileProvider提供临时授权路径
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }

    //region===========点击事件==============
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.picture_left_back || id == R.id.picture_right) {
            if (folderWindow.isShowing()) {
                folderWindow.dismiss();
            } else {
                closeActivity();
            }
        }
        if (id == R.id.picture_title) {//点击文件夹标题
            if (folderWindow.isShowing()) {
                folderWindow.dismiss();
            } else {
                if (images != null && images.size() > 0) {
                    folderWindow.showAsDropDown(rl_picture_title);
                    List<LocalMedia> selectedImages = adapter.getSelectedImages();
                    folderWindow.notifyDataCheckedStatus(selectedImages);
                }
            }
        }

        if (id == R.id.picture_id_preview) {//图片预览
            List<LocalMedia> selectedImages = adapter.getSelectedImages();

            List<LocalMedia> medias = new ArrayList<>();
            for (LocalMedia media : selectedImages) {
                medias.add(media);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(PictureConfig.EXTRA_PREVIEW_SELECT_LIST, (Serializable) medias);
            bundle.putSerializable(PictureConfig.EXTRA_SELECT_LIST, (Serializable) selectedImages);
            bundle.putBoolean(PictureConfig.EXTRA_BOTTOM_PREVIEW, true);
            startActivity(PicturePreviewActivity.class, bundle, UCropMulti.REQUEST_MULTI_CROP);
            overridePendingTransition(R.anim.a5, 0);
        }

        if (id == R.id.id_ll_ok) {
            List<LocalMedia> images = adapter.getSelectedImages();//以adapter的selectedImages为准
            String pictureType = images.size() > 0 ? images.get(0).getPictureType() : "";
            // 如果设置了图片最小选择数量，则判断是否满足条件
            int size = images.size();
            boolean eqImg = pictureType.startsWith(PictureConfig.IMAGE);
            if (config.minSelectNum > 0 && config.selectionMode == PictureConfig.MULTIPLE) {
                if (size < config.minSelectNum) {
                    String str = eqImg ? getString(R.string.picture_min_img_num, config.minSelectNum)
                            : getString(R.string.picture_min_video_num, config.minSelectNum);
                    showToast(str);
                    return;
                }
            }
            if (config.enableCrop && eqImg) {
                // 是图片和选择压缩并且是多张，调用批量压缩
                ArrayList<String> medias = new ArrayList<>();
                for (LocalMedia media : images) {
                    medias.add(media.getPath());
                }
                startCrop(medias);
            } else if (config.isCompress && eqImg) {
                // 图片才压缩，视频不管
                compressImage(images);
            } else {
                onResult(images);
            }
        }
    }

    //endregion===========点击事件==============


    //  通过 Handler 更新 UI 上的组件状态
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (mediaPlayer != null) {
                    tv_musicTime.setText(DateUtils.timeParse(mediaPlayer.getCurrentPosition()));
                    musicSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    musicSeekBar.setMax(mediaPlayer.getDuration());
                    tv_musicTotal.setText(DateUtils.timeParse(mediaPlayer.getDuration()));
                    handler.postDelayed(runnable, 200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //region========音频相关=======================
    /**
     * 初始化音频播放组件
     *
     * @param path
     */
    private void initPlayer(String path) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            playAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放音频点击事件
     */
    public class audioOnClick implements View.OnClickListener {
        private String path;

        public audioOnClick(String path) {
            super();
            this.path = path;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_PlayPause) {
                playAudio();
            }
            if (id == R.id.tv_Stop) {
                tv_musicStatus.setText(getString(R.string.picture_stop_audio));
                tv_PlayPause.setText(getString(R.string.picture_play_audio));
                stop(path);
            }
            if (id == R.id.tv_Quit) {
                handler.removeCallbacks(runnable);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stop(path);
                    }
                }, 30);
                try {
                    if (audioDialog != null
                            && audioDialog.isShowing()) {
                        audioDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 播放音频
     */
    private void playAudio() {
        if (mediaPlayer != null) {
            musicSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            musicSeekBar.setMax(mediaPlayer.getDuration());
        }
        String ppStr = tv_PlayPause.getText().toString();
        if (ppStr.equals(getString(R.string.picture_play_audio))) {
            tv_PlayPause.setText(getString(R.string.picture_pause_audio));
            tv_musicStatus.setText(getString(R.string.picture_play_audio));
            playOrPause();
        } else {
            tv_PlayPause.setText(getString(R.string.picture_play_audio));
            tv_musicStatus.setText(getString(R.string.picture_pause_audio));
            playOrPause();
        }
        if (isPlayAudio == false) {
            handler.post(runnable);
            isPlayAudio = true;
        }
    }

    /**
     * 停止播放
     *
     * @param path
     */
    public void stop(String path) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 暂停播放
     */
    public void playOrPause() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion========音频相关=======================


    @Override
    public void onItemClick(String folderName, List<LocalMedia> images) {
        boolean camera = StringUtils.isCamera(folderName);
        camera = config.isCamera ? camera : false;
        adapter.setShowCamera(camera);
        picture_title.setText(folderName);
        adapter.bindImagesData(images);
        folderWindow.dismiss();
    }


    //region======PictureImageGridAdapter回调====================
    @Override
    public void onTakePhoto() {
        // 启动相机拍照,先判断手机是否有拍照权限
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    startCamera();
                } else {
                    showToast(getString(R.string.picture_camera));
                    if (config.camera) {
                        closeActivity();
                    }
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

    @Override
    public void onChange(List<LocalMedia> selectImages) {
        changeImageNumber(selectImages);
    }

    @Override
    public void onPictureClick(LocalMedia media, int position) {
        List<LocalMedia> images = adapter.getImages();
        startPreview(images, position);
    }





    /**
     * 开启图片浏览或者视频播放
     * @param previewImages
     * @param position
     */
    public void startPreview(List<LocalMedia> previewImages, int position) {
        LocalMedia media = previewImages.get(position);
        String pictureType = media.getPictureType();
        Bundle bundle = new Bundle();
        List<LocalMedia> result = new ArrayList<>();
        int mediaType = PictureMimeType.isPictureType(pictureType);
//        DebugUtil.i(TAG, "mediaType:" + mediaType);
        Logger.i(TAG,"mediaType:" + mediaType);
        switch (mediaType) {
            case PictureConfig.TYPE_IMAGE:
                // image
                if (config.selectionMode == PictureConfig.SINGLE) {
                    if (config.enableCrop) {
                        originalPath = media.getPath();
                        boolean gif = PictureMimeType.isGif(pictureType);
                        if (gif) {
                            result.add(media);
                            handlerResult(result);
                        } else {
                            startCrop(originalPath);
                        }
                    } else {
                        result.add(media);
                        handlerResult(result);
                    }
                } else {
                    List<LocalMedia> selectedImages = adapter.getSelectedImages();
                    ImagesObservable.getInstance().saveLocalMedia(previewImages);
                    bundle.putSerializable(PictureConfig.EXTRA_SELECT_LIST, (Serializable) selectedImages);
                    bundle.putInt(PictureConfig.EXTRA_POSITION, position);
                    startActivity(PicturePreviewActivity.class, bundle, UCropMulti.REQUEST_MULTI_CROP);
                    overridePendingTransition(R.anim.a5, 0);
                }
                break;
            case PictureConfig.TYPE_VIDEO:
                // video
                if (config.selectionMode == PictureConfig.SINGLE) {
                    result.add(media);
                    onResult(result);
                } else {
                    bundle.putString("video_path", media.getPath());
                    startActivity(PictureVideoPlayActivity.class, bundle);
                }
                break;
            case PictureConfig.TYPE_AUDIO:
                // audio
                if (config.selectionMode == PictureConfig.SINGLE) {
                    result.add(media);
                    onResult(result);
                } else {
                    audioDialog(media.getPath());
                }
                break;
        }
    }


    /**
     *  更新底部栏信息
     *
     * @param selectImages
     */
    public void changeImageNumber(List<LocalMedia> selectImages) {
        // 如果选择的视频没有预览功能
        String pictureType = selectImages.size() > 0
                ? selectImages.get(0).getPictureType() : "";
        if (config.mimeType == PictureMimeType.ofAudio()) {
            picture_id_preview.setVisibility(View.GONE);
        } else {
            boolean isVideo = PictureMimeType.isVideo(pictureType);
            boolean eqVideo = config.mimeType == PictureConfig.TYPE_VIDEO;
            picture_id_preview.setVisibility(isVideo || eqVideo ? View.GONE : View.VISIBLE);
        }
        boolean enable = selectImages.size() != 0;
        if (enable) {
            id_ll_ok.setEnabled(true);
            picture_id_preview.setEnabled(true);
            picture_id_preview.setSelected(true);
            picture_tv_ok.setSelected(true);
            if (numComplete) {
                picture_tv_ok.setText(getString
                        (R.string.picture_done_front_num, selectImages.size(), config.maxSelectNum));
            } else {
                if (!anim) {
                    picture_tv_img_num.startAnimation(animation);
                }
                picture_tv_img_num.setVisibility(View.VISIBLE);
                picture_tv_img_num.setText(selectImages.size() + "");
                picture_tv_ok.setText(getString(R.string.picture_completed));
                anim = false;
            }
        } else {
            id_ll_ok.setEnabled(false);
            picture_id_preview.setEnabled(false);
            picture_id_preview.setSelected(false);
            picture_tv_ok.setSelected(false);
            if (numComplete) {
                picture_tv_ok.setText(getString(R.string.picture_done_front_num, 0, config.maxSelectNum));
            } else {
                picture_tv_img_num.setVisibility(View.INVISIBLE);
                picture_tv_ok.setText(getString(R.string.picture_please_select));
            }
        }
    }


    /**
     * 播放音频
     *
     * @param path
     */
    private void audioDialog(final String path) {
        audioDialog = new CustomDialog(mContext,
                LinearLayout.LayoutParams.MATCH_PARENT, audioH
                ,
                R.layout.picture_audio_dialog, R.style.Theme_dialog);
        audioDialog.getWindow().setWindowAnimations(R.style.Dialog_Audio_StyleAnim);
        tv_musicStatus = (TextView) audioDialog.findViewById(R.id.tv_musicStatus);
        tv_musicTime = (TextView) audioDialog.findViewById(R.id.tv_musicTime);
        musicSeekBar = (SeekBar) audioDialog.findViewById(R.id.musicSeekBar);
        tv_musicTotal = (TextView) audioDialog.findViewById(R.id.tv_musicTotal);
        tv_PlayPause = (TextView) audioDialog.findViewById(R.id.tv_PlayPause);
        tv_Stop = (TextView) audioDialog.findViewById(R.id.tv_Stop);
        tv_Quit = (TextView) audioDialog.findViewById(R.id.tv_Quit);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initPlayer(path);
            }
        }, 30);
        tv_PlayPause.setOnClickListener(new audioOnClick(path));
        tv_Stop.setOnClickListener(new audioOnClick(path));
        tv_Quit.setOnClickListener(new audioOnClick(path));
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        audioDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stop(path);
                    }
                }, 30);
                try {
                    if (audioDialog != null
                            && audioDialog.isShowing()) {
                        audioDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        handler.post(runnable);
        audioDialog.show();
    }

    //endregion======PictureImageGridAdapter回调====================


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            List<LocalMedia> medias = new ArrayList<>();
            LocalMedia media;
            String imageType;
            switch (requestCode) {
                case UCrop.REQUEST_CROP:
                    Uri resultUri = UCrop.getOutput(data);
                    String cutPath = resultUri.getPath();
                    media = new LocalMedia(originalPath, 0, false, 0, 0, config.mimeType);
                    media.setCutPath(cutPath);
                    media.setCut(true);
                    imageType = PictureMimeType.createImageType(cutPath);
                    media.setPictureType(imageType);
                    medias.add(media);
                    Logger.i(TAG, "cut createImageType:" + imageType);
                    handlerResult(medias);
                    break;
                case UCropMulti.REQUEST_MULTI_CROP:
                    List<CutInfo> cuts = UCropMulti.getOutput(data);
                    for (CutInfo c : cuts) {
                        media = new LocalMedia();
                        imageType = PictureMimeType.createImageType(c.getPath());
                        media.setCut(true);
                        media.setPath(c.getPath());
                        media.setCutPath(c.getCutPath());
                        media.setPictureType(imageType);
                        media.setMimeType(config.mimeType);
                        medias.add(media);
                    }
                    handlerResult(medias);
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    isAudio(data);
                    // on take photo success
                    final File file = new File(cameraPath);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));//作用,因为设置了FileProvider，共享拍照所得的照片，发送重新扫描资源库的广播，使的媒体库刷新
                    String toType = PictureMimeType.fileToType(file);
                    Logger.i(TAG, "camera result:" + toType);
                    if (config.mimeType != PictureMimeType.ofAudio()) {
                        int degree = PictureFileUtils.readPictureDegree(file.getAbsolutePath());
                        rotateImage(degree, file);
                    }
                    // 生成新拍照片或视频对象
                    media = new LocalMedia();
                    media.setPath(cameraPath);

                    boolean eqVideo = toType.startsWith(PictureConfig.VIDEO);
                    int duration = eqVideo ? PictureMimeType.getLocalVideoDuration(cameraPath) : 0;
                    String pictureType = "";
                    if (config.mimeType == PictureMimeType.ofAudio()) {
                        pictureType = "audio/mpeg";
                        duration = PictureMimeType.getLocalVideoDuration(cameraPath);
                    } else {
                        pictureType = eqVideo ? PictureMimeType.createVideoType(cameraPath)
                                : PictureMimeType.createImageType(cameraPath);
                    }
                    media.setPictureType(pictureType);
                    media.setDuration(duration);
                    media.setMimeType(config.mimeType);

                    // 因为加入了单独拍照功能，所有如果是单独拍照的话也默认为单选状态
                    if (config.selectionMode == PictureConfig.SINGLE || config.camera) {
                        // 如果是单选 拍照后直接返回
                        boolean eqImg = toType.startsWith(PictureConfig.IMAGE);
                        if (config.enableCrop && eqImg) {
                            // 去裁剪
                            originalPath = cameraPath;
                            startCrop(cameraPath);
                        } else if (config.isCompress && eqImg) {
                            // 去压缩
                            medias.add(media);
                            compressImage(medias);
                            if (adapter != null) {
                                images.add(0, media);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            // 不裁剪 不压缩 直接返回结果
                            medias.add(media);
                            onResult(medias);
                        }
                    } else {
                        // 多选 返回列表并选中当前拍照的
                        images.add(0, media);
                        if (adapter != null) {
                            List<LocalMedia> selectedImages = adapter.getSelectedImages();
                            // 没有到最大选择量 才做默认选中刚拍好的
                            if (selectedImages.size() < config.maxSelectNum) {
                                pictureType = selectedImages.size() > 0 ? selectedImages.get(0).getPictureType() : "";
                                boolean toEqual = PictureMimeType.mimeToEqual(pictureType, media.getPictureType());
                                // 类型相同或还没有选中才加进选中集合中
                                if (toEqual || selectedImages.size() == 0) {
                                    if (selectedImages.size() < config.maxSelectNum) {
                                        selectedImages.add(media);
                                        adapter.bindSelectImages(selectedImages);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                    if (adapter != null) {
                        // 解决部分手机拍照完Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                        // 不及时刷新问题手动添加
                        manualSaveFolder(media);
                        tv_empty.setVisibility(images.size() > 0
                                ? View.INVISIBLE : View.VISIBLE);
                    }

                    if (config.mimeType != PictureMimeType.ofAudio()) {
                        int lastImageId = getLastImageId(eqVideo);
                        if (lastImageId != -1) {
                            removeImage(lastImageId, eqVideo);
                        }
                    }
                    break;
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            if (config.camera) {
                closeActivity();
            }
        }
        else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable throwable = (Throwable) data.getSerializableExtra(UCrop.EXTRA_ERROR);
            showToast(throwable.getMessage());
        }
    }


    /**
     * 手动添加拍照后的相片到图片列表，并设为选中
     *
     * @param media
     */
    private void manualSaveFolder(LocalMedia media) {
        try {
            createNewFolder(foldersList);
            LocalMediaFolder folder = getImageFolder(media.getPath(), foldersList);
            LocalMediaFolder cameraFolder = foldersList.size() > 0 ? foldersList.get(0) : null;
            if (cameraFolder != null && folder != null) {
                // 相机胶卷
                cameraFolder.setFirstImagePath(media.getPath());
                cameraFolder.setImages(images);
                cameraFolder.setImageNum(cameraFolder.getImageNum() + 1);
                // 存储新拍照图片的文件夹
                int num = folder.getImageNum() + 1;
                folder.setImageNum(num);
                folder.getImages().add(0, media);
                folder.setFirstImagePath(cameraPath);
                folderWindow.bindFolder(foldersList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().unregister(this);
        }
        ImagesObservable.getInstance().clearLocalMedia();
        if (animation != null) {
            animation.cancel();
            animation = null;
        }
        if (mediaPlayer != null && handler != null) {
            handler.removeCallbacks(runnable);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                // 拍照
                startOpenCamera();
                break;
            case 1:
                // 录视频
                startOpenCameraVideo();
                break;
        }
    }
}
