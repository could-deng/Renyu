package could.bluepay.renyumvvm.view.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import could.bluepay.renyumvvm.R;
import could.bluepay.renyumvvm.databinding.FootItemUserlistBinding;
import could.bluepay.renyumvvm.databinding.HeaderItemUserlistBinding;
import could.bluepay.renyumvvm.databinding.ItemLvHotAndNewBinding;

/**
 * Focus列表适配器
 */

public class UserListFocusAdapter extends UserListAdapter {

    public UserListFocusAdapter(Activity context) {
        super(context);
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
}
