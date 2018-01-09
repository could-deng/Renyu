package could.bluepay.renyumvvm.common.observable;


/**
 *  订阅
 */
public interface SubjectListener {
    void add(ObserverListener observerListener);

    void remove(ObserverListener observerListener);
}
