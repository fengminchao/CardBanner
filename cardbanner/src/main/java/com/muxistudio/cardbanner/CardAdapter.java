package com.muxistudio.cardbanner;

import android.support.v7.widget.CardView;

/**
 * Created by ybao on 17/2/18.
 */

public interface CardAdapter{

    float getBaseElevation();

    float getFloatElevation();

    CardView getCardView(int position);

    int getCount();
}
