package com.muxistudio.cardbanner;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by ybao on 17/2/17.
 */

public class CardBanner<T> extends ViewPager {

    private boolean mIsLoop;
    private float mCardMargin;
    private float mCardRadius;
    private float mSideCardWidth;
    private float mBaseElevation;
    private float mScaleRatio;

    private int currentItem;
    //datalist 真实的大小
    private int realSize;

    private ViewPagerScroller scroller;

    private boolean autoScroll = false;
    private boolean isAutoScrolling = false;
    /**
     * 滚动方向,ture 为正向，false 为逆向
     */
    private boolean scrollDirection = true;
    private long scrollDuration = 3000;

    private AutoScrollTask mAutoScrollTask;

    private CardFragmentPagerAdapter mCardFragmentPagerAdapter;

    public CardBanner(Context context) {
        this(context, null);
    }

    public CardBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipToPadding(false);
        setOffscreenPageLimit(2);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CardBanner);
        mIsLoop = a.getBoolean(R.styleable.CardBanner_isLoop, false);
        mCardRadius = a.getDimension(R.styleable.CardBanner_cardRadius, dp2px(4));
        mCardMargin = a.getDimension(R.styleable.CardBanner_cardMargin, dp2px(8));
        mSideCardWidth = a.getDimension(R.styleable.CardBanner_sideCardWidth, dp2px(16));
        mBaseElevation = a.getDimension(R.styleable.CardBanner_baseElevation, dp2px(0));
        mScaleRatio = a.getFloat(R.styleable.CardBanner_scaleRatio,1);
        a.recycle();

        initScroller();
    }

    /**
     * 初始化 viewpager 界面参数
     */
    private void initBanner() {
        setPadding((int) dp2px(mSideCardWidth), 0, (int)dp2px(mSideCardWidth), 0);
        setOffscreenPageLimit(2);

        if (mIsLoop) {
            post(new Runnable() {
                @Override
                public void run() {
                    CardBanner.this.setCurrentItem(2);
                }
            });
        }

        CardTransformerListener cardTransformerListener = new CardTransformerListener(
                mCardFragmentPagerAdapter, mScaleRatio);
        addOnPageChangeListener(cardTransformerListener);
        cardTransformerListener.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
                if (positionOffset < 0.05 || positionOffset > 0.95) {
                    if (mIsLoop) {
                        if (position == mCardFragmentPagerAdapter.getCount() - 2) {
                            setCurrentItem(2, false);
                        }
                        if (position == 1) {
                            setCurrentItem(mCardFragmentPagerAdapter.getCount() - 3, false);
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setViewHolders(List<ViewHolder<T>> viewHolders, List<T> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }

        mCardFragmentPagerAdapter = new CardFragmentPagerAdapter(
                ((AppCompatActivity) getContext()).getSupportFragmentManager(), viewHolders,
                datas, (int)mCardMargin,mBaseElevation, mScaleRatio, mIsLoop);
        this.setAdapter(mCardFragmentPagerAdapter);
        realSize = datas.size();

        initBanner();
    }

    public void initScroller() {
        Field mScroller;
        try {
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new ViewPagerScroller(getContext());
            mScroller.set(this, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * set is auto scroll
     */
    public void setAutoScroll(boolean autoScroll) {
        if (realSize < 2) {
            return;
        }
        this.autoScroll = autoScroll;
        startScroll();
    }

    /**
     * 设置滑动间隔
     */
    public void setScrollDuration(int duration) {
        this.scrollDuration = duration;
    }

    /**
     * 设置滑动持续时间
     */
    public void setScrollTime(int millisecond) {
        scroller.setScrollTime(millisecond);
    }

    /**
     * 设置是否循环播放
     */
    public void setIsLoop(boolean loop) {
        mIsLoop = loop;
    }

    public void startScroll() {
        if (!isAutoScrolling && autoScroll) {
            mAutoScrollTask = new AutoScrollTask(this);
            postDelayed(mAutoScrollTask, scrollDuration);
            isAutoScrolling = true;
        }
    }

    public void stopScroll() {
        if (isAutoScrolling) {
            isAutoScrolling = false;
            removeCallbacks(mAutoScrollTask);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isAutoScrolling){
            removeCallbacks(mAutoScrollTask);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
                if (autoScroll) {
                    startScroll();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (isAutoScrolling) {
                    stopScroll();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    class AutoScrollTask implements Runnable {

        final WeakReference<CardBanner> mCardBannerWeakReference;

        public AutoScrollTask(CardBanner cardBanner) {
            mCardBannerWeakReference = new WeakReference<CardBanner>(cardBanner);
        }

        @Override
        public void run() {

            CardBanner cardBanner = mCardBannerWeakReference.get();
            if (cardBanner != null) {
                if (isAutoScrolling) {
                    if (mIsLoop) {
                        cardBanner.setCurrentItem(
                                scrollDirection ? ++currentItem : --currentItem);
                        postDelayed(this, scrollDuration);
                    } else {
                        currentItem = CardBanner.this.getCurrentItem();
                        if (currentItem == realSize - 1) {
                            scrollDirection = false;
                        }
                        if (currentItem == 0) {
                            scrollDirection = true;
                        }
                        if (scrollDirection) {
                            cardBanner.setCurrentItem(++currentItem);
                        } else {
                            cardBanner.setCurrentItem(--currentItem);
                        }

                        postDelayed(this, scrollDuration);
                    }
                }
            }
        }
    }

    public float dp2px(float value) {
        return value * getContext().getResources().getDisplayMetrics().density;
    }
}
