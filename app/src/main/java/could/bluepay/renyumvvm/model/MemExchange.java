package could.bluepay.renyumvvm.model;

import android.databinding.ObservableArrayList;

import java.util.ArrayList;
import java.util.List;

import could.bluepay.renyumvvm.http.bean.UserBeanItem;
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

    //region========Focus流行相关==============
    public int popularListPage = 0;
    public List<UserBeanItem> popularList;
    public List<UserBeanItem> getPopularList(){
        if(popularList == null){
            popularList = new ArrayList<>();
        }
        return popularList;
    }
    public int getPopularListPage(){
        return popularListPage;
    }
    public void setPopularList(List<UserBeanItem> popularList,int page) {
        if(page == 1){
            getPopularList().clear();
            getPopularList().addAll(popularList);
        }else{
            getPopularList().addAll(popularList);
        }
        this.popularListPage = page;
    }

    //endregion========Focus流行相关==============


    //region=======Focus关注相关==========
    public int focusListPage = 0;
    public List<UserBeanItem> focusList;
    public List<UserBeanItem> getFocusList(){
        if(focusList == null){
            focusList = new ArrayList<>();
        }
        return focusList;
    }
    public int getFocusListPage(){
        return focusListPage;
    }
    public void setFocusList(List<UserBeanItem> focusList,int page) {
        if(page == 1){
            getFocusList().clear();
            getFocusList().addAll(focusList);
        }else{
            getFocusList().addAll(focusList);
        }
        this.focusListPage = page;
    }
    //endregion=======Focus关注相关==========



    //region==========动态==============
    private int weiboBeanListPage = 0;
    public ArrayList<WeiboBean> weiboBeanList ;
    public ArrayList<WeiboBean> getWeiboBeanList(){
        if(weiboBeanList == null){
            weiboBeanList = new ArrayList<>();
        }
        return weiboBeanList;
    }
    public void setWeiboBeanList(ArrayList<WeiboBean> list,int page){
        weiboBeanListPage = page;
        if(page == 1){
            getWeiboBeanList().clear();
            getWeiboBeanList().addAll(list);
        }else{
            getWeiboBeanList().addAll(list);
        }
    }
    public int getWeiboBeanListPage(){
        return weiboBeanListPage;
    }
    public void clearWeiboData(){
        getWeiboBeanList().clear();
        weiboBeanListPage = 0;
    }
    //region==========动态==============


    public void clear(){
        weiboBeanListPage = 0;
        focusListPage = 0;
        popularListPage = 0;
        if(weiboBeanList!=null) {
            weiboBeanList.clear();
            focusList.clear();
            popularList.clear();
            weiboBeanList = null;
            focusList = null;
            popularList = null;
        }

    }
}
