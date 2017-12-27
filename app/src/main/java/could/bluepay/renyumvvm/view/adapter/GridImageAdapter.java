package could.bluepay.renyumvvm.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.common.entity.LocalMedia;
import could.bluepay.renyumvvm.common.config.PictureConfig;
import could.bluepay.renyumvvm.common.config.PictureMimeType;
import could.bluepay.renyumvvm.databinding.GvFilterImageBinding;
import could.bluepay.renyumvvm.utils.pictureSelector.DateUtils;
import could.bluepay.renyumvvm.utils.pictureSelector.StringUtils;
import could.bluepay.renyumvvm.view.adapter.bindingAdapter.BindingViewHolder;

/**
 * 选中图片GridView的Adapter
 */

public class GridImageAdapter extends RecyclerView.Adapter<BindingViewHolder> {

    public static final int TYPE_CAMERA = 1;//进入相机／相册
    public static final int TYPE_PICTURE = 2;//展示图片

    private LayoutInflater mInflater;
    private List<LocalMedia> list = new ArrayList<>();
    private int selectMax = 9;
    private Context context;

    private OnAddPicClickListener listener;
    public interface OnAddPicClickListener{
        void onAddPicClick();
    }


    public GridImageAdapter(Context context,OnAddPicClickListener listener) {
        this.context = context;
        this.listener = listener;
        mInflater = LayoutInflater.from(context);
    }
    public GridImageAdapter setSelectMax(int maxSize){
        selectMax = maxSize;
        return this;
    }

    public GridImageAdapter setDataList(List<LocalMedia> list){
        if(list == null){
            this.list = new ArrayList<>();
        }else {
            this.list = list;
        }
        return this;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.gv_filter_image,null);

        return new BindingViewHolder(DataBindingUtil.inflate(mInflater,R.layout.gv_filter_image,parent,false,null));
    }

    @Override
    public int getItemViewType(int position) {
        if(isShowAddItem(position)){
            return TYPE_CAMERA;
        }else{
            return TYPE_PICTURE;
        }
    }

    @Override
    public void onBindViewHolder(final BindingViewHolder holder, int position) {
        if(holder.binding instanceof GvFilterImageBinding){
            GvFilterImageBinding gvFilterImageBinding = ((GvFilterImageBinding)(holder.binding));
            if(getItemViewType(position) == TYPE_CAMERA){
                gvFilterImageBinding.fiv.setImageResource(R.drawable.addimg_1x);
                if(listener!=null) {
                    gvFilterImageBinding.fiv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onAddPicClick();
                        }
                    });
                }
                gvFilterImageBinding.llDel.setVisibility(View.INVISIBLE);
            }else{
                gvFilterImageBinding.llDel.setVisibility(View.VISIBLE);
                gvFilterImageBinding.llDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = holder.getAdapterPosition();
                        if(index!=RecyclerView.NO_POSITION){
                            list.remove(index);
                            notifyItemRemoved(index);
                            notifyItemRangeChanged(index,list.size());
                        }

                    }
                });
                LocalMedia media = list.get(position);
                int mimeType = media.getMimeType();
                String path = "";
                if (media.isCut() && !media.isCompressed()) {
                    // 裁剪过
                    path = media.getCutPath();
                } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                    // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                    path = media.getCompressPath();
                } else {
                    // 原图
                    path = media.getPath();
                }
                // 图片
                if (media.isCompressed()) {
                    Log.i("compress image result:", new File(media.getCompressPath()).length() / 1024 + "k");
                    Log.i("压缩地址::", media.getCompressPath());
                }

                Log.i("原图地址::", media.getPath());
                int pictureType = PictureMimeType.isPictureType(media.getPictureType());
                if (media.isCut()) {
                    Log.i("裁剪地址::", media.getCutPath());
                }
                long duration = media.getDuration();
                gvFilterImageBinding.tvDuration.setVisibility(pictureType == PictureConfig.TYPE_VIDEO
                        ? View.VISIBLE : View.GONE);
                if (mimeType == PictureMimeType.ofAudio()) {
                    gvFilterImageBinding.tvDuration.setVisibility(View.VISIBLE);
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.picture_audio);
                    StringUtils.modifyTextViewDrawable(gvFilterImageBinding.tvDuration, drawable, 0);
                } else {
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.video_icon);
                    StringUtils.modifyTextViewDrawable(gvFilterImageBinding.tvDuration, drawable, 0);
                }
                gvFilterImageBinding.tvDuration.setText(DateUtils.timeParse(duration));
                if (mimeType == PictureMimeType.ofAudio()) {
                    gvFilterImageBinding.fiv.setImageResource(R.drawable.audio_placeholder);
                } else {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.color.color_f6)
                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(gvFilterImageBinding.getRoot().getContext())
                            .load(path)
                            .apply(options)
                            .into(gvFilterImageBinding.fiv);
                }
                //itemView 的点击事件
                if (mItemClickListener != null) {
                    gvFilterImageBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int adapterPosition = holder.getAdapterPosition();
                            mItemClickListener.onItemClick(adapterPosition, v);
                        }
                    });
                }

            }
        }

    }

    private boolean isShowAddItem(int position){
        int size = list.size() == 0?0:list.size();
        return position == size;
    }

    @Override
    public int getItemCount() {
        if(list.size()<selectMax){
            return list.size()+1;
        }
        return list.size();
    }

    private onItemClickListener mItemClickListener;
    protected interface onItemClickListener{
        void onItemClick(int position,View view);
    }
    private void setItemClickListener(onItemClickListener listener){
        this.mItemClickListener = listener;
    }

//    public class ViewHolder extends RecyclerView.ViewHolder{
//        ImageView mImg;
//        LinearLayout ll_del;
//        TextView tv_duration;
//
//        public ViewHolder(View view) {
//            super(view);
//            mImg = (ImageView) view.findViewById(R.id.fiv);
//            ll_del = (LinearLayout) view.findViewById(R.id.ll_del);
//            tv_duration = (TextView) view.findViewById(R.id.tv_duration);
//        }
//
//    }

}
