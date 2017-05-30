package it.com.imagewatcherdemo.net;

import it.com.imagewatcherdemo.bean.Image;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;


/**
 * Created by tony on 2017/5/28.
 */
public class RemoteImageSource {
    private static Retrofit mRetrofit;

    public static Observable<Image> getAllImage() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(NetWork.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return mRetrofit.create(ApiCore.class).getImages("image.json");
    }
}
