package com.muxistudio.cardbanner;

import android.content.Context;
import android.view.View;

/**
 * Created by ybao on 17/2/18.
 */

public interface ViewHolder<T> {

    View getView(Context context,T data);

}
