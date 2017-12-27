package could.bluepay.renyumvvm.common.compress;


import java.util.List;

import could.bluepay.renyumvvm.common.entity.LocalMedia;

public interface OnCompressListener {

    /**
     * Fired when the compression is started, override to handle in your own code
     */
    void onStart();

    /**
     * Fired when a compression returns successfully, override to handle in your own code
     */
    void onSuccess(List<LocalMedia> list);

    /**
     * Fired when a compression fails to complete, override to handle in your own code
     */
    void onError(Throwable e);
}
