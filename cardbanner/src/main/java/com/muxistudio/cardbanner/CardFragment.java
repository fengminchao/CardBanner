package com.muxistudio.cardbanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/**
 * Created by ybao on 17/2/18.
 */

public class CardFragment<T> extends Fragment {

    private static final String TAG = "CardFragment";

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

    private CardFragment() {
    }

    public static CardFragment newInstance(float baseElevation, float scaleRatio, int cardMargin) {
        Bundle args = new Bundle();
        CardFragment fragment = new CardFragment();
        args.putFloat(EXTRA_BASE_ELEVATION, baseElevation);
        args.putFloat(EXTRA_SCALE_RATIO, scaleRatio);
        args.putInt(EXTRA_CARD_MARGIN, cardMargin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mBaseElevation = bundle.getFloat(EXTRA_BASE_ELEVATION, 0);
            mScaleRatio = bundle.getFloat(EXTRA_SCALE_RATIO, 1);
            cardMargin = bundle.getInt(EXTRA_CARD_MARGIN, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_card, container, false);

        mCardView = view.findViewById(R.id.card_view);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        if (contentView != null) {
            if (contentView.getParent() != null) {
                ((ViewGroup) contentView.getParent()).removeView(contentView);
            }
            mCardView.addView(contentView, params);
        }

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
        return view;
    }

    public void setCardContentView(ViewHolder viewHolder, T data) {
        contentView = viewHolder.getView(getContext(), data);
    }

    public CardView getCardView() {
        return mCardView;
    }


}
