package could.bluepay.renyumvvm.http.bean;

import could.yuanqiang.http.ParamNames;

/**
 * Created by bluepay on 2017/11/23.
 */

public class BaseBean {
    @ParamNames("errno")
    private int errno;
    @ParamNames("errmsg")
    private String errmsg;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
