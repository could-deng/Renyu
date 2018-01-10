package could.bluepay.widget.xrecyclerview;

/**
 * Created by bluepay on 2017/12/18.
 */

public interface BaseLoadingMoreFooter {
    int STATE_NORMAL = 0;//上拉加载更多
    int STATE_RELEASE_TO_LOAD_MORE = 1;//释放加载更多
    int STATE_LOADING = 2;//正在加载更多
    int STATE_COMPLETE = 3;//加载完成
    int STATE_NOMORE = 4;//无内容

    void onMove(float delta);

    boolean releaseAction();

    void refreshComplate(boolean haveData);

    int getVisibleHeight();
}
