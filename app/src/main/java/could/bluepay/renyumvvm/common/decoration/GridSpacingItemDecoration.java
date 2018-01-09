package could.bluepay.renyumvvm.common.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * PictureSelectorActivity中图片展示RecyclerView，每个item的样式
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;//每行的个数
    private int spacing;//相邻两个item的间隔
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;//0，1，2，3
        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount;
            outRect.right = (column + 1) * spacing / spanCount;
            if (position < spanCount) {
                outRect.top = spacing;
            }
            outRect.bottom = spacing;
        } else {
            //原本recyclerView就marginleft和marginRight都为2dp
            outRect.left = column * spacing / spanCount;
            outRect.right = spacing - (column + 1) * spacing / spanCount;
            //0,3/4spacing
            //1/4spacing，1／2spacing
            //2/4spacing,1/4spacing
            //3/4spacing,0
            if (position < spanCount) {
                outRect.top = spacing;
            }
            outRect.bottom = spacing;
        }
    }

    //ItemDecoration.onDraw()会先于ItemView的onDraw，如果层叠会被遮挡
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }
}