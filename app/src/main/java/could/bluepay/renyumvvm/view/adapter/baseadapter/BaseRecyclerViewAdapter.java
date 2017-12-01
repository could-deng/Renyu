package could.bluepay.renyumvvm.view.adapter.baseadapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bluepay on 2017/11/27.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected List<T> data = new ArrayList<>();
    protected OnItemClickListener<T> listener;
    protected OnItemLongClickListener<T> longClickListener;
    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.onBaseBindViewHolder(data.get(position),position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void addAll(List<T> datas){
        data.addAll(datas);
    }
    public void add(T object){
        data.add(object);
    }
    public void remove(T object){
        data.remove(object);
    }
    public void removeAll(){
        data.clear();
    }
    public void setOnItemClickListner(OnItemClickListener<T> listener){
        this.listener = listener;
    }
    public List<T> getData(){
        return data;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener<T> listener){
        longClickListener = listener;
    }
}
