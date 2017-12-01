package could.bluepay.renyumvvm.http.bean;


import could.yuanqiang.http.ParamNames;

/**
 * Created by bluepay on 2017/11/27.
 */

public class PhotoInfo {
    @ParamNames("url")
    public String url;
    @ParamNames("width")
    public int width;
    @ParamNames("height")
    public int height;
}
