package could.bluepay.renyumvvm.http.bean;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 
* @ClassName: ActionItem 
* @Description: 弹窗内部子类项（绘制标题和图标）
*
 */
public class ActionItem {
	public static final int ACTION_TYPE_DIG = 1;//点赞
	public static final int ACTION_TYPE_CANCEL = 2;//取消
	public static final int ACTION_TYPE_COMMENT = 3;//评论

	private int type;
	// 定义图片对象
	public Drawable mDrawable;
	// 定义文本对象
	public CharSequence mTitle;

	public ActionItem(Drawable drawable, CharSequence title) {
		this.mDrawable = drawable;
		this.mTitle = title;
	}

	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}

	//	public ActionItem(CharSequence title) {
//		this.mDrawable = null;
//		this.mTitle = title;
//	}

	public ActionItem(int type){
		this.type = type;
	}

	public ActionItem(Context context, int titleId, int drawableId) {
		this.mTitle = context.getResources().getText(titleId);
		this.mDrawable = context.getResources().getDrawable(drawableId);
	}

	public ActionItem(Context context, CharSequence title, int drawableId) {
		this.mTitle = title;
		this.mDrawable = context.getResources().getDrawable(drawableId);
	}

}
