package could.bluepay.renyumvvm.view.adapter.bindingAdapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * DynamicBindingAdapter适配器代理RecyclerView.ViewHolder
 */

public class BindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public T binding;

    public BindingViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }


}
