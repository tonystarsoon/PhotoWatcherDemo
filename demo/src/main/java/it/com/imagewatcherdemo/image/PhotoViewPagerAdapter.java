package it.com.imagewatcherdemo.image;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.com.imagewatcherdemo.bean.PositionInfo;
import it.com.imagewatcherdemo.R;
import it.com.imagewatcherdemo.net.NetWork;

/**
 * 图片预览的viewpager的适配器.
 * Created by tony on 2017/5/28.
 */
public class PhotoViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<PositionInfo> mPositionInfos;
    private List<View> mViewList = new ArrayList<>();
    private int mEnterPosition;//进入图片预览时的页面位置

    public PhotoViewPagerAdapter(List<PositionInfo> positionInfoList, List<View> viewList, Context context, int position) {
        mPositionInfos = positionInfoList;
        mContext = context;
        mViewList.addAll(viewList);
        mEnterPosition = position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViewList.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        Glide.with(mContext)
                .load(NetWork.BASE_URL + "show/image/" + mPositionInfos.get(position).getImageUrl())
                .fitCenter()
                .into(imageView);
        container.addView(view);
        //如果是时第一个进入viewpager的播放动画.
        if (position == mEnterPosition) {
            enterAnimation(imageView, view);
            mEnterPosition = -1;
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mPositionInfos.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPositionInfos.get(position).getImageTitle();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //进入动画
    private void enterAnimation(ImageView imageView, View view) {
        PositionInfo positionInfo = mPositionInfos.get(mEnterPosition);
        //设置imageView动画的初始值
        imageView.setPivotX(positionInfo.getLocationX() + positionInfo.getWidth() / 2);    //x缩放中心点
        imageView.setPivotY(positionInfo.getLocationY() + positionInfo.getHeight() / 2);   //y缩放方向的中心点

        imageView.setScaleX(0.35f);  //x方向从多大开始缩放
        imageView.setScaleY(0.35f);  //y方向从多大开始缩放

        //设置动画插速器
        TimeInterpolator sDecelerator = new DecelerateInterpolator();
        TimeInterpolator sAccelerator = new AccelerateInterpolator();
        TimeInterpolator sBounce = new BounceInterpolator();
        TimeInterpolator sInterpolator = new LinearInterpolator();

        //设置view缩放动画，以及缩放开始位置
        imageView.animate()
                .setDuration(300)
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(sInterpolator);

        //获取到view的背景后在进行背景色的渐变过度.
        Drawable background = view.getBackground();
        // 设置view的背景在1000毫秒内透明度从透明到不透明
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(background, "alpha", 0, 255);
        bgAnim.setDuration(300);
        bgAnim.start();
    }

    //退出动画
    public void exitAnimation(Runnable endAction, View view) {
        TimeInterpolator sInterpolator = new LinearInterpolator();
        int position = -1;
        for (int index = 0; index < mViewList.size(); index++) {
            if (view.equals(mViewList.get(index))) {
                position = index;
            }
        }
        if (position == -1) {
            return;
        }
        PositionInfo positionInfo = mPositionInfos.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setPivotX(positionInfo.getLocationX() + positionInfo.getWidth() / 2);    //x的缩放中心点
        imageView.setPivotY(positionInfo.getLocationY() + positionInfo.getHeight() / 2);   //y方向的中心点

        int measuredWidth = imageView.getMeasuredWidth();
        int measuredHeight = imageView.getMeasuredHeight();
        int width = positionInfo.getWidth();
        int height = positionInfo.getHeight();

        //设置imageview缩放动画,以及缩放结束位置
        imageView.animate()
                .setDuration(300)
                .scaleX(width * 1.0f / measuredWidth)
                .scaleY(height * 1.0f / measuredHeight)
                .setInterpolator(sInterpolator)
                .withEndAction(endAction);

        //背景透明度从不透明到透明
        Drawable background = view.getBackground();
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(background, "alpha", 255, 0);
        bgAnim.setDuration(300);
        bgAnim.start();
    }
}
