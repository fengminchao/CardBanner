package com.muxistudio.cardbanner;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;

/**
 * Created by ybao on 17/2/18.
 */

public class CardTransformerListener implements ViewPager.OnPageChangeListener{

    private boolean mScalingEnabled;
    private CardAdapter mAdapter;
    private float mScaleRatioOffset;
    private float mLastOffset;

    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public CardTransformerListener(CardAdapter cardAdapter, float scaleRatio) {
        mAdapter = cardAdapter;
        mScaleRatioOffset = scaleRatio - 1;
        if (mScaleRatioOffset > 0){
            mScalingEnabled = true;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        int currentPosition;
        int nextPosition;
        float baseElevation = mAdapter.getBaseElevation();
        float floatElevation = mAdapter.getFloatElevation();
        float realOffset;
        boolean goingLeft = mLastOffset > positionOffset;

        if (goingLeft) {
            currentPosition = position + 1;
            nextPosition = position;
            realOffset = 1 - positionOffset;
        } else {
            nextPosition = position + 1;
            currentPosition = position;
            realOffset = positionOffset;
        }

        if (nextPosition > mAdapter.getCount() - 1
                || currentPosition > mAdapter.getCount() - 1) {
            return;
        }

        CardView currentCard = mAdapter.getCardView(currentPosition);

        if (currentCard != null) {
            if (mScalingEnabled) {
                currentCard.setScaleX((float) (1 + mScaleRatioOffset * (1 - realOffset)));
                currentCard.setScaleY((float) (1 + mScaleRatioOffset * (1 - realOffset)));
            }
            currentCard.setCardElevation((baseElevation + (floatElevation - baseElevation)
                    * (1 - realOffset)));
        }

        CardView nextCard = mAdapter.getCardView(nextPosition);

        if (nextCard != null) {
            if (mScalingEnabled) {
                nextCard.setScaleX((float) (1 + mScaleRatioOffset * (realOffset)));
                nextCard.setScaleY((float) (1 + mScaleRatioOffset * (realOffset)));
            }
            nextCard.setCardElevation((baseElevation + (floatElevation - baseElevation)
                    * (realOffset)));
        }

        mLastOffset = positionOffset;
        if (mOnPageChangeListener != null){
            mOnPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mOnPageChangeListener != null){
            mOnPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null){
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener){
        mOnPageChangeListener = onPageChangeListener;
    }
}
