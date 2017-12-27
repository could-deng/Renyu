package could.bluepay.renyumvvm.rx;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by yuanqiang on 16/5/17.
 */
public class RxBus {
    /**
     * 参考网址:
     *
     *          http://hanhailong.com/2015/10/09/RxBus%E2%80%94%E9%80%9A%E8%BF%87RxJava%E6%9D%A5%E6%9B%BF%E6%8D%A2EventBus/
     *          http://www.loongwind.com/archives/264.html
     *          https://theseyears.gitbooks.io/android-architecture-journey/content/rxbus.html
     */
    private static final String LOG_BUS = "RXBUS_log";
    private static volatile RxBus mDefaultInstance;

    private Map<Class, List<Disposable>> subscriptionsByEventType = new HashMap<>();

    private Map<Object, List<Class>> eventTypesBySubscriber = new HashMap<>();

    private Map<Class, List<SubscriberMethod>> subscriberMethodByEventType = new HashMap<>();


    /**
     * rxjava2.0
     * Subjcet 不是线程安全的，只有SerializedSubject是线程安全
     * 参考：http://prototypez.github.io/2016/04/10/rxjava-common-mistakes-1/
     */
    private final Subject<Object> _bus ;
    //rxjava1.0
//    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

//    SerializedSubject<Integer,Integer> subject = PublishSubject.<Integer>create().toSerialized();

    private RxBus() {
        this._bus = PublishSubject.create().toSerialized();
    }

    public static RxBus getDefault() {
        RxBus rxBus = mDefaultInstance;
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = mDefaultInstance;
                if (mDefaultInstance == null) {

                    rxBus = new RxBus();
                    mDefaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }


    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param eventType 事件类型
     * @return return
     */
    public  <T> Flowable<T> toObservable(Class<T> eventType) {
        return _bus.toFlowable(BackpressureStrategy.BUFFER).ofType(eventType);
    }

    /**
     * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param code      事件code
     * @param eventType 事件类型
     */
    private <T> Flowable<T> toObservable(final int code, final Class<T> eventType) {
        return _bus.toFlowable(BackpressureStrategy.BUFFER).ofType(Message.class)
                .filter(new Predicate<Message>() {
                    @Override
                    public boolean test(Message o) throws Exception {
                        return o.getCode() == code && eventType.isInstance(o.getObject());
                    }
                }).map(new Function<Message, Object>() {
                    @Override
                    public Object apply(Message o) throws Exception {
                        return o.getObject();
                    }
                }).cast(eventType);
    }
    /**
     * 注册
     *
     * @param subscriber 订阅者
     */
    public void register(Object subscriber) {
        Class<?> subClass = subscriber.getClass();
        Method[] methods = subClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                //获得参数类型
                Class[] parameterType = method.getParameterTypes();
                //参数不为空 且参数个数为1
                if (parameterType != null && parameterType.length == 1) {

                    Class eventType = parameterType[0];

                    addEventTypeToMap(subscriber, eventType);
                    Subscribe sub = method.getAnnotation(Subscribe.class);
                    int code = sub.code();
                    ThreadMode threadMode = sub.threadMode();

                    SubscriberMethod subscriberMethod = new SubscriberMethod(subscriber, method, eventType, code, threadMode);
                    addSubscriberToMap(eventType, subscriberMethod);

                    addSubscriber(subscriberMethod);
                } else if (parameterType == null || parameterType.length == 0) {

                    Class eventType = BusData.class;

                    addEventTypeToMap(subscriber, eventType);
                    Subscribe sub = method.getAnnotation(Subscribe.class);
                    int code = sub.code();
                    ThreadMode threadMode = sub.threadMode();

                    SubscriberMethod subscriberMethod = new SubscriberMethod(subscriber, method, eventType, code, threadMode);
                    addSubscriberToMap(eventType, subscriberMethod);

                    addSubscriber(subscriberMethod);

                }
            }
        }
    }


    /**
     * 将event的类型以订阅中subscriber为key保存到map里
     *
     * @param subscriber 订阅者
     * @param eventType  event类型
     */
    private void addEventTypeToMap(Object subscriber, Class eventType) {
        List<Class> eventTypes = eventTypesBySubscriber.get(subscriber);
        if (eventTypes == null) {
            eventTypes = new ArrayList<>();
            eventTypesBySubscriber.put(subscriber, eventTypes);
        }

        if (!eventTypes.contains(eventType)) {
            eventTypes.add(eventType);
        }
    }

    /**
     * 将注解方法信息以event类型为key保存到map中
     *
     * @param eventType        event类型
     * @param subscriberMethod 注解方法信息
     */
    private void addSubscriberToMap(Class eventType, SubscriberMethod subscriberMethod) {
        List<SubscriberMethod> subscriberMethods = subscriberMethodByEventType.get(eventType);
        if (subscriberMethods == null) {
            subscriberMethods = new ArrayList<>();
            subscriberMethodByEventType.put(eventType, subscriberMethods);
        }

        if (!subscriberMethods.contains(subscriberMethod)) {
            subscriberMethods.add(subscriberMethod);
        }
    }

    /**
     * 将订阅事件以event类型为key保存到map,用于取消订阅时用
     *
     * @param eventType  event类型
     * @param disposable 订阅事件
     */
    private void addSubscriptionToMap(Class eventType, Disposable disposable) {
        List<Disposable> disposables = subscriptionsByEventType.get(eventType);
        if (disposables == null) {
            disposables = new ArrayList<>();
            subscriptionsByEventType.put(eventType, disposables);
        }

        if (!disposables.contains(disposable)) {
            disposables.add(disposable);
        }
    }

    /**
     * 用RxJava添加订阅者
     *
     * @param subscriberMethod d
     */
    @SuppressWarnings("unchecked")
    private void addSubscriber(final SubscriberMethod subscriberMethod) {
        Flowable flowable;
        if (subscriberMethod.code == -1) {
            flowable = toObservable(subscriberMethod.eventType);
        } else {
            flowable = toObservable(subscriberMethod.code, subscriberMethod.eventType);
        }
        Disposable subscription = postToObservable(flowable, subscriberMethod)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        callEvent(subscriberMethod, o);
                    }
                });

        addSubscriptionToMap(subscriberMethod.subscriber.getClass(), subscription);
    }

    /**
     * 用于处理订阅事件在那个线程中执行
     *
     * @param observable       d
     * @param subscriberMethod d
     * @return Observable
     */
    private Flowable postToObservable(Flowable observable, SubscriberMethod subscriberMethod) {
        Scheduler scheduler;
        switch (subscriberMethod.threadMode) {
            case MAIN:
                scheduler = AndroidSchedulers.mainThread();
                break;

            case NEW_THREAD:
                scheduler = Schedulers.newThread();
                break;

            case CURRENT_THREAD:
                scheduler = Schedulers.trampoline();
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscriberMethod.threadMode);
        }
        return observable.observeOn(scheduler);
    }

    /**
     * 回调到订阅者的方法中
     *
     * @param method code
     * @param object obj
     */
    private void callEvent(SubscriberMethod method, Object object) {
        Class eventClass = object.getClass();
        List<SubscriberMethod> methods = subscriberMethodByEventType.get(eventClass);
        if (methods != null && methods.size() > 0) {
            for (SubscriberMethod subscriberMethod : methods) {
                Subscribe sub = subscriberMethod.method.getAnnotation(Subscribe.class);
                int c = sub.code();
                if (c == method.code && method.subscriber.equals(subscriberMethod.subscriber) && method.method.equals(subscriberMethod.method)) {
                    subscriberMethod.invoke(object);
                }

            }
        }
    }


    /**
     * 是否注册
     *
     * @param subscriber
     * @return
     */
    public synchronized boolean isRegistered(Object subscriber) {
        return eventTypesBySubscriber.containsKey(subscriber);
    }

    /**
     * 取消注册
     *
     * @param subscriber object
     */
    public void unregister(Object subscriber) {
        List<Class> subscribedTypes = eventTypesBySubscriber.get(subscriber);
        if (subscribedTypes != null) {
            for (Class<?> eventType : subscribedTypes) {
                unSubscribeByEventType(subscriber.getClass());
                unSubscribeMethodByEventType(subscriber, eventType);
            }
            eventTypesBySubscriber.remove(subscriber);
        }
    }

    /**
     * subscriptions unsubscribe
     *
     * @param eventType eventType
     */
    private void unSubscribeByEventType(Class eventType) {
        List<Disposable> disposables = subscriptionsByEventType.get(eventType);
        if (disposables != null) {
            Iterator<Disposable> iterator = disposables.iterator();
            while (iterator.hasNext()) {
                Disposable disposable = iterator.next();
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 移除subscriber对应的subscriberMethods
     *
     * @param subscriber subscriber
     * @param eventType  eventType
     */
    private void unSubscribeMethodByEventType(Object subscriber, Class eventType) {
        List<SubscriberMethod> subscriberMethods = subscriberMethodByEventType.get(eventType);
        if (subscriberMethods != null) {
            Iterator<SubscriberMethod> iterator = subscriberMethods.iterator();
            while (iterator.hasNext()) {
                SubscriberMethod subscriberMethod = iterator.next();
                if (subscriberMethod.subscriber.equals(subscriber)) {
                    iterator.remove();
                }
            }
        }
    }

















    public void send(int code, Object o) {
        _bus.onNext(new Message(code, o));
    }

    public void post(Object o) {
        _bus.onNext(o);
    }

    public void send(int code) {
        _bus.onNext(new Message(code, new BusData()));
    }



    private class Message {
        private int code;
        private Object object;

        public Message() {
        }

        private Message(int code, Object o) {
            this.code = code;
            this.object = o;
        }

        private int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        private Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }
















//    public void send(Object o) {
//        _bus.onNext(o);
//    }
//
//    public Observable<Object> toObservable() {
//        return _bus;
//    }
//
//
//    /**
//     * 提供了一个新的事件,根据code进行分发
//     * @param code 事件code
//     * @param o
//     */
//    public void post(int code, Object o){
//        _bus.onNext(new RxBusBaseMessage(code,o));
//
//    }

//    /**
//     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
//     * @param eventType 事件类型
//     * @param <T>
//     * @return
//     */
//    public <T> Observable<T> toObservable(Class<T> eventType) {
//        return _bus.ofType(eventType);
//    }

//    /**
//     * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
//     * 对于注册了code为0，class为voidMessage的观察者，那么就接收不到code为0之外的voidMessage。
//     * @param code 事件code
//     * @param eventType 事件类型
//     * @param <T>
//     * @return
//     */
//    public <T> Observable<T> toObservable(final int code, final Class<T> eventType) {
//        return _bus.ofType(RxBusBaseMessage.class)
//                .filter(new Predicate<RxBusBaseMessage>() {
//                    @Override
//                    public boolean test(RxBusBaseMessage rxBusBaseMessage) throws Exception {
//                        return rxBusBaseMessage.getCode() == code && eventType.isInstance(rxBusBaseMessage.getObject());
//                    }
////                    @Override
////                    public Boolean call(RxBusBaseMessage o) {
////                        //过滤code和eventType都相同的事件
////                        return o.getCode() == code && eventType.isInstance(o.getObject());
////                    }
//                })
//                .map(new Function<RxBusBaseMessage, T>() {
//                    @Override
//                    public T apply(RxBusBaseMessage rxBusBaseMessage) throws Exception {
//                        return (T) rxBusBaseMessage.getObject();
//                    }
////                    @Override
////                    public Object call(RxBusBaseMessage o) {
////                        return o.getObject();
////                    }
//                }).cast(eventType);//将转化为eventType类型
//    }
//    /**
//     * 判断是否有订阅者
//     */
//    public boolean hasObservers() {
//        return _bus.hasObservers();
//    }


}
