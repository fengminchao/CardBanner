package com.muxistudio.cardbanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by ybao on 17/2/18.
 */

public class CardFragment<T> extends Fragment {

    private CardView mCardView;
    private View contentView;
    private int cardMargin;

    public static CardFragment newInstance() {
        Bundle args = new Bundle();
        CardFragment fragment = new CardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_card, container, false);

        mCardView = (CardView) view.findViewById(R.id.card_view);
        FrameLayout.LayoutParams cardParams =
                (FrameLayout.LayoutParams) mCardView.getLayoutParams();
        int marginVertical = (int) (16 * getResources().getDisplayMetrics().density);
        Log.d("banner margin", cardMargin + "  " + marginVertical);
        cardParams.setMargins(cardMargin, marginVertical, cardMargin, marginVertical);
        mCardView.setLayoutParams(cardParams);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        if (contentView != null) {
            if (contentView.getParent() != null) {
                ((ViewGroup) contentView.getParent()).removeView(contentView);
            }
            mCardView.addView(contentView, params);
        }
        return view;
    }

    public void setCardContentView(ViewHolder viewHolder, T data) {
        contentView = viewHolder.getView(getContext(), data);
    }

    public void setCardMargin(@Px int margin) {
        cardMargin = margin;
    }

    public CardView getCardView() {
        return mCardView;
    }

}
