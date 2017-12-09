package could.bluepay.renyumvvm.http.bean;


import could.yuanqiang.http.ParamNames;

public class FavortItem {

//	@ParamNames("id")
	private long id;
//	@ParamNames("pid")
	private long pid;
//	@ParamNames("qid")
	private long qid;
//	@ParamNames("nick_name")
	private String nick_name;
//	@ParamNames("like_time")
	private String like_time;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getQid() {
		return qid;
	}

	public void setQid(long qid) {
		this.qid = qid;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getLike_time() {
		return like_time;
	}

	public void setLike_time(String like_time) {
		this.like_time = like_time;
	}
}
