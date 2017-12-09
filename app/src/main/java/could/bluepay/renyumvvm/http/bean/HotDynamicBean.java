package could.bluepay.renyumvvm.http.bean;


import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import java.util.List;

import could.yuanqiang.http.ParamNames;

/**
 * 获取写真的response Bean
 */

public class HotDynamicBean extends BaseBean{

    @ParamNames("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @ParamNames("count")
        private int count;
        @ParamNames("usercount")
        private int usercount;
        @ParamNames("myinfo")
        private UserBeanItem myinfo;
        @ParamNames("weibo")
        private ObservableArrayList<WeiboBean> weibo;
        @ParamNames("user")
        private List<UserBeanItem> user;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getUsercount() {
            return usercount;
        }

        public void setUsercount(int usercount) {
            this.usercount = usercount;
        }

        public UserBeanItem getMyinfo() {
            return myinfo;
        }

        public void setMyinfo(UserBeanItem myinfo) {
            this.myinfo = myinfo;
        }

        public ObservableArrayList<WeiboBean> getWeibo() {
            return weibo;
        }

        public void setWeibo(ObservableArrayList<WeiboBean> weibo) {
            this.weibo = weibo;
        }

        public List<UserBeanItem> getUser() {
            return user;
        }

        public void setUser(List<UserBeanItem> user) {
            this.user = user;
        }
    }
}
