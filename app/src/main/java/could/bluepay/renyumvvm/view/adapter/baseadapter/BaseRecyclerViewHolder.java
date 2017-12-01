package could.bluepay.renyumvvm.view.adapter.baseadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by bluepay on 2017/11/27.
 */

public abstract class BaseRecyclerViewHolder<T, D extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public D binding;

    public BaseRecyclerViewHolder(ViewGroup viewGroup, int layoutId) {
        super(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),layoutId,null,false).getRoot());

        binding = DataBindingUtil.getBinding(this.itemView);
    }


    public abstract void onBindViewHolder(T object,final int position);

    /**
     * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
     */
    void onBaseBindViewHolder(T object,final int position){
        onBindViewHolder(object,position);
        binding.setVariable(BR.bean,object);
        binding.executePendingBindings();
    }

}
