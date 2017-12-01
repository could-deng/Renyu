package could.bluepay.renyumvvm.http.bean;

import could.yuanqiang.http.ParamNames;

/**
 * 单个用户item
 */

public class UserBeanItem {
    /**
     * qid : 24753
     * phone : 19777830667
     * nick_name : 人鱼精选
     * height : 188
     * weight : 62
     * photo : http://1.ry.no17.cn/1490364631283.jpg
     * sexy : 2
     * birthdate : 1989-02-14
     * insert_time : 1489128216
     * update_time : 2017-04-01 10:10:07
     * ip : 114.242.248.112
     * verified : 1
     * recommoned : 0
     * rs : 0
     * note :
     * channel :
     * unionid :
     * vip : 0
     * vip_time : 0000-00-00 00:00:00
     * shot : 0
     * show_name : 人鱼精选
     * verifypic : [{"url":"http:\/\/1.ry.no17.cn\/bb41553272811377b024df7d36f973da","width":1080,"height":1080}]
     * vipexpired : 1
     * shot_starttime : null
     * shot_endtime : null
     * city : null
     * goodat : null
     * fdcount : 476
     * fcount : 1
     */
    @ParamNames("qid")
    private long qid;
    @ParamNames("phone")
    private String phone;
    @ParamNames("nick_name")
    private String nick_name;
    @ParamNames("height")
    private int height;
    @ParamNames("weight")
    private int weight;
    @ParamNames("photo")
    private String photo;
    @ParamNames("sexy")
    private int sexy;
    @ParamNames("birthdate")
    private String birthdate;
    @ParamNames("insert_time")
    private int insert_time;
    @ParamNames("update_time")
    private String update_time;
    @ParamNames("ip")
    private String ip;
    @ParamNames("verified")
    private int verified;
    @ParamNames("recommoned")
    private int recommoned;
    @ParamNames("rs")
    private int rs;
    @ParamNames("note")
    private String note;
    @ParamNames("channel")
    private String channel;
    @ParamNames("unionid")
    private String unionid;
    @ParamNames("vip")
    private int vip;
    @ParamNames("vip_time")
    private String vip_time;
    @ParamNames("shot")
    private int shot;
    @ParamNames("show_name")
    private String show_name;
    @ParamNames("verifypic")
    private String verifypic;
    @ParamNames("vipexpired")
    private String vipexpired;
    @ParamNames("shot_starttime")
    private Object shot_starttime;
    @ParamNames("shot_endtime")
    private Object shot_endtime;
    @ParamNames("shot_price")
    private int shot_price;
    @ParamNames("shot_time")
    private int shot_time;
    @ParamNames("city")
    private Object city;
    @ParamNames("shot_phone")
    private String shot_phone;
    @ParamNames("goodat")
    private Object goodat;
    @ParamNames("fdcount")
    private int fdcount;
    @ParamNames("fcount")
    private int fcount;
    @ParamNames("isLogin")
    private boolean isLogin;
    @ParamNames("password")
    private String password;

    public long getQid() {
        return qid;
    }

    public void setQid(long qid) {
        this.qid = qid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getSexy() {
        return sexy;
    }

    public void setSexy(int sexy) {
        this.sexy = sexy;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(int insert_time) {
        this.insert_time = insert_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public int getRecommoned() {
        return recommoned;
    }

    public void setRecommoned(int recommoned) {
        this.recommoned = recommoned;
    }

    public int getRs() {
        return rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public String getVip_time() {
        return vip_time;
    }

    public void setVip_time(String vip_time) {
        this.vip_time = vip_time;
    }

    public int getShot() {
        return shot;
    }

    public void setShot(int shot) {
        this.shot = shot;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public String getVerifypic() {
        return verifypic;
    }

    public void setVerifypic(String verifypic) {
        this.verifypic = verifypic;
    }

    public String getVipexpired() {
        return vipexpired;
    }

    public void setVipexpired(String vipexpired) {
        this.vipexpired = vipexpired;
    }

    public Object getShot_starttime() {
        return shot_starttime;
    }

    public void setShot_starttime(Object shot_starttime) {
        this.shot_starttime = shot_starttime;
    }

    public Object getShot_endtime() {
        return shot_endtime;
    }

    public void setShot_endtime(Object shot_endtime) {
        this.shot_endtime = shot_endtime;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getGoodat() {
        return goodat;
    }

    public void setGoodat(Object goodat) {
        this.goodat = goodat;
    }

    public int getFdcount() {
        return fdcount;
    }

    public void setFdcount(int fdcount) {
        this.fdcount = fdcount;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public int getShot_price() {
        return shot_price;
    }

    public void setShot_price(int shot_price) {
        this.shot_price = shot_price;
    }

    public int getShot_time() {
        return shot_time;
    }

    public void setShot_time(int shot_time) {
        this.shot_time = shot_time;
    }

    public String getShot_phone() {
        return shot_phone;
    }

    public void setShot_phone(String shot_phone) {
        this.shot_phone = shot_phone;
    }

    public UserBeanItem() {
    }

    public UserBeanItem(int qid, String nick_name, String photo) {

        this.qid = qid;
        this.nick_name = nick_name;
        this.photo = photo;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
