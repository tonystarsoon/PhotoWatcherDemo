package it.com.imagewatcherdemo;

import it.com.imagewatcherdemo.user.Contract;
import it.com.imagewatcherdemo.net.BaseRepository;

/**
 * Created by tony on 2017/5/28.
 */
public abstract class BasePresenter {
    protected Contract.IView mView;
    protected BaseRepository mRepository;

    public BasePresenter(Contract.IView view) {
        this.mView = view;
        mRepository = getRepository();
    }

    public abstract BaseRepository getRepository();
    public abstract void initData();
}
