package com.ms_square.android.util;

import android.content.Context;
import android.widget.Toast;

public final class ToastMaster {

    private static Toast sToast;

    private ToastMaster() {
    }

    public static void showToast(Context context, String message, int duration) {
        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(context, message, duration);
        sToast.show();
    }
}
