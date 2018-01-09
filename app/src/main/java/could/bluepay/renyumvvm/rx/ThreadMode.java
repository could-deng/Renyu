package could.bluepay.renyumvvm.rx;

/**
 *
 * Flowable上游执行的线程枚举
 */
public enum ThreadMode {
    /**
     * current thread
     */
    CURRENT_THREAD,

    /**
     * android main thread
     */
    MAIN,


    /**
     * new thread
     */
    NEW_THREAD
}
