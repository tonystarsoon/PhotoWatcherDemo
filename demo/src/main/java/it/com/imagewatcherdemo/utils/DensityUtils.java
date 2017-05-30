package it.com.imagewatcherdemo.utils;

import android.content.Context;

/**
 * Created by tony on 2017/3/27.
 */

public class DensityUtils {
    public static int dip2px(Context context, int dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }
    public static int px2dip (Context context, int px){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px/density);
    }
}
