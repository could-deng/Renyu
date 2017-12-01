package could.bluepay.renyumvvm.http.bean;

import java.io.Serializable;

/**
 * 
* @ClassName: CommentItem 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author yiw
* @date 2015-12-28 下午3:44:38 
*
 */
public class CommentItem implements Serializable {

	private long id;
	private UserBeanItem user;
	private UserBeanItem toReplyUser;
	private String content;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public UserBeanItem getUser() {
		return user;
	}
	public void setUser(UserBeanItem user) {
		this.user = user;
	}
	public UserBeanItem getToReplyUser() {
		return toReplyUser;
	}
	public void setToReplyUser(UserBeanItem toReplyUser) {
		this.toReplyUser = toReplyUser;
	}
	
}
