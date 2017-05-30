package it.com.imagewatcherdemo.image;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.com.imagewatcherdemo.R;
import it.com.imagewatcherdemo.bean.PositionInfo;
import it.com.imagewatcherdemo.view.Indicator;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 点击后展示大图的的activity
 * Created by tony on 2017/5/29.
 */
public class ShowActivity extends Activity {
    @BindView(R.id.pager_image)
    ViewPager mViewPager;
    @BindView(R.id.indicator)
    Indicator mIndicator;
    private PhotoViewPagerAdapter mAdapter;
    private List<View> mViewList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//导航栏沉浸式
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//状态栏沉浸式

        List<PositionInfo> list = (List<PositionInfo>) getIntent().getSerializableExtra("imageInfo");
        int mFirstPosition = getIntent().getIntExtra("position", 0);
        setContentView(R.layout.fragment_image);
        ButterKnife.bind(this);

        View view;
        for (int index = 0; index < list.size(); index++) {
            view = View.inflate(this, R.layout.image_show, null);
            view.setBackgroundColor(Color.BLACK);
            mViewList.add(view);
            PhotoView photoView = (PhotoView) view.findViewById(R.id.image);
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    exitAnimation();
                }
            });
        }
        mAdapter = new PhotoViewPagerAdapter(list, mViewList, this, mFirstPosition);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setCurrrentItem(mFirstPosition);
        mViewPager.setCurrentItem(mFirstPosition);
    }

    /**
     * 回退键退出动画
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            exitAnimation();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //退出的时候的动画控制
    private void exitAnimation(){
        mAdapter.exitAnimation(new Runnable() {
            @Override
            public void run() {
                ShowActivity.this.finish();
                ShowActivity.this.overridePendingTransition(0, 0);//取消activity动画
            }
        }, mViewList.get(mViewPager.getCurrentItem()));
    }
}
