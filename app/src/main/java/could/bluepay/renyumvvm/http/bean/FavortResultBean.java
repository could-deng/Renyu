package could.bluepay.renyumvvm.http.bean;

import could.yuanqiang.http.ParamNames;

/**
 * 点赞后的response Bean
 */

/**
 * //    {"errno":200,"errmsg":"\u70b9\u8d5e\u6210\u529f","data":{"id":19,"pid":12,"qid":1,"nick_name":"\u4e8c\u86cb","like_time":"2017-12-01 11:27:06"}}
 */
public class FavortResultBean extends BaseBean {

    @ParamNames("data")
    private FavortItem data;

    public FavortItem getData() {
        return data;
    }

    public void setData(FavortItem data) {
        this.data = data;
    }
}
