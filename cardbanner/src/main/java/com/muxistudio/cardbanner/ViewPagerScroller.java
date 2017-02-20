package com.muxistudio.cardbanner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by ybao on 17/2/19.
 */

public class ViewPagerScroller extends Scroller {

    private int mScrollTime = 1000;

    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator,
            boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollTime);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollTime);
    }

    public int getScrollTime() {
        return mScrollTime;
    }

    public void setScrollTime(int scrollTime) {
        this.mScrollTime = scrollTime;
    }

}