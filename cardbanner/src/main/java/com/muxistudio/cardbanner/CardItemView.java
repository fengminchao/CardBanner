package com.muxistudio.cardbanner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/**
 * Created by ybao on 17/2/18.
 */

public class CardItemView<T> extends LinearLayout {

    private static final String TAG = "CardItemView";

    private CardView mCardView;
    private View contentView;
    private int cardMargin;

    private float mBaseElevation;
    private float mScaleRatio;
    //阴影高度至平面所需的长度比率
    private static final float ELEVATION_2_PLANE_RATIO = 1.5f;

    private static final String EXTRA_BASE_ELEVATION = "base_elevation";
    private static final String EXTRA_SCALE_RATIO = "scale_ratio";
    private static final String EXTRA_CARD_MARGIN = "card_margin";

    public CardItemView(Context context,float baseElevation,float scaleRatio,int cardMargin) {
        super(context);
        mBaseElevation = baseElevation;
        mScaleRatio = scaleRatio;
        this.cardMargin = cardMargin;

        this.setGravity(Gravity.CENTER);
        addCardView();
    }

    private void addCardView() {
        mCardView = new CardView(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mCardView,lp);
        mCardView.setRadius(4 * getContext().getResources().getDisplayMetrics().density);
        mCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mCardView.getWidth() == ((ViewGroup) mCardView.getParent()).getWidth()) {
                    LinearLayout.LayoutParams cardParams =
                            (LinearLayout.LayoutParams) mCardView.getLayoutParams();
                    ViewGroup parent = (ViewGroup) mCardView.getParent();
                    Log.v(TAG, "onGlobalLayout: card's parent width:" + parent.getWidth() + " height: " + parent.getHeight());
                    cardParams.width = (int) ((mCardView.getWidth() - cardMargin - mBaseElevation * ELEVATION_2_PLANE_RATIO * mScaleRatio * 2) / mScaleRatio);
                    cardParams.height = (int) ((mCardView.getHeight() - mBaseElevation * mScaleRatio * ELEVATION_2_PLANE_RATIO * 2) / mScaleRatio);
                    mCardView.setLayoutParams(cardParams);
                    Log.v(TAG, "onGlobalLayout: card width: " + cardParams.width + " height: " + cardParams.height);
                    mCardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    public void setCardContentView(ViewHolder viewHolder, T data) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        if (contentView != null) {
            if (contentView.getParent() != null) {
                ((ViewGroup) contentView.getParent()).removeView(contentView);
            }
        }
        contentView = viewHolder.getView(getContext(), data);
        mCardView.addView(contentView, params);
    }

    public CardView getCardView() {
        return mCardView;
    }


}
