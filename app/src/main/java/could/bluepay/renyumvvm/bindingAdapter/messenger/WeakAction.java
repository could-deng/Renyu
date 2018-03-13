package could.bluepay.renyumvvm.bindingAdapter.messenger;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;

/**
 * Created by kelin on 15-8-14.
 */
public class WeakAction<T> {
    private Runnable action;
    private Consumer<T> action1;//接收到后的Runnable
    private boolean isLive;
    private Object target;
    private WeakReference reference; //接收者

    public WeakAction(Object target, Runnable action) {
        reference = new WeakReference(target);
        this.action = action;

    }

    public WeakAction(Object target, Consumer<T> action1) {
        reference = new WeakReference(target);
        this.action1 = action1;
    }

    public void execute() {
        if (action != null && isLive()) {
            action.run();
        }
    }

    public void execute(T parameter) {
        try {
            if (action1 != null
                    && isLive()) {
                action1.accept(parameter);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 标志为无用
     */
    public void markForDeletion() {
        reference.clear();
        reference = null;
        action = null;
        action1 = null;
    }

    public Runnable getAction() {
        return action;
    }

    public Consumer<T> getAction1() {
        return action1;
    }

    public boolean isLive() {
        if (reference == null) {
            return false;
        }
        if (reference.get() == null) {
            return false;
        }
        return true;
    }


    public Object getTarget() {
        if (reference != null) {
            return reference.get();
        }
        return null;
    }
}
