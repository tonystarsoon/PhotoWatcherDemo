package it.com.ninephotoview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿微信朋友圈图片展示
 * Created by tony on 2017/5/29.
 */
public class NinePhotoView extends ViewGroup {
    private Context mContext;
    private int mChildGap = 20;
    private int mUnitWidth;
    private int mUnitHeight;
    private LayoutParams mParams;
    private int mUsableMeasuredWidth;
    private int mLeftMargin;
    private int mTopMargin;

    public NinePhotoView(Context context) {
        this(context, null);
        init(context);
    }

    public NinePhotoView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public NinePhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    //获取layout最终的宽度和高度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mPhotoViewList.size() != getChildCount()) {
            return;
        }
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingEnd() - getPaddingStart();
        int heightSize = 0;
        //设置最终的layout的宽和高.
        if (getChildCount() == 1) {
            heightSize = widthSize * 2 / 3 + getPaddingTop() + getPaddingBottom();
        } else {
            int ratio = (getChildCount() - 1) / 3 + 1;
            heightSize = (int) ((widthSize - 2 * mChildGap) * ratio / 3 + 0.5f
                    + getPaddingTop() + getPaddingBottom())
                    + (ratio - 1) * mChildGap;
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), heightSize);
    }

    /**
     * 当测量完毕的时候可以获取到真实的宽高.
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int measuredWidth = getMeasuredWidth();
        mUsableMeasuredWidth = measuredWidth - 2 * mChildGap - getPaddingEnd() - getPaddingStart();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        mLeftMargin = layoutParams.leftMargin;
        mTopMargin = layoutParams.topMargin;
    }

    //用来做划分,当图片等于4张的时候用田字形.
    private int divideMumber = 3;

    /**
     * 注意理解,一下的l,t,r,b,传递过来的时候就已经带了margin的.
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mPhotoViewList.size() != getChildCount()) {
            return;
        }
//        t = t + getPaddingTop() - mTopMargin;
        t = 0 + getPaddingTop(); //对子控件摆放的位置默认是从自身位置的(0,0)位置开始摆放,同时还要考虑到padding.

        l = l + getPaddingStart() - mLeftMargin;//这里可以这样写,当然也可以不用专门去获取margin,因为四个参数已经带了margin的.
        int childCount = getChildCount();

        int initTop = t;//最初的top值
        int initLeft = l;//最初的left值

        int childLeft = 0;
        int childTop = 0;
        int childRight = 0;
        int childBottom = 0;

        for (int i = 0; i < childCount; i++) {
            if (i % divideMumber == 0) {//判断左侧初始位置
                l = initLeft;
            }
            if (i >= divideMumber) {//判断顶部的初始位置
                t = initTop + ((i / divideMumber) - 1) * mChildGap + (i / divideMumber) * mUnitHeight;
            }
            childTop = t + mChildGap * (i >= divideMumber ? 1 : 0);
            childBottom = childTop + mUnitHeight;

            childLeft = l + (mChildGap + mUnitWidth) * (i % divideMumber);
            childRight = childLeft + mUnitWidth;
            View childAt = getChildAt(i);
            childAt.layout(childLeft, childTop, childRight, childBottom);
        }
    }

    //向布局中添加view
    public ImageView addPhotoView() {
        if (clearAndInputNewPhoto) {
            clearAndInputNewPhoto = false;
            mPhotoViewList.clear();
        }
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mPhotoViewList.add(imageView);
//        imageView.setOnClickListener(this);
        return imageView;
    }

    //当重新添加图片的时候,需要用来做判断并清空布局中的子view和集合中的view对象.
    private boolean clearAndInputNewPhoto = true;
    private List<ImageView> mPhotoViewList = new ArrayList<>();

    //当添加完毕之后再调用用来显示
    public void showPhotoView() {
        clearAndInputNewPhoto = true;
        removeAllViews();
        int childCount = mPhotoViewList.size();

        if (childCount == 1) {
            mUnitWidth = mUsableMeasuredWidth + mChildGap * 2;
            mUnitHeight = (int) (mUnitWidth * 2 / 3 + 0.5f);
        } else {
            mUnitWidth = (int) (mUsableMeasuredWidth / 3 + 0.5f);
            mUnitHeight = mUnitWidth;
        }

        if (mParams == null) {
            mParams = new LayoutParams(mUnitWidth, mUnitHeight);
        }
        if (childCount == 4) {
            divideMumber = 2;
        } else {
            divideMumber = 3;
        }
        for (ImageView photo : mPhotoViewList) {
            photo.setLayoutParams(mParams);
            addView(photo);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    // 以下是用触摸事件来判断点击的哪个条目
    private float mDownX;
    private float mDownY;

    /**
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getRawX();
                mDownY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //当移动了一定的位置超过一定的范围的时候,不是单击事件.
                if (Math.abs(event.getRawX() - mDownX) > 20 || Math.abs(event.getRawY() - mDownY) > 20) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                //当抬起的位置与原先的位置超过一定的范围的时候,不是单击事件.
                if (Math.abs(event.getRawX() - mDownX) > 20 || Math.abs(event.getRawY() - mDownY) > 20) {
                    return false;
                }
                int pos = findView(mDownX, mDownY);
                if (pos != -1) {
                    mListener.onItemClick(pos);
                    return true;
                } else {
                    return false;
                }
        }
        return true;
    }


    /**
     * 通过一个Rect和View的getDrawingRect()方法来指定view的位置到Rect,然后顺序查找.
     *
     * @param downX
     * @param downY
     * @return
     */
    private int findView(float downX, float downY) {
        Rect rect = new Rect();
        int[] location = new int[2];
        for (int index = 0; index < mPhotoViewList.size(); index++) {
            ImageView view = mPhotoViewList.get(index);
            view.getDrawingRect(rect);
            view.getLocationOnScreen(location);
            rect.left = location[0];
            rect.top = location[1];
            rect.right = location[0] + rect.right;
            rect.bottom = location[1] + rect.bottom;
            boolean contains = rect.contains((int) downX, (int) downY);
            if (contains) {
                return index;
            }
        }
        return -1;
    }

    //除了用触摸事件外,可以设置每个view的单机事件,来实现单个条目的单击事件
/*
    @Override
    public void onClick(View v) {
        for (int index = 0; index < mPhotoViewList.size(); index++) {
            if (v.equals(mPhotoViewList.get(index))) {
                if (mListener != null) {
                    mListener.onItemClick(index);
                }
            }
        }
    }*/

}

