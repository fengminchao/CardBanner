package com.muxistudio.example;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.muxistudio.cardbanner.CardBanner;
import com.muxistudio.cardbanner.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CardBanner mCardBanner;

    private List<ViewHolder<Integer>> mViewHolders = new ArrayList<>();

    private Integer[] resIds = {R.drawable.album,R.drawable.ow,R.drawable.bili};
    private List<Integer> resIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCardBanner = (CardBanner) findViewById(R.id.card_banner);

        for (int i = 0;i < resIds.length;i ++) {
            ViewHolder<Integer> viewHolder = new ViewHolder<Integer>() {
                @Override
                public View getView(Context context, Integer data) {
                    ImageView imageView = new ImageView(MainActivity.this);
                    imageView.setImageResource(data);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return imageView;
                }
            };
            mViewHolders.add(viewHolder);
        }
        resIdList = Arrays.asList(resIds);
        mCardBanner.setScrollDirection(CardBanner.DIRECTION_FORWARD);
        mCardBanner.setViewHolders(mViewHolders,resIdList);
        mCardBanner.setScrollDuration(3000);
        mCardBanner.setScrollTime(500);
        mCardBanner.setAutoScroll(true);

    }
}
