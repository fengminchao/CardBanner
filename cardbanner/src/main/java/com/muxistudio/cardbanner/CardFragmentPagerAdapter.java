package com.muxistudio.cardbanner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ybao on 17/2/18.
 */

public class CardFragmentPagerAdapter<T> extends FragmentStatePagerAdapter implements CardAdapter {

    private List<CardFragment> mCardFragments = new ArrayList<>();
    private float mElevation;
    private float mFloatElevation;

    /**
     * data 原始的大小
     */
    private int realSize;
    private int size;

    private List<ViewHolder<T>> mViewHolders = new ArrayList<>();
    private List<T> mData = new ArrayList<>();

    private Fragment[] mStartFragmentsCache = new Fragment[3];
    private Fragment[] mEndFragmentsCache = new Fragment[3];

    private boolean mIsLoop = false;

    public CardFragmentPagerAdapter(FragmentManager fm,
                                    List<ViewHolder<T>> viewHolders, List<T> data, int cardMargin, float baseElevation,
                                    float scaleRatio, boolean isLoop) {
        super(fm);

        if (viewHolders.size() != data.size()) {
            new Exception("view holders and datas size not same").printStackTrace();
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
            CardFragment cardFragment = CardFragment.newInstance(mElevation,scaleRatio,cardMargin);
            cardFragment.setCardContentView(mViewHolders.get(i), mData.get(i));
            mCardFragments.add(cardFragment);
        }

    }

    @Override
    public Fragment getItem(int position) {
        return mCardFragments.get(position);
    }

    @Override
    public int getCount() {
        return size;
    }

    private static final String TAG = "CardFragmentPagerAdapter";

    @Override public Object instantiateItem(ViewGroup container, int position) {
        if (!mIsLoop){
            return super.instantiateItem(container,position);
        }

        if (position >= 1 && position <= 3){
            if (mStartFragmentsCache[position - 1] == null){
                mStartFragmentsCache[position - 1] = (Fragment) super.instantiateItem(container,position);
            }
            CardFragment cardFragment = (CardFragment) mStartFragmentsCache[position - 1];
            CardView cardView = cardFragment.getCardView();
            if (cardView != null) {
                cardView.setScaleX(1);
                cardView.setScaleY(1);
                cardView.setElevation(getBaseElevation());
            }
            return mStartFragmentsCache[position - 1];
        }else if (position <= getCount() - 2 && position >= getCount() - 4){
            if (mEndFragmentsCache[position - getCount() + 4] == null){
                mEndFragmentsCache[position - getCount() + 4] = (Fragment) super.instantiateItem(container,position);
            }
            CardFragment cardFragment = (CardFragment) mEndFragmentsCache[position - getCount() + 4];
            CardView cardView = cardFragment.getCardView();
            if (cardView != null) {
                cardView.setScaleX(1);
                cardView.setScaleY(1);
                cardView.setElevation(getBaseElevation());
            }
            return mEndFragmentsCache[position - getCount() + 4];
        }else {
            return super.instantiateItem(container, position);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mIsLoop) {
            if ((position >= 1 && position <= 3) || (position <= getCount() - 2 && position >= getCount() - 4)){
                return;
            }
        }
        super.destroyItem(container, position, object);
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
        return mCardFragments.get(position).getCardView();
    }

}
