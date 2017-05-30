package it.com.imagewatcherdemo.net;

import it.com.imagewatcherdemo.bean.Image;
import rx.Observable;

/**
 * Created by tony on 2017/5/28.
 */

public abstract class BaseRepository {
    public abstract Observable<Image> getRemoteSource();
    abstract Observable<Object> getLocalSource();
}
