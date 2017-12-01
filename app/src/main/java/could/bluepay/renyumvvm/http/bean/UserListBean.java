package could.bluepay.renyumvvm.http.bean;

import java.io.Serializable;
import java.util.List;
import could.yuanqiang.http.ParamNames;

/**
 * 获取用户列表的 Bean
 */
public class UserListBean extends BaseBean{

    /**
     * errno : 200
     * errmsg :
     * data : {"usercount":468,"user":[{"qid":2567,"phone":19777830299,"nick_name":"麦苹果","height":170,"weight":48,"photo":"http://1.ry.no17.cn/1493291982922.jpg","sexy":2,"birthdate":"1995-06-29","insert_time":1480409731,"update_time":"2017-05-24 18:00:40","ip":"218.30.116.9","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":null,"shot_endtime":null,"city":null,"goodat":null,"fdcount":476,"fcount":1},{"qid":55,"phone":13777830210,"nick_name":"sugar小甜心","height":177,"weight":57,"photo":"http://1.ry.no17.cn/1491033930832.jpg","sexy":2,"birthdate":"1991-09-25","insert_time":1365586964,"update_time":"2017-05-23 12:52:11","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":"0000-00-00","shot_endtime":"0000-00-00","city":"","goodat":"","fdcount":1069,"fcount":3},{"qid":56,"phone":13777830211,"nick_name":"yumi_尤美","height":168,"weight":50,"photo":"http://1.ry.no17.cn/1492652767622.jpg","sexy":2,"birthdate":"1991-10-15","insert_time":1365586964,"update_time":"2017-05-24 18:02:07","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":"0000-00-00","shot_endtime":"0000-00-00","city":"","goodat":"","fdcount":1047,"fcount":4},{"qid":62,"phone":13777830217,"nick_name":"娜露","height":177,"weight":67,"photo":"http://1.ry.no17.cn/1494838954700.jpg","sexy":2,"birthdate":"1997-03-03","insert_time":1365586964,"update_time":"2017-05-24 18:02:17","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":null,"shot_endtime":null,"city":null,"goodat":null,"fdcount":907,"fcount":4},{"qid":73,"phone":19777830210,"nick_name":"沈佳熹","height":162,"weight":44,"photo":"http://1.ry.no17.cn/1492767916653.jpg","sexy":2,"birthdate":"1990-12-10","insert_time":1365586964,"update_time":"2017-05-23 12:53:08","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"2016-04-15 14:23:16","shot":0,"shot_starttime":"0000-00-00","shot_endtime":"0000-00-00","city":"","goodat":"","fdcount":871,"fcount":2},{"qid":84,"phone":19777830222,"nick_name":"刘飞儿","height":165,"weight":47,"photo":"http://1.ry.no17.cn/1491033641935.jpg","sexy":2,"birthdate":"1993-10-10","insert_time":1365586964,"update_time":"2017-05-24 13:15:22","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":null,"shot_endtime":null,"city":null,"goodat":null,"fdcount":748,"fcount":1},{"qid":91,"phone":19777830229,"nick_name":"夏美酱","height":171,"weight":43,"photo":"http://1.ry.no17.cn/1492768685176.jpg","sexy":2,"birthdate":"1996-11-28","insert_time":1365586964,"update_time":"2017-05-23 12:51:50","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":"0000-00-00","shot_endtime":"0000-00-00","city":"","goodat":"","fdcount":1318,"fcount":3},{"qid":93,"phone":19777830230,"nick_name":"熊吖BOBO","height":168,"weight":40,"photo":"http://1.ry.no17.cn/1490943818964.jpg","sexy":2,"birthdate":"1997-05-01","insert_time":1365586964,"update_time":"2017-05-23 12:52:54","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":null,"shot_endtime":null,"city":null,"goodat":null,"fdcount":581,"fcount":3},{"qid":2558,"phone":19777830290,"nick_name":"Cheryl青树","height":165,"weight":43,"photo":"http://1.ry.no17.cn/1492765764906.jpg","sexy":2,"birthdate":"1995-02-14","insert_time":1480409730,"update_time":"2017-05-23 12:53:30","ip":"218.30.116.9","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":"0000-00-00","shot_endtime":"0000-00-00","city":"","goodat":"","fdcount":387,"fcount":0},{"qid":22552,"phone":19777830499,"nick_name":"金鑫","height":169,"weight":73,"photo":"http://1.ry.no17.cn/1492961494656.jpg","sexy":2,"birthdate":"1998-01-21","insert_time":1487568521,"update_time":"2017-05-24 18:03:08","ip":"114.242.250.98","verified":0,"recommoned":1,"rs":567,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":null,"shot_endtime":null,"city":null,"goodat":null,"fdcount":406,"fcount":0}]}
     */
    @ParamNames("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * usercount : 468
         * user : [{"qid":2567,"phone":19777830299,"nick_name":"麦苹果","height":170,"weight":48,"photo":"http://1.ry.no17.cn/1493291982922.jpg","sexy":2,"birthdate":"1995-06-29","insert_time":1480409731,"update_time":"2017-05-24 18:00:40","ip":"218.30.116.9","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":null,"shot_endtime":null,"city":null,"goodat":null,"fdcount":476,"fcount":1},{"qid":55,"phone":13777830210,"nick_name":"sugar小甜心","height":177,"weight":57,"photo":"http://1.ry.no17.cn/1491033930832.jpg","sexy":2,"birthdate":"1991-09-25","insert_time":1365586964,"update_time":"2017-05-23 12:52:11","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":"0000-00-00","shot_endtime":"0000-00-00","city":"","goodat":"","fdcount":1069,"fcount":3},{"qid":56,"phone":13777830211,"nick_name":"yumi_尤美","height":168,"weight":50,"photo":"http://1.ry.no17.cn/1492652767622.jpg","sexy":2,"birthdate":"1991-10-15","insert_time":1365586964,"update_time":"2017-05-24 18:02:07","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":"0000-00-00","shot_endtime":"0000-00-00","city":"","goodat":"","fdcount":1047,"fcount":4},{"qid":62,"phone":13777830217,"nick_name":"娜露","height":177,"weight":67,"photo":"http://1.ry.no17.cn/1494838954700.jpg","sexy":2,"birthdate":"1997-03-03","insert_time":1365586964,"update_time":"2017-05-24 18:02:17","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":null,"shot_endtime":null,"city":null,"goodat":null,"fdcount":907,"fcount":4},{"qid":73,"phone":19777830210,"nick_name":"沈佳熹","height":162,"weight":44,"photo":"http://1.ry.no17.cn/1492767916653.jpg","sexy":2,"birthdate":"1990-12-10","insert_time":1365586964,"update_time":"2017-05-23 12:53:08","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"2016-04-15 14:23:16","shot":0,"shot_starttime":"0000-00-00","shot_endtime":"0000-00-00","city":"","goodat":"","fdcount":871,"fcount":2},{"qid":84,"phone":19777830222,"nick_name":"刘飞儿","height":165,"weight":47,"photo":"http://1.ry.no17.cn/1491033641935.jpg","sexy":2,"birthdate":"1993-10-10","insert_time":1365586964,"update_time":"2017-05-24 13:15:22","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":null,"shot_endtime":null,"city":null,"goodat":null,"fdcount":748,"fcount":1},{"qid":91,"phone":19777830229,"nick_name":"夏美酱","height":171,"weight":43,"photo":"http://1.ry.no17.cn/1492768685176.jpg","sexy":2,"birthdate":"1996-11-28","insert_time":1365586964,"update_time":"2017-05-23 12:51:50","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":"0000-00-00","shot_endtime":"0000-00-00","city":"","goodat":"","fdcount":1318,"fcount":3},{"qid":93,"phone":19777830230,"nick_name":"熊吖BOBO","height":168,"weight":40,"photo":"http://1.ry.no17.cn/1490943818964.jpg","sexy":2,"birthdate":"1997-05-01","insert_time":1365586964,"update_time":"2017-05-23 12:52:54","ip":"","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":null,"shot_endtime":null,"city":null,"goodat":null,"fdcount":581,"fcount":3},{"qid":2558,"phone":19777830290,"nick_name":"Cheryl青树","height":165,"weight":43,"photo":"http://1.ry.no17.cn/1492765764906.jpg","sexy":2,"birthdate":"1995-02-14","insert_time":1480409730,"update_time":"2017-05-23 12:53:30","ip":"218.30.116.9","verified":0,"recommoned":1,"rs":568,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":"0000-00-00","shot_endtime":"0000-00-00","city":"","goodat":"","fdcount":387,"fcount":0},{"qid":22552,"phone":19777830499,"nick_name":"金鑫","height":169,"weight":73,"photo":"http://1.ry.no17.cn/1492961494656.jpg","sexy":2,"birthdate":"1998-01-21","insert_time":1487568521,"update_time":"2017-05-24 18:03:08","ip":"114.242.250.98","verified":0,"recommoned":1,"rs":567,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"shot_starttime":null,"shot_endtime":null,"city":null,"goodat":null,"fdcount":406,"fcount":0}]
         */
        @ParamNames("usercount")
        private int usercount;
        @ParamNames("user")
        private List<UserBeanItem> user;

        public int getUsercount() {
            return usercount;
        }

        public void setUsercount(int usercount) {
            this.usercount = usercount;
        }

        public List<UserBeanItem> getUser() {
            return user;
        }

        public void setUser(List<UserBeanItem> user) {
            this.user = user;
        }

    }

    @Override
    public String toString() {
        String r = "";
        if(data == null){
            return r;
        }

        return (data.getUser() == null)?"size,null":("size="+data.getUser().size());
    }
}
