package it.com.imagewatcherdemo.user;

import java.util.List;

import it.com.imagewatcherdemo.BasePresenter;
import it.com.imagewatcherdemo.DefaultSubscriber;
import it.com.imagewatcherdemo.bean.Image;
import it.com.imagewatcherdemo.net.BaseRepository;
import it.com.imagewatcherdemo.net.ImageRepository;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tony on 2017/5/29.
 */
public class UserPresenter extends BasePresenter implements Contract.IPresenter {
    public UserPresenter(Contract.IView view) {
        super(view);
    }

    @Override
    public void initData() {
        mRepository.getRemoteSource()
                .flatMap(new Func1<Image, Observable<List<Image.ImageBean>>>() {
                    @Override
                    public Observable<List<Image.ImageBean>> call(Image image) {
                        List<Image.ImageBean> imageList = image.getSubjectList();
                        return Observable.just(imageList);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<List<Image.ImageBean>>() {
                    @Override
                    public void onNext(List<Image.ImageBean> imageList) {
                        mView.showData(imageList);
                    }
                });
    }

    @Override
    public BaseRepository getRepository() {
        return new ImageRepository();
    }
}
