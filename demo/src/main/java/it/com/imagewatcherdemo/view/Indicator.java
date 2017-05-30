package it.com.imagewatcherdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.com.imagewatcherdemo.R;

/**
 * viewpager指示器
 * Created by tony on 2017/5/28.
 */
public class Indicator extends LinearLayout {
    //记录的指示器并列的文本说明
    private TextView mTitle;
    private float mTextSize;
    private float mPointSize;
    private int mTextColor;
    private int mNumber = 0;
    private Context mContext;
    private ViewPager mViewPager;
    private PointersView mPointersView;
    private int mMeasuredHeight;
    private float mTotalWidth;
    private int mMeasuredWidth;
    private int mCurrentPosition = 0;//默认是第0项被选中.
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public Indicator(Context context) {
        super(context);
        init(context);
        mContext = context;
    }

    public Indicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Indicator);
        mTextSize = typedArray.getDimension(R.styleable.Indicator_iTitleTextSize, 8);
        mPointSize = typedArray.getDimension(R.styleable.Indicator_iPointSize, 3);
        mTextColor = typedArray.getColor(R.styleable.Indicator_iTextColor, Color.WHITE);
        init(context);
        typedArray.recycle();
    }

    public Indicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        setBackgroundColor(getResources().getColor(R.color.indicator));
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding(10, 0, 10, 0);
        mTitle = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mMeasuredWidth * 2 / 3,
                LinearLayout.LayoutParams.MATCH_PARENT, 2);
        mTitle.setLayoutParams(layoutParams);
        mTitle.setTextSize(mTextSize);
        mTitle.setText("今朝有酒今朝醉");
        mTitle.setTextColor(mTextColor);
        mTitle.setGravity(Gravity.CENTER_VERTICAL);
        addView(mTitle);
    }

    public void setViewPager(ViewPager pager) {
        this.mViewPager = pager;
        if (mNumber != mViewPager.getAdapter().getCount()) {
            mNumber = mViewPager.getAdapter().getCount();
            mTotalWidth = mPointSize * 5 * mNumber;
            addPoints();
        }
        if (mOnPageChangeListener == null) {
            setOnPagerChangeListener();
        }
        mViewPager.setCurrentItem(0);
    }

    /**
     * 对外暴露的设置监听的方法.
     */
    public void setOnPagerChangeListener() {
        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position >= 0 && position < mNumber) {
                    mPointersView.refreshPointerState(position);
                    mTitle.setText(mViewPager.getAdapter().getPageTitle(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
        };
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    /**
     * 初始化指示器小圆点
     */
    private void addPoints() {
        mPointersView = new PointersView(mContext);
        addView(mPointersView);
    }

    /**
     * 设置当前的选中页面,同时刷新viewpager和indicator两者的选中项.
     */
    public void setCurrrentItem(int currrentItem) {
        mViewPager.setCurrentItem(currrentItem);
        mPointersView.refreshPointerState(currrentItem);
        mTitle.setText(mViewPager.getAdapter().getPageTitle(currrentItem));
    }


    /**
     * 指示器对应的圆点
     */
    class PointersView extends View {
        private Paint mPaint;
        private float mUnitWidth;

        public PointersView(Context context) {
            this(context, null);
            setLayoutParams(new LinearLayout.LayoutParams(mMeasuredWidth / 3, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        }

        public PointersView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public PointersView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initPointer();
        }

        private void initPointer() {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.DKGRAY);
            mUnitWidth = mPointSize * 5;
        }

        /**
         * 根据当前选中的第几项来画出被选中和没被选中的点
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            for (int index = 0; index < mNumber; index++) {
                float x = mUnitWidth * (index + 1);
                float y = mMeasuredHeight / 2;
                if (index == mCurrentPosition) {
                    mPaint.setColor(mTextColor);
                    canvas.drawCircle(x, y, (float) (mPointSize * 1.2), mPaint);
                    mPaint.setColor(Color.DKGRAY);
                } else {
                    canvas.drawCircle(x, y, mPointSize, mPaint);
                }
            }
        }

        /**
         * 刷新点的状态
         * @param position
         */
        public void refreshPointerState(int position) {
            mCurrentPosition = position;
            invalidate();
        }
    }
}




