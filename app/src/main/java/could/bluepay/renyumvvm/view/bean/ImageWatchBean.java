package could.bluepay.renyumvvm.view.bean;

import android.view.View;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by bluepay on 2018/1/22.
 */

public class ImageWatchBean {
    View viewOrigin;
    List<ImageView> imageViewList;
    List<String> urlList;

    public ImageWatchBean(View viewOrigin,List<ImageView> imageViewList, List<String> urlList) {
        this.viewOrigin = viewOrigin;
        this.imageViewList = imageViewList;
        this.urlList = urlList;
    }

    public View getViewOrigin() {
        return viewOrigin;
    }

    public void setViewOrigin(View viewOrigin) {
        this.viewOrigin = viewOrigin;
    }

    public List<ImageView> getImageViewList() {
        return imageViewList;
    }

    public void setImageViewList(List<ImageView> imageViewList) {
        this.imageViewList = imageViewList;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }
}
