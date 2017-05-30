package it.com.imagewatcherdemo;

import rx.Subscriber;

/**
 * Created by tony on 2017/5/28.
 */

public abstract class DefaultSubscriber<Object> extends Subscriber<Object> {
    @Override
    public void onCompleted() {
    }
    @Override
    public void onError(Throwable e) {
    }
}
