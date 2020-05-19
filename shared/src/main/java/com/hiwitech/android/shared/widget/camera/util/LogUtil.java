package com.hiwitech.android.shared.widget.camera.util;

import timber.log.Timber;

import static com.hiwitech.android.shared.BuildConfig.DEBUG;


/**
 * author hbzhou
 * date 2019/12/13 10:49
 */
public class LogUtil {

    private static final String DEFAULT_TAG = "CJT";

    public static void i(String tag, String msg) {
        if (DEBUG)
            Timber.tag(tag).i(msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG)
            Timber.tag(tag).v(msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            Timber.tag(tag).d(msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Timber.tag(tag).e(msg);
    }

    public static void i(String msg) {
        i(DEFAULT_TAG, msg);
    }

    public static void v(String msg) {
        v(DEFAULT_TAG, msg);
    }

    public static void d(String msg) {
        d(DEFAULT_TAG, msg);
    }

    public static void e(String msg) {
        e(DEFAULT_TAG, msg);
    }
}
