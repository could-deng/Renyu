package could.bluepay.renyumvvm.view.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.FootItemUserlistBinding;
import could.bluepay.renyumvvm.databinding.HeaderItemUserlistBinding;
import could.bluepay.renyumvvm.databinding.ItemLvHotAndNewBinding;
import could.bluepay.renyumvvm.http.bean.UserBeanItem;
import could.bluepay.renyumvvm.utils.PerfectClickListener;

/**
 * Created by bluepay on 2017/11/23.
 */

public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Activity context;

    private int status = 1;//footview状态标示位
    public static final int LOAD_MORE = 0;//正在加载更多
    public static final int LOAD_PULL_TO = 1;//上拉加载更多
    public static final int LOAD_NONE = 2;//无更多内容
    public static final int LOAD_END = 3;//

    private static final int TYPE_TOP = -1;

    protected static final int TYPE_FOOTER_BOOK = -2;
    protected static final int TYPE_HEADER_BOOK = -3;
    protected static final int TYPE_CONTENT_BOOK = -4;

    private List<UserBeanItem> userData;

    public UserListAdapter(Activity context) {
        this.context = context;
        this.userData = new ArrayList<>();
    }

    public void setList(List<UserBeanItem> userData) {
        this.userData = userData;
        notifyDataSetChanged();
    }
    public void addList(List<UserBeanItem> userData){
        this.userData.addAll(userData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEADER_BOOK;
        }else if(position+1 == getItemCount()){
            return TYPE_FOOTER_BOOK;
        }else{
            return TYPE_CONTENT_BOOK;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER_BOOK:
                HeaderItemUserlistBinding mBindHeader =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.header_item_userlist,parent,false);
                return new HeaderViewHolder(mBindHeader.getRoot());

            case TYPE_FOOTER_BOOK:
                FootItemUserlistBinding mBindFooter = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.foot_item_userlist,parent,false);
                return new FootViewHolder(mBindFooter.getRoot());

            case TYPE_CONTENT_BOOK:
                ItemLvHotAndNewBinding mBindContent =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_lv_hot_and_new,parent,false);
                return new UserViewHolder(mBindContent.getRoot());
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder){
            ((HeaderViewHolder)holder).bindItem();

        }else if(holder instanceof FootViewHolder){
            ((FootViewHolder)holder).bindItem();

        }else if(holder instanceof UserViewHolder){
            if(userData!=null && userData.size()>0) {
                UserBeanItem item = userData.get(position-1);
                ((UserViewHolder) holder).bindItem(item);
            }
        }
    }

    @Override
    public int getItemCount() {
        return userData.size() +2;
    }


    /**
     * 处理LinearLayoutManager 添加头尾布局占满屏幕宽的情况
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof LinearLayoutManager){
            LinearLayoutManager llayoutManager = ((LinearLayoutManager)manager);
//            llayoutManager.se
        }

    }

    /**
     * 处理
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

    }

//    private boolean isHeader(int position){
//        if(position)
//    }



    //region===============头与尾部的ViewHolder================

    public void updateLoadStatus(int status) {
        this.status = status;
        notifyDataSetChanged();
    }

    public int getLoadStatus(){
        return this.status;
    }


    protected class FootViewHolder extends RecyclerView.ViewHolder{
        FootItemUserlistBinding mBinding;
        public FootViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.getBinding(itemView);
            mBinding.rlMore.setGravity(Gravity.CENTER);

        }
        private void bindItem(){
            switch (status){
                case LOAD_MORE:
                    mBinding.progress.setVisibility(View.VISIBLE);
                    mBinding.tvLoadPrompt.setText(context.getResources().getString(R.string.load_more));

                    break;
                case LOAD_PULL_TO:
                    mBinding.progress.setVisibility(View.VISIBLE);
                    mBinding.tvLoadPrompt.setText(context.getResources().getString(R.string.load_pull_to));
                    break;
                case LOAD_END:
                    mBinding.progress.setVisibility(View.VISIBLE);
                    mBinding.tvLoadPrompt.setText(context.getResources().getString(R.string.load_none));
                    break;
                case LOAD_NONE:
                    itemView.setVisibility(View.GONE);
                    break;
            }
        }
    }
    protected class HeaderViewHolder extends RecyclerView.ViewHolder{
        HeaderItemUserlistBinding mBinding;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.getBinding(itemView);
        }
        private void bindItem() {
//            mBindBook.setBean(book);
//            mBindBook.executePendingBindings();
        }
    }

    protected class UserViewHolder extends RecyclerView.ViewHolder{
        ItemLvHotAndNewBinding mBinding;
        public UserViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.getBinding(itemView);
        }

        private void bindItem(UserBeanItem item){
            mBinding.setBean(item);
            mBinding.executePendingBindings();//立即更新内容,如果不设置,需要有段时间
            mBinding.imPhotoBg.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onDoubleClick(View view) {
                    // TODO: 2017/11/24 点击事件
                }
            });
        }
    }

    //endregion===============头与尾部的ViewHolder================



}
