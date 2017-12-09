package could.bluepay.renyumvvm.model;

import android.databinding.ObservableArrayList;

import could.bluepay.renyumvvm.http.bean.WeiboBean;

/**
 * Created by bluepay on 2017/12/8.
 */

public class MemExchange {

    public MemExchange() {
        clear();
    }

    public static MemExchange instance;
    public static MemExchange getInstance(){
        if(instance == null){
            instance = new MemExchange();
        }
        return instance;
    }
    public ObservableArrayList<WeiboBean> weiboBeanList ;
    public ObservableArrayList<WeiboBean> getWeiboBeanList(){
        if(weiboBeanList == null){
            weiboBeanList = new ObservableArrayList<>();
        }
        return weiboBeanList;
    }
    public void setWeiboBeanList(ObservableArrayList<WeiboBean> list,int page){

        if(page == 1){
            getWeiboBeanList().clear();
            getWeiboBeanList().addAll(list);
        }else{
            getWeiboBeanList().addAll(list);
        }

    }
    public void clear(){
        if(weiboBeanList!=null) {
            weiboBeanList.clear();
            weiboBeanList = null;
        }

    }
}
