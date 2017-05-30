package it.com.imagewatcherdemo.net;

import it.com.imagewatcherdemo.bean.Image;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by tony on 2017/5/28.
 */
public interface ApiCore {
    @GET("show/{path}")
    Observable<Image> getImages(@Path("path") String path);
}
