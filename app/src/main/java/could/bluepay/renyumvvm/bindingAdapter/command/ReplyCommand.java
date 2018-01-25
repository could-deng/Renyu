package could.bluepay.renyumvvm.bindingAdapter.command;

import java.util.concurrent.Callable;

import io.reactivex.functions.Consumer;

/**
 * 指令
 */
public class ReplyCommand<T> {

    private Runnable execute0;
    private Consumer<T> execute1;

    private Callable<Boolean> canExecute0;

    public ReplyCommand(Runnable execute) {
        this.execute0 = execute;
    }


    public ReplyCommand(Consumer<T> execute) {
        this.execute1 = execute;
    }

    /**
     *
     * @param execute callback for event
     * @param canExecute0 if this function return true the action execute would be invoked! otherwise would't invoked!
     */
    public ReplyCommand(Runnable execute, Callable<Boolean> canExecute0) {
        this.execute0 = execute;
        this.canExecute0 = canExecute0;
    }

    /**
     *
     * @param execute callback for event,this callback need a params
     * @param canExecute0 if this function return true the action execute would be invoked! otherwise would't invoked!
     */
    public ReplyCommand(Consumer<T> execute, Callable<Boolean> canExecute0) {
        this.execute1 = execute;
        this.canExecute0 = canExecute0;
    }


    public void execute() {
        try {
            if (execute0 != null && canExecute0()) {
                execute0.run();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean canExecute0(){
        try {
            if (canExecute0 == null) {
                return true;
            }
            return canExecute0.call();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public void execute(T parameter){
        try {
            if (execute1 != null && canExecute0()) {
                execute1.accept(parameter);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
