package it.com.imagewatcherdemo.net;

import it.com.imagewatcherdemo.bean.Image;
import rx.Observable;

/**
 * Created by tony on 2017/5/28.
 */

public class ImageRepository extends BaseRepository{

    @Override
    public Observable<Image> getRemoteSource() {
        return RemoteImageSource.getAllImage();
    }

    @Override
    public Observable<Object> getLocalSource() {
        return null;
    }
}
