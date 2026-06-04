package com.ms_square.android.util;

import android.content.Context;
import android.util.TypedValue;

public final class DimenUtil {

    private DimenUtil() {
    }

    public static float convertToPixelFromDip(Context context, float dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                context.getResources().getDisplayMetrics());
    }
}
