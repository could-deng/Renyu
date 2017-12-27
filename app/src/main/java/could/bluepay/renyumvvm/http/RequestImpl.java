package could.bluepay.renyumvvm.http;

import io.reactivex.disposables.Disposable;

/**
 * 用于数据请求的回调
 */

public interface RequestImpl {
    void loadSuccess(Object object);

    void loadFailed();

    void addSubscription(Disposable disposable);
}
