package could.bluepay.renyumvvm.rx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * 订阅了对象
 */
public class SubscriberMethod {
    public Object subscriber;//订阅者activity
    public Method method;//订阅的方法（在activity中声明了@Subscibe()的方法）
    public ThreadMode threadMode;//flowable上游执行的线程
    public Class<?> eventType;//订阅的方法
    public int code;

    public SubscriberMethod(Object subscriber, Method method, Class<?> eventType, int code, ThreadMode threadMode) {
        this.subscriber = subscriber;
        this.method = method;
        this.eventType = eventType;
        this.code = code;
        this.threadMode = threadMode;
    }


    /**
     * 调用方法
     * @param o 参数
     */
    public void invoke(Object o){
        try {
            Class[] parameterType = method.getParameterTypes();
            if(parameterType != null && parameterType.length == 1){
                method.invoke(subscriber, o);
            }else if(parameterType == null || parameterType.length == 0){
                method.invoke(subscriber);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
