package could.bluepay.renyumvvm.http.bean;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.MixApp;
import could.bluepay.renyumvvm.common.PrefsHelper;
import could.bluepay.renyumvvm.utils.AppUtils;
import could.yuanqiang.http.ParamNames;

/**
 * Created by bluepay on 2017/11/27.
 */

public class WeiboBean {
    /**
     * pid : 12746
     * pcontent : 今日推荐：
     另类睡衣，近距离拍摄，是否燃爆你的荷尔蒙？
     加入人鱼vip会员，可以享受照片和视频全免！
     * qid : 24753
     * insert_time : 2017-05-22 19:21:17
     * pic : [{"url":"http:\/\/1.ry.no17.cn\/1495451989883.mp4?vframe\/jpg\/offset\/0","width":1920,"height":1080}]
     * status : 0
     * tagid : 1
     * hot : 10
     * score : 0
     * type : 9
     * paycount : 7
     * fee : 3
     * top : 0
     * sq : 20000
     * video : http://1.ry.no17.cn/1495451989883.mp4
     * priased : 0
     * payed : 0
     * relation : 1
     * user : {"qid":24753,"phone":19777830667,"nick_name":"人鱼精选","height":188,"weight":62,"photo":"http://1.ry.no17.cn/1490364631283.jpg","sexy":2,"birthdate":"1989-02-14","insert_time":1489128216,"update_time":"2017-04-01 10:10:07","ip":"114.242.248.112","verified":1,"recommoned":0,"rs":0,"note":"","channel":"","unionid":"","vip":0,"vip_time":"0000-00-00 00:00:00","shot":0,"show_name":"人鱼精选","verifypic":"[{\"url\":\"http:\\/\\/1.ry.no17.cn\\/bb41553272811377b024df7d36f973da\",\"width\":1080,\"height\":1080}]","vipexpired":1}
     * commentcount : 0
     * praisecount : 10
     */

    @ParamNames("pid")
    private long pid;
    @ParamNames("pcontent")
    private String pcontent;
    @ParamNames("qid")
    private long qid;
    @ParamNames("pic")
    private String pic;
    @ParamNames("insert_time")
    private String insert_time;
    @ParamNames("status")
    private int status;
    @ParamNames("tagid")
    private int tagid;
    @ParamNames("hot")
    private int hot;
    @ParamNames("score")
    private int score;
    @ParamNames("type")
    private int type;
    @ParamNames("paycount")
    private int paycount;
    @ParamNames("fee")
    private int fee;
    @ParamNames("top")
    private int top;
    @ParamNames("sq")
    private int sq;
    @ParamNames("video")
    private String video;
    @ParamNames("priased")
    private int priased;
    @ParamNames("payed")
    private int payed;
    @ParamNames("relation")
    private int relation;
    @ParamNames("user")
    private UserBeanItem user;
    @ParamNames("commentcount")
    private int commentcount;
    @ParamNames("praisecount")
    private int praisecount;


    private boolean isMoneyPicture;
    private boolean isMoneyVideo;
    public int curProgress;
    private List<PhotoInfo> photos = new ArrayList<>();
    private List<FavortItem> favorters = new ArrayList<>();
    private List<CommentItem> comments = new ArrayList<>();
    private boolean isExpand;

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getPcontent() {
        return pcontent;
    }

    public void setPcontent(String pcontent) {
        this.pcontent = pcontent;
    }

    public long getQid() {
        return qid;
    }

    public void setQid(long qid) {
        this.qid = qid;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public List<PhotoInfo> getPic() {
//        Log.i("BluePay", "---pic---"+pic);
        if(photos==null||photos.size()==0){
            Type photosType = new TypeToken<List<PhotoInfo>>() {
            }.getType();
            try {
                photos = AppUtils.getList(pic, photosType);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return photos;
    }

    public void setPic(String pic) {
        this.pic = pic;
        Type photosType = new TypeToken<List<PhotoInfo>>() {
        }.getType();
        try {
            photos = AppUtils.getList(pic, photosType);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setPic(List<PhotoInfo> photos) {
        this.photos = photos;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTagid() {
        return tagid;
    }

    public void setTagid(int tagid) {
        this.tagid = tagid;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPaycount() {
        return paycount;
    }

    public void setPaycount(int paycount) {
        this.paycount = paycount;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getSq() {
        return sq;
    }

    public void setSq(int sq) {
        this.sq = sq;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getPriased() {
        return priased;
    }

    public void setPriased(int priased) {
        this.priased = priased;
    }

    public int getPayed() {
        return payed;
    }

    public void setPayed(int payed) {
        this.payed = payed;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public UserBeanItem getUser() {
        return user;
    }

    public void setUser(UserBeanItem user) {
        this.user = user;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public int getPraisecount() {
        return praisecount;
    }

    public void setPraisecount(int praisecount) {
        this.praisecount = praisecount;
    }

    public List<FavortItem> getFavorters() {
        return favorters==null?new ArrayList<FavortItem>():favorters;
    }

    public void setFavorters(List<FavortItem> favorters) {
        this.favorters = favorters;
    }

    public List<CommentItem> getComments() {
        return comments==null?new ArrayList<CommentItem>():comments;
    }

    public void setComments(List<CommentItem> comments) {
        this.comments = comments;
    }

    public boolean isMoneyPicture() {
        if(qid == PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).readLong(Config.SP_KEY_UID)){
            return false;
        }
        return isMoneyPicture;
    }

    public void setMoneyPicture(boolean moneyPicture) {
        isMoneyPicture = moneyPicture;
    }

    public boolean isMoneyVideo() {
        if(qid== PrefsHelper.with(MixApp.getContext(), Config.PREFS_USER).readLong(Config.SP_KEY_UID)){
            return false;
        }
        return isMoneyVideo;
    }

    public void setMoneyVideo(boolean moneyVideo) {
        isMoneyVideo = moneyVideo;
    }

    public void setExpand(boolean isExpand){
        this.isExpand = isExpand;
    }

    public boolean isExpand(){
        return this.isExpand;
    }

    public boolean hasFavort(){
        if(favorters!=null && favorters.size()>0){
            return true;
        }
        return false;
    }

    public boolean hasComment(){
        if(comments!=null && comments.size()>0){
            return true;
        }
        return false;
    }

    public long getCurUserFavortId(long curUserId){
        long favortid = 0;
        if(hasFavort()){
            for(FavortItem item : favorters){
                if(curUserId==item.getQid()){
                    favortid = item.getId();
                    return favortid;
                }
            }
        }
        return favortid;
    }
}
