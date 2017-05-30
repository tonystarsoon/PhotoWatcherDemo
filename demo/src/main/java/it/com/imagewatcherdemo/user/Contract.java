package it.com.imagewatcherdemo.user;

import java.util.List;

import it.com.imagewatcherdemo.bean.Image;

/**
 * Created by tony on 2017/5/28.
 */

public interface Contract {
    interface IView{
        void showData(List<Image.ImageBean>list);
    }
    interface IPresenter {

    }
}
