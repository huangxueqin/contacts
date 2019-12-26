package com.xueqin.contacts.util;

import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

public class ScreenUtils {

    public static float getDensity(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dp2px(@NonNull Context context, int dp) {
        return (int) (getDensity(context) * dp);
    }

    public static boolean isScreenPortrait(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

}
