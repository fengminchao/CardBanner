package com.muxistudio.cardbanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ybao on 17/2/18.
 */

public class CardPagerAdapter<T> extends PagerAdapter implements CardAdapter {

    private List<CardItemView> mCardItemViews = new ArrayList<>();
    private float mElevation;
    private float mFloatElevation;

    /**
     * data 原始的大小
     */
    private int realSize;
    private int size;

    private List<ViewHolder<T>> mViewHolders = new ArrayList<>();
    private List<T> mData = new ArrayList<>();

    private View[] mStartItemViewsCache = new View[3];
    private View[] mEndItemViewsCache = new View[3];

    private boolean mIsLoop = false;

    public CardPagerAdapter(Context context,List<ViewHolder<T>> viewHolders, List<T> data, int cardMargin, float baseElevation,
                            float scaleRatio, boolean isLoop) {
        super();

        if (viewHolders.size() != data.size()) {
            throw new RuntimeException("The size of viewHolders and data should be same");
        }

        realSize = data.size();
        mIsLoop = isLoop;

        if (isLoop && realSize > 2) {
            ViewHolder viewHolder1 = viewHolders.get(realSize - 2);
            ViewHolder viewHolder2 = viewHolders.get(realSize - 1);
            ViewHolder viewHolder3 = viewHolders.get(0);
            ViewHolder viewHolder4 = viewHolders.get(1);
            mViewHolders.add(viewHolder1);
            mViewHolders.add(viewHolder2);
            mViewHolders.addAll(viewHolders);
            mViewHolders.add(viewHolder3);
            mViewHolders.add(viewHolder4);

            T data1 = data.get(realSize - 2);
            T data2 = data.get(realSize - 1);
            T data3 = data.get(0);
            T data4 = data.get(1);
            mData.add(0, data1);
            mData.add(1, data2);
            mData.addAll(data);
            mData.add(realSize + 2, data3);
            mData.add(data4);
            size = realSize + 4;
        } else {
            mViewHolders = viewHolders;
            mData = data;
            size = realSize;
        }
        mElevation = baseElevation;
        mFloatElevation = scaleRatio * baseElevation;

        for (int i = 0; i < size; i++) {
            CardItemView cardItemView = new CardItemView(context,mElevation,scaleRatio,cardMargin);
            cardItemView.setCardContentView(mViewHolders.get(i), mData.get(i));
            mCardItemViews.add(cardItemView);
        }

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public int getCount() {
        return size;
    }

    private static final String TAG = "CardPagerAdapter";

    @Override public Object instantiateItem(ViewGroup container, int position) {
        CardItemView cardItemView;
        cardItemView = mCardItemViews.get(position);

        if ((position >= 1 && position <= 3) || (position <= getCount() - 2 && position >= getCount() - 4)){
            CardView cardView = cardItemView.getCardView();
            if (cardView != null) {
                cardView.setScaleX(1);
                cardView.setScaleY(1);
                cardView.setElevation(getBaseElevation());
            }
        }
        container.addView(cardItemView);
        return cardItemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        CardItemView itemView = (CardItemView) object;
        container.removeView(itemView);
    }

    @Override
    public float getBaseElevation() {
        return mElevation;
    }

    @Override
    public float getFloatElevation() {
        return mFloatElevation;
    }

    @Override
    public CardView getCardView(int position) {
        return mCardItemViews.get(position).getCardView();
    }

}
